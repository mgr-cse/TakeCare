<?php
	require '../database.php';


	//get data
   	$id = $_GET['id'];

   	//query
   $result = mysqli_query($conn, "CREATE TABLE `patient_{$id}_prescriptions` ( `id` INT NOT NULL AUTO_INCREMENT , `give_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , `json_data` JSON NOT NULL , `buy` VARCHAR(1) NOT NULL DEFAULT '0' , PRIMARY KEY (`id`)) ENGINE = InnoDB");
   $result2 = mysqli_query($conn, "CREATE TABLE `patient_{$id}_reports` ( `id` INT NOT NULL AUTO_INCREMENT , `patho_id` VARCHAR(10) NOT NULL , `link` VARCHAR(255) NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB");

   	//result
   	if($result && $result2)
   		echo "success";
   	else echo "failiure";

   	mysqli_close($conn);
?>