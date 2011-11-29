package edu.mines.mcs.DesireLinesFall11;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

//import ExtractLogs.DistanceMetric;

public class DistanceMatrix {
	
	Double[][] distances;
	Distance metric;
	
	public DistanceMatrix(Distance metric) {
		this.metric = metric;
	}
	
	public DistanceMatrix generate(Vector<Vector<Integer>> pageVectors) {
		distances = new Double[pageVectors.size()][pageVectors.size()];
		Double maxDist = 0.0;
		Double dist;
		
		//store all the distances
		for(int i=0; i<pageVectors.size(); i++) {
			for(int j=0; j<pageVectors.size(); j++) {
				if(i > j) {
					dist = metric.calculate(pageVectors.get(i), pageVectors.get(j));
					if(dist > maxDist) maxDist = dist;
					distances[i][j] = dist;
				}
			}
		}
		
		//scale down all the distances to values between 0 and 1
		for(int i=0; i<pageVectors.size(); i++) {
			for(int j=0; j<pageVectors.size(); j++) {
				if(i > j) {
					distances[i][j] = distances[i][j]/maxDist;
				}
			}
		}
		
		
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		boolean newline;
		
		for(int i=0; i<distances.length; i++) {
			Double[] row = distances[i];
			newline = false;
			for(int j=0; j<distances.length; j++) {
				Double cellValue = row[j];
				if(cellValue != null) {
					if(cellValue.isInfinite()) {
						builder.append("1");
					} else if(cellValue.isNaN()) { 
						builder.append("0");
					} else if(cellValue == 0.0d) {
						builder.append(cellValue);
					} else {
						builder.append(new BigDecimal(cellValue).setScale(10, RoundingMode.HALF_EVEN));
					}
					if(row[j+1] != null) builder.append("\t");
					if(i < distances.length -1) newline = true;
				}
			}
			if(newline) builder.append("\n");
		}
		return builder.toString();
	}

	public static void main(String[] args) {
		
		System.out.println(System.getProperty("user.dir"));
		String inputPath = "/u/au/ac/ztillots/code/Web-Desire-Lines/data/page_ids/dan_output/summitcove-access.good-size.vectors";
		String outputPath = "/u/au/ac/ztillots/code/Web-Desire-Lines/data/page_ids/dan_output/summitcove-access.good-size.distances";
		
		Vector<Vector<Integer>> vectors = new Vector<Vector<Integer>>();
		
		try{
			Scanner in = new Scanner(new FileReader(inputPath));
			PrintWriter out = new PrintWriter(new FileWriter(outputPath));
			
			int i = 0;
			while(in.hasNextLine()) {
				i++;
				String line = in.nextLine();
				
				Vector<Integer> intRow = new Vector<Integer>();
				
				for(String item: line.split(" ")) {
					intRow.add(Integer.parseInt(item));
				}
				
				vectors.add(intRow);
			}
			
			DistanceMatrix matrix = new DistanceMatrix(new LevenshteinDistance()).generate(vectors);
			out.println(vectors.size()-1);
			out.println(matrix);
			
			out.close();
			
		} catch(IOException e) {
			System.out.println("File not found.");
		}
		
	}
}
