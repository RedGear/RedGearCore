package redgear.core.mod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileHelper {

	
	public static ArrayList<String> readLines(String fileName){
		return readLines(new File(fileName));
	}
	
	public static ArrayList<String> readLines(File file){
		ArrayList<String> lines = new ArrayList<String>();
		
		
		if(file.exists()){
			BufferedReader reader = null;
			
			try {
				reader = new BufferedReader(new FileReader(file));
				
				String line;
				
				while((line = reader.readLine()) != null){
					lines.add(line);
				}
				
			} catch (IOException e) {
			}
			finally{
				try {
					if (reader != null)
						reader.close();
				} catch (IOException e) {
				}
			}
		}
		
		
		return lines;
	}
	
	public static void writeLines(ArrayList<String> lines, String fileName) {
		writeLines(lines, new File(fileName));
	}
	
	public static void writeLines(ArrayList<String> lines, File file) {
		BufferedWriter writer = null;
		
		try {
			if(!file.exists())
				file.createNewFile();
			
			String seperator = System.getProperty("line.separator");
			
			writer = new BufferedWriter(new FileWriter(file));
			
			for(String line : lines){
				writer.write(line);
				writer.write(seperator);
			}

		} catch (IOException e) {

		}	
		finally{
			try{
				if(writer != null)
					writer.close();
			}catch(Exception e){}
		}
	}
}
