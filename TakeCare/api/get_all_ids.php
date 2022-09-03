<?php
	
	require '../database.php';


	//get data
   	$table = $_GET['table'];

   	//query
   	$result = mysqli_query($conn, "SELECT id FROM {$table}");
   	$allData = mysqli_fetch_all($result);
   	//reverse array to get latest data on top
   	$revData = array_reverse($allData);

   	if($allData){
   		//elements in array
   		echo count($allData)."\n";

   		//elements
   		for($i=0; $i<count($allData); $i++)
   			echo $revData[$i][0]."\n";
   }else
   {
   		echo "failure!!!";
   }


   	//result

   	mysqli_close($conn);



?>