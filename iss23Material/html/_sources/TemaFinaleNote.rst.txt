.. role:: red 
.. role:: blue 
.. role:: brown 
.. role:: remark
.. role:: worktodo

.. _templateToFill.html: ../../../../../it.unibo.issLabStart/userDocs/templateToFill.html
.. _domande.html: ../../../../../it.unibo.issLabStart/userDocs/domande.html

=========================================
TemaFinaleNote
=========================================

 

-----------------------------
Non functional requirements
-----------------------------

#. The ideal work team is composed of 3 persons. Teams of 1 or 2 persons (:blue:`NOT 4 or more`) are also allowed.
#. The team must present a workplan as the result of the requirement/problem analysis,
   including some significan TestPlan.
#. The team must present the sequence of SPRINT performed, with appropriate motivations.
#. Each SPRINT must be associated with its own 'chronicle' (see `templateToFill.html`_) that presents, in **concise way**,
   the key-points related to each phases of development.
   Hopefully, the team could also deploy the system using Docker.
#. Each team must publish and maintain a :blue:`GIT-repository` (referred in the `templateToFill.html`_)
   with the code and the related documents.
#. The team must present (in synthetic, schematic way) the :blue:`specific activity` of each team-component.

-----------------------------
Linee-guida
-----------------------------

 
- Il numero e le finalità degli SPRINT sono definiti dal Team di sviluppo dopo opportune interazioni con il
  committente.
- Il committente (e/o il product-owner) è disponibile ONLINE in linea di massima ogni :blue:`Giovedi dalle 16 alle 18`
  fino al :blue:`21 Luglio 2022`, ma è sempre contattabile on-demand via email.

Lo svolgimento del lavoro dovrebbe avvenire in diverse fasi:

#. :blue:`Fase di analisi`, che termina con la definizione di una architettura logica del sistema, di modelli eseguibili e
   alcuni, significativi piani di testing.
   E' raccomandato che i risultati di questa fase vengano presentati al committente (con opportuno
   appuntamento) prima della consegna finale del prodotto.
#. :blue:`Fase di progetto e realizzazione`, che termina con il deployment del prodotto.
#. :blue:`Fase di discussione` del lavoro svolto, che dovrà svolgersi IN PRESENZA in LAb2 (preferibilmente).

E' opportuno che ogni partecipante sia pronto a discutere anche sugli elaborati che ha prodotto durante il corso.


-----------------------------
FASI del colloquio
-----------------------------

#. Presentazione (collettiva di gruppo) di una :blue:`demo 'live'` del sistema
   di durata 10-15(max) minuti.
   L'ordine di presentazione dei gruppi verrà opportunamente stabilito dal docente.
   La demo deve mostrare la esecuzione di :blue:`almeno un Test(Plan)` automatizzato ritenuto significativo.
   Per applicazioni che NON usano robot reali NON sono ammessi video
#. Presentazione (collettiva di gruppo) del progetto del sistema e della sua relazione
   con la fase di analisi.
   In questa fase è :blue:`RICHIESTA la preparazione` di ``2-3 SLIDES`` di illustrazione delle architetture con figure e (se
   ritenuto utile) riferimenti al codice.
   Al termine di queste fasi, ogni componente il gruppo può raggiungere un punteggio
   massimo di ``27/30``.
#.  :blue:`Domande` (per esempi, si veda il file `domande.html`_) rivolte dal docente a singole persone,
    riguardo al prodotto, al progetto e alla analisi del problema /requisiti.
    Al termine di questa fase una singola persona può raggiungere un punteggio massimo di ``29/30``.
#.  :blue:`Altre domande` rivolte dal docente a singole persone.
    Al termine di questa fase, una singola persona può raggiungere un punteggio di ``30elode``.

---------------------------------------------
Aggiornamenti del software
---------------------------------------------

Ho pubblicato sul GIT del corso:

- il progetto *unibo.comm22* con varie implementazioni di *Interaction2021* con diversi protocolli.
- il progetto *webrobot22* (e il file *webRobot22.html* nelle dispense) che fornisce una webGUI per il basicrobot.
- il progetto *unibo.testqakexample* come un esempio di JUnit per il testing della richiesta *pickup* del tema finale, 
  basato su un modello qak e sul fatto che gli attori qak sono risorse CoAP.

---------------------------------------------
Precisazioni del commitente
---------------------------------------------

Riportiamo le risposte date dal committente ad alcune  domande:

