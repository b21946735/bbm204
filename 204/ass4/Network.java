import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;


public class Network implements Serializable {
    static final long serialVersionUID = 55L;
    private List<Router> routers = new ArrayList<>();
    private List<Link> links = new ArrayList<>();
    public Map<String,ArrayList<String>> graph = new HashMap<>();

    /**
     * The constructor should read the given file and generate necessary Router and Link objects and initialize link
     * and router arrays.
     * Also, you should implement Link class’s calculateAndSetCost() method in order for the costs to be calculated
     * based on the formula given in the instructions.
     *
     * @param filename Input file to generate the network from
     * @throws FileNotFoundException
     */
    public Network(String filename) throws FileNotFoundException {
        // TODO: YOUR CODE HERE
        File file = new File(filename);
        String fileContent="";
        try(FileReader fr = new FileReader(file))
        {
            char[] chars = new char[(int) file.length()];
            fr.read(chars);
 
            fileContent = new String(chars);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String regexp = "RouterIP:[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
            Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(fileContent);
        while (matcher.find())
        {
            String ipAddress =matcher.group().split(":")[1];
            routers.add(new Router(ipAddress, this));
        }

        for (Router r : routers) {
            graph.put(r.getIpAddress(), new ArrayList<>());
        }

        regexp = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}-[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3} Bandwidth:[0-9]*";
        pattern = Pattern.compile(regexp);
        matcher = pattern.matcher(fileContent);
        while (matcher.find())
        {
            String[] line = matcher.group().split(" ");
            String[] first =line[0].split("-");
            String second = line[1].split(":")[1];
            String ipAddress1 =first[0];
            String ipAddress2 =first[1];
            int bandwidthInMbps =  Integer.valueOf(second);
            links.add( new Link(ipAddress1, ipAddress2, bandwidthInMbps));
            Router r1 = getRouterWithIp(ipAddress1);
            Router r2 = getRouterWithIp(ipAddress2);
            ArrayList<String> temp = graph.get(r1.getIpAddress());
            temp.add(r2.getIpAddress());
            graph.put(r1.getIpAddress(), temp);

            temp = graph.get(r2.getIpAddress());
            temp.add(r1.getIpAddress());
            graph.put(r2.getIpAddress(), temp);
        }
        updateAllRoutingTables();
    }

    /**
     * IP address of the router should be placed in group 1
     * Subnet of the router should be placed group 2
     *
     * @return regex for matching RouterIP lines
     */
    public static String routerRegularExpression() {
        // TODO: REGEX HERE
        
        return "((?:[0-9]{1,3})\\.(?:[0-9]{1,3})\\.([0-9]{1,3})\\.(?:[0-9]{1,3}))";
    }

    /**
     * IP address of the router 1 should be placed in group 1
     * IP address of the router 2 should be placed in group 2
     * Bandwidth of the link should be placed in group 3
     *
     * @return regex for matching Link lines
     */
    public static String linkRegularExpression() {  
        // TODO: REGEX HERE
        return "((?:[0-9]{1,3})\\.(?:[0-9]{1,3})\\.(?:[0-9]{1,3})\\.(?:[0-9]{1,3}))-((?:[0-9]{1,3})\\.(?:[0-9]{1,3})\\.(?:[0-9]{1,3})\\.(?:[0-9]{1,3})).*([0-9]{3,4})";
    }

    public List<Router> getRouters() {
        return routers;
    }

    public List<Link> getLinks() {
        return links;
    }

    public List<RoutingTable> getRoutingTablesOfAllRouters() {
        if (routers != null) {
            List<RoutingTable> routingTableList = new ArrayList<>();
            for (Router router : routers)
                routingTableList.add(router.getRoutingTable());
            return routingTableList;
        }
        return null;
    }

    public Router getRouterWithIp(String ip) {
        if (routers != null) {
            for (Router router : routers) {
                if (router.getIpAddress().equals(ip))
                    return router;
            }
        }
        return null;
    }

