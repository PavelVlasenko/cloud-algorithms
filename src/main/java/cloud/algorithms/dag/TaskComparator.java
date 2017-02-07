package cloud.algorithms.dag;

import java.util.Comparator;

public class TaskComparator implements Comparator<Vertex>
{
    @Override
    public int compare(Vertex o1, Vertex o2)
    {
        if(o1.LST == o2.LST)
            return 0;
        return o1.LST < o2.LST ? -1 : 1;
    }
}