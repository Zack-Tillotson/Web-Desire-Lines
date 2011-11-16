from PIL import Image,ImageDraw
import random
from math import sqrt

def scaledown(distances,rate=0.001):
  """
  Scales n dimensional data down to a plane representation and outputs result.
  """
  n=len(distances[0])

  # The real distances between every pair of items
  realdist=distances

  # Randomly initialize the starting points of the locations in 2D
  loc=[[random.random(),random.random()] for i in range(n)]
  fakedist=[[0.0 for j in range(n)] for i in range(n)]
  
  term_val = 0

  lasterror=None
  for m in range(0,1000):
    # Find projected distances
    for i in range(n):
      for j in range(n):
        fakedist[i][j]=sqrt(sum([pow(loc[i][x]-loc[j][x],2) 
                                 for x in range(len(loc[i]))]))
  
    # Move points
    grad=[[0.0,0.0] for i in range(n)]
    
    totalerror=0
    for k in range(n):
      for j in range(n):
        if j==k: continue
        # The error is percent difference between the distances
        errorterm=(fakedist[j][k]-realdist[j][k])/realdist[j][k]
        
        # Each point needs to be moved away from or towards the other
        # point in proportion to how much error it has
        grad[k][0]+=((loc[k][0]-loc[j][0])/fakedist[j][k])*errorterm
        grad[k][1]+=((loc[k][1]-loc[j][1])/fakedist[j][k])*errorterm

        # Keep track of the total error
        totalerror+=abs(errorterm)
    print totalerror

    # If the answer got worse by moving the points, we are done
    if lasterror and lasterror<totalerror: break
    lasterror=totalerror

    term_val += 1
    if term_val > 100: break
    
    # Move each of the points by the learning rate times the gradient
    for k in range(n):
      loc[k][0]-=rate*grad[k][0]
      loc[k][1]-=rate*grad[k][1]

  return loc

def draw2d(data,labels,png='data_2d.jpg'):
  """
  Displays 2 dimensional data.
  """
  xy = zip(*data)

  minx = min(xy[0])
  maxx = max(xy[0])
  avgx = sum(xy[0])/len(xy[0])

  miny = min(xy[1])
  maxy = max(xy[1])
  avgy = sum(xy[1])/len(xy[1])

  print "x: [" + str(minx) + "," + str(maxx) + "]"
  print "y: [" + str(miny) + "," + str(maxy) + "]"


  x_size = int(maxx-minx)*5
  y_size = int(maxy-miny)*5

  img=Image.new('RGB',(10*x_size,10*y_size),(255,255,255))
  draw=ImageDraw.Draw(img)
  for i in range(len(data)):
    x=((data[i][0]-avgx)*100 + x_size/2)
    y=((data[i][1]-avgy)*100 + y_size/2)
    draw.text((x,y),labels[i],(0,0,0))
  img.save(png,'PNG')