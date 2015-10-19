package org.opennlp.dict.generator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.cogroo.dictionary.impl.FSADictionary;
import org.cogroo.util.PairWordPOSTag;

/**
 * Dictionary generator
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
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public void generatePtBr() throws IllegalArgumentException, IOException{
		PrintWriter writer = new PrintWriter(this.output);
		
		try{	
			FSADictionary dict = FSADictionary.createFromResources("/fsa_dictionaries/pos/pt_br_jspell.dict");
			Iterator<String> it = dict.iterator();
			
			while( it.hasNext() ){
				
				String palavra = it.next();
				List<PairWordPOSTag> tagsAndLemms = dict.getTagsAndLemms(palavra);
				for (PairWordPOSTag lemmaAndTag : tagsAndLemms) {
					String tag = lemmaAndTag.getPosTag();
					
					//Ignores punctuations
					if( tag.matches("^[^\\w]+$") ) continue;
					
					String lemma = lemmaAndTag.getWord();
					writer.write( String.format("%s\t%s\t%s\n",palavra,tag,lemma) );
					
				}
			}
		}finally{
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
