.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

.. _Java annotation: https://en.wikipedia.org/wiki/Java_annotation
.. _Java Reflection: https://www.oracle.com/technical-resources/articles/java/javareflection.html
.. _Java Type Annotations: https://docs.oracle.com/javase/tutorial/java/annotations/type_annotations.html
.. _Spring and Spring Boot: https://www.baeldung.com/spring-vs-spring-boot
.. _Spring Controllers: https://www.baeldung.com/spring-controllers
.. _Pattern matching: https://www.w3schools.com/java/java_regex.asp

======================================
Annotazioni
======================================

:remark:`Le annotazioni sono una forma di metadati`

in quanto forniscono informazioni su un programma.

:remark:`Le annotazioni non influiscono direttamente sulla semantica del programma`

ma influiscono sul modo in cui i programmi vengono 
trattati da **strumenti** e **librerie**, che a loro volta possono influenzare la semantica del programma in esecuzione.

Le `Java annotation`_ sono state introdotte nella  release 5 (Tiger).
A partire dalla versione Java SE 8, le annotazioni possono essere applicate non solo alle dichiarazioni
(come accadeva prima) ma in ogni situazione in cui sia un tipo
(si veda `Java Type Annotations`_)
allo scopo di realizzare uno più forte controllo di tipo (si pensi ad esempio a :blue:`@NonNull` ).

Oggi le annotazioni si trovano quasi ovunque nei programmi e sono introdotte per diversi scopi:

- per dare semantica aggiuntiva a vari elementi di una classe, che 
  possono aiutare a meglio comprenderne l'intento;
- per permettere ulteriori controlli in fase di compilazione che garantiscono il rispetto di vari vincoli;
- per dare supporto ad analisi aggiuntiva del codice  tramite strumenti sensibili alle annotazioni.


Le annotazioni possono essere elaborate  in fase di compilazione e/o in fase di esecuzione,
sfruttando in questo caso le API di `Java Reflection`_, con possibile impatto sulle prestazioni, se non usate con attenzione.

Forse il più grande vantaggio delle annotazioni è 
dare supporto a un **paradigma di progettazione basato su configurazione esplicita**, il che 
permette di semplificare diversi aspetti della configurazione, con 
grande impatto sul processo di sviluppo. 

Framework molto diffusi che sfruttano questo aspetto sono `Spring and Spring Boot`_ che useremo più avanti, quando vorremo dotare 
le nostre applicazioni di una WebGUI.


Per il momento ci limitiamo a illustrare i meccanismi Java che permettono di sfruttare le annotazioni nelle nostre applicazioni.
 

----------------------------------
Meta-Annotation in Java
----------------------------------

Per specificare il comportamento delle Annotation, Java introduce altre Annotation, che 
vengono definite **Meta-Annotation**.

.. list-table:: 
  :widths: 20,80
  :width: 100%

  * - @Target      
    - permette di definire a quale parte del codice può essere collegata l'Annotation.
  
      .. list-table:: 
        :widths: 23,77
        :width: 100% 

        * - ElementType.PACKAGE	        
          - Si applica alla definizione del package
        * - ElementType.TYPE	        
          - Si applica alla definizione di classi, interfacce ed enumeration
        * - ElementType.FIELD	        
          - Si applica agli attributi
        * - ElementType.METHOD	        
          - Si applica ai metodi
        * - ElementType.PARAMETER	    
          - Si applica ai parametri dei metodi
        * - ElementType.CONSTRUCTOR	    
          - Si applica al costruttore
        * - ElementType.LOCAL_VARIABLE	
          - Si applica ad una variabile locale

  * - @Retention
    - specifica come saranno visibili le informazioni collegate all’Annotation.

  * - @Documented
    - serve per includere l’Annotation nel javadoc, visto che per default sono escluse.

  * - @Inherited
    -  una classe che utilizza l'Annotation, la fa ereditare anche alle classi figlie.

----------------------------------
Annotazioni: esempio
----------------------------------

++++++++++++++++++++++++++++++++++++++++
Annotation: definizione 
++++++++++++++++++++++++++++++++++++++++

