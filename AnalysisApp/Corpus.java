
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.awt.List;

public class Corpus {

	public static void main(String[] args) {

		ArrayList<File> transFiles = new ArrayList<File>();
		listFiles("/Users/kwigley/Projects/Capstone/Transcripciones-activas_2015-08-26", transFiles);
		showFiles(transFiles);
	}

	//updates files to include all files in the directory and sub directories with .trs extension
	public static void listFiles(String directoryName, ArrayList<File> files){

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

	//concatenates file contents into string, and prints them out
	public static void showFiles(ArrayList<File> files) {
		for (File file : files) {

			try {
				FileInputStream input = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(input));

				String line;
				String str = "";

				//read file line by line
				try {
					while((line = br.readLine())  != null){
						if(!line.startsWith("<")){
							str = str + line;
						}
					}

					System.out.println(str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
