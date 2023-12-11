const express = require("express");
const app = express();
const emailRoute = require("./routes/email");
const PORT = 5000;

app.use(express.urlencoded({ extended: true }));
app.use(express.json());
app.use("/api/email", emailRoute);

app.listen(PORT, () => {
  console.log(
    `Email Notification Service is running at http://localhost:${PORT}`
  );
});
