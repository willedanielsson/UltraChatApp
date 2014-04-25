<?php
include("connect.php");

if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

if (isset($_POST['message']))
	$message = mysql_escape_string($_POST['message']);
	$author = mysql_escape_string($_POST['username']);
	$color = mysql_escape_string($_POST['color']);
	

mysqli_query($con,"INSERT INTO chat (id, message, author, color) VALUES (id, '$message', '$author', '$color')");


print "
<form action='insert.php' method='post'>
  <input type='text' name='message' id='message' value='Hej hopp jag pwnar' />
  <input type='submit' />
</form>";

?>