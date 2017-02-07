package cloud.algorithms.dag;

import java.util.*;

public class Dag
{
    public List<Vertex> vertices = new ArrayList<Vertex>();
    public List<Edge> edges = new ArrayList<Edge>();

    public List<Vertex> taskList;

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

    public Vertex getVertexByIndex(int index)
    {
        for(Vertex v : vertices)
        {
            if(v.index == index)
            {
                return v;
            }
        }
        return null;
    }
}
