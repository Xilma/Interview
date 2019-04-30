<?php
include "dbconnect.php";

if(isset($_REQUEST['firstname']) 
	&& isset($_REQUEST['email'])
	&& isset($_REQUEST['gender'])
	&& isset($_REQUEST['language'])
	&& isset($_REQUEST['dob'])
	&& isset($_REQUEST['password'])){
	
	$firstname = $_REQUEST['firstname'];
	$email = $_REQUEST['email'];
	$gender = $_REQUEST['gender'];
	$language = $_REQUEST['language'];
	$dob = $_REQUEST['dob'];
	$password = $_REQUEST['password'];
	
	$sql = "SELECT * FROM users WHERE email = '$email'";
	$result = mysqli_query($con,$sql);
	if($result){
		$count = mysqli_num_rows($result);

		if($count > 0){
			echo "user exists";
		}else{
			$sqlreg = "INSERT INTO users (firstname,email,gender,language,dob,password) 	
			VALUES ('$firstname','$email','$gender','$language','$dob','$password')";
			$resultreg = mysqli_query($con,$sqlreg);
			if($resultreg){
				echo "success";
			}else{
				echo "Error";
			}
		}
		
	}else{
		echo "Error";
	}	
}else{
	echo "params missing";
}
mysqli_close($con);
?>