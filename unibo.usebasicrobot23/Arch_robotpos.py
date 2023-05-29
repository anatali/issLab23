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
with Diagram('robotposArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxrobotpos', graph_attr=nodeattr):
          worker=Custom('worker','./qakicons/symActorSmall.png')
          client=Custom('client','./qakicons/symActorSmall.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     worker >> Edge(color='magenta', style='solid', xlabel='engage', fontcolor='magenta') >> basicrobot
     worker >> Edge(color='blue', style='solid', xlabel='disengage', fontcolor='blue') >> basicrobot
     worker >> Edge(color='magenta', style='solid', xlabel='doplan', fontcolor='magenta') >> basicrobot
     client >> Edge(color='magenta', style='solid', xlabel='moverobot', fontcolor='magenta') >> worker
diag
