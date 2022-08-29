// Writing to a log file

// Importing required packages
const cron = require("node-cron");
const express = require("express");
const fs = require("fs");

app = express();

// Setting a cron job
cron.schedule("*/15 * * * * *", function () {
  // Info to log
  let data = `${new Date().toUTCString()} 
                : Server is working\n`;

  // storing data to log
  fs.appendFile("logstatus.txt", data, function (err) {
    if (err) throw err;
    console.log("Status Logged!");
  });
});

app.listen(3000, () => {
  console.log("Application Listening.....");
});

/*
* Run the script: `node example3.js`
* To generate logstatus.txt file
* at scheduled time
*/
