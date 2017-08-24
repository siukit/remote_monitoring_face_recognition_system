<?php

    $host = "localhost";
    $user = "14412104";
    $db = "rpi_schema";
    $pw = "pw";

    $con = mysqli_connect($host, $user, $pw, $db);
    
   	$statement = mysqli_query($con, "SELECT entry_time, picture FROM unauthorized_log");

	$result = array();
	
	while($row = mysqli_fetch_array($statement)){
		array_push($result,
		array('entry_time'=>$row[0],
		'picture'=>$row[1]
		));
	}


 	echo json_encode(array("log"=>$result));

    mysqli_close($con);
?>
