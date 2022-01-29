package ClassBase;

public class BasicGraphOperations {
	
	protected Graph createGraph(String[] connectionNames, int[][] connections) {
        Graph graph = new Graph();

        for (int i = 0; i < connectionNames.length; i++) {    //initialisation of the vertecies in locations
            graph.addVertex(new Vertex(connectionNames[i]));
        }

        for (int i = 0; i < connections.length; i++) {
            for (int j = 0; j < connections[0].length; j++) {
                if (connections[i][j] != 0) {
                    graph.addEdge(new Edge(graph.getVertex(connectionNames[i]), graph.getVertex(connectionNames[j]), connections[i][j]));
                }
            }
        }

        return graph;
    }
	
	protected void outputGraph(Graph graph) {
    	//printing out all vertecies of cities
        List<Vertex> listV = graph.getVertices();
        listV.toFirst();
        while (listV.hasAccess()) {
            System.out.println(listV.getContent().getID());
            listV.next();
        }
    	
    	System.out.println("-");
    	
        //prining out all the neighbours of the vertecies in locations
        List<Edge> listE = graph.getEdges();
        listE.toFirst();
        while (listE.hasAccess()) {
            System.out.println(listE.getContent().getVertices()[0].getID());
            System.out.println(listE.getContent().getVertices()[1].getID());
            System.out.println(listE.getContent().getWeight());
            System.out.println();
            listE.next();
        }
    }
}
