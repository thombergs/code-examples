var express = require('express');
var router = express.Router();

router.route('/:hero_id')
    .get(function (req, res) {
        var heroId = parseInt(req.params['hero_id']);
        res.json({
            id: heroId,
            superpower: 'flying',
            name: 'Superman',
            universe: 'DC'
        });
        res.status(200);
    });

router.route('/')
    .post(function (req, res) {
        res.json({
            id: 42,
            superpower: 'flying',
            name: 'Superman',
            universe: 'DC'
        });
        res.status(201);
    });

module.exports = router;
