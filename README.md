Backend do projeto do curso de Desenvolvimento de Sistemas para Internet

Requisitos mínimos necessários
gradle
java 8
mariadb 10.6.5

Abaixo seguem os passos para instalar e rodar o projeto localmente

criar um banco de dados local para uso com o projeto
caso o acesso ao banco utilize senha o arquivo application.properties
na raiz do projeto deve ser ajustado para a senha correspondente

Abaixo as linhas que deve ser ajustadas
spring.datasource.url=jdbc:mysql://localhost:3306/minha_fortuna?seTimezone=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root

na pasta raiz do projeto execute os seguintes comandos

gradle build bootjar

java -jar build\libs\minhafortuna-backend-0.0.1-SNAPSHOT.jar



esse projeto funciona em paralelo ao frontend localizado em
https://github.com/ezelorenzatti/minhafortuna-frontend
