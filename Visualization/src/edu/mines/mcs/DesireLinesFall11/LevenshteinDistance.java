package edu.mines.mcs.DesireLinesFall11;

import java.util.Vector;

/**
 * An adaptation of Chas Emerick's implementation:
 * http://www.merriampark.com/ldjava.htm
 */
public class LevenshteinDistance implements Distance {

	/**
	 * Returns a value between 0 and 1
	 */
	@Override
	public double calculate(Vector<Integer> s, Vector<Integer> t) {
		
		int resultDistance;
		
		int n = s.size(); // length of s
		int m = t.size(); // length of t

		if(n == 0 || m == 0) {
			if (n == 0) {
				resultDistance = m;
			} else {
				resultDistance = n;
			}
			//transform the distance to [0,1]

			return (double)resultDistance + 0.01;
		}
		
		int p[] = new int[n+1]; //'previous' cost array, horizontally
		int d[] = new int[n+1]; // cost array, horizontally
		int _d[]; //placeholder to assist in swapping p and d

		// indexes into paths s and t
		int i; // iterates through s
		int j; // iterates through t

		int t_j; // jth Integer of t

		int cost; // cost

		for (i = 0; i<=n; i++) {
		    p[i] = i;
		}

		for (j = 1; j<=m; j++) {
		    t_j = t.elementAt(j-1);
		    d[0] = j;

		    for (i=1; i<=n; i++) {
		        cost = s.elementAt(i-1)==t_j ? 0 : 1;
		        // minimum of cell to the left+1, to the top+1, diagonally left and up +cost                
		        d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);  
		    }

		    // copy current distance counts to 'previous row' distance counts
		    _d = p;
		    p = d;
		    d = _d;
		} 

		// our last action in the above loop was to switch d and p, so p now 
		// actually has the most recent cost counts
		resultDistance = p[n];

		return (double)resultDistance + 0.01;
	}

}
