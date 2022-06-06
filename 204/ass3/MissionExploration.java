import java.util.ArrayList;
import java.util.List;
import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class MissionExploration {

    /**
     * Given a Galaxy object, prints the solar systems within that galaxy.
     * Uses exploreSolarSystems() and printSolarSystems() methods of the Galaxy object.
     *
     * @param galaxy a Galaxy object
     */
    public void printSolarSystems(Galaxy galaxy) {
        galaxy.exploreSolarSystems();
        galaxy.printSolarSystems(galaxy.getSolarSystems());
    }

    /**
     * TODO: Parse the input XML file and return a list of Planet objects.
     *
     * @param filename the input XML file
     * @return a list of Planet objects
     */
    public Galaxy readXML(String filename) {
        List<Planet> planetList = new ArrayList<>();
        try {
            File fXmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList planets = doc.getElementsByTagName("Planet");
            Map<String,List<String>> map = new HashMap<>();
            for (int temp = 0; temp < planets.getLength(); temp++){
                Node pNode = planets.item(temp);
                Element eElement = (Element) pNode;
                String id = eElement.getElementsByTagName("ID").item(0).getTextContent();
                int technologyLevel = Integer.valueOf(eElement.getElementsByTagName("TechnologyLevel").item(0).getTextContent());

                NodeList neighList = eElement.getElementsByTagName("PlanetID");

                
                List<String> curr2 = map.get(id);
                if(curr2 != null){

                }
                else{
                    map.put(id, new ArrayList<>());
                    curr2 = map.get(id);
                }
                for (int i = 0; i < neighList.getLength(); i++){
                    Node nNode = neighList.item(i);
                    curr2.add((nNode.getTextContent()));
                    List<String> curr = map.get(nNode.getTextContent());
                    if(curr != null){
                        curr.add(id);
                    }
                    else{
                        List<String> sa = new ArrayList<>();
                        sa.add(id);
                        map.put(nNode.getTextContent(), sa);
                    }
                   
                }

                Planet p = new Planet(id, technologyLevel, map.get(id));
                planetList.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Galaxy(planetList);
    }
}
