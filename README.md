# Gerador de dicionário para Lemmatizer do OpenNLP

Gerador de dicionários de lemmas para o Lemmatizer do OpenNLP baseado no dicionário PT-BR do CoGrOO.

Este gerador utiliza os dicionários do CoGrOO para gerar um arquivo que pode ser utilizado como input
para o Lemmatizer do OpenNLP.

Os lemmas são identificados utilizando o dicionário e lemmatizer do UNITEX-PB.

Foi criado devido a dificuldade de localizar ou real inexistência de um arquivo no formato
esperado pelo OpenNLP.

## Como Utilizar

* Faça o clone do projeto
* Importe o projeto no Eclipse
* Execute a main `org.opennlp.dict.generator.LemmatizerDictionaryGenerator.main`

## Ouput tags

O programa vai descartar ou converter algumas tags do UNITEX como segue:

Original | Convertida
--- | ---
DET+Art******** | DET+Art
DET+Num******** | DET+Num
PREP******** | DET+Art 
Classe+******** | Classe

# License

http://www.apache.org/licenses/LICENSE-2.0