- Il *Waste truck driver* è un operatore umano che non fa parte del sistema. Fanno invece parte del sistema i messaggi che 
  questo deve inviare usando uno smart device (telefonino). Può essere opportuno simulare il *Waste truck driver* e viene
  consigliato di farlo introducendo una applicazione esterna, sviluppata ad esempio in Python. Nelle fasi preliminari 
  è invece accettabile di simulare il *Waste truck driver* con un attore.
- Sarebbe bene mandare via il truck appena possibile.
- Il *WasteSerice* potrebbe ricevere un nuova richiesta mentre sta ancora eseguendo la deposit action di quella precedente.
- Una volta pieni, i contenitori non verranno svuotati, se non riavviando l'applicazione.
- Come  *posizione del trolley*, sono valide anche  indicazioni solo qualitative, quali 'at indooor', 'at glassbox', etc. 
- Non è richiesto che il robot si muova in modo ottimizzato, ma non dovrebbe nemmeno girovagare troppo nella stanza.
- Il tempo di raccolta del materiale dal truck è sempre limitato e prevedibile, mentre il tempo necessario 
  per il deposito potrebbe  essere anche alquanto lungo (anche in relazione al punto preceente).
- Il sonar/led NON sono sul trolley, ma su un RaspberryPi a parte.
- Il Sonar rileva oggetti che saremo noi a porgli davanti.
- Il blocco del trolley in conseguenza di un 'allarme' individuato dal Sonar dovrebbe avvenire 'il prima possibile'.
- Il DDR fornito dal committente ha un sonar on-board, ma questo viene usato per rilevare ostacoli quando il robot si muove.

---------------------------------------------
Note dopo le interazioni in rete
---------------------------------------------

