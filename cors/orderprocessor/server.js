

const express = require('express');
const router = express.Router();
const path = require('path');
const bodyParser = require('body-parser');

const port = 8000;
const app = express();
app.use(bodyParser.json());

router.get('/',function(req,res){
  res.sendFile(path.join(__dirname+'/index.html'));
});

app.use('/', router);

const orders = [
{
  id: 1,
  item: "Television",
  units: 1,
  price: 2345.67
},
{
  id: 2,
  item: "Washing Machine",
  units: 3,
  price: 23.34
},
{
  id: 3,
  item: "Laptop",
  units: 1,
  price: 23455.34
}
];

app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "http://localhost:9000");
  res.header(
    "Access-Control-Allow-Headers",
    "Origin, X-Requested-With, Content-Type, Accept, Authorization"
  );
  res.header(
    "Access-Control-Allow-Methods",
    "GET, POST, PUT, DELETE"
  );
  res.header("Access-Control-Allow-Credentials",true);

  next();
});

app.get('/orders', (req, res) => {
  console.log('Returning orders');
  res.send(orders);
});

app.put('/orders', (req, res) => {
  console.log('updating orders');
  res.send(orders);
});

app.listen(port);
