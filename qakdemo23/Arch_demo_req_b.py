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
with Diagram('demo_req_bArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxcaller', graph_attr=nodeattr):
          caller=Custom('caller','./qakicons/symActorSmall.png')
     with Cluster('ctxcalled', graph_attr=nodeattr):
          called=Custom('called','./qakicons/symActorSmall.png')
     caller >> Edge(color='magenta', style='solid', xlabel='r1', fontcolor='magenta') >> called
diag
