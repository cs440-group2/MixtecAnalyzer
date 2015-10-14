import java.io.*;
import java.util.*;
import java.util.regex.*;

public class RegExp {
	public static void main(String[] args) throws FileNotFoundException {
		
		@SuppressWarnings("resource")
		String file = new Scanner(new File("readme.txt")).useDelimiter("\\A").next();
		
		String lemma = "tu3";

		char last = lemma.charAt(lemma.length()-1);
		String derLemma = lemma.substring(0, lemma.length() - 1) + "\\(" + last + "\\)";
		Pattern p1 = Pattern.compile("(" +lemma +"|"+derLemma+")" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");
		Pattern p2 = Pattern.compile("[\\w]+" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");

		Matcher m1 = p1.matcher(file);
		Matcher m2 = p2.matcher(file);

		List<String> words = new ArrayList<String>();
		while (m1.find()) {
			words.add(m1.group());
			if (m2.find(m1.end())){
				System.out.println("found: " + m1.group() + " " + m2.group());
			}
		}
	}
	
	public static ArrayList<String> search(String lemma) throws FileNotFoundException {
		
		@SuppressWarnings("resource")
		String file = new Scanner(new File("readme.txt")).useDelimiter("\\A").next();

		char last = lemma.charAt(lemma.length()-1);
		String derLemma = lemma.substring(0, lemma.length() - 1) + "\\(" + last + "\\)";
		Pattern p1 = Pattern.compile("(" +lemma +"|"+derLemma+")" + "(\\=|\\b)" + "[\\w'\\(\\)]*[\\w'\\(\\)]*");

		Matcher m1 = p1.matcher(file);

		ArrayList<String> words = new ArrayList<String>();
		while (m1.find()) {
			words.add(m1.group());
		}
		return words;
	}
}


