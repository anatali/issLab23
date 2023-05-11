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
     with Cluster('ctxdemocodedqactor', graph_attr=nodeattr):
          datahandler=Custom('datahandler','./qakicons/symActorSmall.png')
          w1=Custom('w1(coded)','./qakicons/codedQActor.png')
          w2=Custom('w2(coded)','./qakicons/codedQActor.png')
          w3=Custom('w3(coded)','./qakicons/codedQActor.png')
     datahandler >> Edge(color='blue', style='solid', xlabel='start', fontcolor='blue') >> w1
     datahandler >> Edge(color='blue', style='solid', xlabel='start', fontcolor='blue') >> w2
     datahandler >> Edge(color='blue', style='solid', xlabel='start', fontcolor='blue') >> w3
     sys >> Edge(color='red', style='dashed', xlabel='alarm', fontcolor='red') >> datahandler
diag
