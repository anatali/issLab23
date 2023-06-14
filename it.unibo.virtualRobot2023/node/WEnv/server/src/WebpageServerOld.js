/*
==========================================================================
WebpageServer.js
sockets       = {}     //interaction with WebGLScene
wssockets     = {}     //interaction with clients
postResult    != null  //a POST-request pending
==========================================================================
*/



const app          = require('express')()
const express      = require('express')
const hhtpSrv      = require('http').Server(app)
const sceneSocket  = require('socket.io')(hhtpSrv)     //interaction with WebGLScene
const WebSocket    = require('ws');                    //interaction with external clients

const sockets       = {}    //interaction with WebGLScene
const wssockets     = {}    //interaction with clients
let socketIndex     = -1
let wssocketIndex   = -1
const serverPort    = 8090

//STATE variables used by cmd handling on wssockets (see initWs)
var alreadyConnected = false
let runningMovesIndex   = -1
const moveMap   = new Map();
var postResult  = null

var actorObserverClient ;


app.use(function(req, res, next) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
    res.setHeader('Access-Control-Allow-Credentials', true);
    next();
});
app.use(express.static('./../../WebGLScene'));
/*
-----------------------------------------------------------------------------
Defines how to handle GET from browser and from external controls
-----------------------------------------------------------------------------
*/
    app.get('/', (req, res) => {
	    console.log("WebpageServer | GET socketIndex="+socketIndex + " alreadyConnected =" + alreadyConnected )
        if( ! alreadyConnected ){
            alreadyConnected = true;
            res.sendFile('indexOk.html', { root: './../../WebGLScene' })
	     }else{ /*
                if( res != null ){
                    res.writeHead(401, { 'Content-Type': 'text/json' });
                    res.statusCode=401
                    const answerJson = JSON.stringify(answer)
                    res.write( answerJson  );
                    res.end();
                }*/
		    res.sendFile('indexOccupied.html', { root: './../../WebGLScene' })
	     }
    }); //app.get

/*
-----------------------------------------------------------------------------
Defines how to handle POST from browser and from external controls
-----------------------------------------------------------------------------
HTML provides no way to generate JSON from form data

*/	//USING POST : by AN Jan 2021
    app.post("/api/move", function(req, res,next)  {
	    var data = ""
	    req.on('data', function (chunk) { data += chunk; }); //accumulate data sent by POST
        req.on('end',  function () {	//elaborate data received
			//{ robotmove: move, time:duration } - robotmove: turnLeft | turnRight | ...
		console.log("POSTTT /api/move runningMovesIndex=" + runningMovesIndex + " data=" + data + " currentMove="  + moveMap.get(runningMovesIndex) );
		try {
			var jsonData    = JSON.parse(data)
     		var moveTodo    = jsonData.robotmove
     		var duration    = jsonData.time
     		var currentMove = moveMap.get(runningMovesIndex)
     		
     		if( moveTodo=="alarm"){ //do the halt move
     		    var answer = "";
     		    if( currentMove != null && ! currentMove.includes("collision") && ! currentMove.includes("interrupted") ){
 	                execMoveOnAllConnectedScenes(moveTodo, duration) //JULY2021 April 2022
 	                answer       = { 'endmove' : 'true' , 'move' : 'halt' }
                 }else{
                    answer       = { 'endmove' : 'true' , 'move' : 'haltnoop' }
                 }
  	                if( res != null ){
	                    res.writeHead(200, { 'Content-Type': 'text/json' });
	                    res.statusCode=200
	                    //var answer       = { 'endmove' : 'true' , 'move' : 'halt' }  //JSON obj
	                    const answerJson = JSON.stringify(answer)
	                    res.write( answerJson  );
	                    res.end();
	                }
                 return
            }
            
            //the move is not alarm
			//console.log(" runningMovesIndex=" + runningMovesIndex + " " + moveMap[runningMovesIndex] );
			if( moveMap.get(runningMovesIndex) != undefined &&
			    ! moveMap.get(runningMovesIndex).includes("interrupted") ){
			//the move DOES NOT 'interrupt' a move activated in asynch way
	            const answer  = { 'endmove' : "notallowed" , 'move' : moveTodo }
	            const answerJson = JSON.stringify(answer)
	            updateCallers( answerJson )
                if( res != null ){
                    res.writeHead(200, { 'Content-Type': 'text/json' });
                    res.statusCode=200
                    res.write( answerJson  );
                    res.end();
                }
			} else{
	            console.log('$$$ WebpageServer doMove | ' + moveTodo + " duration=" + duration  );
                postResult = res  //MEMO that a POST is running. See
	            execMoveOnAllConnectedScenes(moveTodo, duration)
			}
            }catch(error){
 			    console.log('POSTTT error ' + error  + " res=" + res );
                const answer  = { 'endmove' : "JSON error" , 'move' : "unknown" }
                console.log('POSTTT answer ' + JSON.stringify(answer)   )
                updateCallers( JSON.stringify(answer) )
             }
 	   });
	}); //app.post


