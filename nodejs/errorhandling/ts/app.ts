import express, { Request, Response, NextFunction } from 'express'
import axios from 'axios'

const app = express()
const port:number = 3000


interface Product {

    name: string
    price: number
    brand: string
    category?: string
}

interface ProductCreationResponse {
    productID: string 
    result: string
} 


class AppError extends Error{
    statusCode: number;

    constructor(statusCode: number, message: string) {
      super(message);
  
      Object.setPrototypeOf(this, new.target.prototype);
      this.name = Error.name;
      this.statusCode = statusCode;
      Error.captureStackTrace(this);
    }
}

const requestLogger = (request: Request, response: Response, next: NextFunction) => {
    console.log(`${request.method} url:: ${request.url}`);
    next()
}

app.use(express.static('images'))  
app.use(express.static('htmls'))  
app.use(requestLogger)  

app.use('/products', express.json({ limit: 100 }))

// Error handling Middleware functions
const errorLogger = (error: Error, request: Request, response: Response, next: NextFunction) => {
    console.log( `error ${error.message}`) 
    next(error) // calling next middleware
}
  
const errorResponder = (error: AppError, request: Request, response: Response, next: NextFunction) => {
  response.header("Content-Type", 'application/json')
    
  const status = error.statusCode || 400
  response.status(status).send(error.message)
}

const invalidPathHandler = (request: Request, response: Response, next: NextFunction) => {
  response.status(400)
  response.send('invalid path')
}
  
  
// handle post request for path /products
app.post('/products', (request: Request, response: Response) => {
  const products = []

  const name = request.body.name                

  const brand = request.body.brand

  const category = request.body.category

  if(name == null){
    response.status(500).json({ message: "Mandatory field name is missing. " })
  }else{
    console.log(name + " " + brand)
  
    products.push({name: request.body.name, brand: request.body.brand, price: request.body.price})              
   
    const productCreationResponse = {productID: "12345", result: "success"}
    response.json(productCreationResponse)
  }
})

app.get('/products', async (request: Request, response: Response, next: NextFunction)=>{
  try{
    const apiResponse = await axios.get("http://localhost:3001/products")

    const jsonResponse = apiResponse.data
    console.log("response "+jsonResponse)
    
    response.send(jsonResponse)
  }catch(error){
    next(error)
  }

})

app.get('/product',  (request: Request, response: Response, next: NextFunction)=>{
 
    axios.get("http://localhost:3001/product")
    .then(jsonresponse=>response.send(jsonresponse))
    .catch(next)
})

app.get('/productswitherror', (request, response) => {
  let error:AppError = new AppError(400, `processing error in request at ${request.url}`)
  error.statusCode = 400
  throw error
})

app.get('/productswitherror', (request: Request, response: Response) => {
    let error: AppError = new AppError(400, `processing error in request at ${request.url}`)
    
    throw error
  })
  
  app.use(errorLogger)
  app.use(errorResponder)
  app.use(invalidPathHandler)

app.listen(port, () => {
    console.log(`Server listening at port ${port}.`)
})