from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
with Diagram('sonarArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxsonar', graph_attr=nodeattr):
          sonar=Custom('sonar','./qakicons/symActorSmall.png')
          sonarsimulator=Custom('sonarsimulator(coded)','./qakicons/codedQActor.png')
          datalogger=Custom('datalogger(coded)','./qakicons/codedQActor.png')
          datacleaner=Custom('datacleaner(coded)','./qakicons/codedQActor.png')
          distancefilter=Custom('distancefilter(coded)','./qakicons/codedQActor.png')
     with Cluster('ctxradargui', graph_attr=nodeattr):
          radargui=Custom('radargui(ext)','./qakicons/externalQActor.png')
     sonar >> Edge(color='blue', style='solid', xlabel='simulatorstart', fontcolor='blue') >> sonarsimulator
     sys >> Edge(color='red', style='dashed', xlabel='obstacle', fontcolor='red') >> sonar
     sys >> Edge(color='red', style='dashed', xlabel='sonarRobot', fontcolor='red') >> sonar
     sonar >> Edge( xlabel='sonardata', **eventedgeattr, fontcolor='red') >> sys
diag