    public Link getLinkBetweenRouters(String ipAddr1, String ipAddr2) {
        if (links != null) {
            for (Link link : links) {
                if (link.getIpAddress1().equals(ipAddr1) && link.getIpAddress2().equals(ipAddr2)
                        || link.getIpAddress1().equals(ipAddr2) && link.getIpAddress2().equals(ipAddr1))
                    return link;
            }
        }
        return null;
    }

    public List<Link> getLinksOfRouter(Router router) {
        List<Link> routersLinks = new ArrayList<>();
        if (links != null) {
            for (Link link : links) {
                if (link.getIpAddress1().equals(router.getIpAddress()) ||
                        link.getIpAddress2().equals(router.getIpAddress())) {
                    routersLinks.add(link);
                }
            }
        }
        return routersLinks;
    }

    public void updateAllRoutingTables() {
        for (Router router : getRouters()) {
            router.getRoutingTable().updateTable();
            
        }
    }

    /**
     * Changes the cost of the link with a new value, and update all routing tables.
     *
     * @param link    Link to update
     * @param newCost New link cost
     */
    public void changeLinkCost(Link link, double newCost) {
        // TODO: YOUR CODE HERE
        link.setCost(newCost);        
        updateAllRoutingTables();
    }

    /**
     * Add a new Link to the Network, and update all routing tables.
     *
     * @param link Link to be added
     */
    public void addLink(Link link) {
        // TODO: YOUR CODE HERE
        links.add(link);
        String ip1 = link.getIpAddress1();
        String ip2 = link.getIpAddress2();
        ArrayList<String> temp = graph.get(ip1);
        temp.add(ip2);
        graph.put(ip1, temp);

        temp = graph.get(ip2);
        temp.add(ip1);
        graph.put(ip2, temp);
        updateAllRoutingTables();
    }

    /**
     * Remove a Link from the Network, and update all routing tables.
     *
     * @param link Link to be removed
     */
    public void removeLink(Link link) {
        // TODO: YOUR CODE HERE
        links.remove(link);
        String ip1 = link.getIpAddress1();
        String ip2 = link.getIpAddress2();
        ArrayList<String> temp = graph.get(ip1);
        temp.remove(ip2);
        graph.put(ip1, temp);
        temp = graph.get(ip2);
        temp.remove(ip1);
        graph.put(ip2, temp);
        updateAllRoutingTables();
    }

    /**
     * Add a new Router to the Network, and update all routing tables.
     *
     * @param router Router to be added
     */
    public void addRouter(Router router) {
        // TODO: YOUR CODE HERE
        this.routers.add(router);
        graph.put(router.getIpAddress(), new ArrayList<>());
        //System.out.println(graph);
        updateAllRoutingTables();
    }

    /**
     * Remove a Router from the Network, and update all routing tables. Beware that removing a router also causes the
     * removal of any links connected to it from the Network. Also beware that a router which was removed should not
     * appear in any routing table entry.
     *
     * @param router Router to be removed
     */
    public void removeRouter(Router router) {
        // TODO: YOUR CODE HERE
        String ip = router.getIpAddress();
        
        for (String r : graph.keySet()) {
            ArrayList<String> temp = graph.get(r);
            temp.remove(ip);
            graph.put(r, temp);
        }
        graph.remove(ip);
        for(Link li: getLinksOfRouter(router)){             
            links.remove(li);         
        }        
        routers.remove(router);



        updateAllRoutingTables();
    }

    /**
     * Change the state of the router (down or live), and update all routing tables. Beware that a router which is down
     * should not be reachable and should not appear in any routing table entry’s path. However, this router might appear
     * in other router’s routing-tables as a separate entry with a totalRouteCost=Infinity value because it was not
     * completely removed from the network.
     *
     * @param router Router to update
     * @param isDown New status of the router
     */
    public void changeStateOfRouter(Router router, boolean isDown) {
        // TODO: YOUR CODE HERE
    }

}

