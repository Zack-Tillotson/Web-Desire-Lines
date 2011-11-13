package ExtractLogs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Extract {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		extract("RawLogs/ex111002.log");

	}
	
	public static void extract(String name){
		BufferedReader read;
		boolean isID;
		ArrayList<String> IP;
		IP=new ArrayList<String>();
		int i=0,j=0;
		ArrayList<ArrayList<String>> pages;
		pages=new ArrayList<ArrayList<String>>();
		String currentLine;
		String [] tokens;
		try {
			read= new BufferedReader(new FileReader(name));
			currentLine=read.readLine();
			while(currentLine!=null){
				isID=false;
				tokens=currentLine.split(" ");
				if(currentLine.startsWith("#")){
					
				}
				else if(tokens[7].equals("/merchant.ihtml")){
					if(tokens[8].contains("id=")){
						tokens[8]=tokens[8].substring(tokens[8].indexOf("id=")+3);
						tokens[8]=tokens[8].substring(0,6);
						//System.out.println(tokens[8]);
						if(tokens[8].indexOf("&")>0){
							tokens[8]=tokens[8].substring(0, tokens[8].indexOf("&"));
							isID=true;
							//System.out.println(tokens[8]);
						}
						else if(tokens[8].indexOf("+")>0){
							tokens[8]=tokens[8].substring(0, tokens[8].indexOf("+"));
							isID=true;
						}
						else if(tokens[8].indexOf("E")>0){
							tokens[8]=tokens[8].substring(0, tokens[8].indexOf("E"));
							isID=true;
						}
						else if(tokens[8].indexOf("%")>0){
							tokens[8]=tokens[8].substring(0, tokens[8].indexOf("%"));
							isID=true;
						}
						/*
						else if(tokens[8].indexOf(":")>0){
							System.out.println(tokens[8].indexOf(":"));
							tokens[8]=tokens[8].substring(3, tokens[8].indexOf(":"));
							System.out.println(tokens[8]);
						}
						
						*/
					}
					i++;
					if(!IP.contains(tokens[2])){
						j++;
						IP.add(tokens[2]);
						pages.add(new ArrayList<String>());
					}
					if(isID){
						pages.get(IP.indexOf(tokens[2])).add(tokens[8]);
					}
				}
				currentLine=read.readLine();
			}
			//System.out.println(i);
			//System.out.println(j);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
