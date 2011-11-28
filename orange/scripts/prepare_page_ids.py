import os
from csv import reader, writer

os.chdir("/home/daniel/Workspace/Java/Eclipse/UI Design/Web-Desire-Lines/orange/scripts")

vectors_file = reader(open("../data/larger-access.vectors"), delimiter=' ')

paths = []
maxLen = 0

for line in vectors_file:
	line.remove("")
	paths.append(line)
	if len(line) > maxLen: maxLen = len(line)

for path in paths:
	for i in range(len(path), maxLen+1):
		path.append("-1")

filtered_file = writer(open("../data/larger-access.tab", "w"), delimiter='\t')


filtered_file.writerow(["page_%d" % i for i in range(1, len(paths[0])+1)])
filtered_file.writerow(["discrete"]*len(paths[0]))

for path in paths:
	filtered_file.writerow(path)