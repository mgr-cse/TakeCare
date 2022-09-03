<?php
	
	require 'database.php';


	//get registration data
	$name = $_GET['name'];
	$username = $_GET['username'];
   	$password = $_GET['password'];
   	$email = $_GET['email'];
   	$mobile = $_GET['mobile'];
   	$dob = $_GET['dob'];

   	//check if username available
   	$usernameChkQuery = mysqli_query($conn, "SELECT id FROM users where username='$username'");
   	$rowUsername = mysqli_fetch_array($usernameChkQuery);

   	if($rowUsername)
   	{
   		echo "username taken";
   		mysqli_close($conn);
   		die();
   	}

   	//store data to server
   	$storeUser = mysqli_query($conn, "INSERT INTO users(username, password, name, email, mobile, dob)
   		VALUES ('$username', '$password', '$name', '$email', '$mobile', '$dob')");

   	if(!$storeUser)
   	{
   		echo "unable to store user in database";
   		mysqli_close($conn);
   		die();
   	}

   	//return id
   	$idQuery = mysqli_query($conn, "SELECT id FROM users where username='$username'");
   	$rowID = mysqli_fetch_array($idQuery);
   	echo $rowID[0];

   	mysqli_close($conn);

?>