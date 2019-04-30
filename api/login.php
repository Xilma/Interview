<?php
include "dbconnect.php";

if(isset($_REQUEST['email']) 
	&& isset($_REQUEST['password'])){
	
	$email = $_REQUEST['email'];
	$password = $_REQUEST['password'];
	
	$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";
	$result = mysqli_query($con,$sql);
	if($result){
		$count = mysqli_num_rows($result);

		if($count > 0){
			echo "success";
		}else{
			echo "failed...register first";
		}
		
	}else{
		echo "Error";
	}	
}else{
	echo "params missing";
}
mysqli_close($con);
?>