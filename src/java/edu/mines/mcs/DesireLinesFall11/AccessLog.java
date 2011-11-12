/**
 * csci522 Usability/User Interface Design
 */
package edu.mines.mcs.DesireLinesFall11;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

/**
 * @author Dan Baumann <dbaumann@mines.edu>
 */
public class AccessLog {
	
	private List<LogEntry> entries = new LinkedList<LogEntry>();
	private Set<UserSession> sessions = new LinkedHashSet<UserSession>();
	
	private final SessionHost sessionHost;
	
	public AccessLog(SessionHost host) {
		this.sessionHost = host;
	}
	
	public static AccessLog forIISHost(String logPath, SessionHost host) {
		AccessLog output = new AccessLog(host);
		
		try{
			FileReader reader = new FileReader(logPath);
			Scanner in = new Scanner(reader);
			
			int i = 0;
			while(in.hasNextLine()) {
				i++;
				String line = in.nextLine();
				
				if(line.charAt(0) == '#' || line.length() == 0) continue;
				
				String[] row = line.split(" ");
				
				try {
					output.entries.add(new IISLogEntry.Builder()
						.date(row[0])
						.time(row[1])
						.sourceIp(row[2])
						.username(row[3])
						.destIp(row[4])
						.port(row[5])
						.httpMethod(row[6])
						.uri(row[7])
						.query(row[8])
						.responseCode(row[9])
						.userAgent(row[10])
						.build());
				} catch(IllegalArgumentException e) {
					System.out.println("Improperly formatted line " + i + " in " + logPath + ":" + Arrays.toString(row));
				}
			}
		} catch(FileNotFoundException e) {
			System.out.println("Log file " + logPath + " not found.");
		}
		
		return output;
	}
	
	public static AccessLog forApacheHost(String logPath, SessionHost host) {
		AccessLog output = new AccessLog(host);
		
		try{
			FileReader reader = new FileReader(logPath);
			Scanner in = new Scanner(reader);
			
			Hashtable<String, String> months = new Hashtable<String, String>();
			months.put("Jan", "01");
			months.put("Feb", "02");
			months.put("Mar", "03");
			months.put("Apr", "04");
			months.put("May", "05");
			months.put("Jun", "06");
			months.put("Jul", "07");
			months.put("Aug", "08");
			months.put("Sep", "09");
			months.put("Oct", "10");
			months.put("Nov", "11");
			months.put("Dec", "12");

			while(in.hasNextLine()) {
				String line = in.nextLine();
				
				if(line.charAt(0) == '#') continue;
				
				String[] row = line.split(" ");
				
				try {
					String[] dateTime = row[3].replaceFirst("^\\[", "").split(":");
					String[] date = dateTime[0].split("/");
					
					String dateString = date[2] + "-" + months.get(date[1]) + "-" + date[0];
					String timeString = dateTime[1] + ":" + dateTime[2] + ":" + dateTime[3];
					
					String[] uriQuery = row[6].split("\\?");
	
					output.entries.add(new ApacheLogEntry.Builder()
						.clientIp(row[0])
						.date(dateString)
						.time(timeString)
						.method(row[5].replaceFirst("^\"", ""))
						.uri(uriQuery[0])
						.query(uriQuery.length == 1 ? "":uriQuery[1])
						.responseCode(row[8])
						.referrer(row[10].replaceAll("\"", ""))
						.userAgent(row[11].replaceAll("\"", ""))
						.build());

				} catch(ArrayIndexOutOfBoundsException e) {
					//catches query strings with escape sequences in them, which are assumed to be unimportant
					//e.g. \xc3\xb7\x18\xc26 6P\xaa7\xa8\xa1\xc2z\xe8\xe8G\xbe\xad:\xa5{\xc1\xa11\xe0~\xf8\xfeo\xa1x
				}
			}

		} catch(FileNotFoundException e) {
			System.out.println("Log file " + logPath + " not found.");
		}
		
		return output;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WebLog [\nentries=\n");
		for(LogEntry e: entries) {
			builder.append("\t" + e + "\n");
		}
		if(!sessions.isEmpty()) {
			builder.append("sessions=\n");
			for(UserSession s: sessions) {
				builder.append("\t" + s + "\n");
			}
		}
		
		builder.append("entries_count=" + entries.size() + "\n");
		builder.append("sessions_count=" + sessions.size() + "\n");
		
		builder.append("]");
		return builder.toString();
	}
	
	public List<LogEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}
	
	public Set<UserSession> getSessions() {
		if(sessions.isEmpty()) this.generateSessions();
		return sessions;
	}
	
	private void generateSessions() {
		UserSession itSession;
		
		for(LogEntry le: entries) {
			
			//ignore failed http requests
			if(le.responseCode() != 200) {
				continue;
			}
			
			itSession = new UserSession(le.clientIp(), le.userAgent(), sessionHost);
			
			if(!sessions.contains(itSession)) {
				sessions.add(itSession);
				itSession.addToSession(le);
				continue;
			}
			
			for(UserSession us: sessions) {
				if(us.addToSession(le)) {
					//the log entry found a home
					break;
				}
			}	
		}
	}

