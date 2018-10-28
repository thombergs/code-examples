const express = require('express');
const router = express.Router();

router.route('/')
    .post(function (req, res) {
        const consumer = req.query['consumer'];
        const providerState = req.query['state'];
        // imagine we're setting the server into a certain state
        res.send(`changed to provider state "${providerState}" for consumer "${consumer}"`);
        res.status(200);
    });

module.exports = router;
