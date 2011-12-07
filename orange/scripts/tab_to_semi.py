from csv import reader, writer

read = reader(open("summitcove-access.final.good-size.distances"), delimiter="\t")
write = writer(open("summitcove-access.final.good-size.knime.distances",'w'), delimiter=';')

for line in read:
	write.writerow(line) #"".join(line).replace("\t", ";")