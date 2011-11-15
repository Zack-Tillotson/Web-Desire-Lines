package edu.mines.mcs.DesireLinesFall11;

import java.sql.Date;
import java.sql.Time;

public class ApacheLogEntry implements LogEntry {
	
	public final Date date;
	public final Time time;
	public final String clientIp;
	public final String method;
	public final String uri;
	public final String query;
	public final int responseCode;
	public final String referrer;
	public final String userAgent;

	/*
	 * For the sake of giving named parameters to the constructor.
	 */
	public static class Builder {
		public Date date;
		public Time time;
		public String clientIp;
		public String method;
		public String uri;
		public String query;
		public int responseCode;
		public String referrer;
		public String userAgent;
		
		public Builder date(String dateString) { date = Date.valueOf(dateString); return this; }
		
		public Builder time(String timeString) { time = Time.valueOf(timeString); return this; }
		
		public Builder clientIp(String ip) { clientIp = ip; return this; }
		
		public Builder method(String method) { this.method = method; return this; }

		public Builder uri(String uri) { this.uri = uri; return this; }
		
		public Builder query(String query) { this.query = query; return this; }
		
		public Builder responseCode(String responseCode) {
			try {
				this.responseCode = Integer.parseInt(responseCode);
			} catch(NumberFormatException e) {
				this.responseCode = -1;
			}
			
			return this;
		}
		
		public Builder referrer(String referrer) { this.referrer = referrer; return this; }
		
		public Builder userAgent(String userAgent) { this.userAgent = userAgent; return this; }
		
		public ApacheLogEntry build() {
			return new ApacheLogEntry(this);
		}
	}
	
	private ApacheLogEntry(Builder builder) {
		date = builder.date;
		time = builder.time;
		clientIp = builder.clientIp;
		method = builder.method;
		uri = builder.uri;
		query = builder.query;
		responseCode = builder.responseCode;
		referrer = builder.referrer;
		userAgent = builder.userAgent;
	}
	
	@Override
	public Date date() { return Date.valueOf(date.toString()); }

	@Override
	public Time time() { return Time.valueOf(time.toString()); }

	@Override
	public String clientIp() { return clientIp; }
	
	@Override
	public String method() { return method; }
	
	@Override
	public String uri() { return uri; }

	@Override
	public String query() { return query.equals("-") ? "":query; }
	
	@Override
	public int responseCode() { return responseCode; }
	
	@Override
	public String referrer() { return referrer.equals("-") ? "":referrer; }
	
	@Override
	public String userAgent() { return userAgent; }

	@Override
	public String toString() {
		return "ApacheLogEntry [clientIp=" + clientIp + ", date=" + date
				+ ", method=" + method + ", query=" + query + ", referrer="
				+ referrer + ", responseCode=" + responseCode + ", time="
				+ time + ", uri=" + uri + ", userAgent=" + userAgent + "]";
	}
	
	
}
