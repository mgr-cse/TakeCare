<?php
	
	require '../database.php';


	//get data
	$key = $_GET['key'];
   	$field = $_GET['field'];
   	$table = $_GET['table'];

   	//query
   	$result = mysqli_query($conn, "SELECT {$field} FROM {$table} where id='$key'");
   	$row = mysqli_fetch_array($result);

   	//result
   	if($row)
   		echo $row[0];
   	else echo "failure!!!";

   	mysqli_close($conn);



?>