var express = require('express');
var router = express.Router();
var cassandra = require('cassandra-driver');

var client = new cassandra.Client({contactPoints: ['127.0.0.1']});
client.connect(function(err, result){
  console.log('index: cassandra connected');
});

var getAllSubscribers = 'SELECT * FROM test.arjun';

/* GET home page. */
router.get('/', function(req, res, next) {
  client.execute(getAllSubscribers, [],function(err, result){
    if(err){
      res.status(404).send({msg: err});
    }else{
      res.render('output', {
        arjun: result.rows
      });
    }
  });
});

module.exports = router;
