package model;

public class Part {

    private final Topology topology;
    private final int count;
    private final int start;

    public Part(Topology topology, int count, int start) {
        this.topology = topology;
        this.count = count;
        this.start = start;
    }

    public Topology getTopology() {
        return topology;
    }

    public int getCount() {
        return count;
    }

    public int getStart() {
        return start;
    }
}
