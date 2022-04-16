
import java.util.*;

public class Graph {
    // adjacency list for the graph
    ArrayList<AdjacencyListNode> adjacencyList;
    HashSet<Integer> uniqueStopIds;
    boolean invalid;

    public Graph(ArrayList<Stop> stopList, ArrayList<Transfer> transferList, ArrayList<StopTime> timesList) {
        adjacencyList = new ArrayList<AdjacencyListNode>();
        uniqueStopIds = new HashSet<Integer>();
        invalid = false;
        for (int i = 0; i < stopList.size(); i++) {
            if (!uniqueStopIds.contains(stopList.get(i).stop_id)) {
                adjacencyList.add(new AdjacencyListNode(stopList.get(i), stopList.get(i).stop_id));
                uniqueStopIds.add(stopList.get(i).stop_id);
            } else {
                System.out.println("Stop IDs need to be unique");
                invalid = true;
            }
        }
        if (!invalid) {
            adjacencyList.sort(new AdjacencyComparator());
            timesList.sort(new StopTimesComparator());
            addStopTimes(timesList);
            addTransfers(transferList);
        }
    }

    public ShortestPath getShortestPath(Stop start, Stop destination) {
        double[] distTo = new double[adjacencyList.size()];
        boolean[] relaxed = new boolean[adjacencyList.size()];
        Stop[] edgeTo = new Stop[adjacencyList.size()];
        int src = Collections.binarySearch(adjacencyList, new AdjacencyListNode(start, start.stop_id), new AdjacencyComparator());
        for (int i = 0; i < distTo.length; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
            relaxed[i] = false;
            edgeTo[i] = null;
        }
        distTo[src] = 0;
        for (int i = 0; i < distTo.length - 1; i++) {
            int minIndex = -1;
            boolean cont = false;
            double minDist = Double.POSITIVE_INFINITY;
            for (int j = 0; j < distTo.length; j++) {
                if (distTo[j] < minDist && !relaxed[j]) {
                    minDist = distTo[j];
                    minIndex = j;
                    cont = true;
                }
            }
            if (cont) {
                relaxed[minIndex] = true;
                LinkedList<LinkedListNode> getList = adjacencyList.get(minIndex).edges;
                for (LinkedListNode j = getList.pollFirst(); j != null; j = getList.pollFirst()) {
                    int nodeIndex = Collections.binarySearch(adjacencyList, new AdjacencyListNode(null, j.getStopId()),
                            new AdjacencyComparator());
                    if ((distTo[minIndex] + j.getWeight()) < distTo[nodeIndex]) {
                        distTo[nodeIndex] = distTo[minIndex] + j.weight;
                        edgeTo[nodeIndex] = adjacencyList.get(minIndex).stop;
                    }
                }
            }
        }
        return findShortestPath(destination, distTo, edgeTo);
    }

    private void addStopTimes(ArrayList<StopTime> timesList) {
        int currentTrip = -1;
        int previousStop = -1;
        for (int i = 0; i < timesList.size() && !invalid; i++) {
            StopTime newStopTime = timesList.get(i);
            if (newStopTime.getTripId() == currentTrip) {
                if (uniqueStopIds.contains(previousStop) && uniqueStopIds.contains(newStopTime.getStopId())) {
                    int index = Collections.binarySearch(adjacencyList, new AdjacencyListNode(null, previousStop),
                            new AdjacencyComparator());
                    AdjacencyListNode addEdge = adjacencyList.get(index);
                    addEdge.addEdge(newStopTime.getStopId(), 1);
                    adjacencyList.set(index, addEdge);
                } else {
                    System.out.println("There is an edge to a non existent stop");
                    invalid = true;
                }
            }
            else {
                currentTrip = newStopTime.getTripId();
            }
            previousStop = newStopTime.getStopId();
        }
    }

    private void addTransfers(ArrayList<Transfer> transferList) {
        for (int i = 0; i < transferList.size() && !invalid; i++) {
            Transfer newTransfer = transferList.get(i);
            if (uniqueStopIds.contains(newTransfer.getFromStopId())
                    && uniqueStopIds.contains(newTransfer.getToStopId())) {
                int index = Collections.binarySearch(adjacencyList, new AdjacencyListNode(null, newTransfer.getFromStopId()), new AdjacencyComparator());
                AdjacencyListNode addEdge = adjacencyList.get(index);
                if (newTransfer.getTransferType() == 0) {
                    addEdge.addEdge(newTransfer.getToStopId(), 2);
                }
                else {
                    addEdge.addEdge(newTransfer.getToStopId(), newTransfer.getTransferTime() / 100);
                }
                adjacencyList.set(index, addEdge);
            } else {
                System.out.println("Data contains an edge to or from a stop that doesn't exist");
                invalid = true;
            }
        }
    }

    private ShortestPath findShortestPath(Stop destination, double[] distTo, Stop[] edgeTo) {
        int destIndex = Collections.binarySearch(adjacencyList, new AdjacencyListNode(destination, destination.stop_id), new AdjacencyComparator());
        if (distTo[destIndex] != Double.POSITIVE_INFINITY) {
            ShortestPath shortestPath = new ShortestPath();
            shortestPath.setWeight(distTo[destIndex]);
            int pathIndex = destIndex;
            while (edgeTo[pathIndex] != null) {
                shortestPath.stops.add(edgeTo[pathIndex]);
                pathIndex = Collections.binarySearch(adjacencyList,
                        new AdjacencyListNode(edgeTo[pathIndex], edgeTo[pathIndex].stop_id), new AdjacencyComparator());
            }
            return shortestPath;
        }
        else
            return null;
    }

    private class LinkedListNode {
        private int stopId;
        private double weight;

        public LinkedListNode(int stopId, double weight) {
            this.stopId = stopId;
            this.weight = weight;
        }
        public int getStopId() {
            return stopId;
        }
        public double getWeight() {
            return weight;
        }
    }

    private class AdjacencyListNode {
        private int stopId;
        private Stop stop;
        LinkedList<LinkedListNode> edges;

        public AdjacencyListNode(Stop stop, int stopId) {
            this.stop = stop;
            this.stopId = stopId;
            edges = new LinkedList<LinkedListNode>();
        }
        public int getStopId() {
            return stopId;
        }
        public void addEdge(int stopId, double weight) {
            edges.add(new LinkedListNode(stopId, weight));
        }
    }

    private class AdjacencyComparator implements Comparator<AdjacencyListNode> {
        @Override
        public int compare(AdjacencyListNode node1, AdjacencyListNode node2) {
            return node1.getStopId() - node2.getStopId();
        }
    }
    private class StopTimesComparator implements Comparator<StopTime> {
        @Override
        public int compare(StopTime time1, StopTime time2) {
            if (time1.getTripId() != time2.getTripId()) {
                return time1.getTripId() - time2.getTripId();
            } else
                return time1.getStopSequence() - time2.getStopSequence();
        }
    }
}