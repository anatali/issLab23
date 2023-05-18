var socket;  //set by connect() called by the enduser
var sockConnected = false;
const imageWindow = document.getElementById("imageArea");
const fileInput   = document.getElementById("myfile");
console.log("fileInput="+fileInput.files[0]);

function setConnected(connected) {
	sockConnected = connected;
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    /*
    if (connected) { $("#conversation").show(); }
    else { $("#conversation").hide(); }*/
    $("#output").html("");
    console.log("setConnected " + connected);
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
            addMessageToWindow("Connected " + host + " " + pathname);
        };

        socket.onmessage = function (event) {
             //console.log("onmessage event=" + event.data );
             if (event.data instanceof ArrayBuffer) {
                //addImageToWindow(event.data);
                setImageInWindow(event.data);
            } else {
                addMessageToWindow(`${event.data}`);
            }
        };
        socket.onclose = function (event) {
            //console.log("onclose event=" + event.reason );
            //addMessageToWindow("onclose event=" + event.reason);
            alert("onclose event=" + event.reason)
            connect(); //AUTOMATIC RECONNECTION
        }
    }//connect

    function sendMessage(message) {
        //console.log("sendMessage " + message );
        if( socket == null || ! sockConnected ) alert("Please connect ...");
        else{
            socket.send(message);
            //addMessageToWindow("Sent Message: " + message);
        }
    }
    
    function addMessageToWindow(message) {
    	//console.log("addMessageToWindow " + message);
        $("#output").append("<tr><td>" + message + "</td></tr>");
     }
    function setImageInWindow(image) {
        //console.log("setImageInWindow " + image );
        let url = URL.createObjectURL(new Blob([image]));
        $("#imageArea").html(`<img src="${url}"/>`);
        //imageWindow.innerHTML = `<img src="${url}"/>`
    }
     //Se si vuole appendere una immagine nella outputArea
   function addImageToWindow(image) {
        let url = URL.createObjectURL(new Blob([image]));
        $("#output").append("<tr><td>" + `<img src="${url}"/>` + "</td></tr>");
        //imageWindow.innerHTML += `<img src="${url}"/>`
    }


$(function () {
    $("form").on('submit', function (e) { e.preventDefault(); });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#sendmsg" ).click(function() {  sendMessage($("#inputmsg").val()); });
    //$( "#sendImage" ).click(function() {  let f = $("#myfile"); console.log(  f.files ); sendImage(f); });
    $( "#sendImage" ).click(function() {  let f = fileInput.files[0]; sendMessage(f); });
});

/*
    $.ajax({
        url: "http://localhost:8090/",
         success: function( result ) {
            console.log("" + result)
            $("#output").append("<tr><td>" + result + "</td></tr>");
            //jQuery estrae il body della pagina ...
        }
    });
 */
     $.ajax({
         url: "http://localhost:8090/api/move",
         data:  "{\"robotmove\":\"moveForward\", \"time\":\"1000\"}",
         //data: { move : "{\"robotmove\":\"moveForward\", \"time\":\"1000\"}" },
           statusCode: {
             404: function() {
                $("#output").append("<tr><td>HTTP page not found</td></tr>");
             }
           },
         success: function( result ) {
             console.log("" + result)
             $("#output").append("<tr><td>" + result + "</td></tr>");
             //jQuery estrae il body della pagina ...
         },
         error: function( result ) {
             console.log("ERROR:" + result)
             $("#output").append("<tr><td> ERROR:" + result + "</td></tr>");
             //jQuery estrae il body della pagina ...
         }
     });