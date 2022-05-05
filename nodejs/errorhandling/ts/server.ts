import express, { Request, Response, NextFunction } from 'express'

const app = express()

const port:number = 3001

interface Product {

    name: string
    price: number
    currency: string
    brand: string
    category?: string
}

const products: Product[] = [
    {name:"Television", price: 24.56, currency: "USG", brand: "samsung"},
    {name:"Washing Machine", price: 67.56, currency: "EUR", brand: "LG"}
]

app.get('/products', (request: Request, response: Response)=>{

    response.json(products)
})

app.listen(port, 
     () => console.log(`Server listening on port ${port}.`))
 