//Updates the mirrors
function execMoveOnAllConnectedScenes(moveTodo, moveTime){
    if( moveTodo != "alarm") {
        runningMovesIndex++
        console.log('     $$$ WebpageServer | execMoveOnAllConnectedScenes '  + moveTodo + " moveTime=" + moveTime + " index=" + runningMovesIndex);
        moveMap.set(runningMovesIndex, moveTodo)
    }else{  //alarm
        var haltedMove = moveMap.get(runningMovesIndex)
        console.log('     $$$ WebpageServer | execMoveOnAllConnectedScenes ALARM haltedMove='  + haltedMove + " moveTime=" + moveTime);
        if(  haltedMove != undefined && ! haltedMove.includes("collision" )  )//halt pressed without any move running
        	moveMap.set(runningMovesIndex, haltedMove+"-interrupted")
    }
    //console.log("$$$ WebpageServerrrrrrrrrrrr | execMoveOnAllConnectedScenes" +  " moveTime="+moveTime)
    //See PlayerControls.js:34
	Object.keys(sockets).forEach( key => sockets[key].emit(moveTodo, moveTime, runningMovesIndex) );
}

//Updates the controls and the observers (Jan 2021)
function updateCallers(msgJson){

    //console.log("WebpageServer | updateCallerssss" + msgJson + " actorObserverClient=" + actorObserverClient);
    if( actorObserverClient != undefined ){
        msg = "msg(sceneEv,dispatch,scene,sceneObserver,'"+ msgJson +"',0)\n"
        actorObserverClient.write( msg );
    }

 	Object.keys(wssockets).forEach( key => {
        console.log("     $$$ WebpageServer | updateCallers key="  + key + " msgJson=" + msgJson);
        wssockets[key].send( msgJson )
	} )
}

/*
-------------------------------------------------------------------------------------
Interact with clients over ws (controls that send commands or observers) Jan 2021
-------------------------------------------------------------------------------------
*/


function initWs(){
const wsServer  = new WebSocket.Server( { port: 8091 }  );   // { server: app.listen(8091) }
console.log("       $$$ WebpageServer | initWs wsServer=" + wsServer)

wsServer.on('connection', (ws) => {
  wssocketIndex++
  console.log("     $$$ WebpageServer wssocket | CLIENT CONNECTED wssocketIndex=" + wssocketIndex)
  const key      = wssocketIndex
  wssockets[key] = ws

  ws.on('message', msg => {
	var moveTodo = JSON.parse(msg).robotmove
	var duration = JSON.parse(msg).time
    var curMove  = moveMap.get(runningMovesIndex)
    
    console.log("     $$$ WebpageServer wssocket=" + wssocketIndex   + " msg= "+ msg  + " curMove="+ curMove )
	
	if( moveTodo != "alarm" && curMove != undefined /* && curMove != "interrupted" && curMove != "collision"*/   ){
        console.log("     $$$ WebpageServer ws | SORRY: cmd " + msg + " NOT POSSIBLE, since I'm running:" + curMove)
        const info     = { 'endmove' : 'false', 'move': moveTodo+"_notallowed (asynch)" }
        updateCallers( JSON.stringify(info) )
	    return
	}else  //alarm move could also be sent via HTTP
 	 if(  moveTodo == "alarm" && curMove != undefined && ! curMove.includes("interrupted") && ! curMove.includes("collision")   ){
        //moveMap.set(runningMovesIndex,curMove+"-interrupted")
	    execMoveOnAllConnectedScenes(moveTodo, duration)
	    //alarm does not send answer
        //const info     = { 'endmove' : true, 'move': moveTodo }
        //updateCallers( JSON.stringify(info) )
	    return
	}else{
	    console.log('     $$$ AAA WebpageServer | ' + moveTodo + " duration=" + duration )
        execMoveOnAllConnectedScenes(moveTodo, duration)
	}
  });

  ws.onerror = (error) => {
	  console.log("     $$$ WebpageServer wssocket | error: ${error}")
	  delete wssockets[key];
	  wssocketIndex--
	  console.log( "    $$$ WebpageServer wssocket | disconnect wssocketIndex=" +  wssocketIndex )
  }

  ws.on('close', ()=>{
	  delete wssockets[key];
	  wssocketIndex--
	  console.log( "     $$$ WebpageServer wssocket | disconnect wssocketIndex=" +  wssocketIndex )
  })
}); //wsServer.on('connection' ...
}//initWs


