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

## Manual de utilização:

Para este exemplo, utilizámos as ontologias de estudo de caso (**MyBook_Target.ttl** e **DbpediaOntology_Source.ttl**) e o ficheiro **dataset.ttl** que podem ser encontrados em: *src/main/resources/examples/*

**Página Inicial:**

![image](https://user-images.githubusercontent.com/58669165/94346448-9bf0bc00-0024-11eb-92bc-a7b41bd4b16e.png)

Nesta página é necessário inserir os ficheiros com as ontologias fonte e alvo e especificar em que linguagens de serialização estão essas ontologias apresentadas. As linguagens de serialização suportadas são: RDF/XML, Notation3, Turtle e N-Triples.

**Página de edição de Mapeamentos:**

![image](https://user-images.githubusercontent.com/58669165/94346476-cb072d80-0024-11eb-8e44-04c814f0939c.png)

1. Escolhe-se uma classe ou propriedade da ontologia alvo

![image](https://user-images.githubusercontent.com/58669165/94346488-e07c5780-0024-11eb-969e-c65211254513.png)

2. Escolhe-se uma classe ou propriedade da ontologia fonte

![image](https://user-images.githubusercontent.com/58669165/94346504-f427be00-0024-11eb-94fb-9e2b629b3cdd.png)

De seguida, irá aparecer a AM que relaciona a ontologia alvo com a ontologia fonte

![image](https://user-images.githubusercontent.com/58669165/94346513-0275da00-0025-11eb-9c24-d7988220d3e5.png)

3. Caso seja necessário pode-se definir filtros ou funções de transformação (a última funcionalidade apenas está disponivel para as AMs de propriedades de tipos de dados). 
No exemplo, como se se trata de uma AM de classes, vamos exemplificar como se cria um filtro:
   **Carregar no botão filtro:**

![image](https://user-images.githubusercontent.com/58669165/94346865-7ca75e00-0027-11eb-9600-deee450e7b6b.png)

   As propriedades que aparecem na combobox são dinâmicas e dependem da classe ou propriedade da ontologia fonte escolhida. Neste caso, aparecem todas as propriedades pertencente à classe dbo:Book.

   **Preencher os campos:**
      
![image](https://user-images.githubusercontent.com/58669165/94346887-a3659480-0027-11eb-93f9-6ef792a65211.png)

   **Carregar no botão selecionar**
  
4. Depois de selecionado o filtro, basta adicionar a AM, carregando no botão adicionar que se encontra à frente da AM:

![image](https://user-images.githubusercontent.com/58669165/94346918-c98b3480-0027-11eb-8f5b-a191581409bc.png)

5. Após o passo anterior, a AM já consta na lista de assertivas

![image](https://user-images.githubusercontent.com/58669165/94346929-f0496b00-0027-11eb-9fff-159c73145b27.png)

6. Para obter o tripleset com os dados é necessário ir a “Obter resultados”:

![image](https://user-images.githubusercontent.com/58669165/94346939-01927780-0028-11eb-84ac-873ba7da3a3d.png)

  Irá aparecer uma modal com a lista de todas as AMs definidas. Neste momento é possível eliminar as AMs que não necessite. 

7. Após carregar no botão “Continuar”, irá aparecer um formulário que permite especificar qual o dataset e em que linguagem o mesmo está escrito. Além disso, é possível tamém especificar em que linguagem de serialização é que quer que os dados sejam escritos:

![image](https://user-images.githubusercontent.com/58669165/94346962-3272ac80-0028-11eb-831b-e68d9bade5a5.png)

8. Os dados gerados em RDF/XML para a AM criada são: 

![image](https://user-images.githubusercontent.com/58669165/94346975-461e1300-0028-11eb-8e1e-68426df80d8d.png)
