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
with Diagram('mapwithobstqak23Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxmapwithobstqak23', graph_attr=nodeattr):
          mapwithobstqak23=Custom('mapwithobstqak23','./qakicons/symActorSmall.png')
          mapusage=Custom('mapusage','./qakicons/symActorSmall.png')
          mover=Custom('mover','./qakicons/symActorSmall.png')
     mapwithobstqak23 >> Edge(color='magenta', style='solid', xlabel='engage', fontcolor='magenta') >> basicrobot
     mapwithobstqak23 >> Edge(color='magenta', style='solid', xlabel='step', fontcolor='magenta') >> basicrobot
     mapwithobstqak23 >> Edge(color='blue', style='solid', xlabel='cmd', fontcolor='blue') >> basicrobot
     mapwithobstqak23 >> Edge(color='magenta', style='solid', xlabel='dopath', fontcolor='magenta') >> basicrobot
     mapwithobstqak23 >> Edge(color='blue', style='solid', xlabel='disengage', fontcolor='blue') >> basicrobot
     mapusage >> Edge(color='magenta', style='solid', xlabel='engage', fontcolor='magenta') >> basicrobot
     mapusage >> Edge(color='blue', style='solid', xlabel='gotopos', fontcolor='blue') >> mover
     mapusage >> Edge(color='magenta', style='solid', xlabel='dopath', fontcolor='magenta') >> basicrobot
     mapusage >> Edge(color='blue', style='solid', xlabel='cmd', fontcolor='blue') >> mapusage
     mapusage >> Edge(color='blue', style='solid', xlabel='disengage', fontcolor='blue') >> basicrobot
     mover >> Edge(color='magenta', style='solid', xlabel='engage', fontcolor='magenta') >> basicrobot
     mover >> Edge(color='blue', style='solid', xlabel='gotopos', fontcolor='blue') >> mover
     mover >> Edge(color='magenta', style='solid', xlabel='dopath', fontcolor='magenta') >> basicrobot
     mover >> Edge(color='blue', style='solid', xlabel='cmd', fontcolor='blue') >> basicrobot
diag
