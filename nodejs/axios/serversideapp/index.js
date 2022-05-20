const axios = require('axios')

const express = require('express')
const cors = require('cors')

const app = express()
const port = process.env.port || 3001

let products = [
      { name: 'Television', brand: "LG" }
    , { name: 'Washing Machine',  brand: "Samsung" }
    , { name: 'Laptop',  brand: "Apple" }
  ];

app.use(cors())
app.use(express.json())  

axios.interceptors.request.use(
   (request) => {
    request.time = { startTime: new Date() }
    return request
  },
  (err) => {
    return Promise.reject(err)
  }
)

axios.interceptors.response.use(
   (response) => {
    response.config.time.endTime = new Date()
    response.duration =
    response.config.time.endTime - response.config.time.startTime
    return response
  },
  (err) => {
    return Promise.reject(err);
  }
)

app.get('/products', (request, response) => {
  
  axios({
    method: 'get',
    url: 'http://localhost:3002/products'
  }).then(apiResponse=>{
     const products = apiResponse.data
     console.log(`duration ${apiResponse.duration}` )
     response.json(products)
  })
  // cancel the request
   controller.abort()
})

app.get('/products/deals', (request, response) => {
  const new_instance = axios.create({
    baseURL: 'http://localhost:3002/products',
    timeout: 1000,
    headers: {
      'Accept': 'application/json',
      'Authorization': 'XXXXXX'
    }
  })
  new_instance({
    method: 'get',
    url: '/deals'
  }).then(apiResponse=>{
     const products = apiResponse.data
     response.json(products)
  })
  
})

app.post('/products/new', async (request, response) => {

  const name = request.body.name
  const brand = request.body.brand

  const newProduct = {name: name, brand:brand}

  axios({
    method: 'post',
    url: 'http://localhost:3002/products',
    data: newProduct,
    headers: {'Authorization': 'XXXXXX'}
  }).then(apiResponse=>{
     const products = apiResponse.data
     response.json(products)
  }).catch(error => {
    if (error.response) {
      console.log("response error")
    }else if (error.request) {
      console.log("request error")
    }else {
      // Something happened in setting up the request that triggered an Error
      console.log('Error', error.message);
    }
    response.send(error.toJSON())
  })
})

app.get('/products/:productName', (request, response) => {
  const productName = request.params.productName  

  /*axios.get(`http://localhost:3002/products/${productName}`)
  .then(apiResponse => {
     const product = apiResponse.data
     response.json(product)
  })*/   

  axios.get(`http://localhost:3002/products/`, {
    params: {
      productName: productName
    }
  })
  .then(apiResponse => {
     const product = apiResponse.data
     response.json(product)
  })   
})

app.get('/products/async/:productName', async (request, response) => {
  const productName = request.params.productName  

  const apiResponse = await axios.get(`http://localhost:3002/products/`, {
    params: {
      productName: productName
    }
  })

  const product = apiResponse.data
  response.json(product)
})


app.post('/products', async (request, response) => {

    const name = request.body.name
    const brand = request.body.brand

    const newProduct = {name: name, brand:brand}

    const apiResponse = await axios.post(`http://localhost:3002/products/`, newProduct)

    const product = apiResponse.data
    response.json({result:"OK"})
})

app.get('/products/:productName/inventory', (request, response) => {

  const productName = request.params.productName

  const productApiResponse = axios.get(`http://localhost:3002/products/${productName}`)
  const inventoryApiResponse = axios.get(`http://localhost:3002/products/${productName}/itemsInStock`)

  Promise.all([productApiResponse, inventoryApiResponse])
  .then(results=>{
      const productData = results[0].data
      const inventoryData = results[1].data
      let aggregateData = productData
      aggregateData.unitsInStock = inventoryData.unitsInStock
      response.send(aggregateData)
    
  })
})



app.listen(port, () => {
  console.log(`server side app listening on port ${port}`)
})