<?php
	
	require '../database.php';


	//get data
	$key = $_GET['key'];
   	$field = $_GET['field'];
   	$value = $_GET['value'];
   	$table = $_GET['table'];

   	//query
   	$result = mysqli_query($conn, "UPDATE {$table} SET {$field}='$value' WHERE id=$key");

   	//result
   	if($result)
   		echo "success";
   	else echo "failiure!!!";

   	mysqli_close($conn);


?>