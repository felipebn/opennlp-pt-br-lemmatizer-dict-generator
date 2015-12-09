
/**
 * @author Marcelo C. M. Muniz
 *
 */

package br.usp.nilc.unitex;

import java.io.*;

import br.usp.nilc.unitex.exceptions.*;

public class Alphabet {

	private String t[];
	char t2[];
	
	public Alphabet(){
		t = new String[65536];
		t2 = new char[65536];
		for (int i =0; i < 65536; i++){
			t2[i]=0;
		}
	}
		
	public boolean load_alphabet(String n){
		File infile;
		FileInputStream f;
		infile= new File(n);
		if (!infile.exists()) {
		   System.out.println("Cannot find " + n );
		   return false;
		}
		if (!infile.canRead()) {
			System.out.println("Cannot read " + n );
		   return false;
		}
		if (infile.length() <= 2) {
			System.out.println(n + " is empty" );
		   return false;
		}
		try {
			f= UnicodeIO.openUnicodeLittleEndianFileInputStream(infile);
			int c;
			char min,maj;
			c = UnicodeIO.readChar(f);
			while (c!= -1) {
				maj=(char)c;
				if (maj=='#') {
					 // we are in the case of an interval #AZ -> [A..Z]
					min= (char)UnicodeIO.readChar(f);
					maj= (char)UnicodeIO.readChar(f);
					if (min>maj) {
						System.out.println("Error in alphabet file: for an interval like #AZ, A must be before Z");
						f.close();
						return false;
					}
					for (c=min;c<=maj;c++) {
						t2[c]='1';
				   		t2[c]='2';
				   		ajouter_min_maj((char)c,(char)c);
					}
					UnicodeIO.readChar(f); // reading the \n
				}
				else {
					t2[maj]='1';
					min=(char)UnicodeIO.readChar(f);
					if (min!='\n') {
						t2[min]='2';
					  	UnicodeIO.readChar(f); // reading the \n
					  	ajouter_min_maj((char)min,(char)maj);
					}
					else {
					  // we are in the case of a single (no min/maj distinction like in thai)
					  	t2[maj]='2';
					  	ajouter_min_maj((char)maj,(char)maj);
					}
				}
				c = UnicodeIO.readChar(f);
			}
			f.close();
			return true;
		} catch (NotAUnicodeLittleEndianFileException e) {
			System.out.println( n + " is not a Unicode Little-Endian text");
			return false;
 		}
		catch (IOException e)
		{
			throw new IllegalArgumentException("Error estabilishing input stream to file " + n + 
			": " + e.getMessage());
		}
	}

	private void ajouter_min_maj(char min, char maj){
	   t[min]= "";
	   t[min]= t[(int)min]+ maj;
	}

	public static boolean is_equal_or_case_equal(char dic_letter, char text_letter,Alphabet a) {
		return (dic_letter==text_letter || is_upper_of(dic_letter,text_letter,a));
	}

	static boolean is_upper_of(char min,char maj,Alphabet a) {
		if (a.t[min]== null) return false;
		int i=0;
		while ( i < a.t[min].length()) {
		  	if (a.t[min].charAt(i)==maj) return true;
		  	i++;
		}
		return false;
	}

}