/*
-------------------------------------------------------------------------------------
Interact with the MASTER (the mirrors do not send any info)
-------------------------------------------------------------------------------------
*/
function sceneSocketInfoHandler() {
	console.log("HHH --- WebpageServer sceneSocketInfoHandler |  socketIndex="+socketIndex)
    sceneSocket.on('connection', socket => {
        socketIndex++
        console.log("HHH --- WebpageServer sceneSocketInfoHandler  | connection socketIndex="+socketIndex)
        const key    = socketIndex
        sockets[key] = socket
        if( socketIndex == 0) console.log("HHH --- WebpageServer sceneSocketInfoHandler | MASTER-webpage ready")
		socket.on( 'sonarActivated', (obj) => {  //Obj is a JSON object
			console.log( "HHH --- WebpageServer sceneSocketInfoHandler | sonarActivated " );
			console.log(obj)
			updateCallers( JSON.stringify(obj) )
		})

        socket.on( 'collision',     (obj) => {
		    var move =   moveMap.get(runningMovesIndex)
		    console.log( "HHH --- WebpageServer sceneSocketInfoHandler  | collision DETECTED " + obj
		            + " runningMovesIndex=" + runningMovesIndex
		            + " move="+ move
		            + " target=" + obj  ); //+ " numOfSockets=" + Object.keys(sockets).length
		    //showMoveMap();
		    const info     = {  'collision' : move, 'target': obj}
		    //moveMap.delete( runningMovesIndex )
		    collidedMove = moveMap.get(runningMovesIndex)
 		    moveMap.set(runningMovesIndex,collidedMove+"-collision")    
		    //showMoveMap(); 
		    updateCallers( JSON.stringify(info) )
 		    //answerToPost( JSON.stringify(info) );  //aspetto endmove al termine del tempo
 		} )
 		
        socket.on('endmove', (moveIndex)  => {  //April2022
		    console.log( "HHH --- WebpageServer sceneSocketInfoHandler  | endmove  PRE index=" + moveIndex );
		    //showMoveMap()
      		
       		var curMove  = moveMap.get(runningMovesIndex);   //nome della mossa o interrupted / collision
 
      		if( moveIndex == -1 ){  //-1 means collision or halt
 		        console.log( "HHH --- WebpageServer sceneSocketInfoHandler -1 | curMove=" + curMove);
 		        //showMoveMap();
     		    return;
      		}
      		
      		var answer   = "";
  		    console.log( "HHH --- WebpageServer sceneSocketInfoHandler  | endmoveeeee  curMove=" + curMove);
  		    
  		    if( curMove == undefined ) {  //undefined viene da SocketIO.stopMoving per alarm
   		        answer = { 'endmove' : 'false' , 'move' : "undefined???" };
   		        return
  		    }
  		    else if(  curMove.includes("collision") || curMove.includes("interrupted")  ) { 
  		        answer = { 'endmove' : 'false' , 'move' : curMove };
  		        //answerToPost( JSON.stringify(answer) );
	    		}else{ 
	              //TERMINAZIONE NORMALE 
	              answer  = { 'endmove' : true , 'move' : curMove }
	              //const answerJson = JSON.stringify(answer)
	              //updateCallers( JSON.stringify(answer) )
	              //answerToPost( answerJson );
	            }           
            updateCallers( JSON.stringify(answer) )
            answerToPost( JSON.stringify(answer) );
            //ELIMINAZIONE DELLA MOSSA
            moveMap.delete(moveIndex)  
            //console.log( "WebpageServer sceneSocketInfoHandler  | endmove index=" + moveIndex + " moveMap.size=" + moveMap.size  );
            //showMoveMap()
        })

       socket.on( 'disconnect',     () => {
        		delete sockets[key];
          		socketIndex--;
			    alreadyConnected = ( socketIndex == 0 )
        		console.log("HHH --- WebpageServer sceneSocketInfoHandler  | disconnect socketIndex="+socketIndex)
        })
    })
}//sceneSocketInfoHandler

function setUpActorLocalConnection(actorport){
const Net = require('net');
// The port number and hostname of the server.
const host = 'localhost';
const port = actorport;

// Create a new TCP client.
actorObserverClient = new Net.Socket();
// Send a connection request to the server.

console.log("setUpActorLocalConnection TCP to:" +port + " " + actorObserverClient);

actorObserverClient.connect( { port: 8030, host: "localhost" }, function() {
     console.log('TCP connection establishedddddddddddddddddddddddd with the server.');
    // The client can now send data to the server by writing to its socket.
    //actorObserverClient.write("Hello, server.\n");
    //actorObserverClient.flush();
    updateCallers('{"scene","started"}')
});

// The client can also receive data from the server by reading from its socket.
actorObserverClient.on('data', function(chunk) {
    console.log(`Data received from the server: ${chunk.toString()}.`);
    // Request an end to the connection after the data has been received.
    client.end();
});

actorObserverClient.on('end', function() {
    console.log('Requested an end to the TCP connection');
});

}//setUpActorLocalConnection


function answerToPost(  answerJson ){
   if( postResult != null ){
        console.log('WebpageServer | answerToPost  answer= ' + answerJson  );
        postResult.writeHead(200, { 'Content-Type': 'text/json' });
        postResult.statusCode=200
        postResult.write( answerJson  );
        postResult.end();
        postResult = null;
   }
}


function showMoveMap(){
    moveMap.forEach( (key,v) => console.log("WARNING: movemap key=" + key + " v="+v) )
}

function startServer() {
    console.log("WebpageServer  | startServer" )
        //Connect with an observer actor
        //setUpActorLocalConnection(8030)
    sceneSocketInfoHandler()
    initWs()
    hhtpSrv.listen(serverPort)
}
//MAIN
startServer()



