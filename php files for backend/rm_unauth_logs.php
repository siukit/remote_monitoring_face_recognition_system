<?php

    $host = "localhost";
    $user = "14412104";
    $db = "rpi_schema";
    $pw = "pw";

    $con = mysqli_connect($host, $user, $pw, $db);
    
   	$statement = mysqli_query($con, "TRUNCATE unauthorized_log");

 	echo 'All rows from unauthorized_log table were successfully removed';

    mysqli_close($con);
?>