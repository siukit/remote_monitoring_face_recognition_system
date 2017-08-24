import fnmatch
import os
import cv2
import numpy as np
import datetime
import time
import json

def walk_files(directory, match='*'):
	#loop through all the files in a directory
	for root, dirs, files in os.walk(directory):
		for filename in fnmatch.filter(files, match):
			yield os.path.join(root, filename)
			
			
def prepare_image(filename):
	#read image in grayscale
	return resize(cv2.imread(filename, cv2.IMREAD_GRAYSCALE))
	
def resize(image):
	#resize image for training
	return cv2.resize(image, (200, 200), interpolation=cv2.INTER_LANCZOS4)
	
model = cv2.face.createEigenFaceRecognizer()
model.load('training.xml')
	
faces = []

last_mod_time = None

with open('labels.json') as f:
	labels_dict = json.load(f)

try:
	while True:
		#stored last modified time
		if not last_mod_time:
			last_mod_time = datetime.datetime.today()
	
		#get the time of dir 'face_images' last modified time
		mod_time = datetime.datetime.fromtimestamp(os.path.getmtime('face_images'))
 
		pos = 0
		neg = 0
		#check if there was a 
		if last_mod_time < mod_time :	
			print "last_mod_time: {}".format(last_mod_time)
			print "mod_time: {}".format(mod_time)
			last_mod_time = mod_time
			for filename in walk_files('face_images', '*.pgm'):
				label = model.predict(prepare_image(filename))
				print label
				if label == 2:
					neg += 1
					os.rename(filename, 'unauthorized/'+os.path.basename(filename))
					print filename
				else:
					pos += 1
					for person, labels in labels_dict.iteritems():
						if label == labels:
							this_person = person
					os.rename(filename, 'authorized/'+this_person+'('+str(time.strftime('%m-%d %H:%M'))+')')
					print filename
					
	
		print "{} positive images were moved to authorized folder and {} negative images were moved to unauthroized folder".format(pos, neg)
		time.sleep(5)
		
except KeyboardInterrupt:
	print "Program closed!"
			

