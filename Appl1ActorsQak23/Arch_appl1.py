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
with Diagram('appl1Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxall', graph_attr=nodeattr):
          appl=Custom('appl','./qakicons/symActorSmall.png')
          consoleobs=Custom('consoleobs','./qakicons/symActorSmall.png')
          sonarobs=Custom('sonarobs','./qakicons/symActorSmall.png')
          obsforpath=Custom('obsforpath','./qakicons/symActorSmall.png')
     appl >> Edge(color='blue', style='solid', xlabel='restart', fontcolor='blue') >> appl
     consoleobs >> Edge(color='blue', style='solid', xlabel='stopappl', fontcolor='blue') >> appl
     consoleobs >> Edge(color='blue', style='solid', xlabel='resumeappl', fontcolor='blue') >> appl
     sys >> Edge(color='red', style='dashed', xlabel='sonardata', fontcolor='red') >> sonarobs
     sonarobs >> Edge(color='blue', style='solid', xlabel='stopcmd', fontcolor='blue') >> appl
     sonarobs >> Edge(color='blue', style='solid', xlabel='resumecmd', fontcolor='blue') >> appl
     sys >> Edge(color='red', style='dashed', xlabel='info', fontcolor='red') >> obsforpath
diag
