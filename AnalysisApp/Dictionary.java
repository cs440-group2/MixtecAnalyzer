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
	
	HashMap<String, ArrayList<String>> glosses;
	
	
	public void findLemmas(BufferedReader bReader) throws IOException{
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
		
		JList list = new JList(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.add(list);
		scrollPane.setViewportView(list);
		
		frame.add(scrollPane);
		frame.setSize(500, 500);
		frame.setVisible(true);
		
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		Dictionary dict = new Dictionary();
	}
	
	public Dictionary(){
		FileReader dictionaryReader;
		BufferedReader bReader;
		try {
			dictionaryReader = new FileReader("mixtec_maestro_unicode_2015-08-25.txt");
			bReader = new BufferedReader(dictionaryReader);
			findLemmas(bReader);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
