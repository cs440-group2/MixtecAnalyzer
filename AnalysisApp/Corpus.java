import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

/**
 * Creates the corpus which contains all transcriptions files put in an ArrayList
 * so it can be searched for collocations
 */
public class Corpus {

	private String directoryName;
	public ArrayList<String> corpus;
	private ArrayList<File> transFiles;
	
	
	/**
	 * Corpus constructor
	 * @param directoryName - Absolute path to directory of transcription files
	 */
	public Corpus(String directoryName) {
		this.directoryName = directoryName;
		corpus = new ArrayList<String>();
		transFiles = new ArrayList<File>();
		
		listFiles(this.directoryName, transFiles);
		showFiles(transFiles);
	}

	/**
	 * Updates files to include all files in the directory and sub directories with .trs extension
	 * @param directoryName
	 * @param files
	 */
	public void listFiles(String directoryName, ArrayList<File> files){
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();

		for(File file: fList){
			if(file.isFile()){
				if(file.getName().endsWith(".trs")){
					files.add(file);
				}
			}
			else if(file.isDirectory()){
				listFiles(file.getAbsolutePath(), files); //sub directory name

			}
		}
	}

	/**
	 * Concatenates file contents into string, and adds strings to corpus ArrayList
	 * @param files
	 */
	public void showFiles(ArrayList<File> files) {
		for (File file : files) {

			try {
				FileInputStream input = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(input));

				String line;
				String str = "";

				//read file line by line
				try {
					while((line = br.readLine())  != null){

						if(line.startsWith("</Turn")){
							corpus.add(str);
							str = "";
						}
						else if(!line.startsWith("<")){
							str = str + line + " ";
						}
					}
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * Getter for corpus generated from transcription files
	 * @return corpus
	 */
	public ArrayList<String> getCorpus() {
		return corpus;
	}
	
	public ArrayList<File> getFiles() {
		return transFiles;
	}
	
	//test
	public static void main(String[] args) {
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this folder: " +
	            chooser.getSelectedFile().getName());
	    }
		
		Corpus testCorpus = new Corpus(chooser.getSelectedFile().getAbsolutePath());
		
		ArrayList<String> corpus = testCorpus.getCorpus();

		System.out.println(corpus.size());
		System.out.println(corpus);

	}
}