%==============================================
% CONTEXT HANDLING UTILTY
%==============================================
getCtxNames(CTXNAMES) :-
	findall( NAME, context( NAME, _, _, _ ), CTXNAMES).
getCtxPortNames(PORTNAMES) :-
	findall( PORT, context( _, _, _, PORT ), PORTNAMES).
getTheContexts(CTXS) :-
	findall( context( CTX, HOST, PROTOCOL, PORT ), context( CTX, HOST, PROTOCOL, PORT ), CTXS).
getTheActors(ACTORS) :-
	findall( qactor( A, CTX, CLASS ), qactor( A, CTX, CLASS ), ACTORS).


getOtherContexts(OTHERCTXS, MYSELF) :-
	findall( 
		context( CTX, HOST, PROTOCOL, PORT ), 
		(context( CTX, HOST, PROTOCOL, PORT ), CTX \== MYSELF), 	 
		OTHERCTXS
	).

getLocalContextNames(CTXS):-
	findall(
		CTX,
		context( CTX, localhost, PROTOCOL, PORT ) ,
		CTXS
	).
getTheActors(ACTORS,CTX) :-
	findall( qactor( A, CTX, CLASS ), qactor( A, CTX, CLASS ),   ACTORS).
getActorNames(ACTORS,CTX) :-
    findall( NAME, qactor( NAME, CTX, CLASS ),   ACTORS).

getInfoOnActors(CTX,ACTORS) :-
	findall(  actor(A,CTX,HOST,PORT),
	         (qactor(A,CTX,_),getCtxHost( CTX, HOST ), getCtxPort( CTX, PORT )),
	            ACTORS).
getExternalActors(CTX,ACTORS) :-
	findall(  actor(A,CTX,HOST,PORT),
	         (qactor(A,CTX,"external"),getCtxHost( CTX, HOST ), getCtxPort( CTX, PORT )),
	            ACTORS).
infoOnActor(CTX, actor(A,CTX,HOST,PORT)):-
    qactor(A,CTX,_),getCtxHost( CTX, HOST ), getCtxPort( CTX, PORT ).

getNonlocalActorNames(CTX,OTHERS):-
    getOtherContextNames(OTHERCTXS,CTX),
    stdout <- println( getOtherContextNames(OTHERCTXS,CTX) ),
    findOtherActors(OTHERCTXS,[],OTHERS).
getExternalActorNames(CTX,EXTERNALS):-
    getOtherContextNames(OTHERCTXS,CTX),
    stdout <- println( getOtherContextNames(OTHERCTXS,CTX) ),
    findExternalActors(OTHERCTXS,[],OTHERS).

findOtherActors([],L,L).
findOtherActors([CTX|RCTX],L,RES):-
    getInfoOnActors(CTX,ACTORS),
    stdout <- println( getInfoOnActors(CTX,ACTORS) ),
    append( ACTORS,L,RL ),
    stdout <- println( findOtherActors(L) ),
    findOtherActors(RCTX, RL, RES ).

findExternalActors([],L,L).
findExternalActors([CTX|RCTX],L,RES):-
    getExternalActors(CTX,ACTORS),
    stdout <- println( findExternalActors(CTX,ACTORS) ),
    append( ACTORS,L,RL ),
    stdout <- println( findExternalActors(L) ),
    findExternalActors(RCTX, RL, RES ).

getCtxHost( NAME, HOST )  :- context( NAME, HOST, PROTOCOL, PORT ).
getCtxPort( NAME,  PORT ) :- context( NAME, HOST, PROTOCOL, PORT ).
getCtxProtocol( NAME,  PROTOCOL ) :- context( NAME, HOST, PROTOCOL, PORT ).

getMsgId( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , MSGID  ).
getMsgType( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , MSGTYPE ).
getMsgSenderId( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , SENDER ).
getMsgReceiverId( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , RECEIVER ).
getMsgContent( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , CONTENT ).
getMsgNum( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , SEQNUM ).

