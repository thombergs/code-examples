// Deleting / Refreshing A log file.
const express = require("express");
const cron = require("node-cron");
const fs = require("fs");

app = express();

// Remove the error.log file every twenty-first day of the month.
cron.schedule("* * * * *", function () {
  console.log("---------------------");
  console.log("Deleting Log Status");
  fs.unlink("./logstatus.txt", err => {
    if (err) throw err;
    console.log("Error file successfully deleted");
  });
});

app.listen(3000, () => {
  console.log("Application Listening.....");
});

/*
* Run the script: `node example4.js`
* To delete logstatus.txt file
* at scheduled time
*/
