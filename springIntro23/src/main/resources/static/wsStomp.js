var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) { $("#conversation").show(); }
    else { $("#conversation").hide(); }
    $("#output").html("");
}

function connect() {
    //var socket  = new SockJS('/stomp-websocket');
    var host     = document.location.host;
    //var pathname = document.location.pathname;
    var addr     = "ws://" + host + "/unibo"  ;
    console.log("connect addr="+addr);
    var socket = new WebSocket(addr);

/*
        socket.onopen = function (event) {
        	setConnected(true);
            showMsg("Connected");
        };

        socket.onmessage = function (event) {
             //console.log("onmessage event=" + event.data );
             showMsg(`Got Message: ${event.data}`);
        }
*/
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('stompClient connected: ' + frame);
        stompClient.subscribe('/demoTopic/output', function (info) {
            showMsg(JSON.parse(info.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMsg() {
    console.log("sendMsg");
    stompClient.send("/demoInput/unibo", {}, JSON.stringify({'input': $("#input").val()}));
}

function showMsg(message) {
    $("#output").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) { e.preventDefault(); });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMsg(); });
});

