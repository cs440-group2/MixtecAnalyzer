import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {
	
	public static void findLemmas(BufferedReader bReader) throws IOException{
		String line = bReader.readLine();
		while(line != null){
			if(line.length() >=4 && line.substring(0, 4).equals("\\lx ")){
				System.out.println(line.substring(4));
			}
			line = bReader.readLine();
		}
		
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
