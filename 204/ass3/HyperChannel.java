import java.io.*;
import java.util.*;

public class HyperChannel implements Comparable<HyperChannel>{
    private Planet to;
    private Planet from;
    private Double cost;

    public HyperChannel(Planet to, Planet from, Double cost)  {
        this.to = to;
        this.from = from;
        this.cost = cost;
    }

    public Planet getTo() {
        return to;
    }

    public Planet getFrom() {
        return from;
    }

    public Double getWeight() {
        return cost;
    }

    public int compareTo(HyperChannel m)
    {
        return (int)(this.cost - m.getWeight());
    }

    @Override
    public String toString() {
        return "FROM " + this.getFrom() + " TO " + this.getTo() + " COST = " +getWeight();
    }
}
