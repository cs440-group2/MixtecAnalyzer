import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class Dictionary {
	
	public static void findLemmas(BufferedReader bReader) throws IOException{
		String line = bReader.readLine();
		ArrayList<String> lemmas = new ArrayList<String>();
		while(line != null){
			if(line.length() >=4 && line.substring(0, 4).equals("\\lx ")){
				lemmas.add(line.substring(4));
			}
			line = bReader.readLine();
		}
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		
		DefaultListModel model = new DefaultListModel();
		
		for(int i = 0; i<lemmas.size(); i++){
			model.addElement(lemmas.get(i));
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
