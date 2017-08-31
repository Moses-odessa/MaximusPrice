package ua.moses.maximusprice;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLManager {
    private String xmlFileName;

    public XMLManager(String xmlFileName) {
        this.xmlFileName = xmlFileName;
    }

    public List<Good> getGoods() throws IOException {
        final String GOODS_TAG = "XMLPrice";
        final String GROUP_TAG = "Group";
        final String SUBGROUP_TAG = "SubGroup";
        final String GOOD_NAME_TAG = "Name";
        final String PRICE_TAG = "Price";
        final String DESCRIPTION_TAG = "Description";
        final String GOOD_ID_TAG = "Id";
        List<Good> result = new ArrayList<Good>();
        DocumentBuilder documentBuilder = null;
        Document document = null;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = documentBuilder.parse(xmlFileName);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException(e.getMessage());
        }

        Node root = document.getDocumentElement();
        NodeList goods = root.getChildNodes();
        for (int i = 0; i < goods.getLength(); i++) {
            Node goodNode = goods.item(i);
            Good currentGood = new Good();
            if (goodNode.getNodeType() != Node.TEXT_NODE) {
                NodeList goodChildNodes = goodNode.getChildNodes();
                for (int j = 0; j < goodChildNodes.getLength(); j++) {
                    Node goodDescription = goodChildNodes.item(j);
                    switch (goodDescription.getNodeName()) {
                        case GROUP_TAG:
                            currentGood.setGroup(goodDescription.getTextContent());
                            break;
                        case SUBGROUP_TAG:
                            currentGood.setSubGroup(goodDescription.getTextContent());
                            break;
                        case GOOD_ID_TAG:
                            currentGood.setId(Integer.parseInt(goodDescription.getTextContent()));
                            break;
                        case GOOD_NAME_TAG:
                            currentGood.setName(goodDescription.getTextContent());
                            break;
                        case DESCRIPTION_TAG:
                            currentGood.setDescription(goodDescription.getTextContent());
                            break;
                        case PRICE_TAG:
                            currentGood.setPrice(Integer.parseInt(goodDescription.getTextContent()));
                            break;
                    }
                }
                result.add(currentGood);
            }
        }

        return result;
    }

}
