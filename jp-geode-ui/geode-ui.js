var express = require('express');
var logger = require('winston');
var bodyParser = require('body-parser');

var config = {
    port: 3000,

    staticOptions: {
        dotfiles: 'ignore',
        maxAge: '0',
        redirect: false
    },

    indexRoutes: {
        '/': 'demos',
        '/ng': 'ng-graph',
        '/todo': 'todo'
    }
};

var app = express();

app.use(bodyParser.json()); // for parsing application/json
app.use(express.static('webapp/public', config.staticOptions));

app.set('views', process.cwd() + '/webapp/views');
app.set('view engine', 'ejs');

/**
 * Set up routes
 */
app.get('/', function (req, res) {
    res.render('index', { });
});

/**
 * Start app
 */

var server = app.listen(config.port);

logger.info('Hack Geode UI application started...');
logger.info('Visit: http://localhost:' + config.port);

/**
 * Configure socket.io
 */
var io = require('socket.io')(server);

var sockets = [];

io.on('connection', function (socket) {
    logger.debug('New client connected...');
    sockets.push(socket);

    socket.on('disconnect', function () {
        for (var i = 0; i < sockets.length; i++) {
            if (sockets[i] === socket) {
                break;
            }
        }
        sockets.splice(i, 1);
        logger.debug('Client disconnected...');
    });
});

app.post('/ajax/data-point', function (req, res) {
    io.emit('dataPoint', { t: req.body.t, value: req.body.value });
    res.json(req.body);
});

app.post('/ajax/ts-event', function (req, res) {
    io.emit('tsEvent', req.body);
    res.json({});
});
