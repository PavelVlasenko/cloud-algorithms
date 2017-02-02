package cloud.algorithms.app;

import java.util.*;

public class Dag
{
    public Set<Vertex> vertices = new HashSet<Vertex>();
    public Set<Edge> edges = new HashSet<Edge>();

    public void showDag() {
        System.out.println("==================== Vertices   ================");
        for(Vertex vertex : vertices)
       {
           System.out.println(vertex);
       }

       System.out.println("==================== Edges ================");

       for(Edge edge : edges)
       {
           System.out.println(edge);
       }
    }
}
