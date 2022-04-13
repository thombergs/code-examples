const express = require('express')
const axios = require("axios")
// const morgan = require('morgan')

const app = express()


const requestLogger = (request, response, next) => {
    console.log(`${request.method} url:: ${request.url}`);
    next()
}

app.use(express.static('images'))  
app.use(express.static('htmls'))  

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


// handle post request for path /products
app.post('/products', (request, response) => {
  const products = []

  const name = request.body.name                

  const brand = request.body.brand

  const category = request.body.category

  if(name == null){
    res.status(400).json({ message: "Mandatory field name is missing. " })
  }else{
    console.log(name + " " + brand)
  
    products.push({name: request.body.name, brand: request.body.brand, price: request.body.price})              
   
    const productCreationResponse = {productID: "12345", result: "success"}
    response.json(productCreationResponse)
  }
})

app.get('/products', async (request, response, next)=>{
  try{
    const apiResponse = await axios.get("http://localhost:3001/products")

    const jsonResponse = apiResponse.data
    console.log("response "+jsonResponse)
    
    response.send(jsonResponse)
  }catch(error){
    next(error)
  }

})

app.get('/product',  (request, response, next)=>{
 
    axios.get("http://localhost:3001/product")
    .then(response=>response.json)
    .then(jsonresponse=>response.send(jsonresponse))
    .catch(next)
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
 