import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvancedSearch {
	public static void main(String[] args) throws FileNotFoundException{
		HashMap<String, Double> test = preAdvancedSearch("tu3", 1);
		for (String key : test.keySet())
			System.out.println(key);
	}

	/**
	 * @param lemma
	 * @param NumBtwn
	 * @param position
	 * @return
	 * @throws FileNotFoundException
	 */
	public static HashMap<String, Double> AdvancedSearch(String lemma, int NumBtwn, String position) throws FileNotFoundException {
		if (position == "preceding"){
			
		}
		return null;
	}
	
	/**
	 * 
	 * @param lemma: term searched by the user
	 * @param NumBtwn: Number of words in between searched lemma and found lemma
	 * @return HashMap of found lemmas and their frequency relative to the total lemmas found
	 * @throws FileNotFoundException
	 */
	public static HashMap<String, Double> postAdvancedSearch(String lemma, int NumBtwn) throws FileNotFoundException {

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
			int count = 0;
			m2.find(m1.start());
			while (m2.find() && count <= NumBtwn){
				if (count == NumBtwn){
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
				count ++;
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
	 * @param NumBtwn: Number of words in between searched lemma and found lemma
	 * @return HashMap of found lemmas and their frequency relative to the total lemmas found
	 * @throws FileNotFoundException
	 */
	public static HashMap<String, Double> preAdvancedSearch(String lemma, int NumBtwn) throws FileNotFoundException {

		@SuppressWarnings("resource")
		String file = new Scanner(new File("readme.txt")).useDelimiter("\\A").next();
		int total = 0;

		char last = lemma.charAt(lemma.length()-1);
		String derLemma = lemma.substring(0, lemma.length() - 1) + "\\(" + last + "\\)";
		Pattern p1 = Pattern.compile("(" +lemma +"|"+derLemma+")" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");
		Pattern p2 = Pattern.compile("[\\w]+" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");

		Matcher m1 = p2.matcher(file);
		Matcher m2 = p2.matcher(file);

		HashMap<String, Double> frequency = new HashMap<String, Double>();
		while (m1.find()) {
			int count = 0;
			m2.find(m1.start());
			while (m2.find() && count <= NumBtwn){
				if (count == NumBtwn){
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
				count ++;
			}
		}
		for (String key : frequency.keySet()) {
			frequency.put(key,frequency.get(key)/total);
		}
		return frequency;
	}

}