#. Lo scopo del tema finale NON è (solo) risolvere il problema ma avere un :blue:`caso di studio su cui ragionare sul 
   processo di costruzione del software`.

#. L'uso del linguaggio **qak** non va preso come un vincolo a priori. 
   Ne va invece motivato l'uso (come per ogni altra tecnologia) in relazione alle caratteristiche del (sotto)problema esaminato.
   Poichè il lettore potrebbe non consoscere questo linguaggio, è opportuno scrivere - una volta sola - un 
   (breve) documento che spiega per quali caratteristiche si ritiene opportuno usare i qak. 
   Il documento dovrebbe porre in luce gli aspetti salienti, rinviando (con links) gli approfondimenti ai documenti 'ufficiali', 
   in modo da porre in evidenza il perchè lo si propone, nel contesto del problema.  

#. I link tra i vari documenti dovrebbero essere possibilmente locali, in modo da poter leggere i documenti stessi senza 
   (ove possibile) connessione di rete.

#. Lo :blue:`scopo della analisi dei requisiti` è ridefinire il testo dato dal committente in modo 'preciso' (e formale, cioè
   per noi, comprensibile alla macchina). Per le varie entità menzionate nel testo, occorre dare risposta ad alcune
   precise domande, tra cui (altri tipi di domande sarebbe bene fossero individuate da voi):

     - cosa intende il committente per xxx (ad esempio DDR robot)?
     - il committenete fornisce indicazioni sul software da usare per xxx?
     - se si, è possibile fornire un modello di tale software (per capire bene cosa bisogna sapere per usarlo)?

   Occorre anche porre molta attenzione alle frasi scritte in linguaggio naturale e dare loro una interpretazione
   non ambigua. Ad esempio: 

       - per la frase *a DDR robot working as a transport trolley*, che relazione si pensa debba esistere tra 
         l'entità *trolley* e l'entità *DDR robot*?

#. Ogni fase (a partire dai requisiti) dovrebbe terminare con la specifica di un modello (anche non eseguibile) 
   che costituisce l'inizio della fase successiva.  Al modello dovrebbe essere associato un insieme di TestPlan 
   (ne basta anche solo uno signficativo) di tipo funzionale per chiarire cosa ci si aspetta (come ouput) dal software 
   che *dovrà essere sviluppato per quel requisito*, una volta date le opportune info di ingresso.
#. I file HTML  in *userdocs* non devono essere visti come  'documentazione', ma come una sorta di :blue:`'diario di bordo'`
   che appunta (in modo sintetico,  in linguaggio naturale e con link al codice) i punti essenziali 
   che hanno portato a quei modelli.

#. Ogni modello dovrebbe essere accompagnato da almeno un TestPlan funzionale significativo.
#. Lo :blue:`scopo della fase di analisi del problema` è definire una modello (eseguibile) della architettura logica e 
   dare elementi utili per la costruzione di un *piano di lavoro* e per la :blue:`suddivsione dei compiti` tra i componenti
   del Team.
#. Il primo SPRINT dovrebbe scaturire dal piano di lavoro e iniziare a partire dal modello dell'analisi 
   
#. Ogni SPRINT dovrebbe:

   - essere associato a un preciso obiettivo (SCRUM goal) 
   - approfondire l'analisi relativa al sottoproblema relativo al goal dello SPRINT 
   - estendere/precisare l'architettura logica e i TestPlan
   - definire una architettura di progetto e Test relativi 
   - terminare con un prototipo eseguibile (da discutere con il committente)  e una proposta di nuovo SPRINT 
     (che potrebbe anche consistere in una revisione dell'analisi, se si vede che è stata fatta male)
#. Il 'documento Ogni SPRINT dovrebbe :blue:`terminare con una pagina di sintesi` che riporta l'architettura finale 
   del sistema (con i link al modello e ai Test). 
   Questa pagina sarà l'inizio del documento relativo allo SPRINT successivo.


--------------------------------------------
Come organizzare gli sprint
--------------------------------------------

 
Lo scopo dello SPRINT0 dovrebbe essere quello di formalizzare i singoli termini del testo 
(usando un qualche linguaggio di programmazione e/o con modelli) e anche quello di fornire  una prima visione 
di insieme del sistema da realizzare.

Lo SPRINT0 dovrebbe concludersi fornendo un modello delle macro-parti del sistema,
evidenziando quali componenti sono forniti dal committente (ad esempio DDRrobot, Sonar, etc.) e quelli che invece 
bisogna sviluppare.
I  messaggi che questi componenti si scambiano potrebbero essere veri e propri requisiti ma anche *solo indicativi* 
di chi dovrà inviare informazione e di chi dovrà riceverla.  

Ad esempio, si potrebbe introdurre un modello come quello che segue:

   .. image::  ./_static/img/TF22/sprint0.png 
     :align: center 
     :width: 75%

Questa immagine viene generata in modo automatico da un  MODELLO Qak che può essere scritto 
in pochi minuti, per CATTURARE GLI ASPETTI RITENUTI ESSENZIALI
(e certo - almeno per ora - non perchè sia eseguibile).

Lo scopo di questo modello è di costituire un :blue:`SISTEMA LOGICO  DI RIFERIMENTO` e di evidenziare che: 

- Il sistema è distribuito su almeno 3 nodi computazionali diversi (il *driver* è un simulatore).
- Saremo chiamati a sviluppare i macro-componenti  *wasteservice, transporttrolley, sonaronrasp, wastewervicewtatusgui*.
- La interazione *driver-wasteservice* è di tipo request-response ed è implicata dal testo dei requsiti.
- La interazione *sonaronrasp-transporttrolley* è modellata come un evento, ma non è un requisito. Dunque potrebbe essere 
  modificata negli sviluppi successivi, mentre potrebbe essere ritenuto un requisito che l'informazione prodotta dal 
  sottosistema su *ctxrasp* non sia di pertinenza del *wasteservice* (cosa questa da discutere e da approfondire con 
  il committente).
- Risulta invece un requisito architetturale il fatto che il *wasteservice* 'non vede' il *basicrobot* 
  (che è un componente dato dal committente).

  
Questo modello dovrebbe fornire il quadro architetturale complessivo dal quale dedurre 
un possibile piano di lavoro che conduce allo SPRINT1.

Ad esempio, se il team e il committente concordano sulla opportunità di affrontare in primis il 
**core-business** del problema, allora 
lo SPRINT1 potrebbe escludere di trattare la GUI  e la parte su Rasp (se non in modo astratto
o simulato) e avere come :blue:`GOAL` (si veda SCRUM) quello di fornire un primo prototipo 
che realizza le funzionalità fondamentali del sistema.  

Lo SPRINT1  (come ogni altro sprint in futuro) dovrebbe quindi affrontare un 
:blue:`preciso sottoinsieme dei requisiti`, che va individuato  ed associato a uno o più Test-plan funzionali.

Nel caso specifico, si tatta di analizzare il problema della interazione *wasteservice-transporttrolley-basicrobot*
a partire dalla richiesta del driver.

Ogni sprint deve quindi fare uno :blue:`ZOOMING` entro una parte della architettura di RIFERIMENTO,
analizzando le problematiche poste dal sottoinsieme dei requisiti considerato. Ad esempio
l'analista di questo SPRINT1 potrebbe :

- affrontarre il problema di quando sia opportuno rispondere al driver,
- discutere su chi abbia la responsabilità di muovere il *transporttrolley* (il *wasteservice*, inviando 
  (macro) comandi o il *transporttrolley stesso* ), 
- studiate il problema di chi debba aggiornare la gui e quando
- etc. etc. 

Alla fine della analisi del problema, il modello di riferimento avrà subito una :blue:`evoluzione` che fornisce 
una una :blue:`nuova architattura logica`, che sarà il punto di partenza per un nuovo piano di lavoro.
Infatti sia l'analista prima che il progettista poi, potrebbero precisare la natura di alcune interazioni e 
introdurre nuovi componenti all'interno di *ctxwasteservice*.

Sottolineiamo che, al termine dello SPRINT,
un componente ptrebbe anche essere realizzato non come un QAk actor. 
Ad esempio,   la *WasteServiceStatusGUI*  potrebbe essere realizzata come web-application.
Il componente QAk che definisce la logica di *wastewervicewtatusgui* potrebbe essere 'dimenticato'
o (forse, preferibilmente) :blue:`riusato` all'interno di questa web-application.

--------------------------------------------
Estensioni estate 2022
--------------------------------------------

Nuova versione dei plugin qak:

- :blue:`it.unibo.Qactork_1.3.2`
- :blue:`it.unibo.Qactork.ide_1.3.2`
- :blue:`it.unibo.Qactork.ui_1.3.2`

Nuova versione del supporto run-time: :blue:`unibo.qakactor22-3.2`


++++++++++++++++++++++++++++
Nuove features
++++++++++++++++++++++++++++

- Si genera codice Python che, eseguito, costruisce una rappresentazione grafica del modello qak corrente.
  Le icone usate dal programma, sono memorizzate in: *it.unibo.issLabStart/userDocs/img/IconeQak/*
- Un *dispatch* può essere gestito come :blue:`interrupt` specificando la transizione :brown:`whenInterrupt` che porta a uno stato 
  che, eseguendo la primitiva :brown:`returnFromInterrupt`, ripristina l'insieme delle transizioni dello stato 'interrotto'
  (quello che ha eseguito *whenInterrupt*). Questa feature è stata solleciata da *Hu*.
- Primitiva :brown:`observeResource A` che Possibilità che un attore :blue:`Obs` funga da CoAP oberver delle informazioni emesse da un
  attore :blue:`A` mediante la primitiva :blue:`updateResourse`. 
  Presso :blue:`Obs` viene attivato un CoAP-client verso :blue:`A`. Questo client provvede a trasformare ogni messaggio ricevuto via CoAP 
  da :blue:`A` in un dispatch 
  
   .. code:: 
      
      Dispatch coapUpdate: coapUpdate(RESOURCE, VALUE)

  Questa feature è stata proposta  da *Lenzi* che ne ha fornito una prima realizzazione.
- Possibilità di specificare **più contesti** nello stesso nodo :blue:`localhost`. Questa feature è stata proposta  da *Giannatempo*
  che ne ha fornito una prima realizzazione.
- (2 Settembre 2022) Per poter visualizzare in un diagramma **anche le risposte**, 
  la operazione Qak :blue:`replyTo` può essere terminata 
  da una frase **opzionale** *caller==* che indica il chiamante cui la risposta si intende inviata. Ad esempio:
  
  .. code::

     replyTo ready with readyok : readyok(boundaryqak30) caller== cmdconsole
   
  Se il sender della richiesta cui si risponde non è quello indicato dopo *caller==*,  
  si genera - a runtime - un avviso di errore in colore rosso, insieme
  a un segnale acustico. L'applicazion e prosegue, ma il digramma risulta sbagliato e l'errore va corretto 
  agendo sul modello.

++++++++++++++++++++++++++++
unibo.qk30demo
++++++++++++++++++++++++++++

Un esempio delle nuove features viene fornito nel modello *boundaryqak30.qak*  del progetto :blue:`unibo.qk30demo`.

   .. image::  ./_static/img/TF22/boundaryqak30Arch.png 
     :align: center 
     :width: 60%


L'attore *cmdconsole* invia la richiesta *ready* a boundaryqak30 e quando riceve la **risposta** (*readyok*)
mostra all'utente una GUI (realizzata in Java).

Successivamente, la console emette un evento *alarm* ogni 5 secondi; questo evento 
viene gestito da *applobserver* come esempio di uso degli eventi
(e di alternativa al meccanismo di CoAP-osservabilità).

L'oggetto Kotlin *externalCoapObserver.kt* è un 'alieno' (definito nella directory *resources/observers*) 
che realizza un observer dell'attore *boundaryqak30*.
Questo observer si affianca all'attore-observer interno al modello, di nome *applobserver*.
