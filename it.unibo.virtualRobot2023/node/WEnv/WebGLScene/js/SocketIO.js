/*
SocketIO.js is the sceneSocket
*/
import eventBus       from './eventBus/EventBus.js'
import eventBusEvents from './eventBus/events.js'

import { parseConfiguration, 
   updateSceneConstants, getinitialsceneConstants } from './utils/SceneConfigUtils.js' //DEC 2019
import sceneConfiguration from '../sceneConfig.js'                                     //DEC 2019
import SceneManager from './SceneManager.js'                                           //no more DEC 2019

export default (onKeyUp, onKeyDown, myonKeyDown) => {
    const socket = io()
        //console.log("SocketIO onKeyUp=" + onKeyUp + " onKeyDown=" + onKeyDown)
    socket.on( 'moveForward',  (duration, moveindex) => {moveForward(duration,moveindex)})
    socket.on( 'moveBackward', (duration, moveindex) => moveBackward(duration,moveindex) )
    socket.on( 'turnRight',    (duration, moveindex) => turnRight(duration,moveindex) )
    socket.on( 'turnLeft',     (duration, moveindex) => { turnLeft(duration,moveindex) })
    socket.on( 'alarm',        (duration, moveindex) => stopMoving(moveindex)    )
    socket.on( 'remove',       name => remove( name )  )   //DEC 2019  See WebpageServer.js
    
	socket.on( 'xxx',        obj => console.log("SocketIO xxxxxxxxxxxxxxxxxxxxxxxx")   )

    socket.on( 'disconnect', () => console.log("server disconnected") )

    eventBus.subscribe( eventBusEvents.sonarActivated, sonarId =>
        socket.emit('sonarActivated', sonarId))
    eventBus.subscribe( eventBusEvents.collision, objectName => { 
		//console.log(`SocketIO collision: ${objectName}`);
		socket.emit('collision', objectName); 	//va al callback del main.js
		stopMoving(-1);
	})
   eventBus.subscribe( eventBusEvents.endmove, objectName => { //objectName is the index of the move
 		console.log(`SocketIO endmove: ${objectName}`);
 		socket.emit('endmove', objectName);  //vedi main.js 34
   })

    const keycodes = {
        W: 87,
        A: 65,
        S: 83,
        D: 68,
        R: 82,
        F: 70,
        Z: 90 //April2022: lo uso per stop rotation
    }
        
    let moveForwardTimeoutId
    let moveBackwardTimeoutId

    function moveForward(duration,moveindex) {
    console.log("SocketIO  moveForward " + duration + " moveindex="+moveindex)
        clearTimeout(moveForwardTimeoutId)
        onKeyDown( { keyCode: keycodes.W },duration,true ) //
        if(duration >= 0) moveForwardTimeoutId = setTimeout( () => {
        							onKeyUp( { keyCode: keycodes.W } );
        							//Non tiene conto di possibile collision o HALT?
          							eventBus.post(eventBusEvents.endmove, moveindex)
          						}, duration )
    }

    function moveBackward(duration,moveindex) {
        clearTimeout(moveBackwardTimeoutId)
        onKeyDown( { keyCode: keycodes.S },duration,true )
        if(duration >= 0) moveBackwardTimeoutId = setTimeout( () => {
        							onKeyUp( { keyCode: keycodes.S } )
         							eventBus.post(eventBusEvents.endmove,  moveindex)
        						}, duration )
    }

    function turnRight(duration,moveindex) {
	    console.log("SocketIO turnRight from SocketIO moveindex="+moveindex + " duration=" + duration )
        //induce il movimento simulando onKeyDown
        onKeyDown( { keyCode: keycodes.D }, duration, moveindex )	//remote=true onKeyDown is in PlayerControl
        //eventBus.post(eventBusEvents.endmove,  moveindex)
    }

    function turnLeft(duration,moveindex) {
    	console.log("SocketIO turnLeft  moveindex=" + moveindex + " duration=" + duration) //in browser
        onKeyDown( { keyCode: keycodes.A }, duration, moveindex )  //PlayerControls 34
        //eventBus.post(eventBusEvents.endmove,  moveindex)
    }

    function stopMoving(moveindex) {
       console.log("SocketIO stopMoving " + moveindex )
       onKeyUp( { keyCode: keycodes.W } )
       onKeyUp( { keyCode: keycodes.S } )
       onKeyDown( { keyCode: keycodes.Z } )   //per fermare rotating in PlayControl (38)
       eventBus.post(eventBusEvents.endmove, moveindex)
    }
    
//DEC 2019    
   function remove( objname ) {  //called by line 16
   		sceneConfiguration.staticObstacles.forEach( v  => {
   			//console.log(" ... "+ v.name)
   			if( v.name == objname ) {
		      console.log("SocketIo remove " + v.name  )
		      v.centerPosition.x = 0
		      v.centerPosition.y = 0
		 	  v.size.x = 0
			  v.size.y = 0
		      //console.log(  " SocketIo remove " + v.size.x )       
		      const sceneConstants = getinitialsceneConstants()
		      updateSceneConstants(sceneConstants, parseConfiguration(sceneConfiguration) )			
   			}
   		})
   	  
  }//remove

}