	public void saveOutputToFile(String outputPath) {
		PrintWriter out;
		
		try {
			out = new PrintWriter(new FileWriter(outputPath));
			
			this.generateSessions();
			out.println(this);
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveAsXml(String outputXmlPath) {
		try {
			//create the xml tree
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = doc.createElement("WebLog");
			doc.appendChild(root);
			
			this.generateSessions();
			
			for(UserSession us: sessions) {
				Element user_session = doc.createElement("UserSession");
				root.appendChild(user_session);
				
				user_session.setAttribute("Date", us.getSessionDate().toString());
				user_session.setAttribute("StartTime", us.getStartTime().toString());
				user_session.setAttribute("EndTime", us.getEndTime().toString());
				user_session.setAttribute("ClientIp", us.getClientIp().toString());
				user_session.setAttribute("UserAgent", us.getUserAgent().toString());
				
				for(UserSession.SessionLanding land: us.getClickStream()) {
					Element landing = doc.createElement("SessionLanding");
					landing.setAttribute("Uri", land.uri);
					landing.setAttribute("Query", land.query.toString());
					landing.setAttribute("Referrer", (land.referrer == null ? "null":land.referrer.toString()));
					user_session.appendChild(landing);
				}
			}
			
			//transform the tree into a string with indentation
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
			String xmlString = result.getWriter().toString();
			
			//print the string to a file
			PrintWriter out = new PrintWriter(new FileWriter(outputXmlPath));
			out.println(xmlString);
			out.close();
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveAsIdRows(String idsOutputPath, String indexOutputPath) {
		Set<UserSession.SessionLanding> pageIndex = new TreeSet<UserSession.SessionLanding>();
		
		//generate the sessions
		this.generateSessions();
		
		//build up the index of unique pages (SessionLandings)
		for(UserSession s: sessions) pageIndex.addAll(s.getClickStream());
		
		//convert the pageIndex to a list for index lookup
		List<UserSession.SessionLanding> pageIndexList = new ArrayList<UserSession.SessionLanding>(pageIndex);
		
		//output the index of pages and the rows of page ids
		try {
			PrintWriter pageIdsOutput = new PrintWriter(new FileWriter(idsOutputPath));
			PrintWriter pageIndexOutput = new PrintWriter(new FileWriter(indexOutputPath));

			for(int i=0; i<pageIndexList.size(); i++) {
				pageIndexOutput.println(i + ": " + pageIndexList.get(i));
			}
			pageIndexOutput.close();
			
			Integer key;
			
			for(UserSession s: sessions) {
				StringBuilder line = new StringBuilder();
				for(UserSession.SessionLanding sl: s.getClickStream()) {
					key = pageIndexList.indexOf(sl);
					line.append(key.toString() + " ");
				}
				pageIdsOutput.println(line.toString());
			}
			
			pageIdsOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Hashtable<String, String> parseQueryString(String queryString) {
		Hashtable<String, String> queryParams = new Hashtable<String, String>();
		
		if(!queryString.equals("")) {
			for(String queryAssignment: queryString.split("&")) {
				if(queryAssignment.indexOf('=') == -1) continue;
				String[] queryPair = queryAssignment.split("=");
				if(queryPair.length < 2) continue;
				queryParams.put(queryPair[0], queryPair[1]);
			}
		}
		
		return queryParams;
	}

	public static void main(String[] args) {
		
//		String[] iisLogs = {"ex031008.log", "ex110820.log", "ex110826.log",
//							"ex110904.log", "ex110908.log", "ex111002.log",
//							"ex111009.log", "ex110817.log", "ex110822.log",
//							"ex110829.log", "ex110905.log", "ex110910.log",
//							"ex111007.log", "ex111010.log", "ex110818.log",
//							"ex110824.log", "ex110831.log", "ex110906.log",
//							"ex110918.log", "ex111008.log", "ex111011.log"};
//		for(String logName: iisLogs) {			
//			String fileName = logName.split("\\.")[0];
//
//			System.out.println(fileName);
//			
//			AccessLog iisLog = AccessLog.forIISHost("logs/" + fileName + ".log", new SessionHost(){
//				public HostQuery newQuery(String queryText) {
//					final String queryString = queryText; //final makes it available to HostQuery
//					final Hashtable<String, String> queryTable = parseQueryString(queryText);
//					
//					class VailCoachQuery implements HostQuery {
//						
//						private String queryString;
//						private Hashtable<String, String> queryTable;
//						
//						public VailCoachQuery(String queryString, Hashtable<String, String> queryTable) {
//							this.queryString = queryString;
//							this.queryTable = queryTable;
//						}
//						
//						public String toString() {
//							return queryString;
//						}
//						
//						public int hashCode() {
//							return 31 + ((queryString == null) ? 0 : queryString.hashCode());
//						}
//						
//						/**
//						 * The only feature that distinguishes queries is the id parameter.
//						 */
//						public boolean equals(Object obj) {
//							if (this == obj) return true;
//							if (obj == null) return false;
//							if (getClass() != obj.getClass()) return false;
//							
//							VailCoachQuery other = (VailCoachQuery) obj;
//							
//							String thisId = queryTable.get("id");
//							if(thisId != null) {
//								String otherId = other.queryTable.get("id");
//								if(otherId != null) {
//									return thisId.equals(otherId);
//								}
//							}
//							return true;
//						}
//						
//						/**
//						 * The only feature that distinguishes queries is the id parameter.
//						 */
//						public int compareTo(HostQuery otherQuery) {
//							String thisId = queryTable.get("id");
//							if(thisId != null) {
//								String otherId = ((VailCoachQuery) otherQuery).queryTable.get("id");
//								if(otherId != null) return thisId.compareTo(otherId); //lexicographic comparison
//							}
//							return 0; //if one query or the other lacks a PropertyID parameter, assume they're the same.
//						}
//					}
//					
//					return new VailCoachQuery(queryString, queryTable);
//				}
//			});
//			iisLog.saveAsIdRows("output/breian_output/" + fileName + ".vectors", "output/breian_output/" + fileName + ".index");
//		
//		} //end for each file name
//		System.out.println("IIS Parsing Complete");
		
		
//		AccessLog comradeApacheLog = AccessLog.forApacheHost("logs3/apache-access.log", new SessionHost(){
//			public HostQuery newQuery(String queryText) {
//				final String queryString = queryText; //final makes it available to HostQuery
//				final Hashtable<String, String> queryTable = parseQueryString(queryText);
//				
//				class ComradeQuery implements HostQuery {
//					
//					private String queryString;
//					private Hashtable<String, String> queryTable;
//					
//					public ComradeQuery(String queryString, Hashtable<String, String> queryTable) {
//						this.queryString = queryString;
//						this.queryTable = queryTable;
//					}
//					
//					public String toString() {
//						return queryString;
//					}
//					
//					public int hashCode() {
//						return 31 + ((queryString == null) ? 0 : queryString.hashCode());
//					}
//					
//					/**
//					 * Nothing to distinguish queries.
//					 */
//					public boolean equals(Object obj) {
//						if (this == obj) return true;
//						if (obj == null) return false;
//						if (getClass() != obj.getClass()) return false;
//						
//						return true;
//					}
//					
//					/**
//					 * Nothing to distinguish queries.
//					 */
//					public int compareTo(HostQuery otherQuery) {
//						return 0;
//					}
//				}
//				
//				return new ComradeQuery(queryString, queryTable);
//			}
//		});
//		comradeApacheLog.saveAsIdRows("output/zach_output/apache-access.vectors", "output/zach_output/apache-access.index");
		
		AccessLog summitApacheLog = AccessLog.forApacheHost("logs4/summitcove-access.log", new SessionHost(){
			public HostQuery newQuery(String queryText) {
				final String queryString = queryText; //final makes it available to HostQuery
				final Hashtable<String, String> queryTable = parseQueryString(queryText);
				
				class SummitcoveQuery implements HostQuery {
					
					private String queryString;
					private Hashtable<String, String> queryTable;
					
					public SummitcoveQuery(String queryString, Hashtable<String, String> queryTable) {
						this.queryString = queryString;
						this.queryTable = queryTable;
					}
					
					public String toString() {
						return queryString;
					}
					
					public int hashCode() {
						return 31 + ((queryString == null) ? 0 : queryString.hashCode());
					}
					
					/**
					 * The only feature that distinguishes queries is the PropertyID parameter.
					 */
					public boolean equals(Object obj) {
						if (this == obj) return true;
						if (obj == null) return false;
						if (getClass() != obj.getClass()) return false;
						
						SummitcoveQuery other = (SummitcoveQuery) obj;
						
						String thisId = queryTable.get("PropertyID");
						if(thisId != null) {
							String otherId = other.queryTable.get("PropertyID");
							if(otherId != null) {
								return thisId.equals(otherId);
							}
						}
						return true;
					}
					
					/**
					 * The only feature that distinguishes queries is the PropertyID parameter.
					 */
					public int compareTo(HostQuery otherQuery) {
						String thisId = queryTable.get("PropertyID");
						if(thisId != null) {
							String otherId = ((SummitcoveQuery) otherQuery).queryTable.get("PropertyID");
							if(otherId != null) return thisId.compareTo(otherId); //lexicographic comparison
						}
						return 0; //if one query or the other lacks a PropertyID parameter, assume they're the same.
					}
				}
				
				return new SummitcoveQuery(queryString, queryTable);
			}
		});
		summitApacheLog.saveAsIdRows("output/dan_output/summitcove-access.vectors", "output/dan_output/summitcove-access.index");
		
	}
}
