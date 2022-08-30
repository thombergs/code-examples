// Writing to a log file

// Importing required packages
const process = require("process");
const fs = require("fs");
const cron = require("node-cron");
const express = require("express");

app = express();

// Setting a cron job for every 15 seconds
cron.schedule("*/15 * * * * *", function () {
  let heap = process.memoryUsage().heapUsed / 1000000;

  let data = `Memory usage by heapUsed ${heap}MB
  ${new Date().toUTCString()}
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
