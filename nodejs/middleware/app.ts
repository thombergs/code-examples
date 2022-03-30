import express, { Request, Response, NextFunction } from 'express'
import morgan from 'morgan'

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

app.use(morgan('tiny'))

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
  
  
  
  app.get('product', (request: Request, response: Response)=>{
    response.sendFile("productsample.html")
  })
  
  // handle get request for path /
  app.get('/', (request: Request, response: Response) => {
      response.send('response for GET request');
  })
  
  
  const requireJsonContent = (request: Request, response: Response, next: NextFunction) => {
    if (request.headers['content-type'] !== 'application/json') {
        response.status(400).send('Server requires application/json')
    } else {
      next()
    }
  }


const addProducts = (request: Request, response: Response, next: NextFunction) => {
    let products: Product[] = []

    const name: string = request.body.name                
  
    const brand: string = request.body.brand
  
    const category: string = request.body.category
  
    console.log(name + " " + brand)
    
    products.push({name: request.body.name, brand: request.body.brand, price: request.body.price})              
   
    const productCreationResponse: ProductCreationResponse = {productID: "12345", result: "success"}
    response.json(productCreationResponse)

    response.status(200).json(products);
}
app.post('/products', addProducts)

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