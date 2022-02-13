const express = require('express');

const app = express();

const requestLogger = (request, response, next) => {
  console.log(request);
  next();
};

const requireJsonContent = (request, response, next) => {
  if (request.headers['content-type'] !== 'application/json') {
      response.status(400).send('Server requires application/json')
  } else {
    next()
  }
}

app.use(requestLogger);


app.use((err, request, response, next) => {
  response.status(500).send('Error!')
})

// Set the body size limit to 100 bytes
app.use(express.json({ limit: 100 }));

app.use(express.urlencoded({ extended: false }));

let products = [{"name":"television", "price":112.34, "brand":"samsung"},
{"name":"washing machine", "price": 345.34, "brand": "LG"},
{"name":"Macbook", "price": 3454.34, "brand": "Apple"}];


// handle get request for path /
app.get('/', (request, response) => {
  response.send('response for GET request');
});

// handle get request for path /products
app.get('/products', (request, response) => {

  response.json(products);
});

// handle get request for path /products
app.get('/products/:brand', (request, response) => {

  const brand = req.params.brand;

  console.log("brand ${brand} " + brand);
  
  const productsFiltered = products.filter(product=> product.brand == brand);                  

  response.json(productsFiltered);
});

// handle post request for path /products
app.post('/products', requireJsonContent, (request, response) => {
  const products = [];

  const name = req.body.name  ;                

  const brand = req.body.brand;

  console.log(name + " " + brand);
  
  products.push({name: req.body.name, brand: req.body.brand, price: req.body.price});               
 
  const productCreationResponse = {productID: "12345", result: "success"};
  response.json(productCreationResponse);
});

app.set('view engine', 'pug')
app.set('views', './views')
app.get('/home',  (request, response) => {
  response.render("home", { title: "Home", message: "My home page" , sysdate: new Date().toLocaleString()})
})


// handle get request with 3 middleware functions
app.get('/users', (request, response, next) => {
  console.log("Stage 1 processing ");
  next()
},
(request, response, next) => {
  console.log("Stage 2 processing ");
  next();
},
(request, response) => {
  response.send('response for GET request');
});


app.get('/products/error', (request, response) => {
  throw new Error("processing error!")
});
// start the server
const port = 3000
app.listen(3000, 
   () => console.log(`Server listening on port ${port}.`));

