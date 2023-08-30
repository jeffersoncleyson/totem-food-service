## Executar docker-compose para desenvolvimento

executar o commando na raiz do pasta do projeto

Iniciar somente a infraestrutura e executar o Totem Food Service pela IDE
```
./script-start-dev.sh dev
```
Parar todos os componentes
```
./script-stop-dev.sh
```
Ver os logs da aplicação: `mongo-dev`, `totem-food-service`, `payment-gateway` e `mailhog`
- Exemplo:
```
./script-logs-dev.sh mongo
```
```
./script-logs-dev.sh payment-gateway
```