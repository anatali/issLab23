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
with Diagram('radarsystem23analisiArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxprototipo0', graph_attr=nodeattr):
          sonar23=Custom('sonar23','./qakicons/symActorSmall.png')
          led23=Custom('led23','./qakicons/symActorSmall.png')
          radar23=Custom('radar23','./qakicons/symActorSmall.png')
          controller23=Custom('controller23','./qakicons/symActorSmall.png')
     controller23 >> Edge(color='blue', style='solid', xlabel='sonaractivate', fontcolor='blue') >> sonar23
     sys >> Edge(color='red', style='dashed', xlabel='sonardata', fontcolor='red') >> controller23
     controller23 >> Edge(color='blue', style='solid', xlabel='polar', fontcolor='blue') >> radar23
     controller23 >> Edge(color='blue', style='solid', xlabel='ledCmd', fontcolor='blue') >> led23
diag
