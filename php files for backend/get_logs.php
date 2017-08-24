<?php

    $host = "localhost";
    $user = "14412104";
    $db = "rpi_schema";
    $pw = "pw";

    $con = mysqli_connect($host, $user, $pw, $db);
    
   $statement = mysqli_query($con, "SELECT name, entry_time, picture FROM authorized_log");

	$result = array();
	
	while($row = mysqli_fetch_array($statement)){
		array_push($result,
		array('name'=>$row[0],
		'entry_time'=>$row[1],
		'picture'=>$row[2]
		));
	}


 	echo json_encode(array("log"=>$result));

    mysqli_close($con);
?>
