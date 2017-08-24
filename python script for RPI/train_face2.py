import fnmatch
import os
import cv2
import numpy as np
import random
import json

def uniqueLabel():
    seed = random.getrandbits(4)
    while True:
       yield seed
       seed += 1
       
def walk_files_match(directory, match='*'):
	#iterate through all files in a folder which match the parameter
	for root, dirs, files in os.walk(directory):
		for filename in fnmatch.filter(files, match):
			yield os.path.join(root, filename)

def walk_files(rootdir):
	for subdir, dirs, files in os.walk(rootdir):
		for file in dirs:
			yield os.path.join(subdir, file)
			
			
def prep_img(filename):
	#read an image in grayscale then resize it
	return resize(cv2.imread(filename, cv2.IMREAD_GRAYSCALE))
	
def resize(image):
	#resize an image
	return cv2.resize(image, (200, 200), interpolation=cv2.INTER_LANCZOS4)
	

print "Reading images for training..."
faces = []
labels = []
pos_count = 0
neg_count = 0

labels_dict = {}
unique_label = uniqueLabel()

x = 3

# Loop through all dirs in positive dir
for filename in walk_files('positive'):
	#Loop through all subdirs
	for subfn in walk_files_match(filename, '*.pgm'):
		faces.append(prep_img(subfn))
		#the label of the positive images will be that person's name
		pos_count += 1
		#generate a unique integer for each person, store them in dictionary
# 		this_label = next(unique_label)
		x = x+1
		this_label = x
		labels_dict[os.path.basename(filename) + str(x)] = this_label
		#store the dict in a json file
		with open('labels.json', 'w') as f:
			json.dump(labels_dict, f)
		labels.append(this_label)
		
# Loop through all dirs in negative dir
for filename in walk_files('negative'):
	#Loop through all subdirs
	for subfn in walk_files_match(filename, '*.pgm'):
		faces.append(prep_img(subfn))
		#2 refers to positive
		labels.append(2)
		neg_count += 1
		
		
# Read all negative images
# for filename in walk_files('negative', '*.pgm'):
# 	faces.append(prepare_image(filename))
# 	2 refers to negative
# 	labels.append(2)
# 	neg_count += 1
print 'Read', pos_count, 'positive images and', neg_count, 'negative images.'

# Train model
print 'Training model...'
model = cv2.face.createEigenFaceRecognizer()
# for label in labels:
# 	print label

model.train(np.asarray(faces), np.asarray(labels))
 
# Save model result
model.save('training.xml')
print 'Training data saved to', 'training.xml'

