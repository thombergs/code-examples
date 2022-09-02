const express = require("express");
const cron = require("node-cron");
const nodemailer = require("nodemailer");
app = express();

//send email after 1 minute
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

  // setting credentials
  let mailDetails = {
    from: "<your-email>@gmail.com",
    to: "<user-email>@gmail.com",
    subject: "Test Mail using Cron Job",
    text: "Node.js Cron Job Email Demo Test from Reflectoring Blog",
  };

  // sending email
  mailTransporter.sendMail(mailDetails, function (err, data) {
    if (err) {
      console.log("error occurred", err.message);
    } else {
      console.log("---------------------");
      console.log("email sent successfully");
    }
  });
}

app.listen(3000, () => {
  console.log("application listening.....");
});

/*
 * Run the script: `node example2.js`
 * To send E-mail
 * at scheduled time
 */
