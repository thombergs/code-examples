// server.js
const express = require('express')
const routes = require('./routes')

const { requestLogger } = require('./middleware')
const { errorLogger, errorResponder, invalidPathHandler } = require('./errormiddleware')


const app = express()
const PORT = process.env.PORT || 3000

app.use(requestLogger)
app.use(routes)

app.use(errorLogger)
app.use(errorResponder)
app.use(invalidPathHandler)

app.set('view engine', 'pug')
app.set('views', './views')


app.listen(PORT, () => {
console.log(`Server listening at http://localhost:${PORT}`)
})