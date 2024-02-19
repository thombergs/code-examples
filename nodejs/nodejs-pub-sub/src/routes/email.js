const express = require("express");
const router = express();
const emailController = require("../controllers/emailController");

router.get("/", emailController.welcome);
router.post("/pull", emailController.pullEmail);
router.post("/push", emailController.pushEmail);

module.exports = router;