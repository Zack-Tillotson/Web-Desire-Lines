/**
 * csci522 Usability/User Interface Design
 */
package edu.mines.mcs.DesireLinesFall11;

import java.sql.Time;
import java.sql.Date;

/**
 * A web log entry from Microsoft IIS
 *
 * @author Dan Baumann <dbaumann@mines.edu>
 */
public class IISLogEntry implements LogEntry {
	private final Date date;
	private final Time time;
	private final String cIp; //client ip
	private final String csUsername; //client-to-server username
	private final String sIp; //server ip
	private final String sPort; //server port
	private final String csMethod; //client-to-server method
	private final String csUriStem; //client-to-server uri path
	private final String csUriQuery; //client-to-server query
	private final int scStatus; //server-to-client response code
	private final String csUserAgent; //client-to-server user agent
	
	/*
	 * For the sake of giving named parameters to the constructor.
	 */
	public static class Builder {
		private Date date;
		private Time time;
		private String cIp;
		private String csUsername;
		private String sIp;
		private String sPort;
		private String csMethod; //enum?
		private String csUriStem;
		private String csUriQuery;
		private int scStatus; //enum?
		private String csUserAgent; //enum?
		
		public Builder date(String dateString) { date = Date.valueOf(dateString); return this; }
		
		public Builder time(String timeString) { time = Time.valueOf(timeString); return this; }
		
		public Builder sourceIp(String ip) { cIp = ip; return this; }
		
		public Builder username(String username) { csUsername = username; return this; }
		
		public Builder destIp(String ip) { sIp = ip; return this; }
		
		public Builder port(String port) { sPort = port; return this; }
		
		public Builder httpMethod(String method) { csMethod = method; return this; }
		
		public Builder uri(String uri) { csUriStem = uri; return this; }
		
		public Builder query(String query) { csUriQuery = query; return this; }
		
		public Builder responseCode(String responseCode) { scStatus = Integer.parseInt(responseCode); return this; }
		
		public Builder userAgent(String userAgent) { csUserAgent = userAgent; return this; }
		
		public IISLogEntry build() {
			return new IISLogEntry(this);
		}
	}
	
	private IISLogEntry(Builder builder) {
		date = builder.date;
		time = builder.time;
		cIp = builder.cIp;
		csUsername = builder.csUsername;
		sIp = builder.sIp;
		sPort = builder.sPort;
		csMethod = builder.csMethod;
		csUriStem = builder.csUriStem;
		csUriQuery = builder.csUriQuery;
		scStatus = builder.scStatus;
		csUserAgent = builder.csUserAgent;
	}
	
	@Override
	public Date date() { return Date.valueOf(date.toString()); }

	@Override
	public Time time() { return Time.valueOf(time.toString()); }

	@Override
	public String clientIp() { return cIp; }

	public String username() { return csUsername; }

	public String destinationIp() { return sIp; }

	public String port() { return sPort; }
	
	@Override
	public String method() { return csMethod; }
	
	@Override
	public String uri() { return csUriStem; }

	@Override
	public String query() { return csUriQuery.equals("-") ? "":csUriQuery; }
	
	@Override
	public int responseCode() { return scStatus; }
	
	@Override
	public String referrer() { return ""; }
	
	@Override
	public String userAgent() { return csUserAgent; }
	
	
	@Override
	public String toString() {
		return "IISLogEntry [date=" + date + ", time=" + time + ", cIp=" + cIp + ", csMethod=" + csMethod
				+ ", csUsername=" + csUsername
				+ ", csUriStem=" + csUriStem + ", csUriQuery=" + csUriQuery
				+ ", csUserAgent=" + csUserAgent + ", sIp=" + sIp + ", sPort=" + sPort
				+ ", scStatus=" + scStatus + "]";
	}
}
