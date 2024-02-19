const express = require("express");
const router = express();
const userController = require("../controllers/userController");

router.get("/", userController.welcome);
router.post("/create", userController.createUser);

module.exports = router;
