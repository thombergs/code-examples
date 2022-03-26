const express = require('express')
const morgan = require('morgan')

const app = express();


const requestLogger = (request, response, next) => {
    console.log(`${request.method} url:: ${request.url}`);
    next()
}

app.use(express.static('images'))  
app.use(express.static('htmls'))  

app.use(requestLogger)  

app.use(morgan('tiny'))

app.use('/products', express.json({ limit: 100 }))

// Error handling Middleware functions
const errorLogger = (error, request, response, next) => {
  console.log( `error ${error.message}`) 
  next(error) // calling next middleware
}

const errorResponder = (error, request, response, next) => {
response.header("Content-Type", 'application/json')
  
const status = error.status || 400
response.status(status).send(error.message)
}
const invalidPathHandler = (request, response, next) => {
response.status(400)
response.send('invalid path')
}



app.get('product', (request, response)=>{
  response.sendFile("productsample.html")
})

// handle get request for path /
app.get('/', (request, response) => {
    response.send('response for GET request');
})


const requireJsonContent = (request, response, next) => {
  if (request.headers['content-type'] !== 'application/json') {
      response.status(400).send('Server requires application/json')
  } else {
    next()
  }
}

// handle post request for path /products
app.post('/products', requireJsonContent, (request, response) => {
  const products = []

  const name = request.body.name                

  const brand = request.body.brand

  const category = request.body.category

  console.log(name + " " + brand)
  
  products.push({name: request.body.name, brand: request.body.brand, price: request.body.price})              
 
  const productCreationResponse = {productID: "12345", result: "success"}
  response.json(productCreationResponse)
})

app.get('/productswitherror', (request, response) => {
  let error = new Error(`processing error in request at ${request.url}`)
  error.statusCode = 400
  throw error
})

app.use(errorLogger)
app.use(errorResponder)
app.use(invalidPathHandler)

const port = 3000
app.listen(3000, 
     () => console.log(`Server listening on port ${port}.`));
   