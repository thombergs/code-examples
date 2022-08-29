// Scheduling Email Using Node-Cron
const express = require("express");
const cron = require("node-cron");
const nodemailer = require("nodemailer");
const fs = require("fs");

app = express();

cron.schedule("*/1 * * * *", function () {
  sendMail();
});

function sendMail() {
  let mailTransporter = nodemailer.createTransport({
    service: "gmail",
    auth: {
      user: "holaszyd1@gmail.com",
      pass: "qzzrxmfvhsqjaepy",
    },
  });

  // Setting credentials
  let mailDetails = {
    from: "holaszyd1@gmail.com",
    to: "holaszyd3@gmail.com",
    subject: "Test Mail using Cron Job",
    text: "Node.js Cron Job Email" + " Demo Test from Reflectoring Blog",
  };

  // Sending Email
  mailTransporter.sendMail(mailDetails, function (err, data) {
    if (err) {
      console.log("Error Occurs", err.message);
    } else {
      console.log("Email sent successfully");
    }
  });
}

app.listen(3000, () => {
  console.log("Application Listening.....");
});

/*
* Run the script: `node example2.js`
* To send E-mail
* at scheduled time
*/

/**
 * // Setting credentials
	let mailDetails = {
		from: "<your-email>@gmail.com",
		to: "<user-email>@gmail.com",
		subject: "Test mail using Cron job",
		text: "Node.js cron job email"
		+ " testing for GeeksforGeeks"
	};
 */