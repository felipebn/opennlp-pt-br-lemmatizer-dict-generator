/*
 * Copyright 2015 Felipe Brandão Nascimento
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.github.felipebn.opennlp.dictionary.generator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import org.cogroo.dictionary.impl.FSADictionary;
import org.cogroo.util.PairWordPOSTag;

/**
 * Dictionary generator using Jspell dictionary
 * and UNITEX tagging.
 * 
 * @author felipebn
 *
 */
public class LemmatizerDictionaryGenerator {

	private File output;

	/**
	 * @param output File to be used as output (will be overwritten if already exists)
	 */
	public LemmatizerDictionaryGenerator(File output) {
		this.output = output;
	}
	
	/**
	 * Iterates through the Jspell dictionary building the output to
	 * be used as input on OpneNLP Lemmatizer.
	 * 
	 * The posTags outputed will be in UNITEX format.
	 * 
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public void generatePtBr() throws IllegalArgumentException, IOException{
		PrintWriter writer = new PrintWriter(this.output);
		UnitexLemmatizer unitex = new UnitexLemmatizer("unitex-pb.inf", "Alphabet.txt");
		try{
			
			FSADictionary dict = FSADictionary.createFromResources("/fsa_dictionaries/pos/pt_br_jspell.dict");
			Iterator<String> it = dict.iterator();
			
			//Flush counter
			int f = 0;
			
			while( it.hasNext() ){
				final String palavra = it.next();
				//Collection<PairWordPOSTag> tagsAndLemms = dict.getTagsAndLemms(palavra);
				Collection<PairWordPOSTag> tagsAndLemms = unitex.getLemmas(palavra);
				for (PairWordPOSTag lemmaAndTag : tagsAndLemms) {				
					writer.write( String.format("%s\t%s\t%s\n",palavra,lemmaAndTag.getPosTag(),lemmaAndTag.getWord()) );
					if( ++f % 1000 == 0 ){
						System.out.println( String.format("Flush (%d)",f) );
						writer.flush();
					}
				}
			}
		}finally{
			unitex.destroy();
			writer.flush();
			writer.close();
		}
	}
	
	
	/**
	 * Main program
	 * @param args
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IllegalArgumentException, IOException {
		new LemmatizerDictionaryGenerator(new File("pt-lemmatizer.dict"))
			.generatePtBr();
	}
}
