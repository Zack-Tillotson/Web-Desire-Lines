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
		
		for(int i=0; i<pageVectors.size(); i++) {
			for(int j=0; j<pageVectors.size(); j++) {
				if(distances[j][i] == null) {
					distances[i][j] = metric.calculate(pageVectors.get(i), pageVectors.get(j));
				} else {
					distances[i][j] = distances[j][i];
				}
			}
		}
		
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for(Double[] row: distances) {
			for(Double cellValue: row) {
				if(cellValue.isInfinite()) {
					builder.append("1");
					builder.append("\t\t");
				} else if(cellValue.isNaN()) { 
					builder.append("0");
					builder.append("\t\t");
				} else if(cellValue == 0.0d) {
					builder.append(cellValue);
					builder.append("\t\t");
				} else {
					builder.append(new BigDecimal(cellValue).setScale(10, RoundingMode.HALF_EVEN));
					builder.append("\t");
				}
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	public static void main(String[] args) {
		
		String inputPath = "../data/page_ids/dan_output/larger-access.vectors";
		String outputPath = "../data/distance_matrices/larger-access.distances";
		
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
			out.println(matrix);
			
			out.close();
			
		} catch(IOException e) {
			System.out.println("File not found.");
		}
		
	}
}
