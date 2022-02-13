// errormiddleware.js
const errorLogger = (err, request, response, next) => {
    console.log( `error ${err.message}`) 
    next(err) // calling next middleware
  }
  
const errorResponder = (err, request, response, next) => {
  response.header("Content-Type", 'application/json')
    
  const status = err.status || 400
  response.status(status).send(err.message)
  }
const invalidPathHandler = (request, response, next) => {
  response.status(400)
  response.send('invalid path')
  }
  
module.exports = { errorLogger, errorResponder, invalidPathHandler }

