const express = require("express");
const logger = require("./logger");
const app = express();

app.get("/", (req, res, next) => {
  logger.log("debug", "Hello, Winston!");
  logger.debug("The is the home '/' route.");
  res.status(200).send("Logging Hello World..");
});

app.get("/event", (req, res, next) => {
  try {
    throw new Error("Not User!");
  } catch (error) {
    logger.error("Events Error: Unauthenticated user");
    res.status(500).send("Error!");
  }
});

app.listen(3000, () => {
  logger.info("Server Listenning On Port 3000");
});
