package objs;

import model.Part;
import model.Solid;
import model.Topology;
import model.Vertex;

public class Arrow extends Solid {

    public Arrow() {

        indexBuffer.add(0); //0,1,1,2,3

        vertexBuffer.add(new Vertex());
        vertexBuffer.add(new Vertex());
        vertexBuffer.add(new Vertex());
        vertexBuffer.add(new Vertex());
        vertexBuffer.add(new Vertex());

        partBuffer.add(new Part(Topology.Lines,1,0));
        partBuffer.add(new Part(Topology.Triangles,1,2));

    }
}
