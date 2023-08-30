[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-orange.svg)](https://sonarcloud.io/summary/new_code?id=totem-food-service-4787bf24ba181ac258cb520837a657896a92044c)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=totem-food-service-4787bf24ba181ac258cb520837a657896a92044c&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=totem-food-service-4787bf24ba181ac258cb520837a657896a92044c)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=totem-food-service-4787bf24ba181ac258cb520837a657896a92044c&metric=coverage)](https://sonarcloud.io/summary/new_code?id=totem-food-service-4787bf24ba181ac258cb520837a657896a92044c)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=totem-food-service-4787bf24ba181ac258cb520837a657896a92044c&metric=bugs)](https://sonarcloud.io/summary/new_code?id=totem-food-service-4787bf24ba181ac258cb520837a657896a92044c)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=totem-food-service-4787bf24ba181ac258cb520837a657896a92044c&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=totem-food-service-4787bf24ba181ac258cb520837a657896a92044c)

---

## Indice

Instrução para uso da aplicação se encontra no tópico [Executando a Aplicação](#executando-a-aplicação), os demais tópicos abaixo são documentações complementares.

1. [Postman](#postman-da-aplicação)
2. [Swagger](#swagger-da-aplicação)
3. [Visão Geral](#visão-geral-da-aplicação)
4. [Máquina de Estado](#máquinas-de-estado)
5. [Jornada dos Atores](#jornada-dos-atores)
6. [Requisitos Desenvolvimento](#projeto)
7. [Executando aplicação para_desenvolvimento](#ambiente-para-desenvolvimento-docker-compose)
7. [Executando aplicação via docker-compose](#docker-compose)
8. [Executando aplicação via minikube](#minikube)
9. [Processo de Desenvolvimento](#processo-de-execução-das-tasks)

---

## Postman da Aplicação

Importar o arquivo abaixo no Postman:

- [collection_postman](./documentation/postman-collections/Entrega%20Fase%201.postman_collection.json)

## Swagger da Aplicação

Para visualizar o swagger copiar o conteúdo do arquivo
- [Swagger Totem](./documentation/swaggers/totem-food-service.yaml)

após copiar cole no site do [swagger editor](https://editor.swagger.io/).

### Swagger Totem

![Swagger](./documentation/swaggers/totem-food-service-swagger_customers.png)
![Swagger](./documentation/swaggers/totem-food-service-swagger_order.png)
![Swagger](./documentation/swaggers/totem-food-service-swagger_payment.png)
![Swagger](./documentation/swaggers/totem-food-service-swagger_category.png)
![Swagger](./documentation/swaggers/totem-food-service-swagger_products.png)

---

## Visão Geral da Aplicação

Visão global da aplicação em funcionamento

![Swagger](./documentation/diagrams/GeneralVision.png)

---

## Máquinas de Estado

- Order

![state-machine-order](./documentation/diagrams/state-machine-order.png)

- Payment

![state-machine-payment](./documentation/diagrams/state-machine-payment.png)

---

## Jornada dos Atores

* Administrador

![actors_journey-admin](./documentation/diagrams/actors_journey-admin.png)

* Cliente

![actors_journey-customer](./documentation/diagrams/actors_journey-customer.png)

* Cozinha x Atendente

![actors_journey-cozinha-atendente](./documentation/diagrams/actors_journey-coz-atend.png)

* Monitor

![actors_journey-monitor](./documentation/diagrams/actors_journey-monitor.png)

---

## Ambiente para desenvolvimento Docker Compose

[Executando a aplicação para desenvolvimento via docker-compose](./documentation/readme-doc/exec_app_docker_compose_dev.md)

## Docker Compose

[Executando a aplicação via docker-compose](./documentation/readme-doc/exec_app_docker_compose.md)

## Minikube

[Executando a aplicação via minikube](./documentation/readme-doc/exec_app_minikube.md)

---

## Projeto

Para execução do projeto esta sendo utilzado as seguintes bibliotecas e ferramentas abaixo.

* Java Versão 17
```
openjdk 17.0.7 2023-04-18
OpenJDK Runtime Environment (build 17.0.7+7-Ubuntu-0ubuntu120.04)
OpenJDK 64-Bit Server VM (build 17.0.7+7-Ubuntu-0ubuntu120.04, mixed mode, sharing)
```
* Maven versão 3.8.1
```
Apache Maven 3.8.1 (05c21c65bdfed0f71a2f2ada8b84da59348c4c5d)
Maven home: /usr/local/apache-maven-3.8.1
Java version: 17.0.7, vendor: Private Build, runtime: /usr/lib/jvm/java-17-openjdk-amd64
Default locale: pt_BR, platform encoding: UTF-8
OS name: "linux", version: "5.14.0-1059-oem", arch: "amd64", family: "unix"
```
* Docker version 23.0.1
```
Docker version 23.0.1, build a5ee5b1
```
* Docker Compose version 1.29.2
```
docker-compose version 1.29.2, build 5becea4c
```
* IntelliJ IDE Community Edition
```
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
```
* MongoDB Compass versão 1.31.2

---

## Processo de execução das Tasks

Todo o processo de criação de novas features e correções de bugs devem sguir o processo desenhado abaixo

![Processo de execução das Tasks](./documentation/git-process/ProcessoDeExecucaoTasks.png)


### PR (Pull Request)
Todos os PRs (Pull Requests) devem ter o corpo da mensagem abaixo:

Modelo: Features
```
Features:
  * Task X: Implementação da criação do item H
  * Task Y: Inclusão de novo endpoint na API W para cadastrar Z
```
Modelo: Bug Fix
```
Bug Fix
  * Task U.1: Correção do cadastro do item ABC
```

### Commits

Todos os commits devem contem uma das seguintes tags abaixo:

* *feat*: implementação de uma funcionalidade nova
* *fix*: correção de bugs
* *chore*: ajustes simples no código sem comprometer uma feature
* *style*: ajustes de identação, nome de variável ou remoção de imports não utilizados
* *docs*: alteração ou inclusão de documentação
* *ci*: alteração de pipelines
* *refactor*: refatoração de métodos e classes para uma melhor leitura do código ou desempenho
* *test*: inclusão de testes unitários ou de integração

Mensagem de todo commit:

Modelo
```
TAG_COMMIT: mensagem descrevendo o que foi feito no commit
```
Exemplo
```
feat: Foi criado novo endpoint na implementação de Y
```

---

## Gerar docker build

- Vá até o diretório da aplicação, onde se encontra o pom.xml

- Execute o comando maven para gerar o pacote

  ```bash
  mvn clean package
  ```

- Buildar o dockerfile, informando o Nome da imagem e a TAG, ao final o "." define que é para usar o dockerfile contino no diretório corrente

  ```bash
  docker build -t totem-food-service:1.0.0 .
  ```

- Para visualizar a imagem criada, basta listar com o comando ` docker images` 

- Para executar um container baseado na imagem gerada, basta executar o comando abaixo

  ```bash
  docker run -d -p 8080:8080 --name totem-food-service -t totem-food-service:1.0.0
  ```

- Para ver os logs, execute

  ```bash
  docker logs totem-food-service --follow
  ```

- Para matar a execução do container, execute

  ```bash
  docker rm -f totem-food-service
  ```

---

## Pipeline 

- Para análise de código e vulnerabilidade, utilizamos a ferramenta Sonar Cloud (https://sonarcloud.io/)

 #### Organization: https://sonarcloud.io/organizations/fiap-tech-challenge-4787bf24ba181ac258cb520837a657896a92044c/members
 
  