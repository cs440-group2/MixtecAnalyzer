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
		Pattern p1 = Pattern.compile(lemma + "(\\=|\\b)" + "[\\w'\\(\\)]*[\\w'\\(\\)]*");
		Pattern p2 = Pattern.compile(derLemma + "(\\=|\\b)" + "[\\w'\\(\\)]*[\\w'\\(\\)]*");

		Matcher m1 = p1.matcher(file);
		Matcher m2 = p2.matcher(file);


		List<String> words = new ArrayList<String>();
		while (m1.find()) {
			words.add(m1.group());
			System.out.println("found: " + m1.group());
		}
		while (m2.find()) {
			words.add(m2.group());
			System.out.println("found: " + m2.group());
		}
	}
}


