import cv2
import numpy
import picamera
import io
import os 
import sys
import time

#sys.argv[1] for getting argument from console
def generate_normal():
	global person_name
	person_name = raw_input("Enter person's name for face training: ").replace(" ", "_").upper()
	
	print "Face to the camera with no facial expression (Photo will be taken in three seconds)"
	print "3..."
	time.sleep(1)
	print "2..."
	time.sleep(1)
	print "1..."
	time.sleep(1)
	
	if not os.path.isdir('positive/'+person_name):
		os.makedirs('positive/'+person_name)
    	
	faceCascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
	#eye_cascade = cv2.CascadeClassifier('./cascades/haarcascade_eye.xml')
	
	#camera = cv2.VideoCapture(0)
	
	stream = io.BytesIO()
	
	with picamera.PiCamera() as camera:
		camera.resolution = (1200, 800)
		camera.hflip = True
		camera.vflip = True
		camera.capture(stream, format='jpeg')
		
	buff = numpy.fromstring(stream.getvalue(), dtype=numpy.uint8)
	image = cv2.imdecode(buff, 1)
	
	count = 0
	#ret, frame = camera.read()
	gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

	#faces = face_cascade.detectMultiScale(gray, 1.3, 5)
	faces = faceCascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30), flags = cv2.CASCADE_SCALE_IMAGE)

	x, y, w, h = faces[0]
	#img = cv2.rectangle(image,(x,y),(x+w,y+h),(255,0,0),2)
	img = cv2.rectangle(image, (x,y), (x+w,y+h), (255,255,255), 1)
	f = cv2.resize(gray[y-30:y+h+30, x-30:x+w+30], (200, 200))
		
	cv2.imwrite(os.path.join("positive/" + person_name, person_name + "_normal.pgm"), f)
	#cv2.imwrite('positive/%s.pgm' % str(count), f)
	count += 1

# 	cv2.imshow(".camera", image)
	
	print "The person's face image is stored in positive folder"
	generate_smile()
	#if cv2.waitKey(1000 / 12) & 0xff == ord("q"):
	#break

def generate_smile():
	print "Now please smile to the camera (photo will be taken in three seconds)"
	print "3..."
	time.sleep(1)
	print "2..."
	time.sleep(1)
	print "1..."
	time.sleep(1)
	
	faceCascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
	#eye_cascade = cv2.CascadeClassifier('./cascades/haarcascade_eye.xml')
	
	#camera = cv2.VideoCapture(0)
	
	stream = io.BytesIO()
	
	with picamera.PiCamera() as camera:
		camera.resolution = (1200, 800)
		camera.hflip = True
		camera.vflip = True
		camera.capture(stream, format='jpeg')
		
	buff = numpy.fromstring(stream.getvalue(), dtype=numpy.uint8)
	image = cv2.imdecode(buff, 1)
	
	count = 0
	#ret, frame = camera.read()
	gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

	#faces = face_cascade.detectMultiScale(gray, 1.3, 5)
	
	
	faces = faceCascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30), flags = cv2.CASCADE_SCALE_IMAGE)
	x, y, w, h = faces[0]
	#img = cv2.rectangle(image,(x,y),(x+w,y+h),(255,0,0),2)
	img = cv2.rectangle(image, (x,y), (x+w,y+h), (255,255,255), 1)
# 	f = cv2.resize(gray[y-30:y+h+30, x-30:x+w+30], (200, 200))
	f = cv2.resize(gray[y:y+h, x:x+w], (200, 200))

	cv2.imwrite(os.path.join("positive/" + person_name, person_name + "_smile.pgm"), f)
	print "The person's smile face image is stored in positive folder"
	
	os.system("python train_face2.py")
	

if __name__ == "__main__":
# 	print sys.argv[1]
	generate_normal()