Come esempio, definiamo una annotazione per descrivere il modo di accedere ad una applicazione
(il codice si trova in ``test/annotations`` del progetto ``unibo.actor22``):

.. code:: Java

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface AccessSpec {
        enum issProtocol {UDP,TCP,HTTP,MQTT,COAP,WS} ;
        issProtocol protocol() default issProtocol.TCP;
        String url() default "unknown";
    }

La meta-annotation ``@Retention`` dice che l'annotazione ``@AccessSpec`` è visibile a runtime.

L'annotazione permette di specificare due attributi:

- il tipo di protocollo ( metodo ``protocol()`` ), con default TCP
- un URL, come ad esempio ``"http://localHost:8090/api/move"``

++++++++++++++++++++++++++++++++++++++++
Annotation: uso  
++++++++++++++++++++++++++++++++++++++++

Introduciamo una classe che introduce l'annotazione ``@AccessSpec`` e accede dinamicamente ad essa, per visualizzarla.

.. code:: Java

    @AccessSpec(
        protocol = AccessSpec.issProtocol.HTTP,
        url      = "http://localHost:8090/api/move"
    )   
    public class AnnotationUsageDemo {
    
    public static void readProtocolAnnotation(Object element) { ...}
    
        public AnnotationUsageDemo() {
            readProtocolAnnotation( this );	             
        }
        public static void main( String[] args) {
            new AnnotationUsageDemo();
        }
    }

Definiamo ora il metodo ``readProtocolAnnotation`` in modo da 
recuperare (usando le API di `Java Reflection`_) le informazioni che abbiamo inserito tramite 
l'annotazione:
.

.. code:: Java

    public static void readProtocolAnnotation(Object element) {
    try {
      Class<?> clazz            = element.getClass();
      Annotation[] annotations  = clazz.getAnnotations();
      for (Annotation annot : annotations) {
        if (annot instanceof AccessSpec) {
          AccessSpec p  = (AccessSpec) annot;
          ColorsOut.outappl("Tipo del protocollo: " 
                + p.protocol(), ColorsOut.CYAN);
          ColorsOut.outappl("Url del protocollo:  " 
                + p.url(), ColorsOut.CYAN);
          String v = getHostAddr(
                "(\\w*)://([a-zA-Z]*):(\\d*)/(\\w*)/(\\w*)", 
                p.url());
          ColorsOut.outappl("v: " + v, ColorsOut.CYAN);
        }
      }
    } catch (Exception e) {... }
    }


Il metodo  ``getHostAddr``  estrae la parte *host:port* dall'URL, usando `Pattern matching`_ su espressioni regolari:

.. code:: Java

    public static String getHostAddr(String functor, String line){
        Pattern pattern = Pattern.compile(functor);
        Matcher matcher = pattern.matcher(line);
        ColorsOut.outappl("line: " + line, ColorsOut.CYAN);
        String content = null;
        if( matcher.find()) {
            for( int i = 1; i<=5; i++ ) {
                ColorsOut.outappl("goup " + i + ":" + 
                      matcher.group(i),   ColorsOut.CYAN);          	
            }
            content = matcher.group(2)+":"+matcher.group(3);
         }
        return content;
    }

L'output del programma è il seguente:

.. code:: 

    Tipo del protocollo: HTTP
    Url del protocollo:  http://localHost:8090/api/move
    line: http://localHost:8090/api/move
    goup 1:http
    goup 2:localHost
    goup 3:8090
    goup 4:api
    goup 5:move
    v: localHost:8090    


----------------------------------------
Configurare con Annotation
----------------------------------------

Nel package ``unibo.actor22.annotations`` del progetto ``unibo.actor22``,
definiamo due annotazioni, una per dichiarare attori locali e una per dichiarare attori remoti.

++++++++++++++++++++++++++++++++++++
@ActorLocal e @ActorRemote
++++++++++++++++++++++++++++++++++++


