import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Search {

	public static void main(String[] args) throws FileNotFoundException{
		HashMap<String, Double> test = preSearch("tu3");
		for (String key : test.keySet())
			System.out.println(key);
	}
	
	public static HashMap<String, Double> search(String lemma, String position) throws FileNotFoundException{
		if (position == "preceding"){
			return preSearch(lemma);
		}
		if (position == "following"){
			return postSearch(lemma);
		}
		else{
			return null;
		}
	}
	/**
	 * 
	 * @param lemma: term searched by the user
	 * @return HashMap of found lemmas following searched lemma and their frequency relative to the total lemmas found
	 * @throws FileNotFoundException
	 */
	public static HashMap<String, Double> postSearch(String lemma) throws FileNotFoundException {

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
	/**
	 * 
	 * @param lemma: term searched by the user
	 * @return HashMap of found lemmas preceding searched lemma and their frequency relative to the total lemmas found
	 * @throws FileNotFoundException
	 */
	public static HashMap<String, Double> preSearch(String lemma) throws FileNotFoundException {

		@SuppressWarnings("resource")
		String file = new Scanner(new File("readme.txt")).useDelimiter("\\A").next();
		int total = 0;

		char last = lemma.charAt(lemma.length()-1);
		String derLemma = lemma.substring(0, lemma.length() - 1) + "\\(" + last + "\\)";
		Pattern p1 = Pattern.compile("(" +lemma +"|"+derLemma+")" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");
		Pattern p2 = Pattern.compile("[\\w]+" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");

		String matcher2 = "";
		Matcher m1 = p2.matcher(file);
		Matcher m2 = p2.matcher(file);

		HashMap<String, Double> frequency = new HashMap<String, Double>();
		while (m1.find()) {
			if (m2.find(m1.end())){
				String match = m2.group();
				Matcher m3 = p1.matcher(match);
				if (m3.find()){
					total++;
					String origLemma = m1.group().replaceAll("\\=" + "[\\w'\\(\\)]*", "");
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
		}
		for (String key : frequency.keySet()) {
			frequency.put(key,frequency.get(key)/total);
		}
		return frequency;
	}
}