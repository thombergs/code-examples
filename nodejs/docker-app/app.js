const express = require("express");
const dotenv = require("dotenv");
dotenv.config();
const UserRouter = require("./routes/users");
const app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: false }));

app.get("/", (req, res) => {
  res.send({ data: { message: "Welcome to Docker Crash Course!!!" } });
});

app.use("/users", UserRouter);

app.listen(3333, () => {
  console.log("Server is running on port 3333");
});
