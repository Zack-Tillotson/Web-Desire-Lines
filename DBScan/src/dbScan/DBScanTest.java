package dbScan;

import java.util.Vector;

public class DBScanTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vector<Vector<Integer>> data;	
		double eps;
		System.out.println("test");
		data=FileIO.ReadFile("larger-access.vectors");
		//eps=DBScan.getEPS(4, data);
		eps=.10;
		DBScan.dbScan(4, eps, data);
	}

}
