package ua.moses.maximusprice;

import android.content.Context;
import android.content.SharedPreferences;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class XMLManager {
    private String xmlFileName;
    private Context context;

    XMLManager(String xmlFileName, Context context) {
        this.xmlFileName = xmlFileName;
        this.context = context;
    }

    List<Good> getGoods() throws IOException {
        final String GROUP_TAG = "Group";
        final String SUBGROUP_TAG = "SubGroup";
        final String GOOD_NAME_TAG = "Name";
        final String PRICE_TAG = "Price";
        final String DESCRIPTION_TAG = "Description";
        final String GOOD_ID_TAG = "Id";
        final String AVAILABILITY_TAG = "Availability";
        List<Good> result = new ArrayList<>();
        DocumentBuilder documentBuilder;
        Document document;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = documentBuilder.parse(xmlFileName);
            updateActualDate();
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
                        case AVAILABILITY_TAG:
                            currentGood.setAvailability(goodDescription.getTextContent());
                            break;
                    }
                }
                result.add(currentGood);
            }

        }

        return result;
    }

    private void updateActualDate() throws IOException {
        String actualDate;
            URL obj = new URL(xmlFileName);
            URLConnection conn = obj.openConnection();
            Map<String, List<String>> map = conn.getHeaderFields();
            List<String> lastModified = map.get("Last-Modified");
            if (lastModified != null && lastModified.size() > 0) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.getDefault());
                Date date;
                try {
                    date = dateFormat.parse(lastModified.get(0));
                } catch (ParseException e) {
                    date = new Date();
                }
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                actualDate = dateFormat.format(date);
                SharedPreferences sPref = context.getSharedPreferences("sPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(context.getString(R.string.ACTUAL_DATE_VARIABLE), actualDate);
                ed.commit();
            }
    }

}
