import sys
from csv import reader, writer

input_file = sys.argv[1]
output_file = sys.argv[2]

vectors_file = reader(open(input_file), delimiter=' ')

paths = []
maxLen = 0

for line in vectors_file:
	line.remove("")
	paths.append(line)
	if len(line) > maxLen: maxLen = len(line)

for path in paths:
	for i in range(len(path), maxLen+1):
		path.append("-1")

filtered_file = writer(open(output_file, "w"), delimiter='\t')


filtered_file.writerow(["page_%d" % i for i in range(1, len(paths[0])+1)])
filtered_file.writerow(["discrete"]*len(paths[0]))

for path in paths:
	filtered_file.writerow(path)