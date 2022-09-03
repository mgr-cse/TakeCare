<?php
	
	require '../database.php';


	//get data
	$id = $_GET['id'];
   	$table = $_GET['table'];

   	//query
   	$result = mysqli_query($conn, "DELETE FROM {$table} where id='$id'");

   	//result
   	if($result)
   		echo "success!!!";
   	else echo "failure!!!";

   	mysqli_close($conn);



?>