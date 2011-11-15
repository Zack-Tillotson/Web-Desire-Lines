package edu.mines.mcs.DesireLinesFall11;

public interface SessionHost {
	
	public interface HostQuery extends Comparable<HostQuery> {
		public String toString();
		public int hashCode();
		public boolean equals(Object obj);
	}
	
	public HostQuery newQuery(String queryText);
}
