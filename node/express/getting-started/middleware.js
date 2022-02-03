// middleware.js

const requestLogger = (req, res, next) => {
    console.log(req);
    next();
};
  
const requireJsonContent = (req, res, next) => {
    if (req.headers['content-type'] !== 'application/json') {
        res.status(400).send('Server requires application/json')
    } else {
      next()
    }
}

module.exports = { requestLogger, requireJsonContent}