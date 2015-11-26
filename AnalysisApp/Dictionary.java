import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Dictionary {
	
	private HashMap<String, Lemma> lemmaMap;
	private ArrayList<String> lemmas;  
	private HashMap<String, ArrayList<String>> formMap; //forms -> list of associated lemmas
	private ArrayList<String> allParts;
	/**
	 * @throws IOException
	 */
	public Dictionary() throws IOException{ //TODO: Remove
		this("C:\\Users\\Ian\\Desktop\\New folder\\mixtec_maestro_unicode_2015-08-25.txt");
	}
	
	/**
	 * @param file
	 * @throws IOException
	 */
	public Dictionary(String file) throws IOException{
		FileReader dictionaryReader;
		BufferedReader bReader;

		dictionaryReader = new FileReader(file);
		bReader = new BufferedReader(dictionaryReader);
		
		String line = bReader.readLine();
		lemmaMap = new HashMap<String, Lemma>();
		lemmas = new ArrayList<String>();
		formMap = new HashMap<String, ArrayList<String>>();
		
		String currentLemmaString = "";
		Lemma currentLemma = null;
		String part = "Unknown";
		ArrayList<String> newForms = new ArrayList<String>();
		allParts = new ArrayList<String>();
		
		while(line != null){
			if(line.startsWith("\\lx ")){
				if(!newForms.isEmpty()){
					for(String item : newForms){
						currentLemma.addForm(item, part);
					}
				}
				part = "Unknown";
				newForms = new ArrayList<String>();
				String newLemmaString = line.substring(4);
				newForms.add(newLemmaString);
				if(!newLemmaString.equals(currentLemmaString)){
					lemmas.add(newLemmaString);
					currentLemma = new Lemma(newLemmaString);
					lemmaMap.put(newLemmaString, currentLemma);
					currentLemmaString = newLemmaString;
					ArrayList<String> aLemmas = new ArrayList<String>();
					aLemmas.add(currentLemmaString);
					formMap.put(currentLemmaString, aLemmas);
				}
			}
			else if(line.startsWith("\\glosa ")){
				currentLemma.addGloss(line.substring(7));
			}
			else if(line.startsWith("\\lx_") && !line.startsWith("\\lx_cita")){
				String entry = line.substring(line.indexOf(' '));
				
				String[] entries = entry.split(";");
				for(String item : entries){
					item = item.trim();
					ArrayList<String> aLemmas;
					if(!formMap.containsKey(item)){
						aLemmas = new ArrayList<String>();
						formMap.put(item, aLemmas);
					}
					else{
						aLemmas = formMap.get(item);
					}
					if(!aLemmas.contains(currentLemmaString)){
						aLemmas.add(currentLemmaString);
					}
					newForms.add(item);
				}
			}
			else if(line.startsWith("\\catgr ")){
				part = line.substring(7);
				if(!allParts.contains(part)){
					allParts.add(part);
				}
			}
			
			line = bReader.readLine();
		}
		bReader.close();
	}
	
	/**
	 * returns list of lemmas
	 * @return
	 */
	public ArrayList<String> getLemmaList(){//TODO: switch lemmas to lemmaMap keys
		return (ArrayList<String>) lemmas.clone();
	}
	
	/**
	 * returns list of lemmas with given search term
	 * @param refine
	 * @return
	 */
	public ArrayList<String> getLemmaList(String refine){
		ArrayList<String> returnArr = new ArrayList<String>();
		for(String lemma : lemmas){
			if(lemma.startsWith(refine)) {
				returnArr.add(lemma);
			}
		}
		return returnArr;
	}
	
	/**
	 * returns headers of associated lemmas
	 * @param term
	 * @return
	 */
	public ArrayList<String> findHeaders(String term){
		ArrayList<String> result;
		if(formMap.containsKey(term)){
			result = (ArrayList<String>) formMap.get(term).clone();
		}
		else{
			result = new ArrayList<String>();
		}
		
		return result;
	}
	
	/**
	 * Returns all forms of provided term, term must be a lemma.
	 * @param term
	 * @return
	 */
	public ArrayList<String> getFormList(String term){
		return lemmaMap.get(term).getForms();
	}
	
	/**
	 * returns glosses for provided term, term must be a lemma.
	 * @param term
	 * @return
	 */
	public ArrayList<String> getGlossList(String term){
		return lemmaMap.get(term).getGlosses();
	}
	
	public ArrayList<String> getParts(String term, String lemmaString){//limited to parts as form of a specific lemma
		Lemma lemma = lemmaMap.get(lemmaString);
		return lemma.getParts(term);
	}
	
	public ArrayList<String> getParts(String term){//all parts as form of all associated lemmas
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> aLemmas = formMap.get(term);
		for(String lemmaString : aLemmas){
			result.addAll(getParts(term, lemmaString));
		}
		return result;
	}
	
	public ArrayList<String> getAllParts(){
		return (ArrayList<String>) allParts.clone();
	}
	
	/**
	 *
	 */
	private class Lemma{
		private String header;
		private ArrayList<String> glosses;
		private ArrayList<String> formList;
		private HashMap<String, ArrayList<String>> forms;
		
		/**
		 * @param header
		 */
		public Lemma(String header){
			this.header = header;
			formList = new ArrayList<String>();
			glosses = new ArrayList<String>();
			glosses.add(header);
			forms = new HashMap<String, ArrayList<String>>();//form of lemma ->list of parts for form
		}
		
		/**
		 * @param newForm
		 */
		public void addForm(String form, String part){
			if(forms.containsKey(form)){
				ArrayList<String> list = forms.get(form);
				if(!list.contains(part)){
					list.add(part);
				}
			}
			else{
				ArrayList<String> list = new ArrayList<String>();
				list.add(part);
				forms.put(form, list);
			}
		}
		
		/**
		 * @param newGloss
		 */
		public void addGloss(String newGloss){
			glosses.add(newGloss);
		}
		
		/**
		 * Getter for forms of lemma.
		 * @return ArrayList<String> of forms
		 */
		public ArrayList<String> getForms(){
			return new ArrayList<String>(forms.keySet());
		}
		
		/**
		 * Getter for glosses.
		 * @return ArrayList<String> of glosses
		 */
		public ArrayList<String> getGlosses(){
			return (ArrayList<String>) glosses.clone();
		}
		
		/**
		 * Getter for header field.
		 * @return header
		 */
		public String getHeader(){
			return header;
		}
		
		public ArrayList<String> getParts(String form){
			return (ArrayList<String>) forms.get(form).clone();
		}
		
		
		@Override public String toString(){
			StringBuilder sb = new StringBuilder(String.format("Header: %s\nGloss: ", header));
			for(String gloss : glosses){
				sb.append(gloss);
				sb.append("; ");
			}
			sb.append("\nForms:");
			for(String form : formList){
				sb.append(form + "; ");
			}
			
			return sb.toString();
		}
		
	}
	
	@Override public String toString(){
		StringBuilder sb = new StringBuilder();
		for(String entry : lemmas){
			sb.append(lemmaMap.get(entry).toString());
			sb.append("\n\n");
		}
		return sb.toString();
	}
	
	
	
	/**
	 * Main method for testing
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException{// TODO: remove
		try {
			Dictionary dict = new Dictionary();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
