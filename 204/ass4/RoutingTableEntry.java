import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;

public class RoutingTableEntry implements Serializable {
    static final long serialVersionUID = 88L;
    private String destinationIpAddr;
    private String nextRouterIpAddr;
    private String sourceIpAddr;
    private Stack<Link> fullPath;
    private double totalRouteCost;

    public RoutingTableEntry(String sourceIpAddr, String destinationIpAddr, Stack<Link> fullPath) {
        if (fullPath != null) {
            this.destinationIpAddr = destinationIpAddr;
            this.sourceIpAddr = sourceIpAddr;
            this.fullPath = fullPath;
            this.totalRouteCost = calculateTotalRouteCost(fullPath);
            this.nextRouterIpAddr = !fullPath.isEmpty() ? fullPath.peek().getOtherIpAddress(sourceIpAddr) : null;
        }
    }

    double calculateTotalRouteCost(Stack<Link> fullPath) {
        if (!fullPath.isEmpty()) {
            double cost = 0f;
            for (Link link : fullPath) {
                cost += link.getCost();
            }
            return cost;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    public String getSourceIpAddr() {
        return sourceIpAddr;
    }

    public String getDestinationIpAddr() {
        return destinationIpAddr;
    }
    public double getTotalRouteCost() {
        return totalRouteCost;
    }

    public Stack<Link> getFullPath() {
        return fullPath;
    }

    @Override
    public String toString() {
        return "RoutingTableEntry{" +
                "destinationIpAddr='" + destinationIpAddr + '\'' +
                ", nextRouterIpAddr='" + nextRouterIpAddr + '\'' +
                ", totalRouteCost=" + totalRouteCost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutingTableEntry entry = (RoutingTableEntry) o;
        boolean sameCosts = ((totalRouteCost == Double.POSITIVE_INFINITY && entry.totalRouteCost == Double.POSITIVE_INFINITY)
                || (Math.abs(entry.totalRouteCost - totalRouteCost) <= 0.0001));
        return sameCosts && destinationIpAddr.equals(entry.destinationIpAddr) && sourceIpAddr.equals(entry.sourceIpAddr);
    }

}
