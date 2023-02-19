.. role:: red 
.. role:: blue 
.. role:: brown 
.. role:: remark
.. role:: worktodo  

.. _it.unibo.virtualRobot2020: C:/Didattica2023/issLab2023/it.unibo.virtualRobot2020
.. _unibo.basicrobot23: C:/Didattica2023/issLab2023/unibo.basicrobot23




=============================
Workflow23
=============================


-----------------------------
Un robot virtuale
-----------------------------

Progetto: `it.unibo.virtualRobot2020`_

Esegue i comandi ricevuti via rete

- usando HTTP ClientNaiveUsingHttp org.apache.http.client
- usando websoket ClientNaiveUsingWs

+++++++++++++++++++++++++++++++++++++
Comandi via HTTP
+++++++++++++++++++++++++++++++++++++

concrete-robot interaction language (cril)
.. code::

    {"robotmove":"MOVE", "time":T}

    concrete-robot interaction language o cril
    MOVE ::= "turnLeft" | "turnRight" |
            "moveForward" | "moveBackward" | "alarm"
    T    ::= naturalNum

    {"robotmove": "moveForward" ,  "time": "350 "}

    curl -d "{\"robotmove\":\"turnLeft\", \"time\":\"300\"}" 
         -H "Content-Type: application/json"
         -X POST http://localhost:8090/api/move

    curl -d "{\"robotmove\":\"moveForward\", \"time\":\"1300\"}" 
         -H "Content-Type: application/json"
         -X POST http://localhost:8090/api/move

+++++++++++++++++++++++++++++++++++++
Comandi via WS
+++++++++++++++++++++++++++++++++++++

NaiveGui.html

-----------------------------
Un robot 
-----------------------------


Progetto: `unibo.basicrobot23`_






-----------------------------
Trafila
-----------------------------

#. Applicazioni Java: si parte da J2EE e EJB
#. Spring propone componenti meno pesanti, ma richiede sforzi di configurazione
#. SpringBoot: configuraziona automatica, dipendenze starter (di alto livello), CLI, Actuator.
#. Docker
#. Si va verso applicazioni distribuite basate sul paradigma dei micro-rservizi (a messaggi, dockerabili, stateless)
#. Le interazioni possono essere sincrone (basate su HTTP) o asincrone 
#. Nascono problematiche (H21) e vengono proposti pattern (H23)
#. I sistemi nascono per 'aggregazione' e occorre attenzione nel desgin delle API (H39)
#. Si impone l'idea di CleanArchitecture con al centro il dominio e la business logic
#. Noi partiamo dall'idea di applicazione come sistema che ha una descrizione di alto livello riguardo a 
   struttura (contesti, attori), interazione (messaggi dispatch/request/event) e comportamento (FSM).
#. Le applicazioni qak non hanno bisogno di Spring, ma Spring rientra in gioco per due aspetti:
   
   - dotare una componente / una applicazione di una Web interface
   - fornire Facade che facilitano la messa a punto 
#. Wenv è un sistema basato su Node accessibile via HTTO o via WS. Ha un suo linguaggio di comando 'naive' nato prima di qak.
#. Basicrobot è un (micro)servizio che permette l'uso di un robot virtuale (basato su WEnv) o fisico (ocme aggangio a IOT).
#. Un primo sistema costruito con docker-compose comprende: WEnv, Basicrobot, Boundaryrobot (come esempio di applicazione).
#. La Facade a questo primo sistema permette ad admin di inviare messaggi  a tutti i componenti del sistema per verificarne
   l'uso.