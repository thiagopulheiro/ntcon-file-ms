Oi. Para executar o projeto, e necessario ter o Docker instalado na maquina,
pois o mesmo esta subindo o RabbitMQ para utilizacao fora de container.

## debitos tecnicos intencionais

Nao foi realizado o bi-relacionamento das classes de Sale e SalesPerson pois nao estava 
claro no exercicio qual era a relacao entre estas referencias. Referencia era apenas por nome
e nao garantida na ordem do arquivo.

Nao foi tratado empates para melhor venda e pior vendedor pois iria extrapolar as 72 horas de exercicio.

Nao foi realizado os testes de integracao pois iria extrapolar as 72 horas de exercicio.

## importante

O diretorio dos arquivos de entrada e saida sao configuraveis
no arquivo resources/application.properties.

A aplicacao nao esta criando o diretorio caso nao exista,
entao por favor, selecione o diretorio antes de executar e
salve ele corretamente editando as propriedades, ex:

file.dir.in=/Users/xpto/data/in
file.dir.out=/Users/xpto/data/out

## para executar
```
./mvnw clean install
docker-compose up &
java -jar ./target/thiago-0.0.1-SNAPSHOT.jar
```


Muito obrigado, 
Thiago