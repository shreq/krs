package KSR1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLFile {

    public List<Article> articles = new ArrayList<>();

    static public XMLFile loadFile(File file) throws ParserConfigurationException, SAXException, IOException {
        XMLFile result = new XMLFile();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.parse(file);

        NodeList nodeList = doc.getElementsByTagName("RECIPE");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Article article = new Article();
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                article.type = element.getElementsByTagName("TYPE").item(0).getTextContent().trim();
                article.course = element.getElementsByTagName("COURSE").item(0).getTextContent().trim();

                NodeList nodeListText = element.getElementsByTagName("TEXT");
                for (int j = 0; j < nodeListText.getLength(); j++) {
                    Node nodeText = nodeListText.item(j);

                    if (nodeText.getNodeType() == Node.ELEMENT_NODE) {
                        Element elementText = (Element) nodeText;

                        article.title = elementText.getElementsByTagName("TITLE").item(0).getTextContent().trim();
                        article.setText(elementText.getElementsByTagName("BODY").item(0).getTextContent().trim().replaceAll("\n", "").replaceAll("\\s{2,}", " "));
                    }
                }
            }

            result.articles.add(article);
        }

        return result;
    }
}
