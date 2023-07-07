if [ "${1}" = "dev" ]
then
  echo Starting only MongoDB
  docker-compose -f docker-compose.yaml up -d mongo-dev payment-gateway
else
  echo Starting MongoDB and Totem Food Service
  docker-compose -f docker-compose.yaml up -d --build
fi

