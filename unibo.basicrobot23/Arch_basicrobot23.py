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
          planexec=Custom('planexec','./qakicons/symActorSmall.png')
          robotposendosimbiotico=Custom('robotposendosimbiotico','./qakicons/symActorSmall.png')
     engager >> Edge(color='blue', style='solid', xlabel='disengaged', fontcolor='blue') >> basicrobot
     engager >> Edge(color='blue', style='solid', xlabel='engaged', fontcolor='blue') >> basicrobot
     sys >> Edge(color='red', style='dashed', xlabel='alarm', fontcolor='red') >> planexec
     planexec >> Edge(color='blue', style='solid', xlabel='nextmove', fontcolor='blue') >> planexec
     planexec >> Edge(color='blue', style='solid', xlabel='nomoremove', fontcolor='blue') >> planexec
     planexec >> Edge(color='magenta', style='solid', xlabel='step', fontcolor='magenta') >> basicrobot
     robotposendosimbiotico >> Edge(color='magenta', style='solid', xlabel='doplan', fontcolor='magenta') >> basicrobot
diag
