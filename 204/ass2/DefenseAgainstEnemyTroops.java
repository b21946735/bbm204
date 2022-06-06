import java.util.ArrayList;

/**
 * This class accomplishes Mission Nuke'm
 */
public class DefenseAgainstEnemyTroops {
    private ArrayList<Integer> numberOfEnemiesArrivingPerHour;

    public DefenseAgainstEnemyTroops(ArrayList<Integer> numberOfEnemiesArrivingPerHour){
        this.numberOfEnemiesArrivingPerHour = numberOfEnemiesArrivingPerHour;
    }

    public ArrayList<Integer> getNumberOfEnemiesArrivingPerHour() {
        return numberOfEnemiesArrivingPerHour;
    }

    private int getRechargedWeaponPower(int hoursCharging){
        return hoursCharging*hoursCharging;
    }

    /**
     *     Function to implement the given dynamic programming algorithm
     *     SOL(0) <- 0
     *     HOURS(0) <- [ ]
     *     For{j <- 1...N}
     *         SOL(j) <- max_{0<=i<j} [ (SOL(i) + min[ E(j), P(j − i) ] ]
     *         HOURS(j) <- [HOURS(i), j]
     *     EndFor
     *
     * @return OptimalEnemyDefenseSolution
     */
    public OptimalEnemyDefenseSolution getOptimalDefenseSolutionDP(){
        // TODO: YOUR CODE HERE
        numberOfEnemiesArrivingPerHour.add(0, 0);
        ArrayList<ArrayList<Integer>> hours = new ArrayList<>(); // holds optimal fire hour sequence for every hour
        ArrayList<Integer> sol = new ArrayList<>(); // holds max solution for every hour
        sol.add(0);
        int max = 0;
        hours.add(new ArrayList<>());
        for (int j = 1; j < numberOfEnemiesArrivingPerHour.size(); j++) {
            int k = 0; // if there max change in an hour more than once k holds i index of max of them 
            for (int i = 0; i < j; i++) {
                if (sol.get(i) + min(numberOfEnemiesArrivingPerHour.get(j),getRechargedWeaponPower(j-i)) > max) {
                    max = sol.get(i) + min(numberOfEnemiesArrivingPerHour.get(j),getRechargedWeaponPower(j-i));

                    //System.out.println("i ="+i + " j="+ j  + " max=" +max);
                    k =i;
                }
            }
            hours.add((ArrayList)hours.get(k).clone());
            hours.get(j).add(j);
            //System.out.println("------------");
            
            sol.add(max);
        }
        /*
        for (ArrayList<Integer> h : hours) {
            System.out.println(h);
        }
        */
        return new OptimalEnemyDefenseSolution(max,hours.get(hours.size()-1));
    }

    public static int min(int fir , int seco){
        if(fir<=seco) return fir;
        return seco;
    }
}
