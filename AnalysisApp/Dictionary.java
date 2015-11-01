import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class Dictionary {
	
	private HashMap<String, Lemma> lemmaMap;
	private ArrayList<String> lemmas;
	
	public Dictionary() throws IOException{
		FileReader dictionaryReader;
		BufferedReader bReader;

		dictionaryReader = new FileReader("mixtec_maestro_unicode_2015-08-25.txt");
		bReader = new BufferedReader(dictionaryReader);
		
		String line = bReader.readLine();
		lemmaMap = new HashMap<String, Lemma>();
		lemmas = new ArrayList<String>();
		
		String currentLemmaString = "";
		Lemma currentLemma = null;
		

		while(line != null){
			if(line.startsWith("\\lx ")){
				String newLemmaString = line.substring(4);
				if(!newLemmaString.equals(currentLemmaString)){
					lemmas.add(newLemmaString);
					currentLemma = new Lemma(newLemmaString);
					lemmaMap.put(newLemmaString, currentLemma);
					currentLemmaString = newLemmaString;
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
					currentLemma.addForm(item);
					if(!lemmaMap.containsKey(item)){
						lemmaMap.put(item, currentLemma);
					}

				}
			}
			else if(line.startsWith("\\catgr") && line.substring(line.indexOf(' ') + 1).equals("Sust")){
				currentLemma.markNoun();
			}
			
			line = bReader.readLine();
		}
		
		
	}
	
	
	
	
	public ArrayList<String> getLemmaList(){//returns list of lemmas
		return (ArrayList<String>) lemmas.clone();
	}
	
	public String findHeader(String term){//returns header of lemma associated with supplied word(the "root word")
		return lemmaMap.get(term).getHeader();
	}
	
	public ArrayList<String> getFormList(String term){//returns all forms of provided term
		return lemmaMap.get(term).getForms();
	}
	
	public ArrayList<String> getGlossList(String term){//returns glosses for provided term
		return lemmaMap.get(term).getGlosses();
	}
	
	public boolean isNoun(String term){//returns true if term is marked as a noun
		return lemmaMap.get(term).isNoun();
		
	}
	
	


	private class Lemma{
		private String header;
		private ArrayList<String> glosses;
		private ArrayList<String> forms;
		private boolean noun;
		
		
		
		public Lemma(String header){
			this.header = header;
			noun = false;
			forms = new ArrayList<String>();
			glosses = new ArrayList<String>();
			glosses.add(header);
		}
		
		public void addForm(String newForm){
			forms.add(newForm);
		}
		
		public void addGloss(String newGloss){
			glosses.add(newGloss);
		}
		
		public void markNoun(){
			noun = true;
		}
		
		public boolean isNoun(){
			return noun;
		}
		
		public ArrayList<String> getForms(){
			return (ArrayList<String>) forms.clone();
		}
		
		public ArrayList<String> getGlosses(){
			return (ArrayList<String>) glosses.clone();
		}
		
		public String getHeader(){
			return header;
		}
		
		@Override public String toString(){
			StringBuilder sb = new StringBuilder(String.format("Header: %s\nNoun: %b\nGloss: ", header, noun));
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
	
	
	
/*	public static void main(String[] args) throws FileNotFoundException{ TODO: remove
		try {
			Dictionary dict = new Dictionary();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
	
	
	
}
