<?php
   $command = 'conda activate myenv && python script.py';
   exec($command, $output, $status);
   if ($status) {
      echo "Il comando non è stato eseguito correttamente.";
   } else {
      echo "Output del comando:<br />";
      foreach ($output as $line) {
         echo $line . "<br />";
      }
   }
?>
