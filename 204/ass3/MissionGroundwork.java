import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;


public class MissionGroundwork {

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        for (Project pr : projectList) {
            int[] schedule= pr.getEarliestSchedule();
            pr.printSchedule(schedule);
        }
    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();

        try {
            File fXmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList projects = doc.getElementsByTagName("Project");
            for (int temp = 0; temp < projects.getLength(); temp++){
                List<Task> taskList = new ArrayList<>();

                Node nNode = projects.item(temp);
                Element eElement = (Element) nNode;

                String projectName = eElement.getElementsByTagName("Name").item(0).getTextContent();

                NodeList tasks = eElement.getElementsByTagName("Task");
                for (int i = 0; i < tasks.getLength(); i++) {
                    Node tNode = tasks.item(i);
                    Element tElement = (Element) tNode;

                    int taskID = Integer.parseInt(tElement.getElementsByTagName("TaskID").item(0).getTextContent());
                    String description = tElement.getElementsByTagName("Description").item(0).getTextContent();
                    int duration = Integer.parseInt(tElement.getElementsByTagName("Duration").item(0).getTextContent());

                    NodeList dependencies = tElement.getElementsByTagName("DependsOnTaskID");
                    List<Integer> depsList = new ArrayList<>();
                    for (int j = 0; j < dependencies.getLength(); j++) {
                        Node dNode = dependencies.item(j);
                        depsList.add(Integer.valueOf(dNode.getTextContent()));
                    }

                    Task t = new Task(taskID, description, duration, depsList);
                    taskList.add(t);
                }
                
                Project p = new Project(projectName, taskList);
                projectList.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return projectList;
    }


}
