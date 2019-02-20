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
}
