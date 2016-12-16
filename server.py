import socket
import svgwrite
import random

stack = []

def stringToPoints(pointsData):
	print(type(pointsData))
	pointsList = pointsData.split('#')
	pointsList = pointsList[1:len(pointsList)-2]
	for i in range(len(pointsList)):
		pointsList[i] = pointsList[i][1:len(pointsList[i])-1].split(", ")
		for j in range(len(pointsList[i])):
			pointsList[i][j]=float(pointsList[i][j])
			if(i!=0):
				pointsList[i][j]=pointsList[i-1][j]+pointsList[i][j]
		pointsList[i] = tuple(pointsList[i])
	return pointsList

def toSVG(pointsList, id, dwg):
	factor = 1
	d = [('M', pointsList[0][0]*factor, pointsList[0][1]*factor)]
	i = 0
	if(len(pointsList)%3!=0):
		for i in range(3-len(pointsList)%3):
			pointsList.append(pointsList[len(pointsList)-1])
	while 1:
		if(i+3>len(pointsList)):
			break
		else:
			verts1=pointsList[i:i+3]
			i+=3
			d.append(('C', verts1[0][0]*factor, verts1[0][1]*factor, verts1[1][0]*factor, verts1[1][1]*factor, verts1[2][0]*factor, verts1[2][1]*factor))
	stroke = svgwrite.path.Path(d=d, id=id, stroke='black', fill='none')
	stack.append(stroke)
	dwg.add(stroke)
	return dwg

def undoSVG(id, dwg, fl_name):
	if(id==0):
		del stack[:]
	else:
		for i in range(len(stack)):
			if(stack[i].get_id()==id):
				del stack[i]
				break
	dwg = svgwrite.Drawing(fl_name+'.svg', size=('2000', '1300'), viewBox=('0 0 2000 1300'))
	for i in stack:
		dwg.add(i)
	return dwg

def finSVG(dwg):
	dwg.save()
	fl_name = str(random.randint(100000, 999999))
	dwg = svgwrite.Drawing(fl_name+'.svg', size=('2000', '1300'), viewBox=('0 0 2000 1300')) 
	return dwg, fl_name

def UDPConnection():
	serverSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	serverSocket.bind(("192.168.0.127",0))
	counter = 0
	print(serverSocket.getsockname())	
	fl_name = str(random.randint(100000, 999999))
	dwg = svgwrite.Drawing(fl_name+'.svg', size=('2000', '1300'), viewBox=('0 0 2000 1300'))
	c = 0
	while True:
		data, addr = serverSocket.recvfrom(4096)
		PointsList = (data.decode(encoding='UTF-8')).strip()
		print PointsList
		id = int(PointsList[1])
		if(len(PointsList)>2):
			PointsList = stringToPoints(PointsList[2:])		
			dwg = toSVG(PointsList, id, dwg)
		elif(str(PointsList)=='!0'):
			dwg, fl_name = finSVG(dwg)
		else:
			dwg = undoSVG(id, dwg, fl_name)
		c+=1		

UDPConnection()
