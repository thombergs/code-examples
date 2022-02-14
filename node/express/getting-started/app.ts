import express, { Request, Response, NextFunction } from 'express';

const app = express();
const port = 3000;


interface Product {
    name: string;
    price: number;
    brand: string;
  };

const getProducts = (request: Request, response: Response, next: NextFunction) => {
    let products: Product[] = [
      {"name":"television", "price":112.34, "brand":"samsung"},
      {"name":"washing machine", "price": 345.34, "brand": "LG"},
      {"name":"Macbook", "price": 3454.34, "brand": "Apple"}
    ]

    response.status(200).json(products);
}
app.get('/products', getProducts);

app.listen(port, () => {
    console.log(`Server listening at port ${port}.`);
  });