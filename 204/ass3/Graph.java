import java.util.*;

public class Graph {
    HashMap<Integer,List<Integer>> adjList = new HashMap<>();
    int verticeCount = 0;

    Graph(List<Task> tasks){
        verticeCount = tasks.size();
        for (int i = 0; i < tasks.size(); i++) {
           adjList.put(i, new ArrayList<Integer>());
        }
        for (Task task : tasks) {
            List<Integer> depList = task.getDependencies();
            for (int dep : depList) {
                adjList.get(dep).add(task.getTaskID());
            }
            //adjList.put(task.getTaskID(),depList);
        }
    }

    public void printGraph(){
        for (int i : adjList.keySet()) {
            System.out.println(i + " : " + adjList.get(i));
        }
    }

    public List<Integer> DFS(){
        List<Integer> topologicalOrder = (new DirectedDFS(this, 0)).getTopologicalOrder();
        return topologicalOrder;
    }

    class DirectedDFS{
        private boolean[] marked;
        private List<Integer> topologicalOrder = new ArrayList<Integer>();
        public DirectedDFS(Graph G, int s)
        {
            marked = new boolean[G.verticeCount];
            dfs(G, s);
        }
        private void dfs(Graph G, int v)
        {
            marked[v] = true;
            for (int w : G.adjList.get(v))
                if (!marked[w]) dfs(G, w);
            topologicalOrder.add(v);
        }


        public List<Integer> getTopologicalOrder(){
            Collections.reverse(topologicalOrder);
            return topologicalOrder;
        }
    }
}

