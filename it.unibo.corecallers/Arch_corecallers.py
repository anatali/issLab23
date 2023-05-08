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
with Diagram('corecallersArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxresourcecore', graph_attr=nodeattr):
          resourcecore=Custom('resourcecore(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxcorecaller1', graph_attr=nodeattr):
          corecaller1=Custom('corecaller1','./qakicons/symActorSmall.png')
          alarmoffemitter=Custom('alarmoffemitter','./qakicons/symActorSmall.png')
     corecaller1 >> Edge(color='magenta', style='solid', xlabel='cmd', fontcolor='magenta') >> resourcecore
     corecaller1 >> Edge( xlabel='alarm', **eventedgeattr, fontcolor='red') >> sys
     alarmoffemitter >> Edge( xlabel='alarm', **eventedgeattr, fontcolor='red') >> sys
diag
