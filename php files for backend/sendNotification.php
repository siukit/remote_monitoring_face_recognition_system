<?php
// API access key from Google API's Console
define( 'API_ACCESS_KEY', 'AAAAtnECtcM:APA91bEaNVZB4VRblXamnLWFesflBmmxU0IoTPv_OGC70y7ZPjMklgUcdWJSAIY2emv3OOWUB3nAnNkoED6l8b2N-I3wuMpjba06RuAIjPR8OWYXwaDQPMuklPgsktlZcVzW8l73D8OY' );
// $registrationIds = 'dCBpULT-XA0:APA91bGdXjg0HOuVigRo-mU1aEfbx-lmPG1gyhPIrY6RWlJCPE8Mg89_nnIMHJo1m1L3s3Ya2RE213cCGlkGu1agvnB3HWAKMOP6AtelMZrHVZZh-cND-yPjL9lOF8tIXsFPbRyTv0fD';
$registrationIds = 'ceMZ_9ynJbc:APA91bFeghU_UdmNcxnA-iGZvi7_XZprwKulz_hI6fw9sU9VBUPl5kKeacQ05jBLvjDcTQJRKvahATV-L4RvsZLt2bSpUPYupAGRZ2lvpr8IRLecrRmZSJFXsnusnQ07B7HTj4ZVM-fT';

// prep the bundle
$msg = array
(
	'body' 	=> 'Attention!! Intruder detected!!',
	'title'		=> 'Intruder Alert',
	'vibrate'	=> 1,
	'sound'		=> 1,
	'click_action' => '.ViewIntrudersActivity'
);
$fields = array
(
	'to' 	=> $registrationIds,
	'notification'			=> $msg
);
 
$headers = array
(
	'Authorization: key=' . API_ACCESS_KEY,
	'Content-Type: application/json'
);
 
$ch = curl_init();
curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
curl_setopt( $ch,CURLOPT_POST, true );
curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
$result = curl_exec($ch );
curl_close( $ch );
echo $result;
?>

