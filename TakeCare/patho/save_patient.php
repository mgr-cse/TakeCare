<?php
	
	require '../database.php';


	//get prescription
   $id = $_GET['id'];
   $patId = $_GET['patid'];


   //store data to server
   $storePatient = mysqli_query($conn, "INSERT INTO patho_{$id}_queue (patient_id) VALUES ('$patId')");

   if(!$storePatient)
   {
   	echo "failure!!!";
   	mysqli_close($conn);
   	die();
   }
   else echo "success!!!";


   mysqli_close($conn);

?>