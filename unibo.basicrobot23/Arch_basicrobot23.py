### conda install diagrams
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
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          engager=Custom('engager','./qakicons/symActorSmall.png')
          basicrobot=Custom('basicrobot','./qakicons/symActorSmall.png')
          planexec=Custom('planexec','./qakicons/symActorSmall.png')
          robotpos=Custom('robotpos','./qakicons/symActorSmall.png')
          datacleaner=Custom('datacleaner(coded)','./qakicons/codedQActor.png')
          distancefilter=Custom('distancefilter(coded)','./qakicons/codedQActor.png')
     robotpos >> Edge(color='magenta', style='solid', decorate='true', label='<doplan<font color="darkgreen"> doplandone doplanfailed</font> &nbsp; >',  fontcolor='magenta') >> planexec
     planexec >> Edge(color='magenta', style='solid', decorate='true', label='<step<font color="darkgreen"> stepdone stepfailed</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
     planexec >> Edge(color='blue', style='solid',  decorate='true', label='<nomoremove &nbsp; nextmove &nbsp; >',  fontcolor='blue') >> planexec
diag
