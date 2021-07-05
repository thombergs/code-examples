

const express = require('express');
const router = express.Router();
const path = require('path');
const bodyParser = require('body-parser');

const port = 9000;
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

app.get('/orders', (req, res) => {
  console.log('Returning orders');
  res.send(orders);
});

app.put('/orders', (req, res) => {
  console.log('updating orders');
  res.send(orders);
});


console.log("Server listening on port "+port);
app.listen(port);
