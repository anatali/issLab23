//https://it.javascript.info/websocket
var socket;  //set by connect() called by the enduser
var sockConnected = false;

const fileInput = document.getElementById("myfile");
console.log("fileInput="+fileInput.files[0]);

function setConnected(connected) {
	sockConnected = connected;
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) { $("#conversation").show(); }
    else { $("#conversation").hide(); }
    $("#output").html("");
    //console.log("setConnected " + connected);
}

function disconnect() {
    setConnected(false);
    console.log("Disconnected");
}

    function connect(){
        var host     = document.location.host;
        var pathname =  "/"; 	//document.location.pathname;
        var addr     = "ws://" + host  + pathname + "socket"  ;
   
        // Assicura che sia aperta un unica connessione
        if(socket !== undefined && socket.readyState !== WebSocket.CLOSED){
             console.log("Connessione WebSocket gia' stabilita");
        }
        //console.log(" connect addr" + addr ); //ws://localhost:8085/socket
        socket = new WebSocket(addr);

        socket.binaryType = "arraybuffer";

        socket.onopen = function (event) {
        	setConnected(true);
            addMessageToWindow("Connected");
        };

        socket.onmessage = function (event) {
             console.log("onmessage event=" + event );
             if (event.data instanceof ArrayBuffer) {
                addMessageToWindow('Got Image:');
                //addImageToWindow(event.data);
                setImageInWindow(event.data);
            } else {
                addMessageToWindow(`Got Message: ${event.data}`);
            }
        };
        socket.onclose = function (event) {
            console.log("onclose event=" + event.reason );
        }
    }//connect

    function sendMessage(message) {
        console.log("sendMessage " + message );
        if( socket == null || ! sockConnected ) alert("Please connect ...");
        else{
            socket.send( message );
            //addMessageToWindow("Sent Message: " + message);
        }
    }
    

    function addMessageToWindow(message) {
    	//console.log("addMessageToWindow " + message);
        $("#output").append("<tr><td>" + message + "</td></tr>");
     }

    function addImageToWindow(image) {
        //console.log("addImageToWindow " + image );
        let url = URL.createObjectURL(new Blob([image]));

        $("#output").append("<tr><td>" + `<img src="${url}"/>` + "</td></tr>");
        //imageWindow.innerHTML += `<img src="${url}"/>`
    }
    function setImageInWindow(image) {
        console.log("setImageInWindow " + image );
        console.log($("#output"))  //output è il body di una table
        let url = URL.createObjectURL(new Blob([image]));
        $("#output").set("<tr><td>" + `<img src="${url}"/>` + "</td></tr>");
        //imageWindow.innerHTML += `<img src="${url}"/>`
    }


$(function () {
    $("form").on('submit', function (e) { e.preventDefault(); });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#sendmsg" ).click(function() {  sendMessage($("#inputmsg").val()); });
    //$( "#sendImage" ).click(function() {  let f = $("#myfile");console.log( "f="+ f  ); console.log( "f="+ f.files[0]  ); sendMessage(f); });
    $( "#sendImage" ).click(function() {
       let f = fileInput.files[0]; console.log( "ffff="+ f.name ); sendMessage(f); });
       //https://extendscript.docsforadobe.dev/file-system-access/file-object.html
});

 
 