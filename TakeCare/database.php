<?php
	
	//connect to database
	$conn = mysqli_connect('localhost', 'carbocation', '8bitguy', 'TakeCareDB');

	//check connection
	if(!$conn){
		echo "connection error:".mysqli_connect_error();
		die();
    }
?>