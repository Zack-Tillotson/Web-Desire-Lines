package dbScan;

import java.util.Vector;
import java.io.*;


public class FileIO {

	public static Vector<Vector<Integer>> ReadFile(String FileName){
		Vector<Vector<Integer>> ret;
		Vector<String> temp;
		Vector<Integer>current;
		BufferedReader bufRdr;
		int i;
		String line;
		String[]Tokens;
		ret = new Vector<Vector<Integer>>();
		try {
			temp=new Vector<String>();
			bufRdr= new BufferedReader(new FileReader(FileName));
			while((line = bufRdr.readLine()) != null){
				temp.clear();
				Tokens=line.split(" ");
				for(i=0;i<Tokens.length;i++){
					
					try{
						Integer.parseInt(Tokens[i]);
						temp.add(Tokens[i]);
					}
					catch(NumberFormatException e){
						
					}
				}
				
				current=new Vector<Integer>();
				for(i=0;i<temp.size();i++){
					try{
						current.add(Integer.parseInt(temp.get(i)));
					}
					catch(NumberFormatException e){
					
					}		
				}
				if(temp.size()>0){
					ret.add(current);
				}
			}
			return ret;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		return ret;
		
	}
}
