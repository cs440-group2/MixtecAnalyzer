import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Search {

	static Corpus corpus;

	public Search(Corpus corpus) {
		Search.corpus = corpus;
	}
	/**
	 * @param lemma
	 * @param position
	 * @return
	 * @throws IOException 
	 */
	public static HashMap<String, Integer> search(String lemma, String position, Dictionary dict, List<String> otherForms) throws IOException{

		if (position.equals("preceding")){
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for (String file : corpus.getCorpus()){
				HashMap<String, Integer> curr = preSearch(lemma, dict, file);
				for (String key : curr.keySet()){
					if (!map.containsKey(key)){
						map.put(key, curr.get(key));
					}
					else{
						map.put(key, curr.get(key)+map.get(key));
					}
				}
				if(otherForms != null) {
					for(String form : otherForms) {
						curr = preSearch(form, dict, file);
						for (String key : curr.keySet()){
							if (!map.containsKey(key)){
								map.put(key, curr.get(key));
							}
							else{
								map.put(key, curr.get(key)+map.get(key));
							}
						}
					}
				}
			}
			return map;
		}
		if (position.equals("following")){
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for (String file : corpus.getCorpus()){
				HashMap<String, Integer> curr = postSearch(lemma, dict, file);
				for (String key : curr.keySet()){
					if (!map.containsKey(key)){
						map.put(key, curr.get(key));
					}
					else{
						map.put(key, curr.get(key)+map.get(key));
					}
				}
				if(otherForms != null) {
					for(String form : otherForms) {
						curr = postSearch(form, dict, file);
						for (String key : curr.keySet()){
							if (!map.containsKey(key)){
								map.put(key, curr.get(key));
							}
							else{
								map.put(key, curr.get(key)+map.get(key));
							}
						}
					}
				}
			}
			return map;
		}
		if (position.equals("both")){
			HashMap<String, Integer> pre = search(lemma, "preceding", dict, otherForms);
			HashMap<String, Integer> post = search(lemma, "following", dict, otherForms);
			int total = pre.get("TERM_TOTAL") + post.get("TERM_TOTAL");
			for (String key : post.keySet()){
				if (!pre.containsKey(key)){
					pre.put(key, post.get(key));
				}
				else{
					pre.put(key, post.get(key)+pre.get(key));
				}
			}
			pre.put("TERM_TOTAL", total);
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
	public static HashMap<String, Integer> postSearch(String lemma, Dictionary dictionary, String file) throws FileNotFoundException {
 
		int total = 0;

		char last = lemma.charAt(lemma.length()-1);
		String derLemma = lemma.substring(0, lemma.length() - 1) + "\\(" + last + "\\)";

		String search = lemma + "|" + derLemma;
		for (String form: dictionary.getFormList(lemma)){
			search = search + "|" + form;
		}
		Pattern p1 = Pattern.compile("\\s" + "("+search+")" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");
		Pattern p2 = Pattern.compile("[\\w|\\-|\\*|\\'|\uFFFD|\u02026|[A-zÀ-ú]]+" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");

		Matcher m1 = p1.matcher(file);
		Matcher m2 = p2.matcher(file);

		HashMap<String, Integer> frequency = new HashMap<String, Integer>();

		while (m1.find()) {
			if (m2.find(m1.end())){
				total++;

				String origLemma = m2.group().replaceAll("\\=" + "[\\w'\\(\\)]+", "");
				if (origLemma.contains("("))
				{
					origLemma = origLemma.replace("(", "");
					origLemma = origLemma.replace(")", "");
				}

				ArrayList<String> headers = dictionary.findHeaders(origLemma);
		
				if (!headers.isEmpty()){
					for (String i : headers){
						if (headers.size() > 1){
							i = "<HTML><FONT color=#6B8E23>" + i;
						}
						if (!frequency.containsKey(i))
						{
							frequency.put(i,1);
						}
						else
						{
							frequency.put(i,frequency.get(i) + 1);
						}
					}
				}
				else{
					headers = dictionary.findHeaders(m2.group());
					
					if (!headers.isEmpty()){
						for (String i : headers){
							if (headers.size() > 1){
								i = "<HTML><FONT color=#6B8E23>" + i;
							}
							
							if (!frequency.containsKey(i))
							{
								frequency.put(i,1);
							}
							else
							{
								frequency.put(i,frequency.get(i) + 1);
							}
						}
					}
					else{
						if (!frequency.keySet().contains(m2.group())){
							String s ="<HTML><FONT color=#8B0000>" + m2.group();
							frequency.put(s, 1);
						}
						else
						{
							String s ="<HTML><FONT color=#8B0000>" + m2.group();
							frequency.put(s, frequency.get(m2.group()) + 1);
						}
					}
				}
			}
		}
		frequency.put("TERM_TOTAL", total);
		return frequency;
	}
	/**
	 * 
	 * @param lemma: term searched by the user
	 * @param dictionary: mixtec dictionary
	 * @return HashMap of found lemmas preceding searched lemma and their frequency relative to the total lemmas found
	 * @throws FileNotFoundException
	 */
	public static HashMap<String, Integer> preSearch(String lemma, Dictionary dictionary, String file) throws FileNotFoundException {

		int total = 0;

		char last = lemma.charAt(lemma.length()-1);
		String derLemma = lemma.substring(0, lemma.length() - 1) + "\\(" + last + "\\)";

		String search = lemma + "|" + derLemma;
		for (String form: dictionary.getFormList(lemma)){
			search = search + "|" + form;
		}

		Pattern p1 = Pattern.compile("\\b" + "(" +search+")" + "(\\=|\\b)" + "[\\w\\(\\)]*" + "(\\=|\\b)*" + "[\\w\\(\\)]*");
		Pattern p2 = Pattern.compile("\\b" + "[\\w|\\-|\\*|\\'|\uFFFD|\u02026|[A-zÀ-ú]]+" + "(\\=|\\b)" + "[\\w|'|(|)]*" + "(\\=|\\b)*" + "[\\w|'|(|)]*");

		Matcher m1 = p2.matcher(file);
		Matcher m2 = p2.matcher(file);

		HashMap<String, Integer> frequency = new HashMap<String, Integer>();
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

					ArrayList<String> headers = dictionary.findHeaders(origLemma);
					for (String i : headers)
						if (!frequency.containsKey(i)){
							frequency.put(i, 1);
						}
						else
						{
							frequency.put(i,frequency.get(i) + 1);
						}
				}
			}
		}

		frequency.put("TERM_TOTAL", total);
		return frequency;
	}
}