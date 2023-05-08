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
with Diagram('basicrobot23Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          engager=Custom('engager','./qakicons/symActorSmall.png')
          basicrobot=Custom('basicrobot','./qakicons/symActorSmall.png')
          basicrobotusage=Custom('basicrobotusage','./qakicons/symActorSmall.png')
     sys >> Edge(color='red', style='dashed', xlabel='sonardata', fontcolor='red') >> engager
     engager >> Edge(color='blue', style='solid', xlabel='engaged', fontcolor='blue') >> basicrobot
     sys >> Edge(color='red', style='dashed', xlabel='obstacle', fontcolor='red') >> engager
     basicrobotusage >> Edge(color='magenta', style='solid', xlabel='engage', fontcolor='magenta') >> basicrobot
     basicrobotusage >> Edge(color='magenta', style='solid', xlabel='step', fontcolor='magenta') >> basicrobot
diag
