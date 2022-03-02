// middleware.js

const requestLogger = (request, response, next) => {
    console.log(request);
    next();
};
  
const requireJsonContent = (request, response, next) => {
    if (request.headers['content-type'] !== 'application/json') {
        response.status(400).send('Server requires application/json')
    } else {
      next()
    }
}

module.exports = { requestLogger, requireJsonContent}