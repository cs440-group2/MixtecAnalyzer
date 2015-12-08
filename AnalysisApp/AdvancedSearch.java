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
	public static HashMap<String, Double> advancedSearch(String lemma, String result, int numBtwn, String position, Dictionary dict) throws FileNotFoundException {
		Corpus corpus = Search.corpus;
		if (position.equals("preceding")){
			HashMap<String, Double> map = new HashMap<String, Double>();
			for (String file : corpus.getCorpus()){
				HashMap<String, Double> curr = preAdvancedSearch(lemma, result, numBtwn, dict, file);
				for (String key : curr.keySet()){
					if (!map.containsKey(key)){
						map.put(key, curr.get(key));
					}
					else{
						map.put(key, curr.get(key)+map.get(key));
					}
				}
			} 
			return map;

		}
		if (position.equals("following")){
			HashMap<String, Double> map = new HashMap<String, Double>();
			for (String file : corpus.getCorpus()){
				HashMap<String, Double> curr = postAdvancedSearch(lemma, result, numBtwn, dict, file);
				for (String key : curr.keySet()){
					if (!map.containsKey(key)){
						map.put(key, curr.get(key));
					}
					else{
						map.put(key, curr.get(key)+map.get(key));
					}
				}
			}
			return map;
		}
		if (position.equals("both")){
			HashMap<String, Double> pre = advancedSearch(lemma, result, numBtwn, "preeceeding", dict);
			HashMap<String, Double> post = advancedSearch(lemma, result, numBtwn, "following", dict);
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
	public static HashMap<String, Double> postAdvancedSearch(String lemma, String result, int NumBtwn, Dictionary dictionary, String file) {
		Double total = 0.0;

		char last = lemma.charAt(lemma.length()-1);
		String derLemma = lemma.substring(0, lemma.length() - 1) + "\\(" + last + "\\)";
		last = result.charAt(result.length()-1);
		String derResult = result.substring(0, result.length() - 1) + "\\(" + last + "\\)";

		String search = lemma + "|" + derLemma;
		for (String form: dictionary.getFormList(lemma)){
			search = search + "|" + form;
		}

		String resultSearch = result + "|" + derResult;
		for (String form: dictionary.getFormList(result)){
			resultSearch = resultSearch + "|" + form;
		}

		Pattern p1 = Pattern.compile("\\b" + "(" +search+")" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");
		Pattern p2 = Pattern.compile("[\\w|\\-|\\*]+" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");
		Pattern p3 = Pattern.compile("\\b" + "(" +resultSearch+")" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");

		Matcher m1 = p1.matcher(file);
		Matcher m2 = p2.matcher(file);


		HashMap<String, Double> found = new HashMap<String, Double>();
		while (m1.find()) {
			int count = 0;
			m2.find(m1.start());
			ArrayList<String> wordsBtwn = new ArrayList<String>();
			while (m2.find() && count <= NumBtwn){
				wordsBtwn.add(m2.group());
				if (count == NumBtwn){
					String match = m2.group();
					Matcher m3 = p3.matcher(match);
					if (m3.find()){
						total ++;
						String print = m1.group();
						for (String i : wordsBtwn){
							print = print + " " + i;
						}
						if (found.containsKey(print)){
							found.put(print, found.get(print) + 1);
						}
						else{
							found.put(print, 1.0);
						}
					}
				}
				count ++;
			}
		}
		found.put("TERM_TOTAL", total);
		return found;
	}

	/**
	 * 
	 * @param lemma: term searched by the user
	 * @param NumBtwn: Number of words in between searched lemma and found lemma
	 * @return HashMap of found lemmas and their frequency relative to the total lemmas found
	 * @throws FileNotFoundException
	 */
	public static HashMap<String, Double> preAdvancedSearch(String lemma, String result, int NumBtwn, Dictionary dictionary, String file){
		Double total = 0.0;

		char last = lemma.charAt(lemma.length()-1);
		String derLemma = lemma.substring(0, lemma.length() - 1) + "\\(" + last + "\\)";
		last = result.charAt(result.length()-1);
		String derResult = result.substring(0, result.length() - 1) + "\\(" + last + "\\)";

		String search = lemma + "|" + derLemma;
		for (String form: dictionary.getFormList(lemma)){
			search = search + "|" + form;
		}

		String resultSearch = result + "|" + derResult;
		for (String form: dictionary.getFormList(result)){
			resultSearch = resultSearch + "|" + form;
		}

		Pattern p1 = Pattern.compile("\\b" + "(" +search+")" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");
		Pattern p2 = Pattern.compile("[\\w|//-|\\*]+" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");
		Pattern p4 = Pattern.compile("\\b" + "(" +resultSearch+")" + "(\\=|\\b)" + "[\\w'\\(\\)]*" + "(\\=|\\b)" + "[\\w'\\(\\)]*");

		Matcher m1 = p2.matcher(file);
		Matcher m2 = p2.matcher(file);

		HashMap<String, Double> found = new HashMap<String, Double>();
		while (m1.find()) {
			int count = 0;
			m2.find(m1.start());
			ArrayList<String> wordsBtwn = new ArrayList<String>();
			while (m2.find() && count <= NumBtwn){
				wordsBtwn.add(m2.group());
				if (count == NumBtwn){
					String match = m2.group();
					Matcher m3 = p1.matcher(match);
					if (m3.find()){
						String match2 = m1.group();
						Matcher m4 = p4.matcher(match2);
						if (m4.find()){
							total ++;
							String print = m1.group();
							for (String i : wordsBtwn){
								print = print + " " + i;
							}
							if (found.containsKey(print)){
								found.put(print, found.get(print) + 1);
							}
							else{
								found.put(print, 1.0);
							}
						}
					}
				}
				count ++;
			}
		}
		found.put("TERM_TOTAL", total);
		return found;
	}

}
