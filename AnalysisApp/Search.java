import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Search {

	static Corpus corpus;

	public Search(Corpus corpus) {
		this.corpus = corpus;
	}
	/**
	 * @param lemma
	 * @param position
	 * @return
	 * @throws IOException 
	 */
	public static HashMap<String, Double> search(String lemma, String position, Dictionary dict) throws IOException{

		if (position == "preceding"){
			HashMap<String, Double> map = new HashMap<String, Double>();
			for (String file : corpus.getCorpus()){
				HashMap<String, Double> curr = preSearch(lemma, dict, file);
				for (String key : curr.keySet()){
					if (!map.containsKey(key)){
						map.put(key, curr.get(key));
					}
					else{
						map.put(key, curr.get(key)+map.get(key));
					}
				}
				return map;
			}
		}
		if (position == "following"){
			HashMap<String, Double> map = new HashMap<String, Double>();
			for (String file : corpus.getCorpus()){
				HashMap<String, Double> curr = postSearch(lemma, dict, file);
				for (String key : curr.keySet()){
					if (!map.containsKey(key)){
						map.put(key, curr.get(key));
					}
					else{
						map.put(key, curr.get(key)+map.get(key));
					}
				}
				return map;
			}
		}
		if (position == "both"){
			HashMap<String, Double> pre = search(lemma, "preceding", dict);
			HashMap<String, Double> post = search(lemma, "following", dict);
			for (String key : post.keySet()){
				if (!pre.containsKey(key)){
					pre.put(key, post.get(key));
				}
				else{
					pre.put(key, post.get(key)+pre.get(key));
				}
			}
			return pre;
		}
		else{
			return null;
		}
	}
	/**
	 * 
	 * @param lemma: term searched by the user
	 * @param dictionary: mixtec dictionary
	 * @return HashMap of found lemmas following searched lemma and their frequency relative to the total lemmas found
	 * @throws FileNotFoundException
	 */
	public static HashMap<String, Double> postSearch(String lemma, Dictionary dictionary, String file) throws FileNotFoundException {

		int total = 0;

		char last = lemma.charAt(lemma.length()-1);
		String derLemma = lemma.substring(0, lemma.length() - 1) + "\\(" + last + "\\)";

		String search = lemma + "|" + derLemma;
		for (String form: dictionary.getFormList(lemma)){
			search = search + "|" + form;
		}
		Pattern p1 = Pattern.compile("("+search+")" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");
		Pattern p2 = Pattern.compile("[\\w|-]+" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");

		Matcher m1 = p1.matcher(file);
		Matcher m2 = p2.matcher(file);

		HashMap<String, Double> frequency = new HashMap<String, Double>();
		
		while (m1.find()) {
			if (m2.find(m1.end())){
				total++;
				ArrayList<String> headers = dictionary.findHeaders(m2.group());
				if (!headers.isEmpty()){
					for (String i : headers)
						if (!frequency.containsKey(i)){
							frequency.put(i,1.0);
						}
						else
						{
							frequency.put(i,frequency.get(i) + 1);
						}
				}
				else{
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
		}
		for (String key : frequency.keySet()) {
			frequency.put(key,frequency.get(key)/total);
		}
		return frequency;
	}
	/**
	 * 
	 * @param lemma: term searched by the user
	 * @param dictionary: mixtec dictionary
	 * @return HashMap of found lemmas preceding searched lemma and their frequency relative to the total lemmas found
	 * @throws FileNotFoundException
	 */
	public static HashMap<String, Double> preSearch(String lemma, Dictionary dictionary, String file) throws FileNotFoundException {

		int total = 0;

		char last = lemma.charAt(lemma.length()-1);
		String derLemma = lemma.substring(0, lemma.length() - 1) + "\\(" + last + "\\)";

		String search = lemma + "|" + derLemma;
		for (String form: dictionary.getFormList(lemma)){
			search = search + "|" + form;
		}

		Pattern p1 = Pattern.compile("(" +search+")" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");
		Pattern p2 = Pattern.compile("[\\w|-]+" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");

		Matcher m1 = p2.matcher(file);
		Matcher m2 = p2.matcher(file);

		HashMap<String, Double> frequency = new HashMap<String, Double>();
		while (m1.find()) {
			if (m2.find(m1.end())){
				String match = m2.group();
				Matcher m3 = p1.matcher(match);
				if (m3.find()){
					total++;
					ArrayList<String> headers = dictionary.findHeaders(m1.group());
					if (!headers.isEmpty()){
						for (String i : headers)
							if (!frequency.containsKey(i)){
								frequency.put(i,1.0);
							}
							else
							{
								frequency.put(i,frequency.get(i) + 1);
							}
					}
					else{
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
		}
		for (String key : frequency.keySet()) {
			frequency.put(key,frequency.get(key)/total);
		}
		return frequency;
	}
}