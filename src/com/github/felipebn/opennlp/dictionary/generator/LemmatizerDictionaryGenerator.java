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

import com.google.common.collect.Table.Cell;
import com.google.common.collect.TreeBasedTable;

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
			
			//Build output table making every row unique and naturally ordered by word
			System.out.println( "Building output table on memory..." );
			TreeBasedTable<String, String, String> output = TreeBasedTable.<String, String, String>create();
			while( it.hasNext() ){
				final String word = it.next();
				Collection<PairWordPOSTag> tagsAndLemms = unitex.getLemmas(word);
				for (PairWordPOSTag lemmaAndTag : tagsAndLemms) {
					output.put(word, lemmaAndTag.getPosTag(),lemmaAndTag.getWord());
				}
			}
			
			//Store unique pos tags to be printed when finished
			final String uniquePosTags = output.columnKeySet().toString();
			
			//Writes to file
			Iterator<Cell<String, String, String>> rows = output.cellSet().iterator();
			while (rows.hasNext()) {
				Cell<String, String, String> r = rows.next();
				writer.println( String.format("%s\t%s\t%s",r.getRowKey(),r.getColumnKey(),r.getValue()) );
				if( ++f % 1000 == 0 ){
					System.out.println( String.format("Flushing (%d) ...",f) );
					writer.flush();
				}
				rows.remove();
			}
			
			System.out.println( "Unique postags:" + uniquePosTags );
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
		new LemmatizerDictionaryGenerator(new File("pt-br-lemmatizer.dict"))
			.generatePtBr();
	}
}
