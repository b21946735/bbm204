import java.util.*;

public class Galaxy {

    private final List<Planet> planets;
    private List<SolarSystem> solarSystems;
    public static int maxestTech = 0;
    public static String maxestTechPlanet = "";
    Map<String,Boolean> marked = new HashMap<>();
    Map<String,Integer> id = new HashMap<>();
    int count;

    public Galaxy(List<Planet> planets) {
        this.planets = planets;
        for (Planet planet : planets) {
            marked.put(planet.getId(), false);
        }
    }

    /**
     * Using the galaxy's list of Planet objects, explores all the solar systems in the galaxy.
     * Saves the result to the solarSystems instance variable and returns a shallow copy of it.
     *
     * @return List of SolarSystem objects.
     */
    public List<SolarSystem> exploreSolarSystems() {
        solarSystems = new ArrayList<>();
        count = 0;
        solarSystems.add(new SolarSystem());
        for (int i = 0; i < planets.size(); i++) {
            if (!marked.get(planets.get(i).getId())){
                
                
                dfs(planets.get(i).getNeighbors(),planets.get(i).getId());
                
                solarSystems.add(new SolarSystem());
                count++;
            }
        }
        solarSystems.remove(solarSystems.size()-1);
        return new ArrayList<>(solarSystems);
    }

    private void dfs(List<String> adj,String v){
        marked.put(v,true);
        id.put(v, count);
        SolarSystem slr= solarSystems.get(count);
        Planet p = getPlanetById(v);
        slr.addPlanet(p);
        if(slr.maxTech < p.getTechnologyLevel()){
            slr.maxTech = p.getTechnologyLevel();
            slr.delegatePlanet = p;
            if (maxestTech<slr.maxTech) {
                maxestTech = slr.maxTech;
                maxestTechPlanet = slr.delegatePlanet.getId();
            }
        }
        solarSystems.set(count, slr);
        for (String str : adj) {
            if (!marked.get(str)) {
                dfs(getPlanetById(str).getNeighbors(), str);
            }
        }


    }


    public List<SolarSystem> getSolarSystems() {
        return solarSystems;
    }

    // FOR TESTING
    public List<Planet> getPlanets() {
        return planets;
    }

    public  Planet getPlanetById(String planetId){
        for (Planet planet : planets) {
            if (planet.getId().equals(planetId) ) {
                return planet;
            }
        }
        return null;
    }

    // FOR TESTING
    public int getSolarSystemIndexByPlanetID(String planetId) {
        for (int i = 0; i < solarSystems.size(); i++) {
            SolarSystem solarSystem = solarSystems.get(i);
            if (solarSystem.hasPlanet(planetId)) {
                return i;
            }
        }
        return -1;
    }

    public void printSolarSystems(List<SolarSystem> solarSystems) {
        System.out.printf("%d solar systems have been discovered.%n", solarSystems.size());
        for (int i = 0; i < solarSystems.size(); i++) {
            SolarSystem solarSystem = solarSystems.get(i);
            List<Planet> planets = new ArrayList<>(solarSystem.getPlanets());
            Collections.sort(planets);
            System.out.printf("Planets in Solar System %d: %s", i + 1, planets);
            System.out.println();
        }
    }
}
