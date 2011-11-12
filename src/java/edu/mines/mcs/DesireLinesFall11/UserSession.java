/**
 * csci522 Usability/User Interface Design
 */
package edu.mines.mcs.DesireLinesFall11;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dan Baumann <dbaumann@mines.edu>
 */
public class UserSession {
	
	private Date sessionDate;
	private Time startTime;
	private Time endTime;
	
	private final String clientIp;
	private final String userAgent;
	
	private List<SessionLanding> clickStream = new ArrayList<SessionLanding>();
	
	private final SessionHost sessionHost;
	
	public UserSession(String clientIp, String userAgent, SessionHost host) {
		this.clientIp = clientIp;
		this.userAgent = userAgent;
		this.sessionHost = host;
	}
	
	public Date getSessionDate() { return Date.valueOf(sessionDate.toString()); }
	
	public Time getStartTime() { return Time.valueOf(startTime.toString()); }

	public Time getEndTime() { return Time.valueOf(endTime.toString()); }

	public String getClientIp() { return clientIp; }
	
	public String getUserAgent() { return userAgent; }

	public List<SessionLanding> getClickStream() { return new ArrayList<SessionLanding>(clickStream); }

	/**
	 * Add an entry to the clickStream, if it qualifies as part of this session.
	 * 
	 * @param LogEntry	The entry to consider adding to this sesssion.
	 * @return boolean	Whether or not the entry was added to this session.
	 */
	public boolean addToSession(LogEntry entry) {
		if(!entry.clientIp().equals(this.clientIp) || !entry.userAgent().equals(this.userAgent)) {
			return false;
		}
		
		//sessionDate should be the earliest discovered
		if(this.sessionDate == null || entry.date().compareTo(this.sessionDate) < 0) {
			this.sessionDate = entry.date();
		}
		
		//startTime should be the earliest discovered
		if(this.startTime == null || entry.time().compareTo(this.startTime) < 0) {
			this.startTime = entry.time();
		}
		
		//endTime should be the latest discovered
		if(this.endTime == null || entry.time().compareTo(this.endTime) > 0) {
			this.endTime = entry.time();
		}
		
		clickStream.add(new SessionLanding(entry.uri(), sessionHost.newQuery(entry.query()), entry.referrer()));
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientIp == null) ? 0 : clientIp.hashCode());
		result = prime * result
				+ ((userAgent == null) ? 0 : userAgent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSession other = (UserSession) obj;
		if (clientIp == null) {
			if (other.clientIp != null)
				return false;
		} else if (!clientIp.equals(other.clientIp))
			return false;
		if (userAgent == null) {
			if (other.userAgent != null)
				return false;
		} else if (!userAgent.equals(other.userAgent))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserSession [sessionDate=" + sessionDate
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", clientIp=" + clientIp + ", userAgent=" + userAgent + ", landingCount=" + landingCount() + "]";
	}

	public static class SessionLanding implements Comparable<SessionLanding> {
		public final String uri;
		public final SessionHost.HostQuery query;
		public final String referrer;
		
		public SessionLanding(String uri, SessionHost.HostQuery query, String referrer) {
			this.uri = uri;
			this.query = query;
			this.referrer = referrer;
		}

		@Override
		public String toString() {
			String output = uri;
			if(query.toString().length() > 0) output += "?" + query;
			return output;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((query == null) ? 0 : query.hashCode());
			result = prime * result + ((uri == null) ? 0 : uri.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			
			SessionLanding other = (SessionLanding) obj;
			
			if (uri == null) {
				if (other.uri != null)
					return false;
			} else if (!uri.equals(other.uri))
				return false;
			
			if (query == null) {
				if (other.query != null)
					return false;
			} else if (!query.equals(other.query))
				return false;
			
			return true;
		}
		
		public int compareTo(SessionLanding otherLanding) {			
			int result = uri.compareTo(otherLanding.uri);
			if(result == 0) result = query.compareTo(otherLanding.query);
			
			return result;
		}
		
	}
	
	/*
	 * FEATURES
	 */
	public int landingCount() {
		return clickStream.size();
	}
}
