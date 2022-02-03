// errormiddleware.js
const errorLogger = (err, req, res, next) => {
    console.log( `error ${err.message}`) 
    next(err) // calling next middleware
  }
  
  const errorResponder = (err, req, res, next) => {
    res.header("Content-Type", 'application/json')
    
    res.status(err.statusCode).send(err.message)
  }
  const invalidPathHandler = (req, res, next) => {
    res.status(400)
    res.send('invalid path')
  }
  
module.exports = { errorLogger, errorResponder, invalidPathHandler }

