import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvancedSearch {
	
	/**
	 * @param lemma
	 * @param NumBtwn
	 * @param position
	 * @return
	 * @throws FileNotFoundException
	 */
	public static HashMap<String, Double> advancedSearch(String lemma, int numBtwn, String position, Dictionary dict) throws FileNotFoundException {
		Corpus corpus = Search.corpus;
		if (position == "preceding"){
			HashMap<String, Double> map = new HashMap<String, Double>();
			for (String file : corpus.getCorpus()){
				HashMap<String, Double> curr = preAdvancedSearch(lemma, numBtwn, dict, file);
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
				HashMap<String, Double> curr = postAdvancedSearch(lemma, numBtwn, dict, file);
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
			HashMap<String, Double> pre = advancedSearch(lemma, numBtwn, "preeceeding", dict);
			HashMap<String, Double> post = advancedSearch(lemma, numBtwn, "following", dict);
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
	 * @param NumBtwn: Number of words in between searched lemma and found lemma
	 * @return HashMap of found lemmas and their frequency relative to the total lemmas found
	 * @throws FileNotFoundException
	 */
	public static HashMap<String, Double> postAdvancedSearch(String lemma, int NumBtwn, Dictionary dictionary, String file) {
		int total = 0;

		char last = lemma.charAt(lemma.length()-1);
		String derLemma = lemma.substring(0, lemma.length() - 1) + "\\(" + last + "\\)";
		
		String search = lemma + "|" + derLemma;
		for (String form: dictionary.getFormList(lemma)){
			search = search + "|" + form;
		}
		
		Pattern p1 = Pattern.compile("(" +search+")" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");
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
	public static HashMap<String, Double> preAdvancedSearch(String lemma, int NumBtwn, Dictionary dictionary, String file){
		int total = 0;

		char last = lemma.charAt(lemma.length()-1);
		String derLemma = lemma.substring(0, lemma.length() - 1) + "\\(" + last + "\\)";
		
		String search = lemma + "|" + derLemma;
		for (String form: dictionary.getFormList(lemma)){
			search = search + "|" + form;
		}
		
		Pattern p1 = Pattern.compile("(" +search+")" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");
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
