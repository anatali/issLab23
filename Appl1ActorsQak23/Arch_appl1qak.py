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
with Diagram('appl1qakArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxall', graph_attr=nodeattr):
          appl=Custom('appl','./qakicons/symActorSmall.png')
          consoleobs=Custom('consoleobs','./qakicons/symActorSmall.png')
          sonarobs=Custom('sonarobs','./qakicons/symActorSmall.png')
          obsforpath=Custom('obsforpath','./qakicons/symActorSmall.png')
          console=Custom('console','./qakicons/symActorSmall.png')
     appl >> Edge(color='blue', style='solid', xlabel='restart', fontcolor='blue') >> appl
     consoleobs >> Edge(color='blue', style='solid', xlabel='stopappl', fontcolor='blue') >> appl
     consoleobs >> Edge(color='blue', style='solid', xlabel='resumeappl', fontcolor='blue') >> appl
     sys >> Edge(color='red', style='dashed', xlabel='sonardata', fontcolor='red') >> sonarobs
     sonarobs >> Edge(color='blue', style='solid', xlabel='stopcmd', fontcolor='blue') >> appl
     sonarobs >> Edge(color='blue', style='solid', xlabel='resumecmd', fontcolor='blue') >> appl
     sys >> Edge(color='red', style='dashed', xlabel='info', fontcolor='red') >> obsforpath
     sys >> Edge(color='red', style='dashed', xlabel='guicmd', fontcolor='red') >> console
     sys >> Edge(color='red', style='dashed', xlabel='getpath', fontcolor='red') >> console
     console >> Edge(color='blue', style='solid', xlabel='startcmd', fontcolor='blue') >> appl
     console >> Edge(color='blue', style='solid', xlabel='stopcmd', fontcolor='blue') >> appl
     console >> Edge(color='blue', style='solid', xlabel='resumecmd', fontcolor='blue') >> appl
     console >> Edge(color='magenta', style='solid', xlabel='getpath', fontcolor='magenta') >> appl
diag
