package UtilThings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import Game.Village;
import Game.VillageBuilderDirector;
import GameComponents.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class VillageLoader {
    public static Village XMLtoVillage(String filename) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(filename));

        List<Building> buildings = new ArrayList<>();
        List<Inhabitant> inhabitants = new ArrayList<>();
        List<ArmyUnit> army = new ArrayList<>();
        List<DefenceBuilding> defences = new ArrayList<>();
        List<Village.QueueTask> buildQueue = new ArrayList<>();
        List<Village.QueueTask> trainQueue = new ArrayList<>();

        int hallLevel = Integer.parseInt(document.getElementsByTagName("villageHallLevel").item(0).getTextContent());
        long guardedUntil = Long.parseLong(document.getElementsByTagName("guardedUntil").item(0).getTextContent());

        Element attackNode = (Element) document.getElementsByTagName("playerAttackStats").item(0);
        int attackWins = Integer.parseInt(attackNode.getElementsByTagName("wins").item(0).getTextContent());
        int attackLosses = Integer.parseInt(attackNode.getElementsByTagName("losses").item(0).getTextContent());

        Element defenceNode = (Element) document.getElementsByTagName("playerDefenceStats").item(0);
        int defenceWins = Integer.parseInt(defenceNode.getElementsByTagName("wins").item(0).getTextContent());
        int defenceLosses = Integer.parseInt(defenceNode.getElementsByTagName("losses").item(0).getTextContent());

        Element resElem = (Element) document.getElementsByTagName("resources").item(0);
        int gold = Integer.parseInt(resElem.getElementsByTagName("gold").item(0).getTextContent());
        int iron = Integer.parseInt(resElem.getElementsByTagName("iron").item(0).getTextContent());
        int lumber = Integer.parseInt(resElem.getElementsByTagName("lumber").item(0).getTextContent());

        loadInhabitants(document, inhabitants, army);
        loadBuildings(document, buildings, defences);
        loadBuildQueue(document, buildings, buildQueue);
        loadTrainQueue(document, trainQueue);


        return VillageBuilderDirector.buildLoadedVillage(buildings, inhabitants, army, defences,
                buildQueue, trainQueue,
                gold, iron, lumber,
                hallLevel, guardedUntil,
                attackWins, attackLosses, defenceWins, defenceLosses);
    }

    private static void loadInhabitants(Document document, List<Inhabitant> inhabitants, List<ArmyUnit> army) {
        NodeList savedInhabitants = document.getElementsByTagName("inhabitant");
        for (int i = 0; i < savedInhabitants.getLength(); i++) {
            Node node = savedInhabitants.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                EntityType type = EntityType.valueOf(elem.getElementsByTagName("type").item(0).getTextContent());
                int level = Integer.parseInt(elem.getElementsByTagName("level").item(0).getTextContent());
                Inhabitant newInhabitant = EntityFactory.createNewInhabitant(type);
                newInhabitant.setStats(EntityLevelData.getLevels(type).get(level - 1));
                inhabitants.add(newInhabitant);
                if (newInhabitant instanceof ArmyUnit) {
                    army.add((ArmyUnit) newInhabitant);
                }
            }
        }
    }

    private static void loadBuildings(Document document, List<Building> buildings, List<DefenceBuilding> defences) {
        NodeList savedBuildings = document.getElementsByTagName("building");
        for (int i = 0; i < savedBuildings.getLength(); i++) {
            Node node = savedBuildings.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;

                EntityType type = EntityType.valueOf(elem.getElementsByTagName("type").item(0).getTextContent());
                int level = Integer.parseInt(elem.getElementsByTagName("level").item(0).getTextContent());

                Building newBuilding = EntityFactory.createNewBuilding(type);
                newBuilding.setStats(EntityLevelData.getLevels(type).get(level - 1));

                buildings.add(newBuilding);

                if (newBuilding instanceof DefenceBuilding) {
                    defences.add((DefenceBuilding) newBuilding);
                }
            }
        }
    }

    private static void loadBuildQueue(Document document, List<Building> buildings, List<Village.QueueTask> buildQueue) {
        Node buildQueueNode = document.getElementsByTagName("buildQueue").item(0);
        if (buildQueueNode != null && buildQueueNode.getNodeType() == Node.ELEMENT_NODE) {
            NodeList tasks = ((Element) buildQueueNode).getElementsByTagName("task");
            for (int i = 0; i < tasks.getLength(); i++) {
                Element task = (Element) tasks.item(i);

                EntityType type = EntityType.valueOf(task.getElementsByTagName("type").item(0).getTextContent());
                long time = Long.parseLong(task.getElementsByTagName("completionTime").item(0).getTextContent());
                String target = task.getElementsByTagName("buildingToUpgrade").item(0).getTextContent();

                if (target.equals("null")) {
                    //then its a build task
                    buildQueue.add(new Village.QueueTask(type, time));
                } else {
                    //upgrade task
                    int currentLevel = Integer.parseInt(task.getElementsByTagName("currentLevel").item(0).getTextContent());
                    Building match = null;

                    for (Building b : buildings) {
                        //find the building which matches the upgrade (this shouldnt cause issues if there are multiple of the same type and level buildings. look into IDs for each entity (like we did before)
                        if (b.getEntityType() == type && b.getStats().level() == currentLevel) {
                            match = b;
                            break;
                        }
                    }
                    if (match != null) {
                        //if the correct building is found (which it should, otherwise it would have never been added to the queue), then get the next level stats
                        EntityStats nextStats = EntityLevelData.getLevels(match.getEntityType()).get(currentLevel); //if anything causes issues, this might be it I dont know if I did the call correct
                        buildQueue.add(new Village.QueueTask(type, match, nextStats, time));
                    }
                }
            }
        }
    }

    private static void loadTrainQueue(Document document, List<Village.QueueTask> trainQueue) {
        Node trainQueueNode = document.getElementsByTagName("trainQueue").item(0);
        if (trainQueueNode != null && trainQueueNode.getNodeType() == Node.ELEMENT_NODE) {
            NodeList tasks = ((Element) trainQueueNode).getElementsByTagName("task");
            for (int i = 0; i < tasks.getLength(); i++) {
                Element tElem = (Element) tasks.item(i);

                EntityType type = EntityType.valueOf(tElem.getElementsByTagName("type").item(0).getTextContent());
                long time = Long.parseLong(tElem.getElementsByTagName("completionTime").item(0).getTextContent());

                trainQueue.add(new Village.QueueTask(type, time));
            }
        }
    }
}