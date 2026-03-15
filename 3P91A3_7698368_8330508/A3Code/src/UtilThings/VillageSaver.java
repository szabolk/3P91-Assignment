package UtilThings;

import Game.*;
import GameComponents.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class VillageSaver {
    //from the xml example code
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    /**
     * Used whenever we want to add a child to an already existing parent element
     * @param document - The overall document (DOM structure)
     * @param parent - The parent we want to add a child element to
     * @param tag - The XML tag associated with the element
     * @param value - The value we want to save
     */
    public static void addChildElement(Document document, Element parent, String tag, String value) {
        Element childElement = document.createElement(tag);
        childElement.appendChild(document.createTextNode(value));
        parent.appendChild(childElement);
    }

    /**
     * Once the player chooses to exit the game, the game will run this to save the state of the player's village before they quit, and then use the XML structure in the file to load the village when the
     * player decides to play again.
     * @param village - the player's village
     * @param filePath - the XML file where the player's village will be saved
     * @throws ParserConfigurationException - throws if an issue with DocumentBuilder or DocumentBuilderFactory
     * @throws TransformerException - throws if an error occurs during the transformer work
     */
    public static void villageToXML(Village village, String filePath) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        factory.setAttribute(JAXP_SCHEMA_SOURCE, "villageSchema.xsd");
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        //start the dom with village as the root element (everything a part of the village will be
        //added as child elements)
        Element rootElement = document.createElement("Village");
        document.appendChild(rootElement);

        addChildElement(document, rootElement, "villageHallLevel", String.valueOf(village.getVillageHall().getStats().level()));
        addChildElement(document, rootElement, "guardedUntil", String.valueOf(village.getGuardedUntil()));

        //saves the player's win/loss count for both attacking and defending
        Element playerAttackStats = document.createElement("playerAttackStats");
        rootElement.appendChild(playerAttackStats);
        addChildElement(document, playerAttackStats, "wins", String.valueOf(village.getOwner().getWinTotal()));
        addChildElement(document, playerAttackStats, "losses", String.valueOf(village.getOwner().getLossTotal()));

        Element playerDefenceStats = document.createElement("playerDefenceStats");
        rootElement.appendChild(playerDefenceStats);
        addChildElement(document, playerDefenceStats, "wins", String.valueOf(village.getOwner().getDefenseVictory()));
        addChildElement(document, playerDefenceStats, "losses", String.valueOf(village.getOwner().getDefenseLosses()));

        //saves the player resources
        Element resources = document.createElement("resources");
        rootElement.appendChild(resources);
        addChildElement(document, resources, "gold", String.valueOf(village.getResources().getGold()));
        addChildElement(document, resources, "iron", String.valueOf(village.getResources().getIron()));
        addChildElement(document, resources, "lumber", String.valueOf(village.getResources().getLumber()));

        //saves the player inhabitants
        Element inhabitants = document.createElement("inhabitants");
        rootElement.appendChild(inhabitants);
        for (Inhabitant inhabitant : village.getInhabitants()) {
            Element inhabitantNode = document.createElement("inhabitant");
            addChildElement(document, inhabitantNode, "type", String.valueOf(inhabitant.getEntityType()));
            addChildElement(document, inhabitantNode, "level", String.valueOf(inhabitant.getStats().level()));
            inhabitants.appendChild(inhabitantNode);
        }

        //saves the player buildings
        Element buildings = document.createElement("buildings");
        rootElement.appendChild(buildings);
        for (Building building : village.getBuildings()) {
            Element buildingNode = document.createElement("building");
            addChildElement(document, buildingNode, "type", String.valueOf(building.getEntityType()));
            addChildElement(document, buildingNode, "level", String.valueOf(building.getStats().level()));
            buildings.appendChild(buildingNode);
        }

        //save the build queue status before the player quits the game
        Element buildQueue = document.createElement("buildQueue");
        rootElement.appendChild(buildQueue);
        for (Village.QueueTask task : village.getBuildQueue()) {
            Element queueNode = document.createElement("task");
            addChildElement(document, queueNode, "type", String.valueOf(task.getType()));
            addChildElement(document, queueNode, "completionTime", String.valueOf(task.getCompletionTime()));
            addChildElement(document, queueNode, "buildingToUpgrade", String.valueOf(task.getExistingBuilding()));
            addChildElement(document, queueNode, "currentLevel", String.valueOf(task.getExistingBuilding().getStats().level()));
            buildQueue.appendChild(queueNode);
        }

        //save the train queue status before the player quits the game
        Element trainQueue = document.createElement("trainQueue");
        rootElement.appendChild(trainQueue);
        for (Village.QueueTask task : village.getTrainQueue()) {
            Element queueNode = document.createElement("task");
            addChildElement(document, queueNode, "type", String.valueOf(task.getType()));
            addChildElement(document, queueNode, "completionTime", String.valueOf(task.getCompletionTime()));
            trainQueue.appendChild(queueNode);
        }

        //actually put the saved info into a file through transformer
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer transformer = tfactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }
}
