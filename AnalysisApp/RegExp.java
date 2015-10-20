import java.io.*;
import java.util.*;
import java.util.regex.*;

public class RegExp {
	
	public static HashMap<String, Double> search(String lemma) throws FileNotFoundException {
		
		@SuppressWarnings("resource")
		String file = new Scanner(new File("readme.txt")).useDelimiter("\\A").next();
		int total = 0;
		
		char last = lemma.charAt(lemma.length()-1);
		String derLemma = lemma.substring(0, lemma.length() - 1) + "\\(" + last + "\\)";
		Pattern p1 = Pattern.compile("(" +lemma +"|"+derLemma+")" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");
		Pattern p2 = Pattern.compile("[\\w]+" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");

		Matcher m1 = p1.matcher(file);
		Matcher m2 = p2.matcher(file);

		HashMap<String, Double> frequency = new HashMap<String, Double>();
		while (m1.find()) {
			if (m2.find(m1.end())){
				total++;
				String origLemma = m2.group().replaceAll("\\=" + "[\\w'\\(\\)]*", "");
				if (origLemma.contains("(")){
					origLemma = origLemma.replace("(", "");
					origLemma = origLemma.replace(")", "");
				}
				if (!frequency.containsKey(origLemma)){
					frequency.put(origLemma,1.0);
				}
				else
				{
					frequency.put(origLemma,frequency.get(origLemma) + 1);
				}
			}
		}
		for (String key : frequency.keySet()) {
			frequency.put(key,frequency.get(key)/total);
		}
		return frequency;
	}
}