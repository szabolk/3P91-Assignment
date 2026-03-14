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

    public static void addChildElement(Document document, Element parent, String tag, String value) {
        Element childElement = document.createElement(tag);
        childElement.appendChild(document.createTextNode(value));
        parent.appendChild(childElement);
    }

    public static void villageToXML(Village village, String filePath) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        //start the dom with village as the root element (everything a part of the village will be
        //added as child elements)
        Element rootElement = document.createElement("Village");
        document.appendChild(rootElement);

        addChildElement(document, rootElement, "villageHallLevel", String.valueOf(village.getVillageHall().getStats().level()));
        addChildElement(document, rootElement, "guardedUntil", String.valueOf(village.getGuardedUntil()));

        //add resources to the structure
        Element resources = document.createElement("resources");
        rootElement.appendChild(resources);
        addChildElement(document, resources, "gold", String.valueOf(village.getResources().getGold()));
        addChildElement(document, resources, "iron", String.valueOf(village.getResources().getIron()));
        addChildElement(document, resources, "lumber", String.valueOf(village.getResources().getLumber()));

        Element inhabitants = document.createElement("inhabitants");
        rootElement.appendChild(inhabitants);
        for (Inhabitant inhabitant : village.getInhabitants()) {
            Element inhabitantNode = document.createElement("inhabitant");
            addChildElement(document, inhabitantNode, "unitType", String.valueOf(inhabitant.getEntityType()));
            addChildElement(document, inhabitantNode, "level", String.valueOf(inhabitant.getStats().level()));
            inhabitants.appendChild(inhabitantNode);
        }

        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer transformer = tfactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }
}
