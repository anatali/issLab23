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
with Diagram('qakdemo23Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxdemo0', graph_attr=nodeattr):
          demo0=Custom('demo0','./qakicons/symActorSmall.png')
          perceiver=Custom('perceiver','./qakicons/symActorSmall.png')
          sender=Custom('sender','./qakicons/symActorSmall.png')
     sys >> Edge(color='red', style='dashed', xlabel='alarm', fontcolor='red') >> perceiver
     sender >> Edge(color='blue', style='solid', xlabel='msg1', fontcolor='blue') >> demo0
     sender >> Edge(color='blue', style='solid', xlabel='msg2', fontcolor='blue') >> demo0
     sender >> Edge( xlabel='alarm', **eventedgeattr, fontcolor='red') >> sys
diag
