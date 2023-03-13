const mongoose = require("mongoose");

const Student = mongoose.model("Student", {
  firstName: String,
  lastName: String,
  age: Number,
});

module.exports = { Student };
