import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Graph {
    public ArrayList<Node> nodes = new ArrayList<>();
    private Integer nbNodes = null;
    private Integer nbEdges = null;

    public Graph(String filePath) {
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            HashMap<String, Integer> nameToId = new HashMap<>();
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] edgeNames = line.split(" ");
                Node[] edgeNodes = {null, null};
                for (int x = 0; x <= 1; x++) {
                    int nodeIndex;
                    if (nameToId.containsKey(edgeNames[x])) {
                        nodeIndex = nameToId.get(edgeNames[x]);
                    } else {
                        nodeIndex = nodes.size();
                        nameToId.put(edgeNames[x], nodeIndex);
                        this.nodes.add(new Node(nodeIndex, edgeNames[x]));
                    }
                    edgeNodes[x] = this.nodes.get(nodeIndex);
                }
                
                edgeNodes[0].neighbours.add(edgeNodes[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  

    public Graph(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public Graph() {

    }

    public int getNbNodes() {
        if (this.nbNodes != null) {
            return this.nbNodes;
        } else {
            return (this.nbNodes = this.nodes.size());
        }
    }

    public int getNbEdges() {
        if (this.nbEdges != null) {
            return this.nbEdges;
        } else {
            this.nbEdges = 0;
            for (int i = 0; i < getNbNodes(); i++) {
                this.nbEdges += this.nodes.get(i).neighbours.size();
            }
            return (this.nbEdges /= 2);
        }
    }

    /**
     * Returns the density of the graph (Number of nodes divided by number of edges)
     * @return The density
     */
    public float getDensity() {
        return (float)getNbNodes() / (float)getNbEdges();
    }

    /**
     * Creates an induced subgraph of the current graph
     * @param nodesToKeep Nodes from the graph to keep in the subgraph
     * @return The induced subgraph 
     */
    public Graph subgraph(ArrayList<Node> nodesToKeep) {
        int NOT_IN_THE_GRAPH = -2;
        int NOT_YET_ADDED = -1;

        ArrayList<Node> subgraphNodes = new ArrayList<>();
        ArrayList<Integer> subgraphIds = new ArrayList<>();

        for (int i = 0; i < getNbNodes(); i++){
            subgraphIds.add(NOT_IN_THE_GRAPH);
        }
        for (int i = 0; i < nodesToKeep.size(); i++) {
            subgraphIds.set(nodesToKeep.get(i).id, NOT_YET_ADDED);
        }

        for (int i = 0; i < getNbNodes(); i++) {
            if (subgraphIds.get(i) != NOT_IN_THE_GRAPH) {
                Node u = this.nodes.get(i);
                if (subgraphIds.get(u.id) == NOT_YET_ADDED) {
                    subgraphIds.set(u.id, subgraphNodes.size());
                    subgraphNodes.add(new Node(subgraphNodes.size(), u.name));
                }
                for (int p = 0; p < u.neighbours.size(); p++) {
                    Node v = u.neighbours.get(p);
                    if (subgraphIds.get(v.id) == NOT_YET_ADDED) {
                        subgraphIds.set(v.id, subgraphNodes.size());
                        subgraphNodes.add(new Node(subgraphNodes.size(), v.name));
                    }
                    if(subgraphIds.get(v.id) != NOT_IN_THE_GRAPH) {
                        int si = subgraphIds.get(u.id);
                        int sj = subgraphIds.get(v.id);
                        Node su = subgraphNodes.get(si);
                        Node sv = subgraphNodes.get(sj);
                        su.neighbours.add(sv);
                    }
                }
            }
        }
        return new Graph(subgraphNodes);
    }

    /**
     * Computes a 2-approximation densest subgraph with a linear complexity
     * @return A 2-apporximation densest subgraph
     */
    public Graph approxDensestSubgraph() {
        int n = getNbNodes();
        int m = getNbEdges();

        ArrayList<Integer> subDegrees = new ArrayList<>();
        ArrayList<ArrayList<Integer>> subNodes = new ArrayList<>();
        ArrayList<ArrayList<Integer>> subNodesTracker = new ArrayList<>();
        ArrayList<ArrayList<Integer>> degreeToNodes = new ArrayList<>();
        ArrayList<Integer> degreeToNodesTracker = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Node u = this.nodes.get(i);
            int d = u.neighbours.size();
            subDegrees.add(d);
            subNodes.add(new ArrayList<>());
            subNodesTracker.add(new ArrayList<>());
            degreeToNodes.add(new ArrayList<>());
        }

        int minDegree = n;

        for (int i = 0; i < n; i++) {
            Node u = this.nodes.get(i);

            for (int p = 0; p < u.neighbours.size(); p++) {
                Node v = u.neighbours.get(p);
                int j = v.id;
                if (i < j) {
                    subNodesTracker.get(j).add(subNodes.get(i).size());
                    subNodes.get(i).add(j);
                    subNodesTracker.get(i).add(subNodes.get(j).size());
                    subNodes.get(j).add(i);
                }
            }

            int d = subDegrees.get(i);
            if (d < minDegree) minDegree = d;

            degreeToNodesTracker.add(degreeToNodes.get(d).size());
            degreeToNodes.get(d).add(i);
        }

        ArrayList<Integer> removed = new ArrayList<>();
        float maxDensity = getDensity();
        int selectedIteration = 0;

        while (n > 0) {
            ArrayList<Integer> nodesOfMinDegree = degreeToNodes.get(minDegree);
            int i = nodesOfMinDegree.remove(nodesOfMinDegree.size() - 1);
            removed.add(i);
            n--;
            for (int p = 0; p < subNodes.get(i).size(); p++){
                int j = subNodes.get(i).get(p);
                int neighboursCount = subNodes.get(j).size();

                if (subNodesTracker.get(i).get(p) < neighboursCount - 1) {
                    int k = subNodes.get(j).get(neighboursCount - 1);
                    subNodesTracker.get(k).set(subNodesTracker.get(j).get(neighboursCount - 1), subNodesTracker.get(i).get(p));
                    Collections.swap(subNodes.get(j), subNodesTracker.get(i).get(p), neighboursCount - 1);
                    Collections.swap(subNodesTracker.get(j), subNodesTracker.get(i).get(p), neighboursCount - 1);
                }
                subNodes.get(j).remove(neighboursCount - 1);
                m--;

                int d = subDegrees.get(j);
                int nodesWithSameDegreeCount = degreeToNodes.get(d).size();
                
                if (degreeToNodesTracker.get(j) != nodesWithSameDegreeCount - 1) {
                    int k = degreeToNodes.get(d).get(nodesWithSameDegreeCount - 1);
                    degreeToNodesTracker.set(k, degreeToNodesTracker.get(j));
                    Collections.swap(degreeToNodes.get(d), degreeToNodesTracker.get(j), nodesWithSameDegreeCount - 1);
                }
                degreeToNodes.get(d).remove(nodesWithSameDegreeCount - 1);

                subDegrees.set(j, d - 1);
                degreeToNodesTracker.set(j, degreeToNodes.get(d - 1).size());
                degreeToNodes.get(d - 1).add(j);
            }

            subDegrees.set(i, null);
            if (minDegree > 0) minDegree--;
            while (degreeToNodes.get(minDegree).size() == 0 && n > 0) {
                minDegree++;
            }

            if (m > 0) {
                float currentDensity = (float)m / (float)n;

                if (currentDensity > maxDensity) {
                    maxDensity = currentDensity;
                    selectedIteration = getNbNodes() - n;
                }
            }
        }

        ArrayList<Node> nodesToKeep = new ArrayList<>();
        for (int i = selectedIteration; i < getNbNodes(); i++){
            nodesToKeep.add(this.nodes.get(removed.get(i)));
        }

        return this.subgraph(nodesToKeep);
    }

    /**
     * Exports the graph as a .edges file.
     * @param path Path to the file into which the graph is exported.s
     */
    public void export(String path) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(path));
            for (int i = 0; i < getNbNodes(); i++) {
                Node u = this.nodes.get(i);
                for (int p = 0; p < u.neighbours.size(); p++) {
                    Node v = u.neighbours.get(p);
                    writer.append(String.format("%s %s\n", u.name, v.name));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {writer.close();} catch (IOException e) {e.printStackTrace();}
        }
    }
 
}
