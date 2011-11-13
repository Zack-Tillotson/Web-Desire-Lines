package ExtractLogs;

import java.util.Vector;

public class DistanceMetric {
	
	public static double findPathDistance(Vector<Integer> firstPath, Vector<Integer> secondPath){
		double ret=0;
		double avgLen;
		int firstPathValue;
		avgLen=firstPath.size()+secondPath.size();
		Vector<Integer> currentPaths;
		currentPaths=new Vector<Integer>();
		int curlength=0;
		int minDist=1;
		avgLen=avgLen/2;
		for(int i=0;i<secondPath.size();i++){
			if(secondPath.get(i)==firstPath.get(0)){
				currentPaths.add(i);
			}
		}
		for(int i=0;i<firstPath.size();i++){
			minDist=Integer.MAX_VALUE;
			firstPathValue=firstPath.get(i);
			for(int j=0;j<currentPaths.size();j++){
				
				if(secondPath.size()>currentPaths.get(j)+1&&secondPath.get(currentPaths.get(j)+1)==firstPathValue){
					currentPaths.set(j, currentPaths.get(j)+1);
				}
				else{
					if(minDist>Math.abs(i-currentPaths.get(j))){
						minDist=i-currentPaths.get(j);
					}
					currentPaths.remove(j);
					j--;
				}
			}
			if(currentPaths.size()==0){
				ret=ret+((curlength/avgLen)*(curlength/avgLen)*((double)1/(minDist+1)));
				while(currentPaths.size()==0&&i<firstPath.size()){
					for(int j=0;j<secondPath.size();j++){
						if(secondPath.get(j)==firstPathValue){
							currentPaths.add(j);
						}
					}
					i++;
				}
				if(i==firstPath.size()){
					curlength=0;
				}
				else{
					curlength=1;
				}
			}
			else{
				curlength++;
			}
		}
		ret=ret+(curlength/avgLen*(curlength/avgLen)*((double)1/(minDist+1)));
		
		return ret;
	}
	

}
