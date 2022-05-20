import React, { useState } from 'react'
import axios from 'axios'

export default function ProductList(){
    const [products, setProducts] =  useState([])

    const controller = new AbortController()
    const fetchProducts = ()=>{
       axios.get(`http://localhost:3001/products`, {signal: controller.signal})
      .then(response => {
        const products = response.data
        setProducts(products)
      })      
      controller.abort()
    }

    return (
        <>
          <p>Product List</p>
          <p><button onClick={fetchProducts}>Fetch Products</button></p>
          <ul>
          {
            products
              .map(product =>
                <li key={product.id}>{product.name}&nbsp;{product.brand}</li>
              )
          }
          </ul>
        </>
    )
}