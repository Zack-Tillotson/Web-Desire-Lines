package edu.mines.mcs.DesireLinesFall11;

import java.sql.Date;
import java.sql.Time;

public interface LogEntry {
	
	public Date date();
	public Time time();
	public String clientIp();
	public String method();
	public String uri();
	public String query();
	public int responseCode();
	public String referrer();
	public String userAgent();
}
