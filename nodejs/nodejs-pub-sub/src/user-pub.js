const express = require("express");
const app = express();
const userRoute = require("./routes/user");
const PORT = 3000;

app.use(express.urlencoded({ extended: true }));
app.use(express.json());

app.use("/api/user", userRoute);

app.listen(PORT, () => {
  console.log(`User service is running at http://localhost:${PORT}`);
});
