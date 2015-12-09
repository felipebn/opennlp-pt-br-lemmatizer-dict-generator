# OpenNLP Lemmatizer Dictionary Generator
Lemma dictionary generator for OpenNLP's Lemmatizer base on CoGrOO PT-BR dictionary.

This generator uses CoGrOO dictionaries to generate a file which can be use as input for OpenNLP's Lemmatizer.

Lemmas are discovered using UNITEX-PB dictionary and lemmatizer.

I made it because it was to hard to find an already formated dictionary expected by OpenNLP's Lemmatizer. 

## How to use

* Clone this repository
* Import the project using Eclipse
* Run `org.opennlp.dict.generator.LemmatizerDictionaryGenerator.main`

## Ouput tags

This program will discard or convert some UNITEX tags for convenience as follows:

Original | Converted
--- | ---
DET+Art******** | DET+Art
DET+Num******** | DET+Num
PREP******** | DET+Art 
Class+******** | Class


# License

http://www.apache.org/licenses/LICENSE-2.0