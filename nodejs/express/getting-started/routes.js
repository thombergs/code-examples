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
router.get('/products', (request, response) => {
  console.log(products)
  response.json(products)
});

// handle get request for path /products
router.get('/products/:brand', (request, response) => {

  const brand = request.params.brand;

  console.log(`brand ${brand} `);
  
  const productsFiltered = products.filter(product=> product.brand == brand);                  

  response.json(productsFiltered);
});


// handle post request for path /products
router.post('/products', requireJsonContent,(request, response) => {

  const name = request.body.name  ;                

  const brand = request.body.brand;

  console.log(name + " " + brand);
  
  products.push({name: request.body.name, brand: request.body.brand, price: request.body.price});               
 
  const productCreationResponse = {productID: "12345", result: "success"};
  response.json(productCreationResponse);
});

// handle put request for path /products
router.put('/products', (request, response) => {

  const name = request.body.name  ;                

  const brand = request.body.brand;

  console.log(name + " " + brand);
  
  products.push({name: request.body.name, brand: request.body.brand, price: request.body.price});               
 
  const productCreationResponse = {productID: "12345", result: "success"};
  response.json(productCreationResponse);
})

router.get('/home',  (request, response) => {
  console.log("rendering...")
  response.render("home", { title: "Home", message: "My home page" , sysdate: new Date().toLocaleString()})
})

router.get('/productswitherror', (request, response) => {
  let err = new Error("processing error ")
  err.statusCode = 400
  throw err
});


module.exports = router