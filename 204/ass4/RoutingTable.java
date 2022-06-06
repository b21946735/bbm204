import java.io.Serializable;
import java.security.Key;
import java.util.*;

public class RoutingTable implements Serializable {

    static final long serialVersionUID = 99L;
    private final Router router;
    private final Network network;
    private final List<RoutingTableEntry> entryList;
    HashMap<String,Double> distTo;
    HashMap<String,String> edgeTo;
    public RoutingTable(Router router) {
        this.router = router;
        this.network = router.getNetwork();
        this.entryList = new ArrayList<>();
    }

    /**
     * updateTable() should calculate routing information and then instantiate RoutingTableEntry objects, and finally add
     * them to RoutingTable object’s entryList.
     */
    public void updateTable() {
        // TODO: YOUR CODE HERE
        Dijkstra dj = new Dijkstra(router.getIpAddress(), network);
        distTo = dj.distTo;
        edgeTo = dj.edgeTo;
        entryList.clear();
        for (String destIp : distTo.keySet()) {
            if (destIp.equals(router.getIpAddress())) {
                continue;
            }
            RoutingTableEntry entry = new RoutingTableEntry(router.getIpAddress(), destIp, pathTo(network.getRouterWithIp(destIp)));

            entryList.add(entry);
        }

    }




    /**
     * pathTo(Router destination) should return a Stack<Link> object which contains a stack of Link objects,
     * which represents a valid path from the owner Router to the destination Router.
     *
     * @param destination Destination router
     * @return Stack of links on the path to the destination router
     */
    public Stack<Link> pathTo(Router destination) {
        // TODO: YOUR CODE
        Stack<Link> stack = new Stack<>();
        String key = destination.getIpAddress();

        if (edgeTo.get(destination.getIpAddress()) == null) {
            return stack;
        }
        while (!key.equals(router.getIpAddress())) {

            
            stack.add(network.getLinkBetweenRouters(key, edgeTo.get(key)));
            key = edgeTo.get(key);


        }
        return stack;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutingTable that = (RoutingTable) o;
        return router.equals(that.router) && entryList.equals(that.entryList);
    }

    public List<RoutingTableEntry> getEntryList() {
        return entryList;
    }
}

class Dijkstra{
    HashMap<String,String> edgeTo;
    HashMap<String,Double> distTo;
    Pq pq;
    Network network;

    public Dijkstra( String key,Network net){
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();
        pq = new Pq();
        network = net;
        Map<String,ArrayList<String>> graph = net.graph;
       for (String i : graph.keySet()) {
            distTo.put(i, Double.POSITIVE_INFINITY);
       } 
       distTo.put(key, 0d);
       pq.insert(key, 0d);
       while (!pq.isEmpty()) {
           String v = pq.delMin();


           for (String str : graph.get(v)) {
               relax(v,str);
           }
       }

    }

    void relax(String v,String w){
        distTo.get(w);
        distTo.get(v);
        Link l = network.getLinkBetweenRouters(v, w);
        if(null == l){
            return;
        }
        if (distTo.get(w) > distTo.get(v) +l.getCost()) {
            distTo.put(w, distTo.get(v) + l.getCost());
            edgeTo.put(w, v);
            if (pq.contains(w)) {
                pq.decreaseKey(w,distTo.get(w));
            }else{
                pq.insert(w, distTo.get(w));
            }
        }
    }
}

class Pq{
    ArrayList<String> keys  = new ArrayList<>();
    ArrayList<Double> costs = new ArrayList<>();

    public void insert(String key,Double cost){
        keys.add(key);
        costs.add(cost);
    }

    public void decreaseKey(String key,Double cost){
        int i = 0;
        for (String str : keys) {
            if (str.equals(key)) {
                costs.set(i,cost);
                return;
            }
            i++;
        }
    }

    public boolean contains(String key){
        for (String str : keys) {
            if (str.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(){
        if (keys.size()==0) {
            return true;
        }
        return false;
    }

    public String delMin(){

        double min = costs.get(0);
        int index = 0;
        for (int i = 0; i < costs.size(); i++) {
            if (costs.get(i)< min) {
                min = costs.get(i);
                index = i;
            }
        }
        String ret = keys.get(index);
        costs.remove(index);
        keys.remove(index);
        return ret;
    }
}