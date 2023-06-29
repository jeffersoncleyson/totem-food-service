# app.py
from flask import Flask, request, jsonify
import json
from json import JSONEncoder
import logging

app = Flask(__name__)
app.logger.setLevel(logging.INFO)


class MyEncoder(JSONEncoder):
    def default(self, obj):
        return obj.__dict__


def not_none(param: object):
    return param is not None


def equals(param_1: str, param_2: str):
    return param_1 == param_2


def is_path_match(request_param: dict, path: str):
    return not_none(request_param) and not_none(request_param["path"]) and equals(request_param["path"], path)


def is_method_match(request_param: dict, method: str):
    return not_none(request_param) and not_none(request_param["method"]) and equals(request_param["method"], method)


def is_response_valid(response_param: dict):
    return not_none(response_param) and not_none(response_param["body"]) and not_none(response_param["status_code"])


def find_request_and_response(path: str, method: str):
    f = open('mocks/mock.json')
    data = json.load(f)
    for i in data:
        if is_path_match(i["request"], path) and is_method_match(i["request"], method) \
                and is_response_valid(i["response"]):
            return i["response"]["body"], i["response"]["status_code"]

    return {"error": "Not Found"}, 404


def normalize_path(text: str):
    if not_none(text) and not text.startswith("/"):
        return "/" + text
    return text


@app.route('/<path:text>', methods=['POST', 'GET', 'PUT', 'DELETE', 'PATCH'])
def get_countries(text):
    params = {
        "url": request.url,
        "path": normalize_path(text),
        "args": request.args,
        "headers": dict(request.headers),
        "body": dict(request.json)
    }
    app.logger.info(json.dumps(params, indent=2, cls=MyEncoder))

    response_found, status_code = find_request_and_response(normalize_path(text), request.method)

    return jsonify(response_found), status_code


if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=False)
