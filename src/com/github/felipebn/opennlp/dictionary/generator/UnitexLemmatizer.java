package com.github.felipebn.opennlp.dictionary.generator;

import java.util.ArrayList;
import java.util.Collection;

import org.cogroo.util.PairWordPOSTag;

import br.usp.nilc.unitex.DelaEntry;
import br.usp.nilc.unitex.Lexicon;

/**
 * Lemmatizer which uses UNITEX dictionary and USP Lexicon
 * to extract words lemmas and tags.
 * 
 * @author felipebn
 *
 */
public class UnitexLemmatizer {

	private Lexicon unitexPB;

	public UnitexLemmatizer(String arquivoInf, String alphabet) {
	    this.unitexPB = new Lexicon();//inicializa objeto de acesso ao lexico
	    this.unitexPB.CarregaDicionario(arquivoInf, alphabet);
	}
	
	public Collection<PairWordPOSTag> getLemmas(String palavra){
		Collection<PairWordPOSTag> retorno = new ArrayList<PairWordPOSTag>();
		String flexoes = unitexPB.DescompactaEntradaSimples(palavra);
	    DelaEntry resultado = new DelaEntry(flexoes);
		for (int i=0; i< resultado.size(); i++){
			//exibe cada uma das entradas encontradas no lexicon para a palavra
			final String r = resultado.getEntry(i);
			//Separa as informações para montar o retorno
			String[] split1 = r.toString().split(",");
			String original = split1[0];
			String[] infoLemma = split1[1].split("\\.");
			String lemma = infoLemma[0];
			String tagLemma = infoLemma[1].split(":")[0];
			
			if( original.equals(lemma) ) continue;
	        retorno.add(new PairWordPOSTag(lemma, tagLemma));
	    }
	    return retorno;
	}
	
	public void destroy(){
		this.unitexPB.LiberaDicionario();
	}
}
