FROM python:3.8-slim-buster
WORKDIR /app
COPY ./requirements.txt /app/requirements.txt
RUN pip install -r requirements.txt
COPY ./gateway.py /app/gateway.py
ENTRYPOINT [ "python" ]
CMD [ "gateway.py" ]