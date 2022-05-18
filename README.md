# library-api
[![codecov](https://codecov.io/gh/brunoyillli/library-api/branch/master/graph/badge.svg?token=KED8B9DLGT)](https://codecov.io/gh/brunoyillli/library-api)
[![Java CI with Maven](https://github.com/brunoyillli/library-api/actions/workflows/maven.yml/badge.svg)](https://github.com/brunoyillli/library-api/actions/workflows/maven.yml)

# Resumo do projeto
Projeto de api para uso em bibliotecas, realizado para estudar mais a fundo TDD e JUnit5.

## Setup

### Java Version
Projeto rodando com Java 8, porém você pode rodar com o java 11

### Informações úteis 
* Documentacao da api: http://localhost:8080/swagger-ui/index.html#/ 
* Actuator para obter metricas em geral sobre a API: http://localhost:8080/actuator
* Arquivo de log: http://localhost:8080/actuator/logfile ou pelo arquivo libraryapilog.log na raiz do projeto.
* Na raiz do projeto execute o comando maven na raiz do projeto 'mvnw test' é então gerado na pasta target\site\jacoco a cobertura do código, acesse o index.html para melhor visualização.
* O banco de dados está local usando o H2, mas pode ser configurado um banco relacional facilmente pelo arquivo application.properties 
