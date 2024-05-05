const express = require("express");
const {
  initializeRedisClient,
  cacheMiddleware,
  invalidateCacheMiddleware,
} = require("./middlewares/redis");
const { productController } = require("./controllers/product");

const app = express();
app.use(express.json());

// connect to Redis
initializeRedisClient();

// register an endpoint
app.get(
  "/api/v1/products",
  cacheMiddleware({
    EX: 3600, // 1h
  }),
  productController.getproducts
);

app.post("/api/v1/product", invalidateCacheMiddleware, (req, res) => {
  // Implement your logic to update data in Application data store
  res.json({ message: "Product data updated successfully" });
});

// start the server
const port = 7000;
app.listen(port, () => {
  console.log(`Server is running on port: http://localhost:${port}`);
});
