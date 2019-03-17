package objs;

import model.Part;
import model.Solid;
import model.Topology;
import model.Vertex;
import transforms.Col;

import java.awt.*;

public class Arrow extends Solid {

    public Arrow() {


        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(3);

        //indexBuffer.add(0); //test pixel

        vertexBuffer.add(new Vertex(10,30,0));
        vertexBuffer.add(new Vertex(50,30,0));
        vertexBuffer.add(new Vertex(40,20,0));
        vertexBuffer.add(new Vertex(40,40,0));

        //test pixel
        //vertexBuffer.add(new Vertex(100,100,0, new Col(0,255,0)));

        partBuffer.add(new Part(Topology.Lines,1,0));
        partBuffer.add(new Part(Topology.Triangles,1,1));

        //test pixel
        //partBuffer.add(new Part(Topology.Points,1,0));

    }
}
