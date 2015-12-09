/**
 * @author Marcelo C. M. Muniz
 *
 */


package br.usp.nilc.unitex;

import java.io.*;

import br.usp.nilc.unitex.exceptions.*;


public class DELA {


	public static token_list inserer_token(String token, token_list l) {
		token_list tmp;
		tmp= new token_list();
		tmp.token= token;
		tmp.suivant=l;
		return tmp;
	}



	//
	//	this function takes a line of a .INF file and tokenize it into
	//	several single codes.
	//	Example: .N,.V  =>  token 0=".N" ; token 1=".V"
	//
 	public static token_list tokenize_compressed_info(String line) {
 		token_list res=null;
 		String tmp="";
 		int pos,i;
 		pos=0;
 		while (pos < line.length()) {
			if (line.charAt(pos)==',') {
	   		// if we are at the end of a token
	   			res=inserer_token(tmp,res);
	   			tmp = "";
	   			pos++;
			}
			else {
		   		if (line.charAt(pos)=='\\') {
			  	// if we find a backslash, we take it with the following char
			  	// doing like this avoids to take a protected comma for a token separator
					tmp=tmp + line.charAt(pos++);
	   			}
		   		tmp=tmp + line.charAt(pos++);
			}
 		}
 		res=inserer_token(tmp,res);
 		return res;
 	}



	public static INF_codes load_INF_file(String nom) {
		INF_codes res = null;
		File infile;
		FileInputStream f;
		
		infile= new File(nom);
		if (!infile.exists()) {
		   System.out.println("Cannot find " + nom );
		   return res;
		}
		if (!infile.canRead()) {
			System.out.println("Cannot read " + nom );
		   return res;
		}
		if (infile.length() <= 2) {
			System.out.println(nom + " is empty" );
		   return res;
		}
		try {
			f= UnicodeIO.openUnicodeLittleEndianFileInputStream(infile);
			res = new INF_codes();
			res.N= Integer.parseInt(UnicodeIO.readLine(f));
			res.tab= new token_list[res.N];
			String s = UnicodeIO.readLine(f);
			int i=0;
			while (s.compareTo("") != 0) {
	  			res.tab[i++]=tokenize_compressed_info(s);
				s = UnicodeIO.readLine(f);
			}
			f.close();
		} catch (NotAUnicodeLittleEndianFileException e) {
			System.out.println( nom + " is not a Unicode Little-Endian text");
			return res;
		}
		catch (IOException e)
		{
			throw new IllegalArgumentException("Error estabilishing input stream to file " + nom + 
			": " + e.getMessage());
		}
	return res;
	}
	
	public static int[] load_BIN_file(String nom) {
		byte[] tab;
		int[] res;

		try
		{
			FileInputStream f =new FileInputStream(nom);
			int a,b,c,d;
			a=f.read();
			if (a < 0) a = 256 + a;
			b=f.read();
			if (b < 0) b = 256 + b;
			c=f.read();
			if (c < 0) c = 256 + c;
			d=f.read();
			if (d <0) d = 256 + d;
			int taille=d+256*c+256*256*b+256*256*256*a;
			f.close();
			f = null;

			f =new FileInputStream(nom);

			tab= new byte[taille];
			res = new int[taille];
			if (tab == null) {
	   			System.out.println("Memory error: cannot load " + nom);
			   return null;
			}
			if (taille!= f.read(tab,0,taille)) {
				System.out.println("Error while reading " + nom);
	   			tab = null;
	   			f.close();
	   			return null;
			}
			f.close();
			for (int i =0; i < taille; i++){
				if ((int)tab[i] < 0) res[i] = 256 + (int) tab[i];
				else res[i] = tab[i];
			}
			
		}
		catch (IOException e)
		{
			throw new IllegalArgumentException("Error estabilishing input stream to file " + nom + 
			": " + e.getMessage());
		}
		return res;
	}

}
