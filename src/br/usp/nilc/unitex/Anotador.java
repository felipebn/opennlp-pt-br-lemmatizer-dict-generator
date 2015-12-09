/*
 * Created on 04/12/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

/**
 * @author Marcelo C. M. Muniz
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

package br.usp.nilc.unitex;

import java.io.*;


public class Anotador {

	private	Lexicon lexico;
	private String alf;
	private String dir = "C:\\jwsdp-1.2\\webapps\\unitex-pb\\build\\WEB-INF\\classes\\br\\usp\\nilc\\unitex\\";

	public static void main(String[] args) 
	{
		Anotador objeto;
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(args[0]));
			objeto = new Anotador(in);
			in.close();
		}
		catch (IOException e)
		{
			throw new IllegalArgumentException("Error estabilishing input stream to file " + args[0] + 
			": " + e.getMessage());

		}
	}



	public Anotador(BufferedReader in){
		DelaEntry entrada; 
		String linha;
		String saida = "";
		int tamanho;
		String palavra = "";
		

		alf ="";
		carrega_alfabeto(dir + "Alphabet.txt");

		lexico = new Lexicon();
		lexico.CarregaDicionario(dir + "unitex-pb.bin", dir + "Alphabet.txt");

		try{
			PrintWriter out = new PrintWriter( new BufferedWriter( 
				new FileWriter("saida.html")));
			out.println("<HTML><HEAD><TITLE>Anotador</TITLE></HEAD><BODY>");
			out.flush();
			
			linha = in.readLine();
			// Enquanto n�o chegar ao fim do arquivo
			while (linha != null){
				out.println(palavra + "<BR>");
				out.flush();
				tamanho = linha.length();
				// Enquanto nao chegar ao fim da linha
				for (int i =0; i < tamanho; i++){
					//Se for um caracter do alfabeto
					if (alf.indexOf(linha.charAt(i)) != -1){
						palavra = palavra + linha.charAt(i);
					// Se nao for um caracter do alfabeto
					} else {
						// Se a palavra nao for vazia
						if (palavra.length() > 0){
							saida = lexico.DescompactaEntradaSimples(palavra);
							entrada = new DelaEntry(saida);
							saida = "";
							// Se a palavra estiver no dicionario
							if (entrada.size() > 0){
								// Imprime todas anotacoes
								for (int j=0; j < entrada.size(); j++) {
									out.println("{" +palavra + entrada.getEntry(j,false,true, false, true) + "} ");
									out.flush();
								}
								out.println("<BR>");
								out.flush();
							// Se a palavra NAO estiver no dicion�rio
							} else {
								out.println(palavra + "<BR>");
								out.flush();
							}
							// Limpa a palavra para pegar a pr�xima
							palavra = "";
							
							if (linha.charAt(i) != ' ')	{
								out.println(linha.charAt(i) + "<BR>");
								out.flush();
							}
							
						// Se a palavra for vazia
						} else {
							if (linha.charAt(i) != ' ')	{
								out.println(linha.charAt(i) + "<BR>");
								out.flush();
							}
						}
					}
				}
				if (palavra.length() > 0){
					saida = lexico.DescompactaEntradaSimples(palavra);
					entrada = new DelaEntry(saida);
					saida = "";
					// Se a palavra estiver no dicion�rio
					if (entrada.size() > 0){
						// Imprime todas anota��es
						for (int j=0; j < entrada.size(); j++) {
							out.println("{" +entrada.getEntry(j) + "} ");
							out.flush();
						}
						out.println("<BR>");
						out.flush();
					// Se a palavra NAO estiver no dicion�rio
					} else {
						out.println(palavra + "<BR>");
						out.flush();
					}
					// Limpa a palavra para pegar a pr�xima
					palavra = "";
				}
				linha = in.readLine();
			}
			out.println("</BODY></HTML>");
			out.flush();
			out.close();
			lexico.LiberaDicionario();
		}
		catch (IOException e)
		{	
		}
	}
	

	public Anotador(Lexicon lex){
		alf ="";
		carrega_alfabeto( dir + "Alphabet.txt");
		lexico = lex;
	}


	public String Anotada(String linha, boolean canonica, boolean classe, boolean traco, boolean flexao){
		DelaEntry entrada; 
		String saida = "";
		int tamanho;
		String palavra = "";
		String out = "";
		
			
		out += "<BR>";

		tamanho = linha.length();
		// Enquanto n�o chegar ao fim da linha
		for (int i =0; i < tamanho; i++){
			//Se for um caracter do alfabeto
			if (alf.indexOf(linha.charAt(i)) != -1){
				palavra = palavra + linha.charAt(i);
			// Se n�o for um caracter do alfabeto
			} else {
				// Se a palavra n�o for vazia
				if (palavra.length() > 0){
					saida = lexico.DescompactaEntradaSimples(palavra);
					entrada = new DelaEntry(saida);
					saida = "";
					// Se a palavra estiver no dicion�rio
					if (entrada.size() > 0){
						// Imprime todas anota��es
						for (int j=0; j < entrada.size(); j++) {

							out += "{" +palavra + entrada.getEntry(j,canonica, classe, traco, flexao) + "} ";
						}
						out += "<BR>";
					// Se a palavra NAO estiver no dicion�rio
					} else {
						out += palavra + "<BR>";
					}
					// Limpa a palavra para pegar a pr�xima
					palavra = "";
					
					if (linha.charAt(i) != ' ')	{
						out += linha.charAt(i) + "<BR>";
					}
					
				// Se a palavra for vazia
				} else {
					if (linha.charAt(i) != ' ')	{
						out += linha.charAt(i) + "<BR>";
					}
				}
			}
		}
		if (palavra.length() > 0){
			saida = lexico.DescompactaEntradaSimples(palavra);
			entrada = new DelaEntry(saida);
			saida = "";
			// Se a palavra estiver no dicion�rio
			if (entrada.size() > 0){
				// Imprime todas anota��es
				for (int j=0; j < entrada.size(); j++) {
					out += "{" +entrada.getEntry(j) + "} ";
				}
			// Se a palavra NAO estiver no dicion�rio
			} else {
				out += palavra;
			}
			// Limpa a palavra para pegar a pr�xima
		}
		return out;
	}


	public String AnotadaTXT(BufferedReader in, boolean canonica, boolean classe, boolean traco, boolean flexao){
		DelaEntry entrada; 
		String saida = "";
		int tamanho;
		String palavra = "";
		String out = "";
		String linha = "";
		
			
		try{
			linha = in.readLine();
			// Enquanto n�o chegar ao fim do arquivo
			while (linha != null){
				out += System.getProperty("line.separator");
				tamanho = linha.length();
				// Enquanto n�o chegar ao fim da linha
				for (int i =0; i < tamanho; i++){
					//Se for um caracter do alfabeto
					if (alf.indexOf(linha.charAt(i)) != -1){
						palavra = palavra + linha.charAt(i);
					// Se n�o for um caracter do alfabeto
					} else {
						// Se a palavra n�o for vazia
						if (palavra.length() > 0){
							saida = lexico.DescompactaEntradaSimples(palavra);
							entrada = new DelaEntry(saida);
							saida = "";
							// Se a palavra estiver no dicion�rio
							if (entrada.size() > 0){
								// Imprime todas anota��es
								for (int j=0; j < entrada.size(); j++) {
									out += "{" +palavra + entrada.getEntry(j,canonica,classe, traco, flexao) + "} ";
								}
								out += System.getProperty("line.separator");
							// Se a palavra NAO estiver no dicion�rio
							} else {
								out += palavra + System.getProperty("line.separator");
							}
							// Limpa a palavra para pegar a pr�xima
							palavra = "";
							
							if (linha.charAt(i) != ' ')	{
								out += linha.charAt(i) + System.getProperty("line.separator");
							}
							
						// Se a palavra for vazia
						} else {
							if (linha.charAt(i) != ' ')	{
								out += linha.charAt(i) + System.getProperty("line.separator");
							}
						}
					}
				}
				if (palavra.length() > 0){
					saida = lexico.DescompactaEntradaSimples(palavra);
					entrada = new DelaEntry(saida);
					saida = "";
					// Se a palavra estiver no dicion�rio
					if (entrada.size() > 0){
						// Imprime todas anota��es
						for (int j=0; j < entrada.size(); j++) {
							out += "{" +entrada.getEntry(j) + "} ";
						}
						out += System.getProperty("line.separator");
					// Se a palavra NAO estiver no dicion�rio
					} else {
						out += palavra + System.getProperty("line.separator");
					}
					// Limpa a palavra para pegar a pr�xima
					palavra = "";
				}
				linha = in.readLine();
			}
		}
		catch (IOException e)
		{	
		}
		return out;
	}

	public String AnotadaHTML(BufferedReader in, boolean canonica, boolean classe, boolean traco, boolean flexao){
		DelaEntry entrada; 
		String saida = "";
		int tamanho;
		String palavra = "";
		String out = "";
		String linha = "";
		
			
		try{
			linha = in.readLine();
			// Enquanto n�o chegar ao fim do arquivo
			while (linha != null){
				out += "<BR>";
				tamanho = linha.length();
				// Enquanto n�o chegar ao fim da linha
				for (int i =0; i < tamanho; i++){
					//Se for um caracter do alfabeto
					if (alf.indexOf(linha.charAt(i)) != -1){
						palavra = palavra + linha.charAt(i);
					// Se n�o for um caracter do alfabeto
					} else {
						// Se a palavra n�o for vazia
						if (palavra.length() > 0){
							saida = lexico.DescompactaEntradaSimples(palavra);
							entrada = new DelaEntry(saida);
							saida = "";
							// Se a palavra estiver no dicion�rio
							if (entrada.size() > 0){
								// Imprime todas anota��es
								for (int j=0; j < entrada.size(); j++) {
									out += "{" +palavra + entrada.getEntry(j,canonica,classe, traco, flexao) + "} ";
								}
								out += "<BR>";
							// Se a palavra NAO estiver no dicion�rio
							} else {
								out += palavra + "<BR>";
							}
							// Limpa a palavra para pegar a pr�xima
							palavra = "";
							
							if (linha.charAt(i) != ' ')	{
								out += linha.charAt(i) + "<BR>";
							}
							
						// Se a palavra for vazia
						} else {
							if (linha.charAt(i) != ' ')	{
								out += linha.charAt(i) + "<BR>";
							}
						}
					}
				}
				if (palavra.length() > 0){
					saida = lexico.DescompactaEntradaSimples(palavra);
					entrada = new DelaEntry(saida);
					saida = "";
					// Se a palavra estiver no dicion�rio
					if (entrada.size() > 0){
						// Imprime todas anota��es
						for (int j=0; j < entrada.size(); j++) {
							out += "{" +entrada.getEntry(j) + "} ";
						}
						out += "<BR>";
					// Se a palavra NAO estiver no dicion�rio
					} else {
						out += palavra + "<BR>";
					}
					// Limpa a palavra para pegar a pr�xima
					palavra = "";
				}
				linha = in.readLine();
			}
		}
		catch (IOException e)
		{	
		}
		return out;
	}



	private boolean carrega_alfabeto(String alfabeto){
		try{
			BufferedReader in = new BufferedReader(new FileReader(alfabeto));
			String linha = in.readLine();
			while (linha != null){
				alf = alf + linha;
				linha = in.readLine();			
			}
			return true;
		}
		catch (IOException e)
		{
			return false;
		}
	}
	
}
