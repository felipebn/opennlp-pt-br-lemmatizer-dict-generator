/*
 * Created on 02/10/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

/**
 * Esta classe contem uma estrutura de dados para manipular 
 * uma entrada descompactada de um dicionario DELAF.
 * @author Marcelo C. M. Muniz
 *
 */
package br.usp.nilc.unitex;


public class DelaEntry {
	private int n; // Número de classificacoes
	private String entrada;
	private String canonica[];
	private String classe[];
	private String traco[]; //tracos
	private String flexao[];

	/**
 	* DelaEntry contrutor default. Recebe uma <code>String</code> com a 
 	* representa��o da entrada descompactada e cria um objeto com essa 
 	* entrada divida em estruturas. Caso a entrada seja vazia o n�mero
 	* de classifica��o ser� 0.  
 	* @param entradaDescompactada <code>String</code> com a entrada descompactada.
 	*/

	public DelaEntry(String entradaDescompactada){
		String entradaAtual;
		n = 0;
		if (entradaDescompactada.length() > 0)
		{
			for (int i = 0; i < entradaDescompactada.length(); i++ ){
				if (entradaDescompactada.charAt(i) == ';') n++; 
			}
			entrada = entradaDescompactada.substring(0,entradaDescompactada.indexOf(','));
			canonica = new String[n];
			classe = new String[n];
			traco = new String[n];
			flexao = new String[n];
			for (int i = 0; i < n; i++){
				entradaAtual = entradaDescompactada.substring(0,entradaDescompactada.indexOf(';'));
				entradaDescompactada = entradaDescompactada.substring(entradaDescompactada.indexOf(';')+1);
				entradaAtual = entradaAtual.substring(entradaAtual.indexOf(',')+1);
				if (entradaAtual.charAt(0) == '.') {
					canonica[i] = entrada;
					entradaAtual = entradaAtual.substring(1);
				} else {
					canonica[i] = entradaAtual.substring(0,entradaAtual.indexOf('.'));
					entradaAtual = entradaAtual.substring(entradaAtual.indexOf('.')+1);
				}
				// Se a entrada tem informa��es de flex�o
				if (entradaAtual.indexOf(':') > 0){
					// Se a entrada tem informa��o de tra�os
					if (entradaAtual.indexOf('+') > 0){
						classe[i] = entradaAtual.substring(0,entradaAtual.indexOf('+'));
						entradaAtual = entradaAtual.substring(entradaAtual.indexOf('+'));
						traco[i] = entradaAtual.substring(0,entradaAtual.indexOf(':'));
						entradaAtual = entradaAtual.substring(entradaAtual.indexOf(':'));
						flexao[i] = entradaAtual;
					}
					// Se a entrada n�o tem informa��es de tra�os 
					else {
						classe[i] = entradaAtual.substring(0,entradaAtual.indexOf(':'));
						entradaAtual = entradaAtual.substring(entradaAtual.indexOf(':'));
						traco[i] = "";
						flexao[i] = entradaAtual;
					}
				}
				// Se a entrada n�o tem informa��es de flex�o
				else {
					// Se a entrada tem informa��o de tra�os
					if (entradaAtual.indexOf('+') > 0){
						classe[i] = entradaAtual.substring(0,entradaAtual.indexOf('+'));
						entradaAtual = entradaAtual.substring(entradaAtual.indexOf('+'));
						traco[i] = entradaAtual;
						flexao[i] = "";
					}
					// Se a entrada n�o tem informa��es de tra�os 
					else {
						classe[i] = entradaAtual;
						traco[i] = "";
						flexao[i] = "";
					}
				}
			} // Fim do For
		}
	} // Fim do construtor
	
	/**
	 * Fun��o que devolve uma espec�fica classifica��o da entrada.
	 * @param i Indice da classifica��o (varia de 0 � <code>size() - 1</code>).
	 * @return Uma string com classifica��o da entrada para determinado indice.
	 * Devolve um ponteiro <code>null</code> caso o indice n�o existir.
	 */
	// Entradas come�am por 0 at� n-1;
	public String getEntry(int i){
		String saida = null;
		if ((i >= 0) && (i < n) ) {
			saida = entrada +","+ canonica[i] +"."+ classe[i]+ traco[i]+flexao[i];
		}
		return saida;
	}
	
	public String getEntry(int i, boolean can, boolean cla, boolean tra, boolean fle ){
		String saida = null;
		if ((i >= 0) && (i < n) ) {
			saida = ",";
			if (can) saida += canonica[i];
			saida += ".";
			if (cla) saida += classe[i];
			if (tra) saida += traco[i];
			if (fle) saida += flexao[i];
		}
		return saida;
	}
	
	public boolean isPartofSpeech(String s){
		boolean saida = false;
		for (int i=0; i < n; i++){
			if (classe[i].equals(s)) saida = true;
		}
		return saida;
	}
	
	public String getLema(String PartofSpeech){
		String saida = null;
		for (int i=0; i < n; i++){
			if (classe[i].equals(PartofSpeech)) saida = canonica[i];
		}
		return saida;
	}
	/**
	 * Fun��o que devolve o n�mero de classifica��es que a entrada possui.
	 * @return N�mero de classifica��es que a entrada possui. Zero se n�o 
	 * possuir nenhuma.
	 */	
	public int size(){
		return n;
	}
}
