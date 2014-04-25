<?php
include_once("connect.php");

$sth = mysql_query("SELECT * FROM chat");
$rows = array();
while($r = mysql_fetch_assoc($sth)) {
    $rows[] = $r;
}
print json_encode($rows);


?>