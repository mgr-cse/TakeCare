<?php
	
	require '../database.php';


	//get prescription
   $id = $_GET['id'];
   $pathoId = $_GET['pathoid'];
   $link = $_GET['link'];


   //store data to server
   $storeReport = mysqli_query($conn, "INSERT INTO patient_{$id}_reports (patho_id , link) VALUES ('$pathoId', '$link')");

   if(!$storeReport)
   {
   	    echo "failure!!!";
        mysqli_close($conn);
   	    die();
   }
   else echo "success!!!";

   mysqli_close($conn);

?>