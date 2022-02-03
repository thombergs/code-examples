// routes.js
const express = require('express')
const { requireJsonContent } = require('./middleware')

const router = express.Router()

// Set the body size limit to 100 bytes
router.use(express.json({ limit: 100 }))

let  products = [{"name":"television", "price":112.34, "brand":"samsung"},
{"name":"washing machine", "price": 345.34, "brand": "LG"},
{"name":"Macbook", "price": 3454.34, "brand": "Apple"}];

// handle get request for path /products
router.get('/products', (req, res) => {
  res.json(products);
});

// handle get request for path /products
router.get('/products/:brand', (req, res) => {

  const brand = req.params.brand;

  console.log(`brand ${brand} `);
  
  const productsFiltered = products.filter(product=> product.brand == brand);                  

  res.json(productsFiltered);
});


// handle post request for path /products
router.post('/products', requireJsonContent,(req, res) => {

  const name = req.body.name  ;                

  const brand = req.body.brand;

  console.log(name + " " + brand);
  
  products.push({name: req.body.name, brand: req.body.brand, price: req.body.price});               
 
  const productCreationResponse = {productID: "12345", result: "success"};
  res.json(productCreationResponse);
});

// handle put request for path /products
router.put('/products', (req, res) => {

  const name = req.body.name  ;                

  const brand = req.body.brand;

  console.log(name + " " + brand);
  
  products.push({name: req.body.name, brand: req.body.brand, price: req.body.price});               
 
  const productCreationResponse = {productID: "12345", result: "success"};
  res.json(productCreationResponse);
})

router.get('/home',  (req, res) => {
  console.log("rendering...")
  res.render("home", { title: "Home", message: "My home page" , sysdate: new Date().toLocaleString()})
})

router.get('/productswitherror', (req, res) => {
  let err = new Error("processing error ")
  err.statusCode = 400
  throw err
});


module.exports = router