const express = require('express');
const router = express.Router();

router.route('/:hero_id')
    .get(function (req, res) {
        const heroId = parseInt(req.params['hero_id']);
        res.status(200);
        res.json({
            id: heroId,
            superpower: 'flying',
            name: 'Superman',
            universe: 'DC'
        });
    });

router.route('/')
    .post(function (req, res) {
        res.status(201);
        res.json({
            id: 42,
            superpower: 'flying',
            name: 'Superman',
            universe: 'DC'
        });
    });

module.exports = router;
