//Chase Parker(821253141)
//Ahmet Gueye(821319753)
package edu.sdsu.cs.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

public class DirectedGraph<V> implements IGraph<V> {
    List<V> vtsList = new ArrayList<V>();
    List<Vertex> vertices = new ArrayList<Vertex>();

    public String toString() {
        Iterator<V> iter = vtsList.iterator();
        StringBuffer graphStructure = new StringBuffer();
        while (iter.hasNext()) {
            V v = iter.next();
            graphStructure.append("Vertex: "+ v  + '\n' +  dashedLine());
            int index = vtsList.indexOf(v);
            Vertex vetex = vertices.get(index);
            List<Edge> edges = vetex.getEdges();

            for (Edge edge : edges) {
                graphStructure.append( edge.getTarget().getV()+" " );
            }
            graphStructure.append('\n');

        }
        return graphStructure.toString();
    }

    public DirectedGraph() {
        //TODO Auto-generated constructor stub
    }

    @Override
    public void add(V vertexName) {
        vtsList.add(vertexName);
        vertices.add(new Vertex(vertexName));
    }

    @Override
    public void connect(V start, V destination) {
        int startIndex = vtsList.indexOf(start);
        int destinationIndex = vtsList.indexOf(destination);
        if (startIndex < 0 || destinationIndex < 0)
            throw new NoSuchElementException("Can't connect : One of the element is not available in Graph to connect");

        Vertex startVertex = vertices.get(startIndex);
        List<Edge> edges = startVertex.getEdges();
        for (Edge edge : edges)
            if (edge.getTarget().getV().equals(destination))
                return;
        Vertex edge = vertices.get(destinationIndex);
        startVertex.addEdge(edge, 1);

    }

    @Override
    public void clear() {
        vtsList.clear();
        vertices.clear();

    }

    @Override
    public boolean contains(V label) {
        if (vtsList.indexOf(label) >= 0)
            return true;
        return false;
    }

    @Override
    public void disconnect(V start, V destination) {
        int startIndex = vtsList.indexOf(start);
        int destinationIndex = vtsList.indexOf(destination);
        if (startIndex < 0 || destinationIndex < 0)
            throw new NoSuchElementException("Can't connect : One of the element is not available in Graph to connect");

        Vertex startVertex = vertices.get(startIndex);
        startVertex.removeNeighbor(destination);

    }

    @Override
    public boolean isConnected(V start, V destination) {
        int startIndex = vtsList.indexOf(start);
        int destinationIndex = vtsList.indexOf(destination);
        if (startIndex < 0 || destinationIndex < 0)
            throw new NoSuchElementException("Can't connect : One of the element is not available in Graph to connect");
        if (start.equals(destination))
            return true;
        try {
            for (V v : neighbors(start))
                if (destination.equals(v))
                    return true;

            for (V v : neighbors(start))
                return isConnected(v, destination);
        } catch (NoSuchElementException e) {
            return false;
        }
        return false;
    }

    @Override
    public Iterable<V> neighbors(V vertexName) {
        int startIndex = vtsList.indexOf(vertexName);
        if (startIndex < 0)
            throw new NoSuchElementException("No " + vertexName + " available");
        Vertex startVertex = vertices.get(startIndex);
        List<V> vts = new ArrayList<V>();
        List<Edge> edges = startVertex.getEdges();
        for (Edge edge : edges)
            vts.add((V) edge.getTarget().getV());
        if (vts.size() > 0)
            return vts;
        else
            throw new NoSuchElementException("No neighbor vertexs available");
    }

    @Override
    public void remove(V vertexName) {

        if (!vtsList.contains(vertexName))
            throw new NoSuchElementException("No " + vertexName + " is available");
        // 1. remove from vertices
        int index = vtsList.indexOf(vertexName);
        vertices.remove(index);

        // 2. remove from list
        vtsList.remove(vertexName);

        // 3. remove in neighbor vertices
        for (Vertex v : vertices) {
            v.removeNeighbor(vertexName);
        }

    }

