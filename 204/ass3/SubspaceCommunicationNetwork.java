import java.text.DecimalFormat;
import java.util.*;


public class SubspaceCommunicationNetwork {

    private List<SolarSystem> solarSystems;

    /**
     * Perform initializations regarding your implementation if necessary
     * @param solarSystems a list of SolarSystem objects
     */
    public SubspaceCommunicationNetwork(List<SolarSystem> solarSystems) {
        // TODO: YOUR CODE HERE
        this.solarSystems = solarSystems;
    }

    /**
     * Using the solar systems of the network, generates a list of HyperChannel objects that constitute the minimum cost communication network.
     * @return A list HyperChannel objects that constitute the minimum cost communication network.
     */
    public List<HyperChannel> getMinimumCostCommunicationNetwork() {
        List<HyperChannel> minimumCostCommunicationNetwork = new ArrayList<>();
        List<String> channels = new ArrayList<>();
        Set<String> set = new HashSet<String>();
        Map<String,List<HyperChannel>> adj = new HashMap<>();


        for (SolarSystem slr : solarSystems) {
            for (SolarSystem slr2 : solarSystems) {
                String id = slr2.delegatePlanet.getId()+slr.delegatePlanet.getId();
                String id2 = slr.delegatePlanet.getId()+slr2.delegatePlanet.getId();
                if (slr.equals(slr2) || channels.contains(id2) ) {continue;}
                channels.add(id);

                int tech1 = slr.maxTech;
                int tech2 = slr2.maxTech;
                double cost = getCost(tech1,tech2);
                //cost  = (double)Math.round(cost * 1000000d) / 1000000d;
                
                //DecimalFormat df = new DecimalFormat("#.000000");
                //cost = Double.parseDouble(df.format(cost));
                Planet to;
                Planet from;
                int first = Integer.valueOf(slr.delegatePlanet.getId().substring(1));
                int second = Integer.valueOf(slr2.delegatePlanet.getId().substring(1));
                if (first<second ) {
                    to = slr2.delegatePlanet;
                    from = slr.delegatePlanet;
                }else{
                    from = slr2.delegatePlanet;
                    to = slr.delegatePlanet;
                }
                set.add(to.getId());
                set.add(from.getId());
                HyperChannel hyper =new HyperChannel(to, from, cost);
                minimumCostCommunicationNetwork.add(hyper);
            }
        }
        for (String s : set) {
            adj.put(s, new ArrayList<>());
        }

        for (HyperChannel h : minimumCostCommunicationNetwork) {

  
            List<HyperChannel> temp= adj.get(h.getFrom().getId());
            temp.add(h);
            adj.put(h.getFrom().getId(), temp);

            
            temp= adj.get(h.getTo().getId());
            temp.add(h);
            adj.put(h.getTo().getId(), temp);

        }

        //System.out.println(set);
        List<HyperChannel> mst = new  ArrayList<>();
        Collections.sort(minimumCostCommunicationNetwork, Comparator.comparing(HyperChannel::getWeight));
        /*
        for (String s : adj.keySet()) {
            System.out.println("current = "+ s);
            for (HyperChannel h : adj.get(s)) {
                System.out.println(h.getFrom() + "   " + h.getTo());
            }
            System.out.println("----------");
        }
        */
        //LazyPrimMST l = new LazyPrimMST(set, adj);
        //mst = l.mst();
        if (adj.containsKey(Galaxy.maxestTechPlanet)) {
            for (HyperChannel h : adj.get(Galaxy.maxestTechPlanet)) {
                mst.add(h);
            }
        }
        

        //Collections.sort(mst, Comparator.comparing(HyperChannel::getWeight));
        return mst;
    }



    private double getCost(int tech1, int tech2){
        double cost = Constants.SUBSPACE_COMMUNICATION_CONSTANT / ((tech1+tech2) / 2.0);
        return cost;
    }

    public void printMinimumCostCommunicationNetwork(List<HyperChannel> network) {
        double sum = 0;
        for (HyperChannel channel : network) {
            Planet[] planets = {channel.getFrom(), channel.getTo()};
            Arrays.sort(planets);
            System.out.printf("Hyperchannel between %s - %s with cost %f", planets[0], planets[1], channel.getWeight());
            System.out.println();
            sum += channel.getWeight();
        }
        System.out.printf("The total cost of the subspace communication network is %f.", sum);
        System.out.println();
    }
}

class LazyPrimMST{
    private Map<String,Boolean> marked ;

    private List<HyperChannel> mst;

    private PriorityQueue<HyperChannel> pq; 

    public LazyPrimMST(Set<String> set ,Map<String,List<HyperChannel>> adj )
    {
        pq = new PriorityQueue<>(new CustomIntegerComparator());
        mst =  new ArrayList<>(); 
        marked = new HashMap<>();
        String visited ="";
        for (String s : set) {
            marked.put(s, false);
            
        }
        visited = Galaxy.maxestTechPlanet;
        visit(visited,adj);

        while (!pq.isEmpty() && !(mst.size()>=set.size()-1)) // 
        {
            //System.out.println(pq);
            HyperChannel e = pq.poll();


            String v = e.getFrom().getId(), w = e.getTo().getId();

            if (marked.get(v) && marked.get(w)) continue;
            
            if (marked.get(v)) {
                visited = w;
            }else{ visited = v;}

            
            //mst.enqueue(e);
            marked.put(v, true);
            marked.put(w, true);
            mst.add(e);
            //if (!marked.get(v)) visit(v,adj);
            //if (!marked.get(w)) visit(w,adj);
            visit(visited, adj);

            
        }

        



    }
    private void visit(String v,Map<String,List<HyperChannel>> adj )
    {
        for (HyperChannel e : adj.get(v))
        {
            if(marked.get(e.getTo().getId()) && marked.get(e.getFrom().getId())){
                continue;
            }
            pq.add(e);
        }
    }
    /*
    private void visit(String v,Map<String,List<HyperChannel>> adj )
    {
        marked.put(v, true);
        for (HyperChannel e : adj.get(v))
        {
            String s = "";
            if (e.getTo().getId().equals(v)) {
                s = e.getFrom().getId();
            }
            else{
                s = e.getTo().getId();
            }
            if (!marked.get(s))
                pq.add(e);
        }
    }
    */
    public List<HyperChannel> mst()
    { return mst; }

    static class CustomIntegerComparator implements Comparator<HyperChannel> {
        @Override
        public int compare(HyperChannel o1, HyperChannel o2) {
            return o1.getWeight() > o2.getWeight() ? 1 : -1;
        }
    }
}