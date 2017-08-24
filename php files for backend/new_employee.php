<?php

	header('Content-type : bitmap; charset=utf-8');
    
// 	$host = "194.81.104.22";
//     $user = "s14412104";
//     $pw = "14412104";
//     $db = "db14412104";

	$host = "192.168.3.2";
    $user = "14412104";
    $pw = "pw";
    $db = "rpi_schema";

	$con = mysqli_connect($host, $user, $pw, $db);    

    $name = $_POST["name"];
    $frontal_pic = $_POST["frontal_pic"];
    $smile_pic = $_POST["smile_pic"];
    $glasses_pic = $_POST["glasses_pic"];
    $reg_time = $_POST["reg_time"];



    $statement = mysqli_prepare($con, "INSERT INTO employee_faces (name, frontal_pic, smile_pic, glasses_pic, reg_time) VALUES (?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "sssss", $name, $frontal_pic, $smile_pic, $glasses_pic, $reg_time);
    mysqli_stmt_execute($statement);
    $response = array();
    $response["success"] = true;  

    print_r(json_encode($response));
    
	mysqli_close($con);
	

?>