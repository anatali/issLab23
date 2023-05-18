
    const messageWindow   = document.getElementById("messageArea");
    const messageInput    = document.getElementById("inputmessage");
    const sendButton      = document.getElementById("sendMessage");

     sendButton.onclick = function (event) {
        sendMessage(messageInput.value);
        messageInput.value = "";
     };

    function sendMessage(message) {
        //console.log("sendMessage " + message)
        var jsonMsg = JSON.stringify( {'input': message} );
        stompClient.send( "/demoInput/unibo", {}, jsonMsg );
        //addMessageToWindow("Sent Message: " + message ); //+ " stompClient=" + stompClient
    }

    function addMessageToWindow(message) {
        console.log("addMessageToWindow " + message)
        messageWindow.innerHTML += message  + "\n"
    }
    function showAnswer(message) {addMessageToWindow("Answer:" + message);}




function connect() {
        //var socket   = new SockJS('/unibo');  //NON USIAMO SockJs
        var host       = document.location.host;
        var pathname   = document.location.pathname;
        //var addr       = "ws://" + host  + "/unibo"  ;
        var addr     = "ws://" +host + pathname + "unibo"  ;
        console.log("connect addr="+addr);
        var socket     = new WebSocket(addr);

            socket.onopen = function (event) {
                addMessageToWindow("socket Connected " + event.data);
            };

            socket.onmessage = function (event) {
              //alert(`Got Message: ${event.data}`)
              addMessageToWindow(`Got Message: ${event.data}`);

            };

    stompClient = Stomp.over(socket);

    //console.log("stompClient " + stompClient);
    //addMessageToWindow("Stomp Connecting ... "  );

    stompClient.connect({}, function (frame) {
        //setConnected(true);
        addMessageToWindow("stompClient Connected " ); //+ frame
        stompClient.subscribe('/demoTopic/output', function (greeting) {
            showAnswer(JSON.parse(greeting.body).content);
        });
    });
}

connect();