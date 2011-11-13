package dbScan;

import java.util.ArrayList;
import java.util.Vector;

public class DBScan {
	
	public static double getEPS(int numpoints,Vector<double[]> data){
		double ret=0;
		double averageKth=0;
		double[] kthDist;
		double[] currentdistances;
		double currentDist,temp;
		int i;
		int j;
		int k;
		kthDist=new double[data.size()];
		currentdistances=new double[numpoints];
		for(i=0;i<data.size();i++){
			kthDist[i]=Double.MAX_VALUE;
		}
		for(i=0;i<data.size();i++){
			for(j=0;j<numpoints;j++){
				currentdistances[j]=Double.MAX_VALUE;
			}
			for(j=0;j<data.size();j++){
				if(j!=i){
					//System.out.println(data.get(i).length+","+data.get(j).length);
					currentDist=similarity_metrics.Similarity.euclidean(data.get(i), data.get(j));
					for(k=0;k<numpoints;k++){
						if(currentDist<currentdistances[k]){
							temp=currentdistances[k];
							currentdistances[k]=currentDist;
							currentDist=temp;
						}
					}
				}
			}
			
			currentDist=currentdistances[numpoints-1];
			for(j=0;j<data.size();j++){
				if(currentDist<kthDist[j]){
					temp=kthDist[j];
					kthDist[j]=currentDist;
					currentDist=temp;
				}
			}
		}
		j=0;
		averageKth=kthDist[data.size()-1]-kthDist[0];
		averageKth=averageKth/data.size();
		for(i=0;i<data.size()-1&&j<3;i++){
			if(kthDist[i+1]-kthDist[i]>averageKth){
				j++;
			}
			else{
				j=0;
			}
		}
		ret=kthDist[i-20];
		//System.out.println(ret);
		return ret;
	}
	
	public static Vector<String> dbScan(int numpoints, double eps,Vector<Vector<Integer>> data){
		Vector<String> ret;
		Vector<Vector<Integer>> allpoints;
		ret=new Vector<String>();
		ArrayList<Integer> pending,nearpoints;
		pending = new ArrayList<Integer>();
		int currentset=0;
		int i,j;
		boolean isset;
		int pointCount;
		for(i=0;i<data.size();i++){
			ret.add("Noise");
		}
		for(i=0;i<data.size();i++){
			if(ret.get(i).startsWith("Noise")){
				isset=false;
				pending.add(i);
				while(pending.size()>0){
					nearpoints=new ArrayList<Integer>();
					pointCount=0;
					for(j=0;j<data.size();j++){
						if(j!=i){
							if(ExtractLogs.DistanceMetric.findPathDistance(data.get(i),data.get(j))>eps){
								pointCount++;
								if(ret.get(j).startsWith("Noise")){
									nearpoints.add(j);								
								}
							}
						}
					}
					if(pointCount>numpoints){
						isset=true;
						ret.set(pending.get(0),""+currentset);
						for(j=0;j<nearpoints.size();j++){
							if(!pending.contains(nearpoints.get(j))){
								pending.add(nearpoints.get(j));
							}
							if(ret.get(nearpoints.get(j)).startsWith("Noise")){
								ret.set(nearpoints.get(j), ""+currentset);
							}
						}
					}
					//System.out.println("processesed " +pending.get(0));
					pending.remove(0);
				}
				if(isset){
					currentset++;
				}
			}
			//System.out.println("group complete");
		}
		allpoints=new Vector<Vector<Integer>>();
		allpoints.add(new Vector<Integer>());
		for(i=0;i<currentset;i++){
			allpoints.add(new Vector<Integer>());
			
		}
		for(i=0;i<data.size();i++){
			if(ret.get(i).contentEquals("Noise")){
				allpoints.get(allpoints.size()-1).add(i);
			}
			else{
				j=Integer.parseInt(ret.get(i));
				allpoints.get(j).add(i);
			}
			//System.out.println(i+":"+ret.get(i));
		}
		for(i=0;i<allpoints.size();i++){
			if(i==allpoints.size()-1){
				System.out.println("Group Noise size " +allpoints.get(i).size());
			}
			else{
				System.out.println("Group "+i+" size " +allpoints.get(i).size());
			}
			for(j=0;j<allpoints.get(i).size();j++){
				System.out.print(allpoints.get(i).get(j)+":");
				for(int k = 0;k<data.get(allpoints.get(i).get(j)).size();k++){
					System.out.print(data.get(allpoints.get(i).get(j)).get(k)+",");
				}
				System.out.println();
			}
		}
		
		return ret;
	}
}
