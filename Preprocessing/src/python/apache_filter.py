from csv import reader, writer
from collections import OrderedDict
import re

file_reader = reader(open('../../../data/apache_logs/summitcove-access.log'), delimiter=' ')
file_writer = writer(open('../../../data/apache_logs/summitcove-access.final.log', 'w'), delimiter=' ')

month_map = {
	'Jan':"01",
	'Feb':"02",
	'Mar':"03",
	'Apr':"04",
	'May':"05",
	'Jun':"06",
	'Jul':"07",
	'Aug':"08",
	'Sep':"09",
	'Oct':"10",
	'Nov':"11",
	'Dec':"12"
	}

def extract_params(line):
	row = OrderedDict()
	row['client_ip'] = line[0]

	#merge non-quoted user ids starting in the third slot
	s = 2
	if(line[2] not in ['-', '']):
		while(line[s][0] != '['):
			s += 1
		line[2:s] = [" ".join(line[2:s])]

	datetime = line[3].lstrip("[").split(":")
	date_elements = datetime[0].split("/")
	row['date'] = date_elements[2] + "-" + month_map[date_elements[1]] + "-" + date_elements[0]
	row['time'] = ":".join(datetime[1:4])
	method_uri_query = line[5].split(" ")
	row['method'] = method_uri_query[0]
	if(len(method_uri_query) > 1):
		uri_query = method_uri_query[1].split("?")
		row['uri'] = uri_query[0]
		row['query'] = uri_query[1] if len(uri_query) > 1 else ""
	else:
		row['uri'] = row['query'] = ""
	row['response_code'] = line[6]
	row['referrer'] = line[8]
	row['user_agent'] = line[9]
	return row

def search_for_substrings(data, search_strings):
    for k in search_strings:
        if k in data:
            return True
    return False

non_content_file_extensions = re.compile(r"\.(js|css|jpg|jpeg|gif|png|ico|xml)$", re.IGNORECASE)
excluded_paths = re.compile(r"^/(admin|owners/admin|i/|feed|screensaver|ssp|agentlogin|confirmation|icontact|robots|site-map|wp-)")

filter_chain = [
	#non-content filter
	lambda row: non_content_file_extensions.search(row['uri']) != None,

	#excluded path filter
	lambda row: excluded_paths.search(row['uri']) != None,
	
	#failed request filter
	lambda row: row['response_code'] != "200",

	#robot filter
	lambda row: search_for_substrings(row['user_agent'], [
		"80legs.com",
		"AdsBot",
		"Aghaven",
		"archive.org",
		"ask.com",
		"BacklinkCrawler",
		"baidu",
		"bingbot", "msnbot",
		"bnf.fr",
		"Butterfly",
		"Covario-IDS",
		"Exabot",
		"Ezooms",
		"flightdeckreports",
		"Gigabot",
		"Googlebot",
		"HuaweiSymantecSpider",
		"ia_archiver",
		"InfoPath",
		"magpie-crawler",
		"Mail.Ru",
		"majestic12",
		"MLBot",
		"MojeekBot",
		"NaverBot",
		"NHN Corp.",
		"Nutch",
		"radian6",
		"ScooperBot",
		"ScoutJet",
		"seoprofiler.com",
		"SeznamBot",
		"ShopSalad",
		"SiteIntel.net",
		"Sogou",
		"Sosospider",
		"Speedy Spider",
		"Spinn3r",
		"SurveyBot",
		"TimKimSearch",
		"Twitterbot",
		"UnisterBot",
		"VoilaBot",
		"Yahoo", 
		"YandexBot",
		"YandexImages",
		"YodaoBot", "YoudaoBot"
		])
]


try:
	i = 0
	for line in file_reader:
		row = extract_params(line)

		exclude = False
		for filter in filter_chain:
			if(filter(row)):
				exclude = True

		if(exclude): continue

		i += 1
		print i

		file_writer.writerow(line)

except IndexError:
	print "index error:"
	print line