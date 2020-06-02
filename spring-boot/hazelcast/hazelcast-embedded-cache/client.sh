curl -H "Content-Type: application/json" -X POST -d '{"number":"BO5489","name":"VW"}' http://localhost:8081/cars
curl -i -H "Accept: application/json" http://localhost:8081/cars/BO5489