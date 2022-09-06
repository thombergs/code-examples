// Deleting / Refreshing A log file.
const express = require("express");
const cron = require("node-cron");
const fs = require("fs");

app = express();

// remove the demo.csv file every twenty-first day of the month.
cron.schedule("0 0 25 * *", function () {
  console.log("---------------------");
  console.log("deleting logged status");
  fs.unlink("./demo.csv", err => {
    if (err) throw err;
    console.log("deleted successfully");
  });
});

app.listen(3000, () => {
  console.log("application listening.....");
});

/*
* Run the script: `node example4.js`
* To delete demo.csv file
* at scheduled time
*/