    private void computePaths(Vertex source) {
        source.setMiniDistance(0);
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();

            // Visit each edge exiting u
            Iterator<Edge> iter = u.getEdges().iterator();

            while (iter.hasNext()) {
                Edge edge = iter.next();
                Vertex v = edge.getTarget();
                int weight = edge.getWeight();
                int distanceThroughU = u.getMiniDistance() + weight;
                if (distanceThroughU < v.getMiniDistance()) {
                    vertexQueue.remove(v);
                    v.setMiniDistance(distanceThroughU);
                    v.previous = u;
                    vertexQueue.add(v);

                }
            }
        }
    }
    private String dashedLine() {
        StringBuilder sb = new StringBuilder(85);
        for (int n = 0; n < 85; ++n)
            sb.append('-');
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    @Override
    public List<V> shortestPath(V start, V destination) {
        int startIndex = vtsList.indexOf(start);
        int destinationIndex = vtsList.indexOf(destination);
        if (startIndex < 0 || destinationIndex < 0)
            throw new NoSuchElementException(
                    "Can't provide shortestpath : One of the element is not available in Graph");
        computePaths(vertices.get(startIndex));
        Vertex target = vertices.get(destinationIndex);

        List<V> path = new ArrayList<V>();
        for (Vertex Vertx = target; Vertx != null; Vertx = Vertx.previous)
            path.add((V) Vertx.getV());

        Collections.reverse(path);
        return path;
    }

    @Override
    public int size() {
        return vtsList.size();
    }

    @Override
    public Iterable<V> vertices() {
        return vtsList;
    }

    private ArrayList<V> search(V origin) {
        ArrayList<V> found = new ArrayList<>();
        Queue<V> queue = new PriorityQueue();
        TreeMap<V, Boolean> visited = new TreeMap<>();
        for (V v : vtsList) // adjList contains all nodes, and then a list of
            // connections for each node
            visited.put(v, false);

        if (visited.get(origin) == false)
            queue.add(origin);

        while (!queue.isEmpty()) {
            V qpeek = queue.peek();
            int index = vtsList.indexOf(qpeek);
            Vertex vertex = vertices.get(index);

            List<Edge> edges = vertex.getEdges();
            for (Edge edge : edges) {
                V temp = (V) edge.getTarget().getV();
                if (visited.get(temp) == false) {
                    queue.add(temp);
                    visited.put(origin, true);
                    visited.put(temp, true);
                }
            }
            found.add(queue.poll());

        }

        return found;
    }

    private void setVertex(Vertex v) {
        vertices.add(v);
        vtsList.add((V) v.getV());
    }

    @Override
    public IGraph<V> connectedGraph(V origin) {
        if (!contains(origin))
            throw new NoSuchElementException();

        DirectedGraph<V> newGraph = new DirectedGraph<>();
        for (V element : search(origin)) {
            int index = vtsList.indexOf(element);
            Vertex v = vertices.get(index);
            newGraph.setVertex(v);

        }

        System.out.println(newGraph.toString() + " connected graph");

        return newGraph;
    }
    class Vertex<V> implements Comparable<Vertex> {
        private final V name;
        private List<Edge> adjacencies;
        public int minDistance = Integer.MAX_VALUE;
        public Vertex previous;

        private Vertex(V argName) {
            name = argName;
            adjacencies = new ArrayList<Edge>();
        }

        public int compareTo(Vertex other) {
            return Integer.compare(minDistance, other.minDistance);
        }

        private void addEdge(V target, int weight) {
            Vertex newv = new Vertex(target);
            adjacencies.add(new Edge(newv, 1));
        }

        private void addEdge(Vertex target, int weight) {
            adjacencies.add(new Edge(target, 1));
        }

        private List<Edge> getEdges() {
            return adjacencies;
        }

        private V getV() {
            return name;
        }

        private int getMiniDistance() {
            return minDistance;

        }

        private void setMiniDistance(int minDistance) {
            this.minDistance = minDistance;

        }

        private void removeNeighbor(V destination) {
            Iterator<Edge> iter = getEdges().iterator();

            while (iter.hasNext()) {
                Edge edge = iter.next();
                if (edge.getTarget().getV().equals(destination))
                    iter.remove();
            }

        }
    }
    private class Edge {
        private final Vertex target;
        private final int weight;

        private Edge(Vertex argTarget, int argWeight) {
            target = argTarget;
            weight = argWeight;
        }

        private Vertex getTarget() {
            return target;
        }

        private int getWeight() {
            return weight;
        }
    }


}