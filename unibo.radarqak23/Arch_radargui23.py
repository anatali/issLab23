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
with Diagram('radargui23Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxradargui', graph_attr=nodeattr):
          radargui=Custom('radargui','./qakicons/symActorSmall.png')
     sys >> Edge(color='red', style='dashed', xlabel='polar', fontcolor='red') >> radargui
     sys >> Edge(color='red', style='dashed', xlabel='sonardata', fontcolor='red') >> radargui
diag
