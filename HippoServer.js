var http = require('http');
var qs = require('querystring');

var gameState = {};
var playerCount = 0;

serverStart();
http.createServer(function(req, res) {
	var body = '';
    req.on('data', function(chunk) {
        body += chunk;
    });
    req.on('end', function () {
        var post = qs.parse(body);
       	console.log("Recieved post");
       	handlePost(post, res);
    });
}).listen(8080);

//FUNCTIONS

function serverStart() {
	gameState.blocks = [];
	gameState.maxSize = 50;
	for(var i=0; i<50; i++) {
		var b = {
			'x' : Math.random()*720,
			'y' : Math.random()*640,
			'vx' : Math.floor(Math.random()* 100 - 50),
			'vy' : Math.floor(Math.random()* 100 - 50),
			'size' : Math.random()* 50 + 10,
			'color' : [Math.floor(Math.random()*255),Math.floor(Math.random()*255),Math.floor(Math.random()*255)]
		};
		gameState.blocks.push(b);
	}	
	gameState.players = {};

	setInterval(function() {
		moveBlocks();
		var players = gameState.players;
		for(var i in players) {
			var player = players[i];
			if(player.dying) {
				player.size += 2;
				player.x -= 1;
				player.y -= 1;
			}
		}
	}, 33);
}

function moveBlocks() {
	for(var i in gameState.blocks) {
		var b = gameState.blocks[i];
		b.x = b.x + b.vx / 30.0;
		b.y  = b.y + b.vy / 30.0;
		
		if(b.x + b.size < 0 && b.vx < 0) {
			b.size = Math.random()* gameState.maxSize*2;
			b.vx = Math.floor(Math.random()* gameState.maxSize);
			b.x = - b.size - Math.floor(Math.random()*50);
		}
		else if(b.x > 720 && b.vx > 0) {
			b.size = Math.random()* gameState.maxSize *2;
			b.vx = -Math.floor(Math.random()* gameState.maxSize);
			b.x = 720 + Math.floor(Math.random()*50);	
		}
		if(b.y + b.size < 0 && b.vy < 0) {
			b.size = Math.random()* gameState.maxSize*2;
			b.vy = Math.floor(Math.random()* gameState.maxSize);
			b.y =  - b.size - Math.floor(Math.random()*50);
		}
		else if(b.y > 640 && b.vy > 0) {
			b.size = Math.random()* gameState.maxSize*2;
			b.vy = -Math.floor(Math.random()* gameState.maxSize);
			b.y =  640 + Math.floor(Math.random()*50);
		}
	}
}

function handlePost(playerData, res) {
	playerData = updatePlayer(playerData);
	gameState.playerId = playerData.playerId;
	res.write(JSON.stringify(gameState));
	res.end();
}

function createPlayer() {
	var player = {
		'playerId' : playerCount,
		'x' : Math.floor(Math.random()*720),
		'y' : Math.floor(Math.random()*640),
		'size' : 13.5,
		'color' : [Math.floor(Math.random()*255),Math.floor(Math.random()*255),Math.floor(Math.random()*255)],
		'ttl' : 100,
		'dying' : false
	}
	playerCount++;
	gameState.players[player.playerId] = player;
	console.log('created player');

	return player;
}

function check(player, blocks) {
	var x = player.x;
	var y = player.y;
	var size = player.size;

	for(var i in blocks) {
		var block = blocks[i];
		if((block.playerId != null && block.playerId != player.playerId && block.dying == false) || block.playerId == null) {
			if (block.size > 0 && x < block.x + block.size && x + size > block.x) {
		       	if (y < block.y + block.size && y + size > block.y) {
		           	if(player.size >= block.size) {
		               player.size += (block.size / player.size);
		               block.size = 0.0;
		           	}
		           	else if(player.ttl <= 0)
		            	player.dying = true;
		       	}
	   		}
		}
	}
}

function collisionDetection(player) {
	check(player, gameState.blocks);
	check(player, gameState.players);
}

function updatePlayer(playerData) {
	if(playerData.size > gameState.maxSize) {
		gameState.maxSize = playerData.size;
	}

	if(playerData.playerId == -1)
		return createPlayer();
	else {
		var player = gameState.players[playerData.playerId];
		if(player.dying == false) {
			var speed = player.size * 6;
			if(playerData.vx < 0)
				player.x -= speed / 30;
			else if(playerData.vx > 0)
				player.x += speed / 30;
			if(playerData.vy < 0)
				player.y -= speed / 30;
			else if(playerData.vy > 0)
				player.y += speed / 30;
			player.ttl--;
			collisionDetection(player);
		}
		return player;
	}
}
