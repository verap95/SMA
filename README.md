# SMA (SPARQL Mapping with Assertions)

Ferramenta desenvolvida no âmbito da dissertação: **GERAÇÃO SEMI-AUTOMÁTICA DE MAPEAMENTOS DE VOCABULÁRIOS ENTRE DATASETS DA WEB DE DADOS USANDO SPARQL**

## Introdução:

A ferramenta SPARQL Mapping with Assertions (SMA) tem como principal objetivo a geração automática de mapeamentos SPARQL através da utilização de padrões de mapeamento. Na figura abaixo, é apresentada a arquitetura da ferramenta:

![Arquitetura](https://user-images.githubusercontent.com/58669165/93823259-7006bc80-fc59-11ea-8c11-b5068e16c81f.jpg)

Esta ferramenta é constituida por 4 partes:
* **Parte 1 (configuração inicial das ontologias):** através do módulo GUI, o utilizador indica quais as ontologias que deseja mapear, assim como a linguagem em que os ficheiros das mesmas estão escritos;

* **Parte 2 - criação das Assertivas de Mapeamento (AMs):** através do módulo GUI, o utilizador especifica quais os mapeamentos que deseja definir, incluindo possíveis transformações ou filtros que sejam necessários aplicar aos dados. É a partir das AMs que são geradas as respetivas regras de mapeamento e mapeamentos SPARQL;

* **Parte 3- configuração para a geração de mapeamentos:** através do módulo ER, o utilizador introduz o ficheiro com o Dataset da ontologia fonte e identifica a linguagem de serialização em que o mesmo está escrito. Além disso, também escolhe qual a linguagem de serialização que deseja aquando da geração de triplos;

* **Parte 4 - geração de triplos através dos mapeamentos SPARQL 1.1:** a partir dos pontos anteriores, a nossa ferramenta irá retornar um ficheiro com todos os resultados na linguagem de serialização escolhida.

Além disso, a partir do:

* **Módulo CRM –** é possível obter a lista de todas as regras de mapeamento

* **Módulo CMS –** é possível obter a lista de todos os mapeamentos SPARQL

* **Módulo GUI –** é possível obter a lista de todas as assertivas de mapeamento

## Requisitos:

* Java 8 ou superior

## Manual de Instalação:

1. Na pasta target (onde se encontra o jar executável) executar o seguinte comando: 

```
java -jar .\FrameworkSparqlProject-0.0.1-SNAPSHOT.jar

```

**Nota:** É necessário possuir o java como variável de ambiente, de modo a ser possível executar este comando. Para mais informações consultar: https://confluence.atlassian.com/doc/setting-the-java_home-variable-in-windows-8895.html Caso não possua esta variável de ambiente, mas possua o java, efetue este comando no diretório do java com o caminho completo para o jar.

2. A aplicação está pronta a utilizar. :smiley:


