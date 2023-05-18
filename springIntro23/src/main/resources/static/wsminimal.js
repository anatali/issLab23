/*
wsminimal.js
*/
const messageWindow   = document.getElementById("messageArea");
const messageInput    = document.getElementById("inputmessage");
const sendButton      = document.getElementById("sendMessage");
var socket;

sendButton.onclick = function (event) {
    sendMessage(messageInput.value);
    messageInput.value = "";
}

    function sendMessage(message) {
        //console.log("sendMessage"+message);
        var jsonMsg = JSON.stringify( {'name': message});
        socket.send(jsonMsg);
        //addMessageToWindow("Sent Message: " + jsonMsg);
    }

    function addMessageToWindow(message) {
        //var output = message.replaceAll("\n","<br/>")
        messageWindow.innerHTML += message  + "\n"
        //messageWindow.innerHTML += `<div>${message}</div>`
    }

    function connect(){
      var host       =  document.location.host; //"localhost:8085"; //
        var pathname =  "/"//document.location.pathname;
        var addr     = "ws://" +host  + pathname + "socket"  ;
        //alert("connect addr=" + addr   );

        // Assicura che sia aperta un unica connessione
        if(socket !== undefined && socket.readyState !== WebSocket.CLOSED){
             alert("WARNING: Connessione WebSocket già stabilita");
        }
        socket = new WebSocket(addr);

        //socket.binaryType = "arraybuffer";

        socket.onopen = function (event) {
            console.log("ws-onopen event.data:" + event.data);
            addMessageToWindow("Connected to " + addr);
        };

        socket.onmessage = function (event) {
            //console.log("ws-onmessage:" + `${event.data}`);
            addMessageToWindow(""+`${event.data}`);
            //alert(`Got Message: ${event.data}`)
        };
        //return socket;
    }//connect

connect()
