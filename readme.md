# Dependência

# Gerar docker build

- Vá até o diretório da aplicação, onde se encontra o pom.xml

- Execute o comando maven para gerar o pacote

  ```bash
  mvn clean package
  ```

- Buildar o dockerfile, informando o Nome da imagem e a TAG, ao final o "." define que é para usar o dockerfile contino no diretório corrente

  ```bash
  docker build -t totem-food:1.0.0 .
  ```

- Para visualizar a imagem criada, basta listar com o comando ` docker images` 

- Para executar um container baseado na imagem gerada, basta executar o comando abaixo

  ```bash
  docker run -d -p 8081:8081 --name totem-food -t totem-food:1.0.0
  ```

- Para ver os logs, execute

  ```bash
  docker logs totem-food --follow
  ```

- Para matar a execução do container, execute

  ```bash
  docker rm -f totem-food
  ```
