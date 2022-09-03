<?php
	
	require 'database.php';

	//verify username password
	$username = $_GET['username'];
   	$password = $_GET['password'];

   	//query
   	$result = mysqli_query($conn, "SELECT id FROM users where username='$username' and password='$password'");
   	$row = mysqli_fetch_array($result);

   	//result
   	if($row)
   		echo $row[0];
	   else echo "Username or password incorrent!";
	   
	   mysqli_close($conn);
?>