<?php
	
	require 'database.php';


   	//query
       $result = mysqli_query($conn, "CREATE TABLE `users` (
        id INT NOT NULL AUTO_INCREMENT,
        username VARCHAR(255) NOT NULL,
        `password` VARCHAR(255) NOT NULL,
        `name` VARCHAR(255) NOT NULL,
        `email` VARCHAR(255) NOT NULL,
        `mobile` VARCHAR(10) NOT NULL,
        `dob` VARCHAR(10) NOT NULL,
        `user_type` VARCHAR(10) NULL DEFAULT NULL,
        `id_card` JSON NULL DEFAULT NULL,
        PRIMARY KEY (`id`),
        UNIQUE (`username`)) ENGINE = InnoDB");

   	//result
   	if($result)
   		echo "success";
   	else echo "failiure!!!";

   	mysqli_close($conn);

?>