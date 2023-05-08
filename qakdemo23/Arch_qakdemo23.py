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
     with Cluster('ctxdemoreq', graph_attr=nodeattr):
          caller=Custom('caller','./qakicons/symActorSmall.png')
          called=Custom('called','./qakicons/symActorSmall.png')
     caller >> Edge(color='magenta', style='solid', xlabel='r1', fontcolor='magenta') >> called
diag
