import cv2
import numpy as np
import picamera
import io
import os 
import sys
import time
import mysql.connector
import base64
import cStringIO
import re

def walk_files(rootdir):
	for subdir, dirs, files in os.walk(rootdir):
		for file in dirs:
			yield os.path.join(subdir, file)
			
def rotateImage(image, angle):
    row,col = image.shape
    center=tuple(np.array([row,col])/2)
    rot_mat = cv2.getRotationMatrix2D(center,angle,1.0)
    new_image = cv2.warpAffine(image, rot_mat, (col,row))
    return new_image

cnx = mysql.connector.connect(user='s14412104', password='14412104',
                              host='194.81.104.22',
                              database='db14412104')
                              
# declare new object MySQLCursor        
cursor = cnx.cursor()

cursor.execute("SELECT name FROM employee_faces")
row = cursor.fetchall()

faceCascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')

try:
	while True:
		for name in row:
			is_exist = 'no'
			#get only the name string from the tuple
			new_person = str(re.findall("'(.*?)'", str(name))).strip('[]').strip("''").replace(" ", "_").upper()	
		# 	print str(new_user).strip('[]').strip("''").replace(" ", "_").upper() == 'TEST_GUY'
			for filename in walk_files('positive'):
				exist_user = os.path.basename(filename)
				if new_person == exist_user :
					is_exist = 'yes'
			#if the name does not exist in the positive dir, that means he/she was just added to the database
			if is_exist != 'yes' :
				os.makedirs('positive/'+new_person)
				name_in_db = str(re.findall("'(.*?)'", str(name))).strip('[]').strip("''")
				cursor.execute("SELECT frontal_pic FROM employee_faces WERE name = "  + "'" + name_in_db + "'")
				frontal_pic = cursor.fetchone()
				# create dir in the positive folder with the name of the person (upper case)
				# decode image from base64 string, and then convert to grey, and then crop the face out
				#write face images in the dir
		
				#decode the base64 image string from the server 	
				decoded_front_pic = str(frontal_pic[0]).decode('base64')
				#write it to a jpeg file in 'server_pictures' folder for cv2 to read 
				fp = open("server_pictures/" + new_person + "_normal.jpeg", "wb")
				fp.write(decoded_front_pic)
				fp.close()
				#read the image and covert to grey (0 means black and white)
				frontal_img = cv2.imread("server_pictures/" + new_person + "_normal.jpeg", 0)
				#detect the face and crop it out
				normal_face = faceCascade.detectMultiScale(frontal_img, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30), flags = cv2.CASCADE_SCALE_IMAGE)
				x, y, w, h = normal_face[0]
				#resize image to 200x200 and write it as .pgm file in positive folder for face training 
				final_frontal_pic = cv2.resize(frontal_img[y-30:y+h+30, x-30:x+w+30], (200, 200))
				cv2.imwrite(os.path.join("positive/" + new_person + "/" + new_person + "_normal.pgm"), final_frontal_pic)
				print new_person + "'s frontal face image has been stored in positive folder"
		
				#do the same thing as above with smile picture
				cursor.execute("SELECT smile_pic FROM employee_faces WHERE name = "  + "'" + name_in_db + "'")
				smile_pic = cursor.fetchone()
				decoded_smile_pic = str(smile_pic[0]).decode('base64')
				sp = open("server_pictures/" + new_person + "_smile.jpeg", "wb")
				sp.write(decoded_smile_pic)
				sp.close()
				smile_img = cv2.imread("server_pictures/" + new_person + "_smile.jpeg", 0)
				smile_face = faceCascade.detectMultiScale(smile_img, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30), flags = cv2.CASCADE_SCALE_IMAGE)
				x, y, w, h = smile_face[0]
				final_smile_pic = cv2.resize(smile_img[y-30:y+h+30, x-30:x+w+30], (200, 200))
				cv2.imwrite(os.path.join("positive/" + new_person + "/" + new_person + "_smile.pgm"), final_smile_pic)
				print new_person + "'s smile face image has been stored in positive folder"
		
				#do the same thing as above with glasses picture, but check if the picture exists first, if it doesn't, that means the person doesn't wear glasses
				cursor.execute("SELECT glasses_pic FROM employee_faces WHERE name = "  + "'" + name_in_db + "'")
				glasses_pic = cursor.fetchone()
				#fetch image only if the image exists
				if glasses_pic[0] != 'no_glasses' :
					decoded_glasses_pic = str(glasses_pic[0]).decode('base64')
					gp = open("server_pictures/" + new_person + "_glasses.jpeg", "wb")
					gp.write(decoded_glasses_pic)
					gp.close()
					glasses_img = cv2.imread("server_pictures/" + new_person + "_glasses.jpeg", 0)
					glasses_face = faceCascade.detectMultiScale(glasses_img, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30), flags = cv2.CASCADE_SCALE_IMAGE)
					x, y, w, h = glasses_face[0]
					final_glasses_pic = cv2.resize(glasses_img[y-30:y+h+30, x-30:x+w+30], (200, 200))
					cv2.imwrite(os.path.join("positive/" + new_person + "/" + new_person + "_glasses.pgm"), final_glasses_pic)
					print new_person + "'s glasses face image has been stored in positive folder"
			
				#train the faces to the face recognition system by running the train face script
				os.system("python train_face2.py")
				
			time.sleep(30)
			
except KeyboardInterrupt:
	print "Program closed!" 
		
cnx.close() 