package ExtractLogs;

import java.util.Vector;

public class DistanceMetric {
	
	public static double findPathDistance(Vector<Integer> firstPath, Vector<Integer> secondPath){
		double ret=0;
		double avgLen;
		int firstPathValue;
		int secondPathSize = secondPath.size();
		avgLen=firstPath.size()+secondPath.size();
		Vector<Integer> currentPaths;
		currentPaths=new Vector<Integer>();
		int curlength=0;
		int curDist = 0;
		int minDist=1;
		avgLen=avgLen/2;
		
		for(int i =0;i<firstPath.size();i++){
			firstPathValue=firstPath.get(i);
			if(currentPaths.size()==0){
				for(int j =0;j<secondPath.size();j++){
					if(secondPath.get(j)==firstPathValue){
						currentPaths.add(j);
					}
				}
				if(currentPaths.size()>0){
					curlength=1;
				}
			}
			else{
				minDist=Integer.MAX_VALUE;
				for(int j = 0; j<currentPaths.size();j++){
					currentPaths.set(j, currentPaths.get(j)+1);
					if(secondPathSize==currentPaths.get(j)){
						curDist=Math.abs(currentPaths.get(j)-i);
						if(curDist<minDist){
							minDist=curDist;
						}
						currentPaths.remove(j);
						j--;
					}
					else if(secondPath.get(currentPaths.get(j))!=firstPathValue){
						curDist=Math.abs(currentPaths.get(j)-i);
						if(curDist<minDist){
							minDist=curDist;
						}
						currentPaths.remove(j);
						j--;
					}
				}
				if(currentPaths.size()>0){
					curlength++;
				}
				else{
					ret=ret+(Math.pow(curlength/avgLen,1.25)*((double)1/(minDist+1)));
				}
			}
		}
		if(currentPaths.size()>0){
			firstPathValue=firstPath.size()-1;
			minDist=Integer.MAX_VALUE;
			for(int i = 0;i<currentPaths.size();i++){
				curDist=Math.abs(currentPaths.get(i)-firstPathValue);
				if(minDist>curDist){
					minDist=curDist;
				}
			}
			ret=ret+(Math.pow(curlength/avgLen,1.25)*Math.pow((double)1/(minDist+1),.33));
		}
		
		return ret;
	}
	

}
