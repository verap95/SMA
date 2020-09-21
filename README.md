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
