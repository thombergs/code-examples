const express = require("express");
const cron = require("node-cron");
const nodemailer = require("nodemailer");
app = express();

//Send email after 1 minute
cron.schedule("1 * * * *", function () {
  mailService();
});

function mailService() {
  let mailTransporter = nodemailer.createTransport({
    service: "gmail",
    auth: {
      user: "<your-email>@gmail.com",
// use generated app password for gmail
      pass: "***********",
    },
  });

  // Setting credentials
  let mailDetails = {
    from: "<your-email>@gmail.com",
    to: "<user-email>@gmail.com",
    subject: "Test Mail using Cron Job",
    text: "Node.js Cron Job Email Demo Test from Reflectoring Blog",
  };

  // Sending Email
  mailTransporter.sendMail(mailDetails, function (err, data) {
    if (err) {
      console.log("Error Occured", err.message);
    } else {
      console.log("---------------------");
      console.log("Email sent successfully");
    }
  });
}

app.listen(3000, () => {
  console.log("Application Listening.....");
});
