from csv import reader, writer

#read in cluster assignments
cluster_assignment_reader = reader(open("medoid_clusters_2.csv"), delimiter=',', quotechar='"')

cluster_assignments = []
for line in cluster_assignment_reader:
	if line[0] == "row ID": continue
	cluster_assignments.append(line[-1])


colors = {
	'Row15': {
		'medoid': ["0.0", "0.9", "0.0"],
		'data_point': ["0.9", "0.0", "0.0"]
	},
	'Row870': {
		'medoid': ["0.0", "0.9", "0.9"],
		'data_point': ["0.0", "0.0", "0.9"]
	}
	# 'Row55': {
	# 	'medoid': ["0.54", "0.72", "0.0"],
	# 	'data_point': ["0.9", "0.0", "0.0"]
	# },
	# 'Row654': {
	# 	'medoid': ["0.0", "0.9", "0.9"],
	# 	'data_point': ["0.0", "0.9", "0.0"]
	# },
	# 'Row187': {
	# 	'medoid': ["0.4", "0.2", "0.9"],
	# 	'data_point': ["0.0", "0.0", "0.9"]
	# }
}

#write colors into python script
data_reader = reader(open("mds.data.py"), delimiter=',')
data_writer = writer(open("mds.data.colorized.py", 'w'), delimiter=',')
rownum = 0
for line in data_reader:
	if len(line) > 0 and line[0][0:4] == "plot":

		if colors.has_key("Row" + str(rownum)):
			#increase marker size
			line[4] = line[4][0:-1] + "50"

			#change marker symbol
			line[2] = line[2][0:-2] + "x'"

			# line.insert(8, line[8][0:-6] + '(' + str(colors[cluster_assignments[rownum]]['medoid'][0]))
			# line.insert(9, str(colors[cluster_assignments[rownum]]['medoid'][1]))
			# line.insert(10, str(colors[cluster_assignments[rownum]]['medoid'][2]) + ')')

			#assign red
			line[5] = line[5][0:-3] + colors[cluster_assignments[rownum]]['medoid'][0]
			#assign green
			line[6] = colors[cluster_assignments[rownum]]['medoid'][1]
			#assign blue
			line[7] = colors[cluster_assignments[rownum]]['medoid'][2] + line[7][4]

		else:
			#assign red
			line[5] = line[5][0:-3] + colors[cluster_assignments[rownum]]['data_point'][0]
			#assign green
			line[6] = colors[cluster_assignments[rownum]]['data_point'][1]
			#assign blue
			line[7] = colors[cluster_assignments[rownum]]['data_point'][2] + line[7][4]

		

		rownum += 1

	data_writer.writerow(line)