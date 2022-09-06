// Scheduling a simple task with node cron.

const cron = require("node-cron");
const express = require("express");

const app = express();

cron.schedule("*/15 * * * * *", function () {
  console.log("---------------------");
  console.log("running a task every 10 second");
  //replace with any task
});

app.listen(3000);

/*
* Run the script: `node example1.js`
* To log on application
* at scheduled time
*/
