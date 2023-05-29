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
with Diagram('userobotArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
          pathobs=Custom('pathobs(coded)','./qakicons/codedQActor.png')
     with Cluster('ctxuserobot', graph_attr=nodeattr):
          userobotclient=Custom('userobotclient','./qakicons/symActorSmall.png')
     userobotclient >> Edge(color='magenta', style='solid', xlabel='engage', fontcolor='magenta') >> basicrobot
     userobotclient >> Edge(color='blue', style='solid', xlabel='setrobotstate', fontcolor='blue') >> basicrobot
     userobotclient >> Edge(color='magenta', style='solid', xlabel='moverobot', fontcolor='magenta') >> basicrobot
diag
