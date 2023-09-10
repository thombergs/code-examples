const express = require("express");
const rateLimitMiddleware = require("./middlewares/ratelimit");
const app = express();

app.use(rateLimitMiddleware);

// A simple API route
app.get("/api/blog", (req, res) => {
  res.send({
    success: true,
    message: "Welcome to our Blog API Rate Limiter Project 🎉",
  });
});

app.get("/api/blog/post", (req, res) => {
  res.send({
    success: true,
    author: "Mike Abdul",
    title: "Creating NodeJs Rate Limiter",
    post: "...",
  });
});

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
