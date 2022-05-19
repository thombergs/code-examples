const express = require('express')
const app = express()
const port = process.env.port || 3002

let products = [
    { name: 'Television', brand: "LG" }
    , { name: 'Washing Machine',  brand: "Samsung" }
    , { name: 'Laptop',  brand: "Apple" }
  ];

app.use(express.json())  

app.get('/products', (req, res) => {
  res.json(products)
})

app.get('/products/deals', (req, res) => {
  res.json(products)
})

app.get('/products/:name', (req, res) => {
  const name = req.params.name
  let matchedProduct = products.find(prod=>prod.name == name)
  matchedProduct.price = (Math.random() * 1000).toFixed(2)
  res.json(matchedProduct)
})

app.post('/products', (req, res) => {

    console.log(req.headers)

    const name = req.body.name
    const brand = req.body.brand

    const product = {name: name, brand:brand}
    products.push(product)
    res.send({result:"OK"})
})

app.get('/products/:name/itemsInStock', (req, res) => {
  const name = req.params.name
  let matchedProduct = products.find(prod=>prod.name == name)
  matchedProduct.unitsInStock = (Math.random() * 1000).toFixed(0)
  res.json(matchedProduct)
})


app.listen(port, () => {
  console.log(`API server listening on port ${port}`)
})