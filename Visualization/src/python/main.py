from csv import reader, writer

import draw_clusters

file_reader = reader(open('../../../data/distance_matrices/larger-access.distances'), delimiter='\t')

distances = []
i = 0
for line in file_reader:
	if i == 0:
		labels = [str(i) for i in range(len(line))]

	distance_line = []
	for dist in line:
		if(dist != ""):
			distance_line.append(float(dist))
	distances.append(distance_line)

plane_points = draw_clusters.scaledown(distances)

draw_clusters.draw2d(plane_points,labels,jpeg='../../../data/visualizations/md_scaled_clusters.jpg')