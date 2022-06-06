import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class XMLParser {
    /**
     * TODO: Parse the input XML file and return a dictionary as described in the assignment insturctions
     *
     * @param filename the input XML file
     * @return a dictionary as described in the assignment insturctions
     */
    public static Map<String, Malware> parse(String filename) {
        Map<String,Malware> malwares = new HashMap<String,Malware>();
        try {
            File inputFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("row");
            
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                Element eElement = (Element) nNode;
                String title = eElement.getElementsByTagName("title").item(0).getTextContent();
                String hash = eElement.getElementsByTagName("hash").item(0).getTextContent();
                int level = Integer.valueOf(eElement.getElementsByTagName("level").item(0).getTextContent());
                Malware mlw = new Malware(title, level, hash);
                malwares.put(hash, mlw);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return malwares;
    }
}
