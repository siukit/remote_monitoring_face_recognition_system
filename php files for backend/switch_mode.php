<?php

    $host = "localhost";
    $user = "14412104";
    $db = "rpi_schema";
    $pw = "pw";

// 	$host = "194.81.104.22";
//     $user = "s14412104";
//     $pw = "14412104";
//     $db = "db14412104";

    $con = mysqli_connect($host, $user, $pw, $db);
    
    $is_on = $_POST["is_on"];
    $mode = $_POST["mode"];
    
    $statement = mysqli_prepare($con, "UPDATE system_mode SET is_on = ? WHERE mode = ?");
    mysqli_stmt_bind_param($statement, "ss", $is_on, $mode);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
    
    mysqli_close($con);
?>