.. list-table:: 
  :widths: 30,70
  :width: 100%

  * - @ActorLocal
  
      .. code:: Java

        @Retention(RetentionPolicy.RUNTIME) 
        public @interface ActorLocal {
          String[] name();
           Class[]  implement();
        }  

    - La dichiarazione consiste nella specifica di un array di nomi di attori e di un corrispondente
      array di classi di implentazione
        

  * - @ActorRemote
  
      .. code:: Java
           
        @Retention(RetentionPolicy.RUNTIME)  
        public @interface ActorRemote {
	      String[] name();
	      String[] host();
	      String[] port();
	      String[] protocol();
        }  

    - La dichiarazione consiste nella specifica di un array di nomi di attori e di  corrispondenti
      array per specificare l'idirizzo (host) la porta (port) e il protocollo (protocol) relativi
      al contesto (si veda :ref:`Qak22Context`) in cui sono dichiarati come attori locali.
  
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Esempio
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Nel package ``unibo.actor22.distrib.annot`` della directory ``test`` del progetto ``unibo.actor22``,
definiamo il main della parte di sistema ad attori allocata sul PC che contiene un ``ControllerActor``
che utilizza attori remoti relativi a dispositivi Led e Sonar allocati su uno stesso RaspberryPi
di indirizzo definito in ``ApplData.raspAddr``:

.. code:: Java

    @ActorLocal(name =     {"controller" }, 
            implement = {unibo.actor22.common.ControllerActor.class })
    @ActorRemote(name =   {"led","sonar"}, 
                host=    { ApplData.raspAddr, ApplData.raspAddr}, 
                port=    { ""+ApplData.ctxPort, ""+ApplData.ctxPort}, 
                protocol={ "TCP" , "TCP" })
    public class UsingActorsWithAnnotOnPc {
        ...
        protected void configure() {
            Qak22Context.handleLocalActorDecl(this);
            Qak22Context.handleRemoteActorDecl(this);
        }
        ...
    }

La fase di configurazione consiste ora nella invocazione di operazioni del contesto capaci di 
analizzare le dichiarazioni ed agire di conseguenza.

++++++++++++++++++++++++++++++++++++
Qak22Context.handleLocalActorDecl
++++++++++++++++++++++++++++++++++++

La gestione della dichiarazione degli attori locali consiste nel creare una istanza di ciascuna classe,
trasferendo al costruttore il nome corrispondente.

.. code:: Java

    public static void handleLocalActorDecl(Object element) {
    new EventMsgHandler(   );	//attore di sistema
    Class<?> clazz            = element.getClass();
    Annotation[] annotations  = clazz.getAnnotations();
      for (Annotation annot : annotations) {
        if (annot instanceof ActorLocal) {
          ActorLocal a = (ActorLocal) annot;
          for( int i=0; i<a.name().length; i++) {
            String name     = a.name()[i];
            Class  impl     = a.implement()[i];
            try {
              impl.getConstructor( String.class ).newInstance( name );
            } catch ( Exception e) { ... }
          }
        }
      }
    }

.. Notamo che in questa fase viene anche creato l'attore di sistema :ref:`EventMsgHandler` per la  gestione degli eventi (si veda :ref:`Eventi`).




++++++++++++++++++++++++++++++++++++
Qak22Context.handleRemoteActorDecl
++++++++++++++++++++++++++++++++++++
La gestione della dichiarazione degli attori remoti si traduce in una automatizzazione della
invocazione alla operazione  :ref:`setActorAsRemote` con i parametri specificati nella annotazione
per ciascun attore.


.. code:: Java

    public static void handleRemoteActorDecl(Object element) {
        Class<?> clazz            = element.getClass();
        Annotation[] annotations  = clazz.getAnnotations();
        for (Annotation annot : annotations) {
        if (annot instanceof ActorRemote) {
            ActorRemote a = (ActorRemote) annot;
            for( int i=0; i<a.name().length;i++) {
                String name     = a.name()[i];
                String host     = a.host()[i];
                String port     = a.port()[i];
                String protocol = a.protocol()[i];        			 
                Qak22Context.setActorAsRemote(
                    name, port, host, ProtocolInfo.getProtocol(protocol));
                }
            }
        }
    }

++++++++++++++++++++++++++++++++++++
Altre annotazioni di configurazione
++++++++++++++++++++++++++++++++++++

:worktodo:`WORKTODO: proporre altre forme di dichiarazione del sistema mediante annotazioni`


