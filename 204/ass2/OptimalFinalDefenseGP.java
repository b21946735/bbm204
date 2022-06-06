import java.util.ArrayList;
import java.util.Collections;

/**
 * This class accomplishes Mission Exterminate
 */
public class OptimalFinalDefenseGP
{
    private ArrayList<Integer> bombWeights;

    public OptimalFinalDefenseGP(ArrayList<Integer> bombWeights) {
        this.bombWeights = bombWeights;
    }

    public ArrayList<Integer> getBombWeights() {
        return bombWeights;
    }

    /**
     *
     * @param maxNumberOfAvailableAUAVs the maximum number of available AUAVs to be loaded with bombs
     * @param maxAUAVCapacity the maximum capacity of an AUAV
     * @return the minimum number of AUAVs required using first fit approach over reversely sorted items.
     * Must return -1 if all bombs can't be loaded onto the available AUAVs
     */
    public int getMinNumberOfAUAVsToDeploy(int maxNumberOfAvailableAUAVs, int maxAUAVCapacity)
    {
        Collections.sort(bombWeights, Collections.reverseOrder());
        ArrayList<Integer> remainingSpaces = new ArrayList<>();
        for (int i = 0; i < maxNumberOfAvailableAUAVs; i++) {
            remainingSpaces.add(maxAUAVCapacity);
        }
        int maxJ = 0;

        for (int i = 0; i < bombWeights.size(); i++) {
            int currentBomb = bombWeights.get(i);
            int j = 0;
            for (j = 0; j < remainingSpaces.size(); j++) {
                if (j>maxJ) {
                    maxJ = j;
                }
                if (currentBomb<= remainingSpaces.get(j)) {
                    remainingSpaces.set(j,remainingSpaces.get(j)-currentBomb);
                    break;
                }
                
            }
            if(j==maxNumberOfAvailableAUAVs)return -1;
        }
        maxJ +=1;
        return maxJ;
    }
    public void printFinalDefenseOutcome(int maxNumberOfAvailableAUAVs, int AUAV_CAPACITY){
        int minNumberOfAUAVsToDeploy = this.getMinNumberOfAUAVsToDeploy(maxNumberOfAvailableAUAVs, AUAV_CAPACITY);
        if(minNumberOfAUAVsToDeploy!=-1) {
            System.out.println("The minimum number of AUAVs to deploy for complete extermination of the enemy army: " + minNumberOfAUAVsToDeploy);
        }
        else{
            System.out.println("We cannot load all the bombs. We are doomed.");
        }
    }
}
