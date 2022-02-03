const express = require('express');

const app = express();

const requestLogger = (req, res, next) => {
  console.log(req);
  next();
};

const requireJsonContent = (req, res, next) => {
  if (req.headers['content-type'] !== 'application/json') {
      res.status(400).send('Server requires application/json')
  } else {
    next()
  }
}

app.use(requestLogger);

const myErrorHandler = (err, req, res, next) => {
                            if (req.foo) {
                              res.status(500).send('Fail!');
                            }else {
                              next(err);
                            }
                          };

app.use(myErrorHandler);
app.use((err, req, res, next) => {
  res.status(500).send('Error!')
})

// Set the body size limit to 100 bytes
app.use(express.json({ limit: 100 }));

app.use(express.urlencoded({ extended: false }));

let products = [{"name":"television", "price":112.34, "brand":"samsung"},
{"name":"washing machine", "price": 345.34, "brand": "LG"},
{"name":"Macbook", "price": 3454.34, "brand": "Apple"}];


// handle get request for path /
app.get('/', (req, res) => {
  res.send('response for GET request');
});

// handle get request for path /products
app.get('/products', (req, res) => {

  res.json(products);
});

// handle get request for path /products
app.get('/products/:brand', (req, res) => {

  const brand = req.params.brand;

  console.log("brand ${brand} " + brand);
  
  const productsFiltered = products.filter(product=> product.brand == brand);                  

  res.json(productsFiltered);
});

// handle post request for path /products
app.post('/products', requireJsonContent, (req, res) => {
  const products = [];

  const name = req.body.name  ;                

  const brand = req.body.brand;

  console.log(name + " " + brand);
  
  products.push({name: req.body.name, brand: req.body.brand, price: req.body.price});               
 
  const productCreationResponse = {productID: "12345", result: "success"};
  res.json(productCreationResponse);
});

app.set('view engine', 'pug')
app.set('views', './views')
app.get('/home',  (req, res) => {
  res.render("home", { title: "Home", message: "My home page" , sysdate: new Date().toLocaleString()})
})


// handle get request with 3 middleware functions
app.get('/users', (req, res, next) => {
  console.log("Stage 1 processing ");
  next()
},
(req, res, next) => {
  console.log("Stage 2 processing ");
  next();
},
(req, res) => {
  res.send('response for GET request');
});


app.get('/products/error', (req, res) => {
  throw new Error("processing error!")
});
// start the server
const port = 3000
app.listen(3000, 
   () => console.log(`Server listening on port ${port}.`));

