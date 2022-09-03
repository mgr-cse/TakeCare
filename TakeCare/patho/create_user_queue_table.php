<?php
	require '../database.php';

	//get data
   	$id = $_GET['id'];

   	//query
   $result = mysqli_query($conn, "CREATE TABLE `patho_{$id}_queue` ( `id` INT NOT NULL AUTO_INCREMENT , `patient_id` VARCHAR(11) NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB");

   	//result
   	if($result)
   		echo "success";
   	else echo "failiure";

   	mysqli_close($conn);
?>