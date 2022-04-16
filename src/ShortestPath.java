import java.util.ArrayList;

public class ShortestPath {
    ArrayList<Stop> stops;
    private double weight;

    public ShortestPath() {
        stops = new ArrayList<Stop>();
        weight = -1;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
}