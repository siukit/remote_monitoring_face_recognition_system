<?php

    $host = "localhost";
    $user = "14412104";
    $db = "rpi_schema";
    $pw = "pw";
    
//     $host = "194.81.104.22";
//     $user = "s14412104";
//     $pw = "14412104";
//     $db = "db14412104";

    $con = mysqli_connect($host, $user, $pw, $db);
    
   	$statement = mysqli_query($con, "SELECT mode, is_on FROM system_mode");

	$result = array();
	
	while($row = mysqli_fetch_array($statement)){
		array_push($result,
		array('mode'=>$row[0],
		'is_on'=>$row[1]
		));
	}

 	echo json_encode(array("system_mode"=>$result));

    mysqli_close($con);
?>

