import mysql.connector
import os
import sys
# from PIL import Image
import base64
import cStringIO
# import PIL.Image
import time
import datetime
import fnmatch


def walk_files(directory, match='*'):
	#loop through all the files in a directory
	for root, dirs, files in os.walk(directory):
		for filename in fnmatch.filter(files, match):
			yield os.path.join(root, filename)
			
# cnx = mysql.connector.connect(user='14412104', password='pw',
#                               host='192.168.3.2',
#                               database='rpi_schema')
                              

                              
add_auth_log = ("INSERT INTO authorized_log "
               "(name, entry_time, picture) "
               "VALUES (%s, %s, %s)")
            
add_unauth_log = ("INSERT INTO unauthorized_log "
               "(entry_time, picture) "
               "VALUES (%s, %s)")
                              
last_mod_time = None


try:
	while True:
		cnx = mys baql.connector.connect(user='s14412104', password='14412104',
                              host='194.81.104.22',
                              database='db14412104')                              
		# declare new object MySQLCursor        
		cursor = cnx.cursor()
		#check (every second)if the user has turned on anti-burglar mode by checking if the 'is_on' column on table system_mode has the value 'on
		cursor.execute("SELECT is_on FROM system_mode WHERE mode = 'burglar'")
		row = cursor.fetchone()
		
		#stored last modified time
		if not last_mod_time:
			last_mod_time = datetime.datetime.today()
				
		#if anti-burglar is not on, then run the face recognition mode
		if row[0] != 'on' :
			print 'Anti-burglar mode is off'
			#get the time of dir 'authorized' last modified time
			auth_mod_time = datetime.datetime.fromtimestamp(os.path.getmtime('authorized'))  
			if last_mod_time < auth_mod_time :
				for filename in walk_files('authorized', '*.jpg'):
					# person_name = (os.path.splitext(filename)[0]).split('(')[0]
					#get the last part of the path(this file name)
					fn = os.path.basename(filename)
					#get the name of the person by extracting the part of filename before "("
					name = fn.split('(')[0]
					#get the entry_time by extracting the part between brackets
					entry_time = fn[fn.find("(")+1:fn.find(")")]
					#get the image bytes from the file
					blob_value = open(filename, 'rb').read()
					encoded_image = base64.encodestring(blob_value)
			
					#put the above variables to a tuple
					auth_log = (name, entry_time, encoded_image)
					#insert query
					cursor.execute(add_auth_log, auth_log)
					os.rename(filename, "auth_backup/"+fn)
				print "this is working"
				cnx.commit()             
				last_mod_time = auth_mod_time

				
			#get the time of dir 'unauthorized' last modified time
			unauth_mod_time = datetime.datetime.fromtimestamp(os.path.getmtime('unauthorized'))  
			if last_mod_time < unauth_mod_time :
				for filename in walk_files('unauthorized', '*.jpg'):
					# person_name = (os.path.splitext(filename)[0]).split('(')[0]
					#the name of the file is time of entry
					fn = os.path.basename(filename)
					timestamp = os.path.splitext(fn)[0]
					#get the image bytes from the file
					blob_value = open(filename, 'rb').read()
					encoded_image = base64.encodestring(blob_value)
					#put the above variables to a tuple
					unauth_log = (timestamp, encoded_image)
					#insert query
					cursor.execute(add_unauth_log, unauth_log)
					print timestamp
					os.rename(filename, "unauth_backup/"+fn)
				
				#run the php script to send notification to user
	# 			os.system("php /path/to/your/file.php %s"%(value))
				os.system("php /var/www/html/sendNotification.php")
				cnx.commit()             
				last_mod_time = unauth_mod_time
		#if anti-burglar mode is on, all face images being captured will be send to the intruder's list on the app, regardless of who the person is
		else:
			print 'Anti-burglar mode is on'
			#get the time of dir 'authorized' last modified time
			auth_mod_time = datetime.datetime.fromtimestamp(os.path.getmtime('authorized'))  
			if last_mod_time < auth_mod_time :
				for filename in walk_files('authorized', '*.jpg'):
					# person_name = (os.path.splitext(filename)[0]).split('(')[0]
					#get the last part of the path(this file name)
					fn = os.path.basename(filename)
					#get the name of the person by etxtracting the part of filename before "("
					name = fn.split('(')[0]
					#get the entry_time by extracting the part between brackets
					entry_time = fn[fn.find("(")+1:fn.find(")")]
					#get the image bytes from the file
					blob_value = open(filename, 'rb').read()
					encoded_image = base64.encodestring(blob_value)
			
					#put the above variables to a tuple
					auth_log = (entry_time, encoded_image)
					#insert query
					cursor.execute(add_unauth_log, auth_log)
					os.rename(filename, "unauth_backup/"+fn)
				os.system("php /var/www/html/sendNotification.php")
				cnx.commit()             
				last_mod_time = auth_mod_time

				
			#get the time of dir 'unauthorized' last modified time
			unauth_mod_time = datetime.datetime.fromtimestamp(os.path.getmtime('unauthorized'))  
			if last_mod_time < unauth_mod_time :
				for filename in walk_files('unauthorized', '*.jpg'):
					# person_name = (os.path.splitext(filename)[0]).split('(')[0]
					#the name of the file is time of entry
					fn = os.path.basename(filename)
					timestamp = os.path.splitext(fn)[0]
					#get the image bytes from the file
					blob_value = open(filename, 'rb').read()
					encoded_image = base64.encodestring(blob_value)
					#put the above variables to a tuple
					unauth_log = (timestamp, encoded_image)
					#insert query
					cursor.execute(add_unauth_log, unauth_log)
					print timestamp
					os.rename(filename, "unauth_backup/"+fn)
				
				#run the php script to send notification to user
	# 			os.system("php /path/to/your/file.php %s"%(value))
				os.system("php /var/www/html/sendNotification.php")
				cnx.commit()             
				last_mod_time = unauth_mod_time
				
			cnx.close()
			time.sleep(1)
			
except KeyboardInterrupt:
	print "Program closed!"                  
    
             
