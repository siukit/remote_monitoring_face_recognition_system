from picamera.array import PiRGBArray
from picamera import PiCamera
import time
import cv2
import sys
import imutils
import RPi.GPIO as GPIO
import os
import datetime
import json
import numpy as np


os.system("sudo pkill uv4l")

GPIO.setmode(GPIO.BCM)
PIR_PIN = 7
GPIO.setup(PIR_PIN, GPIO.IN)

#cascPath = sys.argv[1]

faceCascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
 
# initialize the camera and grab a reference to the raw camera capture
camera = PiCamera()

camera.hflip = True
camera.vflip = True

camera.resolution = (640, 480)
camera.framerate = 32
rawCapture = PiRGBArray(camera, size=(640, 480))
 
# allow the camera to warmup
time.sleep(0.1)

#num used for naming pictures of detected face
x = 0

model = cv2.face.createEigenFaceRecognizer()
model.load('training.xml')

with open('labels.json') as f:
	labels_dict = json.load(f)

try:
	while True:
		while GPIO.input(PIR_PIN):
			print "Motion Detected, started streaming"
			print GPIO.input(PIR_PIN)
			
			# capture frames from the camera
			for frame in camera.capture_continuous(rawCapture, format="bgr", use_video_port=True):
				# the numpy array represents the image
				image = frame.array
				timeout = time.time() + 10
				gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
				faces = faceCascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=7, minSize=(50, 50), flags = cv2.CASCADE_SCALE_IMAGE)
				print "Found " + str(len(faces)) + " face(s)"		
		
				for (x, y, w, h) in faces:
					x += 1
					cv2.rectangle(image, (x,y), (x+w,y+h), (255,255,255), 1)
					resizedImage = cv2.resize(gray[y-30:y+h+30, x-30:x+w+30], (200, 200))
# 					resizedImage = cv2.resize(gray[y-30:y+h+30, x-30:x+w+30], (200, 200))
# 					cv2.imwrite(os.path.join("face_images", str(time.strftime('%Y-%m-%d %H:%M:%S')) + ".pgm"), resizedImage)
					
# 					label = model.predict(resizedImage)
					params = model.predict(resizedImage)
					print "Label: %s, Confidence: %.2f" % (params[0], params[1])
					label = params[0]
					if label == 2:
						print "This person is an intruder!!"
# 						# neg += 1
						cv2.imwrite(os.path.join("unauthorized", str(time.strftime('%m-%d %H:%M')) + ".jpg"), image)
						
					else:
						#pos += 1
						for person, labels in labels_dict.iteritems():
#  							print person, 'is the key for ', labels
#  							print label
							if label == labels:
								print "This person was authorized with label ", label
								this_person = person
								cv2.imwrite(os.path.join("authorized", this_person+'('+str(time.strftime('%m-%d %H:%M'))+')' + ".jpg"), image)
# 						os.rename(filename, 'authorized/'+this_person+'('+str(time.strftime('%m-%d %H:%M'))+')')
						

				
	
				# show the frame
				cv2.imshow("Streaming from PiCamera", image)
				key = cv2.waitKey(1) & 0xFF
 
				# clear the stream in preparation for the next frame
				rawCapture.truncate(0)
 
				# if q key was pressed, break 
				if key == ord("q") or GPIO.input(PIR_PIN) == 0 or time.time() > timeout:
					cv2.destroyAllWindows() 
					os.system("sudo service uv4l_raspicam restart")
					print "Stopped streaming"
					break
					
# except KeyboardInterrupt or GPIO.input(PIR_PIN) == 0:
except KeyboardInterrupt:
	print "Program closed!"
	os.system("sudo service uv4l_raspicam restart")
