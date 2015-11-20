import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Dictionary {
	
	private HashMap<String, Lemma> lemmaMap;
	private ArrayList<String> lemmas;
	private HashMap<String, ArrayList<String>> formMap;
	
	/**
	 * @throws IOException
	 */
	public Dictionary() throws IOException{ //TODO: Remove
		this("mixtec_maestro_unicode_2015-08-25.txt");
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
		ArrayList<String> formList = null;
		

		while(line != null){
			if(line.startsWith("\\lx ")){
				String newLemmaString = line.substring(4);
				if(!newLemmaString.equals(currentLemmaString)){
					lemmas.add(newLemmaString);
					currentLemma = new Lemma(newLemmaString);
					lemmaMap.put(newLemmaString, currentLemma);
					currentLemmaString = newLemmaString;
					formList = new ArrayList<String>();
					formList.add(currentLemmaString);
					formMap.put(currentLemmaString, formList);
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
					if(!formList.contains(item)){
						formList.add(item);
						currentLemma.addForm(item);
					}
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
	public ArrayList<String> getLemmaList(){
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
	
	/**
	 *
	 */
	private class Lemma{
		private String header;
		private ArrayList<String> glosses;
		private ArrayList<String> forms;
		
		/**
		 * @param header
		 */
		public Lemma(String header){
			this.header = header;
			forms = new ArrayList<String>();
			glosses = new ArrayList<String>();
			glosses.add(header);
		}
		
		/**
		 * @param newForm
		 */
		public void addForm(String newForm){
			forms.add(newForm);
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
			return (ArrayList<String>) forms.clone();
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
		
		@Override public String toString(){
			StringBuilder sb = new StringBuilder(String.format("Header: %s\nGloss: ", header));
			for(String gloss : glosses){
				sb.append(gloss);
				sb.append("; ");
			}
			sb.append("\nForms:");
			for(String form : forms){
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
