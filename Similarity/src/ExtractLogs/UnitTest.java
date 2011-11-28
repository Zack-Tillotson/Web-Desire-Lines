package ExtractLogs;

import java.util.Vector;

public class UnitTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Vector<Integer> a,b,c,d;
		a=new Vector<Integer>();
		b=new Vector<Integer>();
		c=new Vector<Integer>();
		d=new Vector<Integer>();
		a.add(1);
		a.add(1);
		a.add(2);
		a.add(2);
		System.out.println(DistanceMetric.findPathDistance(a, a));
		b.add(1);
		b.add(1);
		b.add(2);
		b.add(3);
		System.out.println(DistanceMetric.findPathDistance(b, a));
		c.add(2);
		c.add(2);
		c.add(1);
		c.add(1);
		System.out.println(DistanceMetric.findPathDistance(c, a));
		d.add(5);
		d.add(1);
		d.add(1);
		d.add(2);
		d.add(2);
		System.out.println(DistanceMetric.findPathDistance(d, a));
		System.out.println(DistanceMetric.findPathDistance(d, b));
		System.out.println(DistanceMetric.findPathDistance(d, d));
		
		
		
	}

}
