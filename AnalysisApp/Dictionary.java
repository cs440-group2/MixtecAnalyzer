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
	
	HashMap<String, Lemma> lemmaMap;
	ArrayList<String> lemmas;
	
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
	
	
	
	/*public void findLemmas(BufferedReader bReader) throws IOException{
		String line = bReader.readLine();
		ArrayList<String> lemmas = new ArrayList<String>();
		glosses = new HashMap<String, ArrayList<String>>();
		forms = new HashMap<String, ArrayList<String>>();
		
		String currentLemma = "";
		
		while(line != null){
			if(line.startsWith("\\lx ")){
				String newLemma = line.substring(4);
				if(!newLemma.equals(currentLemma)){
					lemmas.add(newLemma);
					glosses.put(newLemma, new ArrayList<String>());
					currentLemma = newLemma;
				}
			}
			else if(line.startsWith("\\glosa ")){
				ArrayList<String> glossList = glosses.get(currentLemma);
				glossList.add(line.substring(7));
			}
			else if(line.startsWith("\\lx_") && !line.startsWith("\\lx_cita")){
				if(!forms.containsKey(currentLemma)){
					forms.put(currentLemma, new ArrayList<String>());
				}
				ArrayList<String> currentList = forms.get(currentLemma);
				String entry = line.substring(line.indexOf(' '));
				
				if(entry.contains(";")){
					String[] entries = entry.split(";");
					for(String item : entries){
						currentList.add(item.trim());
					}
				}
				
				forms.get(currentLemma).add(entry);
				
			}
			line = bReader.readLine();
		}
		
		System.out.println(forms.toString());
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		
		DefaultListModel model = new DefaultListModel();
		
		for(int l = 0; l<lemmas.size(); l++){
			String lemma = lemmas.get(l);
			ArrayList<String> glossList = glosses.get(lemma);
			
			StringBuilder sb = new StringBuilder(lemma);
			
			for(int g = 0; g < glossList.size(); g++){
				sb.append(g == 0? " (" : ", ");
				sb.append(glossList.get(g));
				if(g==glossList.size() - 1) {sb.append(")");}
			}
			
			
			
			model.addElement(sb.toString());
		}
		
		JList list = new JList(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.add(list);
		scrollPane.setViewportView(list);
		
		frame.add(scrollPane);
		frame.setSize(500, 500);
		frame.setVisible(true);
		
	}*/
	
	/*public static DefaultListModel getLemmaList() throws IOException{
		HashMap<String, ArrayList<String>> glosses;
		
		FileReader dictionaryReader;
		BufferedReader bReader = null;
		try {
			dictionaryReader = new FileReader("mixtec_maestro_unicode_2015-08-25.txt");
			bReader = new BufferedReader(dictionaryReader);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String line = bReader.readLine();
		ArrayList<String> lemmas = new ArrayList<String>();
		glosses = new HashMap<String, ArrayList<String>>();
		
		String currentLemma = "";
		
		while(line != null){
			if(line.length() >=4 && line.substring(0, 4).equals("\\lx ")){
				String newLemma = line.substring(4);
				if(!newLemma.equals(currentLemma)){
					lemmas.add(newLemma);
					glosses.put(newLemma, new ArrayList<String>());
					currentLemma = newLemma;
				}
			}
			else if(line.length() >=7 && line.substring(0, 7).equals("\\glosa ")){
				ArrayList<String> glossList = glosses.get(currentLemma);
				glossList.add(line.substring(7));
			}
			line = bReader.readLine();
		}
		
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		
		DefaultListModel model = new DefaultListModel();
		
		for(int l = 0; l<lemmas.size(); l++){
			String lemma = lemmas.get(l);
			ArrayList<String> glossList = glosses.get(lemma);
			
			StringBuilder sb = new StringBuilder(lemma);
			
			for(int g = 0; g < glossList.size(); g++){
				sb.append(g == 0? " (" : ", ");
				sb.append(glossList.get(g));
				if(g==glossList.size() - 1) {sb.append(")");}
			}
			
			model.addElement(sb.toString());
		}
		
		return model;
	}*/
	
	
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
	
	public void addToMap(String key, Lemma lemma){//FOR TESTING PURPOSES ONLY
		if(lemmaMap.containsKey(key) && lemma != lemmaMap.get(key)){
			System.out.printf("OVERWRITE\nKey: %s\nOld Lemma: %s \nNew Lemma: %s\n", key, lemmaMap.get(key).getHeader(), lemma.getHeader());
		}
		lemmaMap.put(key, lemma);
	}
	
	
	
	
	public static void main(String[] args) throws FileNotFoundException{
		try {
			Dictionary dict = new Dictionary();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
		
		int lemmaConflict = 0;
		int formConflict = 0;
		int noConflict = 0;
		while(line != null){
			if(line.startsWith("\\lx ")){
				String newLemmaString = line.substring(4);
				if(!newLemmaString.equals(currentLemmaString)){
					if(lemmaMap.containsKey(newLemmaString)){
						lemmaConflict++;
					}
					else{
						noConflict++;
					}
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
					if(!lemmaMap.containsKey(item)){//TODO: change fter testing
						lemmaMap.put(item, currentLemma);
						noConflict++;
					}
					else{
						String oldLemma = lemmaMap.get(item).getHeader();

						if(!oldLemma.equals(currentLemmaString)){
							System.out.printf("OVERWRITE\nKey: %s\nOld Lemma: %s \nNew Lemma: %s\n", item, oldLemma, currentLemmaString);
							if(lemmaMap.get(item).getHeader().equals(item)){
								lemmaConflict++;
								System.out.printf("Lemma Conflict: %d\n\n", lemmaConflict);
							}
							else{
								formConflict++;
								System.out.printf("Form Conflict: %d\n\n", formConflict);
							}
						}



					}

				}
			}
			else if(line.startsWith("\\catgr") && line.substring(line.indexOf(' ') + 1).equals("Sust")){
				currentLemma.markNoun();
			}
			
			line = bReader.readLine();
		}
		
		System.out.printf("Total Conflicts:%d \nLemma Conflicts:%d \nForm Conflicts:%d\nNo Conflicts: %d", lemmaConflict + formConflict, lemmaConflict, formConflict, noConflict);
		
	}
	
	@Override public String toString(){
		StringBuilder sb = new StringBuilder();
		for(String entry : lemmas){
			sb.append(lemmaMap.get(entry).toString());
			sb.append("\n\n");
		}
		return sb.toString();
	}
	
	
	
	
}
