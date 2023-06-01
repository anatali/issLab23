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
with Diagram('sonarqak23Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxsonarqak23', graph_attr=nodeattr):
          sonar23=Custom('sonar23','./qakicons/symActorSmall.png')
          appl=Custom('appl','./qakicons/symActorSmall.png')
          sonar=Custom('sonar(coded)','./qakicons/codedQActor.png')
          datacleaner=Custom('datacleaner(coded)','./qakicons/codedQActor.png')
          distancefilter=Custom('distancefilter(coded)','./qakicons/codedQActor.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     sys >> Edge(color='red', style='dashed', xlabel='sonardata', fontcolor='red') >> sonar23
     sys >> Edge(color='red', style='dashed', xlabel='obstacle', fontcolor='red') >> sonar23
     sonar23 >> Edge( xlabel='alarm', **eventedgeattr, fontcolor='red') >> sys
     appl >> Edge(color='magenta', style='solid', xlabel='engage', fontcolor='magenta') >> basicrobot
     appl >> Edge(color='magenta', style='solid', xlabel='moverobot', fontcolor='magenta') >> basicrobot
     appl >> Edge(color='blue', style='solid', xlabel='disengage', fontcolor='blue') >> basicrobot
diag
