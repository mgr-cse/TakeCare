<?php
	
	require '../database.php';


	//get prescription
   $id = $_GET['id'];
	$json = $_GET['json'];


   //store data to server
   $storePrescription = mysqli_query($conn, "INSERT INTO patient_{$id}_prescriptions (json_data) VALUES ('$json')");

   if(!$storePrescription)
   {
   	echo "failure!!!";
   	mysqli_close($conn);
   	die();
   }
   else echo "success!!!";


   mysqli_close($conn);

?>