<?php
	
	require 'database.php';


	//get data
   	$id = $_GET['id'];
   	$json = $_GET['json'];

   	//query
   	$result = mysqli_query($conn, "UPDATE users SET id_card='$json' WHERE id=$id");

   	//result
   	if($result)
   		echo "success";
   	else echo "failiure!!!";

   	mysqli_close($conn);
?>