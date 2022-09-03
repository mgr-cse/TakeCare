<?php
	
	require 'database.php';

	//get data
	$usertype = $_GET['usertype'];
   	$id = $_GET['id'];

   	//query
   	$result = mysqli_query($conn, "UPDATE users SET user_type='$usertype' WHERE id=$id");

   	//result
   	if($result)
   		echo "success";
   	else echo "failiure!!!";

   	mysqli_close($conn);

?>