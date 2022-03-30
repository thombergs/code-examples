const express = require('express')
// const morgan = require('morgan')

const app = express()

const port = 3001

const products = [
    {name:"Television", price: 24.56, currency: "USG", brand: "samsung"},
    {name:"Washing Machine", price: 67.56, currency: "EUR", brand: "LG"}
]

app.get('/products', (request, response)=>{

    response.json(products)
})

app.listen(port, 
     () => console.log(`Server listening on port ${port}.`))
 