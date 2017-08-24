import mysql.connector
import os
import sys
import time


    


try:
	while True:
		cnx = mysql.connector.connect(user='s14412104', password='14412104',
								  host='194.81.104.22',
								  database='db14412104')
		cursor = cnx.cursor()
		#check (every second)if the user has turned on siren mode by checking if the 'is_on' column on table system_mode has the value 'on
		cursor.execute("SELECT is_on FROM system_mode WHERE mode = 'siren'")
		row = cursor.fetchone()
		print row[0]
		#if siren mode is on, turn on the siren (for this demo, we will use led instead)
		if row[0] == 'on' :
			print "Siren mode is: ON"
		else :
			print "Siren mode is: OFF"
			
		cnx.close()
		time.sleep(1)
		
except KeyboardInterrupt:
	print "Program closed!" 

