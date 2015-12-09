/**
 * @author Marcelo C. M. Muniz
 *
 */
package br.usp.nilc.unitex;


public class Lexicon {

	private boolean carregado; // informa se o dicionario esa ou nao carregado
	private Alphabet alph; 
	private INF_codes inf;
	private int[] bin;



	/**
	 * A rotina 'CarregaDicionario' carrega o dicionrio compactado.
	 * @param path_dic Deve fornecer a localizao (absoluta ou
	 * relativa) do arquivo que contm este dicionrio. O arquivo com os
	 * cdigos de flexo, deve estar no mesma mesma localizao mas deve ter
	 * a extenso .inf ao contrrio de .bin do dicionrio compactado.
	 * @param path_alph Deve fornecer a localizao (absoluta ou
	 * relativa) do arquivo que contm o alfabeto.
	 * @return Devolve um cdigo de controle:<br>
	 * 0 - T�rmino normal da operao.<br>
	 * 1 - Erro na abertura de arquivo do alphabeto.<br>
	 * 2 - Erro na abertura de arquivo dic.bin<br>
	 * 3 - Erro na abertura de arquivo dic.inf
	 */
	public int CarregaDicionario(String path_dic, String path_alph){
		int i = 0;
		String nom_inf;
		String nom_bin;
		
		if (carregado == true) {
			LiberaDicionario();
		}  
		
		alph= new Alphabet();
		inf = new INF_codes();
		
		if (alph.load_alphabet(path_alph) == false) {
			i = 1;
		} else {
			nom_inf = path_dic.substring(0,path_dic.lastIndexOf('.'));
			nom_bin = nom_inf;
		   	nom_inf = nom_inf + ".inf";
		   	nom_bin = nom_bin + ".bin";
		   	
		   	inf=DELA.load_INF_file(nom_inf);
		   	if (inf == null) {
				i = 3;
		   	} else {
				bin = DELA.load_BIN_file(nom_bin);
			  	if (bin==null) {
					inf = null;
				 	i = 2;
			  	} else {
					i = 0;
			  	}
		   	}
		} 
		if (i == 0) carregado = true;
		else  carregado = false;
		return i;
	}
	
	public void LiberaDicionario(){
		alph = null;
		inf = null;
		bin = null;
		carregado = false;	
	}

	public String DescompactaEntradaSimples(String entrada){
		return explorer_bin_simples_tokens(4,entrada,new String(),0,0);
	}

	private String explorer_bin_simples_tokens(int pos,String contenu,String entry,
									 int string_pos,int token_number) {
		String saida = "";
		int n_transitions;
		int ref;
		int cond;
		n_transitions=bin[pos]*256+bin[pos+1];
		pos=pos+2;
		if (string_pos == contenu.length()) {
	   	// if we are at the end of the string
	   		//entry[string_pos]='\0';
			cond = (n_transitions & 32768);
			cond = cond / 32768;
	   		if (cond != 1) {
		 		ref=(bin[pos])*256*256+(bin[pos+1])*256+bin[pos+2];
		 		token_list tmp = inf.tab[ref];
		 		while (tmp!= null) {
					String res = uncompress_entry(entry,tmp.token) + ";";
					saida = saida + res; 
					tmp=tmp.suivant;
				}
	   		}
	   		return saida;
		}
		cond = (n_transitions & 32768);
		cond = cond / 32768;
		//if ((n_transitions & 32768) == 1) {
		if (cond == 1) {
	   	// if we are in a normal node, we remove the control bit to
	   	// have the good number of transitions
	   		n_transitions=n_transitions-32768;
		} else {
	  	// if we are in a final node, we must jump after the reference to the INF line number
	  		pos=pos+3;
		}
		for (int i=0;i<n_transitions;i++) {
			char c = (char)((bin[pos])*256+bin[pos+1]);
			pos=pos+2;
			int adr=(bin[pos])*256*256+(bin[pos+1])*256+bin[pos+2];
			pos=pos+3;
			if (Alphabet.is_equal_or_case_equal(c,contenu.charAt(string_pos),alph)) {
			   // we explore the rest of the dictionary only
			   // if the dico char is compatible with the token char
			   entry = entry + c;
			   return saida + explorer_bin_simples_tokens(adr,contenu,entry,string_pos+1,token_number);
			}
		}
		return saida;
	}


	//
	//	this function takes an entry of the BIN file and a code of the INF file
	//	it returns in res the rebuilt line
	//	Example: entry="mains" & info="1.N:fs"  ==>  res="mains,main.N:fs"
	//
 	private String uncompress_entry(String entry,String info) {
		String res;
 		int n;
 		int pos,i;
 		//copy_inflected(res,entry);
 		res = entry;
 		res = res + ',';
 		if (info.charAt(0)=='.') {
			// first case: lemma is the same that entry
			res = res + info;
			return res;
 		}
 		if (info.charAt(0)=='_') {
		// in this case we rawly suppress chars, before adding some
			pos=1;
			n=0;
			// we read the number of chars to suppress
			while (info.charAt(pos)>='0' && info.charAt(pos)<='9') {
		   		n=n*10+(info.charAt(pos)-'0');
	   			pos++;
			}
			res = res + entry;
			i=res.length()-n;
			res = res.substring(0,i+1);
			info = info.substring(pos);
	  		res= res + info;
			return res;
 		}
		//	last case: we have to process token by token
 		int pos_entry = 0;
 		pos = 0;
 		i= res.length();
 		while (info.charAt(pos)!='.') {
   			if (info.charAt(pos)==' ' || info.charAt(pos)=='-') {
	  		// case of a separator
	  			res= res + info.charAt(pos++);
	  			pos_entry++;
   			}
   			else {
	  			char[] tmp = new char[10000];
	  			char[] tmp_entry = new char[10000];
	  			// we read the compressed token
				int j=0;
	  			while (info.charAt(pos)!='.' && info.charAt(pos)!=' ' && info.charAt(pos)!='-') {
		 			tmp[j++]=info.charAt(pos++);
	  			}
	  			tmp[j]='\0';
	  			// and now the entry token
	  			j=0;
				while (pos_entry < entry.length() && entry.charAt(pos_entry)!='.' && entry.charAt(pos_entry)!=' ' && entry.charAt(pos_entry)!='-') {
		 			tmp_entry[j++]=entry.charAt(pos_entry++);
	  			}
	  			tmp_entry[j]='\0';
				String tmp_entry_Str = rebuild_token((new String(tmp_entry,0,j)),tmp);
	  			res = res + tmp_entry_Str;
   			}
 		}
 		while (pos < info.length()) {
   			res= res + info.charAt(pos++);
 		}
 		return res;
 	}

	private String rebuild_token(String res,char[] info) {
		int n=0;
		int i,pos=0;
		while (info[pos]>='0' && info[pos]<='9') {
	  		n=n*10+(info[pos]-'0');
	  		pos++;
		}
		i=res.length()-n;
		if (i<0) {
	   		i=0;
		}
		res = res.substring(0,i);
		while (info[pos]!='\0') {
	  		if (info[pos]=='\\') {
		 		pos++;
	  		}
	  		res= res + info[pos++];
		}
		return res;
	}


}