checkMsg( MSG, MSGLIST, PLANLIST, RES ) :- 
	%%stdout <- println( checkMsg( MSG, MSGLIST, PLANLIST, RES ) ),
	checkTheMsg(MSG, MSGLIST, PLANLIST, RES).	
checkTheMsg( MSG, [], _, dummyPlan ).
checkTheMsg( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ), [ MSGID | _ ], [ PLAN | _ ], PLAN ):-!.
checkTheMsg( MSG, [_|R], [_|P], RES ):- 
	%%stdout <- println( checkMsg( MSG, R, P, RES ) ),
	checkTheMsg(MSG, R, P, RES).

msgContentToPlan( MSG, CONTENTLIST,PLANLIST,RES ):-
	%stdout <- println( msgContentToPlan( MSG, CONTENTLIST,PLANLIST,RES) ),
	msgContentToAPlan( MSG, CONTENTLIST,PLANLIST,RES ).
msgContentToAPlan( MSG, [], _, dummyPlan ).
msgContentToAPlan( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ), [ CONTENT | _ ], [ PLAN | _ ], PLAN ):-!.
msgContentToAPlan( event(  CONTENT  ), [ CONTENT | _ ], [ PLAN | _ ], PLAN ):-!.
msgContentToAPlan( MSG, [_|R], [_|P], RES ):- 
	%stdout <- println( msgContentToAPlan( MSG, R, P, RES ) ),
	msgContentToPlan(MSG, R, P, RES).	

removeCtx( CtxName, HOST, PORT ) :-
	%% stdout <- println( removeCtx(  CtxName ) ),
	retract( context( CtxName, HOST, _ , PORT ) ),!,
	removeAllActors( CtxName ).
	 
removeAllActors( CtxName ) :-
	retract( qactor( NAME, CtxName, _ ) ),
	removeAllActors( CtxName ).
removeAllActors( CtxName ).  

showSystemConfiguration :-
	stdout <- println("&&&&&&&&&&&&&&&&&&SysRules&&&&&&&&&&&&&&&&&&&&"),
  	getTheContexts(CTXS),
	stdout <- println('CONTEXTS IN THE SYSTEM:'),
	showElements(CTXS),
	stdout <- println('ACTORS   IN THE SYSTEM:'),
	getTheActors(ACTORS),
	showElements(ACTORS),
	stdout <- println("&&&&&&&&&&&&&&&&&&SysRules&&&&&&&&&&&&&&&&&&&&").
 
showElements(ElementListString):- 
	text_term( ElementListString, ElementList ),
 	showListOfElements(ElementList).
showListOfElements([]).
showListOfElements([C|R]):-
	stdout <- println( C ),
	showElements(R).

unify(A,B)    :-  A = B.

assign( I,V ) :-  retract( value(I,_) ),!, assert( value( I,V )).
assign( I,V ) :-  assert( value( I,V )).
getVal( I, V ):-  value(I,V), !.
getVal( I, fail ).
inc(I,K,N):- value( I,V ), N is V + K, assign( I,N ).
dec(I,K,N):- value( I,V ), N is V - K, assign( I,N ).

addRule( Rule ):-
	%%output( addRule( Rule ) ),
	assert( Rule ).
removeRule( Rule ):-
	retract( Rule ),
	%%output( removedFact(Rule) ),
	!.
removeRule( A  ):- 
	%%output( remove(A) ),
	retract( A :- B ),!.
removeRule( _  ).

replaceRule( Rule, NewRule ):-
	removeRule( Rule ),addRule( NewRule ).

%==============================================
% MEMENTO
%==============================================
%%% :- stdout <- println( hello ).
%%% --------------------------------------------------
% context( NAME, HOST, PROTOCOL, PORT )
% PROTOCOL : "TCP" | "UDP" | "SERIAL" | "PROCESS" | ...
%%% --------------------------------------------------

%%% --------------------------------------------------
% msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
% MSGTYPE : dispatch request answer
%%% --------------------------------------------------
