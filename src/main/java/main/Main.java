package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class Comparator implements java.util.Comparator<Vertex> {

    @Override
    public int compare(Vertex o1, Vertex o2) {

        if (o1.getDistance() < o2.getDistance()) {
            return -1;
        }
        if (o1.getDistance() > o2.getDistance()) {
            return 1;
        }
        return 0;
    }
}

class Vertex {
    private final int label;
    private double distance;
    private final List<Edge> edgeList;

    public Vertex(int label) {
        this.label = label;
        this.distance = Double.POSITIVE_INFINITY;
        this.edgeList = new ArrayList<>(1000);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getLabel() {
        return label;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }
}

class Edge {
    private Vertex adjVertex;
    private double edgeWeight;

    public double getEdgeWeight() {
        return edgeWeight;
    }

    public void setEdgeWeight(double edgeWeight) {
        this.edgeWeight = edgeWeight;
    }

    public Vertex getNameVertexGoes() {
        return adjVertex;
    }

    public void setNameVertexGoes(Vertex nameVertexGoes) {
        this.adjVertex = nameVertexGoes;
    }
}

class Graph {
    private final List<Vertex> vertexList;
    private final PriorityQueue<Vertex> vertexPriorityQueue;

    public Graph() {
        this.vertexList = new ArrayList<>(1000);
        this.vertexPriorityQueue = new PriorityQueue<>(1000, new Comparator());
    }

    public List<Vertex> getVertexList() {
        return vertexList;
    }

    public void addVertex(Vertex v) {
        this.vertexList.add(v);
    }

    public void showGraph(int finalVertex) {
        for (Vertex v : this.vertexList) {
            if (v.getLabel() == finalVertex) {
                if (v.getDistance() == Double.POSITIVE_INFINITY) {

                    System.out.println("Nao e possivel entregar a carta");
                } else
                    System.out.println((int) v.getDistance());
            }
        }
    }

    public void insertEdge(Vertex vertexGoesEdge, Vertex vertexReceiveEdge, double weight) {
        Edge a = new Edge();
        a.setNameVertexGoes(vertexReceiveEdge);
        a.setEdgeWeight(weight);
        this.vertexList.get(vertexGoesEdge.getLabel() - 1).getEdgeList().add(a);

        for (int i = 0; i < this.vertexList.get(vertexGoesEdge.getLabel() - 1).getEdgeList().size(); i++) {

            if (this.vertexList.get(vertexGoesEdge.getLabel() - 1).getEdgeList().get(i).getNameVertexGoes()
                    .getLabel() == vertexReceiveEdge.getLabel()) {

                for (int j = 0; j < vertexReceiveEdge.getEdgeList().size(); j++) {

                    if (vertexReceiveEdge.getEdgeList().get(j).getNameVertexGoes().getLabel() == this.vertexList
                            .get(vertexGoesEdge.getLabel() - 1).getLabel()) {

                        vertexReceiveEdge.getEdgeList().get(j).setEdgeWeight(0);
                        vertexGoesEdge.getEdgeList().get(i).setEdgeWeight(0);
                        break;
                    }
                }
            }
        }
    }

    private boolean vertexFound(int vertexLabel) {
        for (int i = 0; i < this.getVertexList().size(); i++) {

            if (this.getVertexList().get(i).getLabel() == vertexLabel) {
                this.getVertexList().get(i).setDistance(0);
                vertexPriorityQueue.add(this.getVertexList().get(i));
                return true;
            }
        }
        return false;
    }

    private void BFS(Vertex v) {
        v.getEdgeList().forEach(edge -> {
            double sum;
            sum = v.getDistance() + edge.getEdgeWeight();
            if (sum < edge.getNameVertexGoes().getDistance()) {
                edge.getNameVertexGoes().setDistance(sum);
                this.vertexPriorityQueue.add(edge.getNameVertexGoes());
            }
        });
    }

    public void dijkstra(int labelInitialVertex) {
        if (vertexFound(labelInitialVertex)) {
            while (!this.vertexPriorityQueue.isEmpty()) {
                BFS(Objects.requireNonNull(vertexPriorityQueue.poll()));
            }
        }
    }
}

public class Main {

    public static void main(String[] args) throws NumberFormatException, IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str;

        Graph g = new Graph();
        int numberOfVertex, numberOfEdges;
        List<Vertex> vertexList = new ArrayList<>(1000);

        str = in.readLine();
        numberOfVertex = Integer.parseInt(str.split(" ")[0]);
        numberOfEdges = Integer.parseInt(str.split(" ")[1]);

        while (numberOfVertex != 0 || numberOfEdges != 0) {

            receiveVertex(g, numberOfVertex, vertexList);

            receiveEdges(in, g, numberOfEdges, vertexList);

            executeDijkstra(in, g);

            str = in.readLine();
            numberOfVertex = Integer.parseInt(str.split(" ")[0]);
            numberOfEdges = Integer.parseInt(str.split(" ")[1]);

            vertexList.clear();
            g = new Graph();
            System.out.print("\n");
        }
    }

    private static void executeDijkstra(BufferedReader in, Graph g) throws IOException {
        String str3;
        String str4;
        str3 = in.readLine();
        int amountOfSearches = Integer.parseInt(str3.split(" ")[0]);
        int a, b;

        for (int i = 0; i < amountOfSearches; i++) {

            str4 = in.readLine();
            a = Integer.parseInt(str4.split(" ")[0]);
            b = Integer.parseInt(str4.split(" ")[1]);

            g.dijkstra(a);
            g.showGraph(b);

            for (int j = 0; j < g.getVertexList().size(); j++) {
                g.getVertexList().get(j).setDistance(Double.POSITIVE_INFINITY);
            }
        }
    }

    private static void receiveEdges(BufferedReader in, Graph g, int amountOfEdges, List<Vertex> vertexList) throws IOException {
        int auxEdgeGoes;
        String str2;
        int auxEdgeReceives;
        int auxEdgeWeight;
        Vertex auxVertex1 = null, auxVertex2 = null;

        for (int i = 0; i < amountOfEdges; i++) {
            str2 = in.readLine();

            auxEdgeGoes = Integer.parseInt(str2.split(" ")[0]);
            auxEdgeReceives = Integer.parseInt(str2.split(" ")[1]);
            auxEdgeWeight = Integer.parseInt(str2.split(" ")[2]);

            for (Vertex vertex : vertexList) {

                if (auxEdgeGoes == vertex.getLabel()) {
                    auxVertex1 = vertex;
                }
                if (auxEdgeReceives == vertex.getLabel()) {
                    auxVertex2 = vertex;
                }
            }
            g.insertEdge(auxVertex1, auxVertex2, auxEdgeWeight);
        }
    }

    private static void receiveVertex(Graph g, int amountOfVertex, List<Vertex> vertexList) {
        for (int i = 0; i < amountOfVertex; i++) {
            Vertex v = new Vertex(i + 1);
            vertexList.add(v);
            g.addVertex(v);
        }
    }
}