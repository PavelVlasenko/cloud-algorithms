package cloud.algorithms.app;

import cloud.algorithms.dag.Dag;
import cloud.algorithms.dag.Edge;
import cloud.algorithms.dag.Vertex;

import java.util.Set;

public class AppProcessor
{
    private Dag dag;
    private Set<Vertex> entryVertices;
    double wBar;
    double sf = 1; //change this value for other scenario

    public DagProcessor(Dag dag) {
        this.dag = dag;
    }

    private Set<Vertex> findEntryVertex()
    {
        //find V entry
        Set<Vertex> entryVertices = new HashSet<>();
        for(Vertex vertex : dag.vertices)
        {
            boolean hasPredecessors = false;
            for(Edge edge : dag.edges)
            {
                if(edge.endVertex == vertex.index)
                {
                    hasPredecessors = true;
                    break;
                }
            }
            if(!hasPredecessors)
            {
                entryVertices.add(vertex);
                //vertex.EST = vertex.averageExecutionTime;
            }
        }
        this.entryVertices = entryVertices;
        return entryVertices;
    }

    public void calculateEST()
    {
        Set<Vertex> procesedVertices = new HashSet<>(dag.vertices);
        Set<Vertex> currentVertices = findEntryVertex();
        Set<Vertex> bufferVertices = new HashSet<>();

        //set EST for entry vertices and remove from processedVertices
        for(Vertex v : currentVertices)
        {
            //v.EST = v.averageExecutionTime;
            procesedVertices.remove(v);
        }

        //while processedList has vertices, calculate EST
        while(procesedVertices.size() > 0)
        {
            currentVertices = findSuccessors(currentVertices);
            for(Vertex v : currentVertices)
            {
                if(defineEST(v))
                {
                    procesedVertices.remove(v);
                }
                else
                {
                    bufferVertices.add(v);
                }
            }

            for (Iterator<Vertex> iterator = bufferVertices.iterator(); iterator.hasNext(); )
            {
                Vertex v = iterator.next();
                if (defineEST(v))
                {
                    iterator.remove();
                    procesedVertices.remove(v);
                }
            }
        }
    }

    public void calculateLST()
    {
        Set<Vertex> procesedVertices = new HashSet<>(dag.vertices);
        Set<Vertex> currentVertices = findEndVertex();
        Set<Vertex> bufferVertices = new HashSet<>();

        //set LST for enf vertices and remove from processedVertices
        for(Vertex v : currentVertices)
        {
            v.LST = v.EST;
            procesedVertices.remove(v);
        }

        //while processedList has vertices, calculate EST
        while(procesedVertices.size() > 0)
        {
            currentVertices = findPredecessors(currentVertices);
            for(Vertex v : currentVertices)
            {
                if(defineLST(v))
                {
                    procesedVertices.remove(v);
                }
                else
                {
                    bufferVertices.add(v);
                }
            }

            for (Iterator<Vertex> iterator = bufferVertices.iterator(); iterator.hasNext(); )
            {
                Vertex v = iterator.next();
                if (defineLST(v))
                {
                    iterator.remove();
                    procesedVertices.remove(v);
                }
            }
        }
    }

    private Set<Vertex> findEndVertex()
    {
        //find V entry
        Set<Vertex> endVertices = new HashSet<>();
        for(Vertex vertex : dag.vertices)
        {
            boolean hasSuccessors = false;
            for(Edge edge : dag.edges)
            {
                if(edge.startVertex == vertex.index)
                {
                    hasSuccessors = true;
                    break;
                }
            }
            if(!hasSuccessors)
            {
                endVertices.add(vertex);
            }
        }
        return endVertices;
    }

    private Set<Vertex> findPredecessors(Set<Vertex> current)
    {
        Set<Vertex> predecessors = new HashSet<>();
        for(Vertex v : current)
        {
            for(Edge e : dag.edges)
            {
                if(e.endVertex == v.index)
                {
                    predecessors.add(dag.getVertexByIndex(e.startVertex));
                }
            }
        }
        return predecessors;
    }

    private Set<Vertex> findSuccessors(Set<Vertex> current)
    {
        Set<Vertex> successors = new HashSet<>();
        for(Vertex v : current)
        {
            for(Edge e : dag.edges)
            {
                if(e.startVertex == v.index)
                {
                    successors.add(dag.getVertexByIndex(e.endVertex));
                }
            }
        }
        return successors;
    }

    private boolean defineEST(Vertex vertex)
    {
        Set<Vertex> predecessors = findPredecessors(vertex);
        boolean isDefined = false;
        int maxEST = 0;
        for(Vertex v : predecessors)
        {
            if(v.EST == 0 & !entryVertices.contains(v))
            {
                return false;
            }
            else
            {
                if(maxEST < (v.EST + v.averageExecutionTime))
                {
                    maxEST = v.EST + v.averageExecutionTime;
                }
            }
        }
        vertex.EST = maxEST;
        return true;
    }

    private boolean defineLST(Vertex vertex)
    {
        Set<Vertex> successors = findSuccessors(vertex);
        boolean isDefined = false;
        int minLST = Integer.MAX_VALUE;
        for(Vertex v : successors)
        {
            if(v.LST == 0)
            {
                return false;
            }
            else
            {
                if(minLST > (v.LST - vertex.averageExecutionTime))
                {
                    minLST = (v.LST - vertex.averageExecutionTime);
                }
            }
        }
        vertex.LST = minLST;
        return true;
    }

    private Set<Vertex> findPredecessors(Vertex v)
    {
        Set<Vertex> predecessors = new HashSet<>();
        for(Edge e : dag.edges)
        {
            if(e.endVertex == v.index)
            {
                predecessors.add(dag.getVertexByIndex(e.startVertex));
            }
        }
        return predecessors;
    }

    private Set<Vertex> findSuccessors(Vertex v)
    {
        Set<Vertex> successors = new HashSet<>();
        for(Edge e : dag.edges)
        {
            if(e.startVertex == v.index)
            {
                successors.add(dag.getVertexByIndex(e.endVertex));
            }
        }
        return successors;
    }

    public void calculateTaskList()
    {
        List<Vertex> resultTaskList = new ArrayList<>();
        LinkedList<Vertex> stackS = new LinkedList<>();

        //Identify the most critical path nodes CN based on  value EST& LST = equal or 0
        for(Vertex v : dag.vertices)
        {
            if(v.EST == v.LST)
            {
                stackS.add(v);
            }
        }

        //Sorting stack S in the decreasing order of their LST
        Collections.sort(stackS, new TaskComparator());

        while(stackS.size() > 0)
        {
            Vertex vertex = stackS.peek();
            Set<Vertex> predecessors = findPredecessors(vertex);
            Set<Vertex> unstackedPredecessors = new HashSet<>();

            for(Vertex v : predecessors)
            {
                if (!stackS.contains(v) & !resultTaskList.contains(v))
                {
                    unstackedPredecessors.add(v);
                }
            }

            if(unstackedPredecessors.size() == 0) //if top(S) has unstacked immediate predecessors then
            {
                resultTaskList.add(stackS.poll());
            }
            else  //S ←the immediate predecessor with least LST
            {
                Vertex leastLST = null;
                int minLST = Integer.MAX_VALUE;
                for(Vertex v : unstackedPredecessors)
                {
                    if(v.LST < minLST)
                    {
                        leastLST = v;
                        minLST = v.LST;
                    }
                }
                stackS.addFirst(leastLST);
            }
        }

        dag.taskList = resultTaskList;
    }

}
