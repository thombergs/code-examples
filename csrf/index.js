const express = require('express');
const csrf = require('csurf');
const cookieParser = require('cookie-parser');

var csrfProtection = csrf({ cookie: true });
var parseForm = express.urlencoded({ extended: false });

var app = express();
app.set('view engine','ejs')

app.use(cookieParser());

app.get('/transfer', csrfProtection, function (req, res) {
// pass the csrfToken to the view
res.render('transfer', { csrfToken: req.csrfToken() });
});

app.post('/process', parseForm,
	csrfProtection, function (req, res) {
    res.send('Transfer Successful!!');
});

app.listen(3000, (err) => {
    if (err) console.log(err);
        console.log('Server listening on 3000');
    }
);

