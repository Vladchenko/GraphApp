package ru.yanchenko.vlad.graphapp.persistence;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import ru.yanchenko.vlad.graphapp.AppConfig;
import ru.yanchenko.vlad.graphapp.domain.graph.GraphDomainService;
import ru.yanchenko.vlad.graphapp.models.domain.Edge;
import ru.yanchenko.vlad.graphapp.models.presentation.VertexTextSizer;
import ru.yanchenko.vlad.graphapp.models.vertex.Vertex;
import ru.yanchenko.vlad.graphapp.models.vertex.VertexFont;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.yanchenko.vlad.graphapp.NumberUtils.isInteger;

/**
 * XML-based implementation of {@link Persistable} using SAX parsing.
 * <p>
 * Reads graph data via {@code DefaultHandler} callbacks and writes
 * using DOM serialization with indented output. Vertex links are stored
 * as child elements of the vertex element.
 *
 * @see Persistable
 * @see DefaultHandler
 */
public class XMLPersistence extends DefaultHandler implements Persistable {

    private static final String LINK_ATTRIBUTE_NAME = "link";
    private static final String VERTEX_ELEMENT_NAME = "Vertex";
    private static final String VERTEX_LINK_ELEMENT_NAME = "VertexLink";
    private static final String VERTEX_NAME_ATTRIBUTE_NAME = "name";
    private static final String X_ATTRIBUTE_NAME = "x";
    private static final String Y_ATTRIBUTE_NAME = "y";
    private static final String ROOT_ELEMENT_NAME = "Graph";

    private Locator locator;
    private int currentVertex1 = -1;
    private GraphDomainService graphService;

    /**
     * Loads graph data from an XML file using SAX parsing.
     *
     * @param graphService the graph domain service to populate
     * @throws ParserConfigurationException if XML parser configuration fails
     */
    @Override
    public void loadFromFile(GraphDomainService graphService) throws ParserConfigurationException {
        this.graphService = graphService;
        XMLReader xmlReader = null;
        SAXParser saxParser;
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);
        try {
            saxParser = spf.newSAXParser();
            xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(this);
        } catch (SAXException ex) {
            Logger.getLogger(XMLPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputSource source = new InputSource(AppConfig.getPersistenceFilePath());
        try {
            assert xmlReader != null;
            xmlReader.parse(source);
            Logger.getLogger(XMLPersistence.class.getName()).log(Level.INFO, "XML parsing done");
        } catch (IOException | SAXException ex) {
            Logger.getLogger(XMLPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Saves graph data to an XML file using DOM serialization.
     *
     * @param graphService the graph domain service to save
     */
    @Override
    public void saveToFile(GraphDomainService graphService) {
        this.graphService = graphService;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();

            // Adding a root element
            boolean addNode;
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement(ROOT_ELEMENT_NAME);
            doc.appendChild(rootElement);

            Node[] vertexElement = new Node[graphService.getCurrentGraph().getVertices().size()];
            Node[] vertexLinkElement = new Node[graphService.getCurrentGraph().getEdges().size()];

            for (int i = 0; i < graphService.getCurrentGraph().getVertices().size(); i++) {
                // Adding a vertex element
                if (!graphService.getCurrentGraph().getVertices().get(i).getVertexName().isEmpty()) {
                    vertexElement[i] = doc.createElement(VERTEX_ELEMENT_NAME);
                    ((Element) vertexElement[i]).setAttribute(VERTEX_NAME_ATTRIBUTE_NAME,
                            graphService.getCurrentGraph().getVertices().get(i).getVertexName());
                    ((Element) vertexElement[i]).setAttribute(X_ATTRIBUTE_NAME,
                            Long.toString(Math.round(graphService.getCurrentGraph().getVertices().get(i).getX())));
                    ((Element) vertexElement[i]).setAttribute(Y_ATTRIBUTE_NAME,
                            Long.toString(Math.round(graphService.getCurrentGraph().getVertices().get(i).getY())));
                    rootElement.appendChild(vertexElement[i]);
                }

                for (int j = 0; j < graphService.getCurrentGraph().getEdges().size(); j++) {
                    Edge edge = graphService.getCurrentGraph().getEdges().get(j);
                    if (graphService.getCurrentGraph().getVertices().get(edge.from()).getVertexName()
                            .equals(((Element) vertexElement[i]).getAttribute(VERTEX_NAME_ATTRIBUTE_NAME))) {
                        addNode = true;
                        // Check if such a vertexLink exists
                        for (int k = 0; k < vertexElement[i].getChildNodes().getLength(); k++) {
                            if (Objects.equals(
                                    graphService.getCurrentGraph().getVertices().get(edge.to()).getVertexName(),
                                    vertexElement[i].getChildNodes().item(k).getAttributes()
                                            .getNamedItem(LINK_ATTRIBUTE_NAME).getNodeValue())) {
                                addNode = false;
                                break;
                            }
                        }
                        // Adding a vertexLink element
                        if (addNode) {
                            vertexLinkElement[j] = doc.createElement(VERTEX_LINK_ELEMENT_NAME);
                            ((Element) vertexLinkElement[j]).setAttribute(LINK_ATTRIBUTE_NAME,
                                    graphService.getCurrentGraph().getVertices().get(
                                            edge.to()).getVertexName());
                            vertexElement[i].appendChild(vertexLinkElement[j]);
                        }
                    }

                    if (graphService.getCurrentGraph().getVertices().get(edge.to()).getVertexName()
                            .equals(((Element) vertexElement[i]).getAttribute(VERTEX_NAME_ATTRIBUTE_NAME))) {
                        addNode = true;
                        // Check if such a vertexLink exists
                        for (int k = 0; k < vertexElement[i].getChildNodes().getLength(); k++) {
                            if (Objects.equals(
                                    graphService.getCurrentGraph().getVertices().get(edge.from()).getVertexName(),
                                    vertexElement[i].getChildNodes().item(k).getAttributes()
                                            .getNamedItem(LINK_ATTRIBUTE_NAME).getNodeValue())) {
                                addNode = false;
                                break;
                            }
                        }
                        // Adding a vertexLink element
                        if (addNode) {
                            vertexLinkElement[j] = doc.createElement(VERTEX_LINK_ELEMENT_NAME);
                            ((Element) vertexLinkElement[j]).setAttribute(LINK_ATTRIBUTE_NAME,
                                    graphService.getCurrentGraph().getVertices().get(
                                            edge.from()).getVertexName());
                            vertexElement[i].appendChild(vertexLinkElement[j]);
                        }
                    }
                }
            }
            // Write the content into XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // Injecting an indentation to an XML file saving
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            doc.normalizeDocument();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(AppConfig.getPersistenceFilePath()));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(XMLPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //region "Load from an XML file methods (DefaultHandler)"
    @Override
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
        // If a vertex is present
        if (localName.equals(VERTEX_ELEMENT_NAME)) {
            if (atts.getLocalName(0).equals(VERTEX_NAME_ATTRIBUTE_NAME)) {
                int vertexPosition = isExistingVertex(
                        atts.getValue(0),
                        graphService.getCurrentGraph().getVertices());
                if (vertexPosition == -1) {
                    if (!atts.getLocalName(1).isBlank()
                            && atts.getLocalName(1).equals(X_ATTRIBUTE_NAME)
                            && isInteger(atts.getValue(1))) {
                        if (!atts.getLocalName(2).isBlank()
                                && atts.getLocalName(2).equals(Y_ATTRIBUTE_NAME)
                                && isInteger(atts.getValue(2))) {
                            double radius = VertexTextSizer.computeRadius(
                                    VertexFont.VERTICES_FONT, atts.getValue(0));
                            graphService.getCurrentGraph().getVertices().add(
                                    new Vertex(
                                            Integer.parseInt(atts.getValue(1)),
                                            Integer.parseInt(atts.getValue(2)),
                                            radius,
                                            atts.getValue(0)));
                        } else {
                            Logger.getLogger(XMLPersistence.class.getName())
                                    .log(Level.SEVERE, "\"y\" attribute should go after \"x\"");
                        }
                    } else {
                        Logger.getLogger(XMLPersistence.class.getName())
                                .log(Level.SEVERE, "\"x\" attribute should go after \"name\"");
                    }
                    currentVertex1 = graphService.getCurrentGraph().getVertices().size() - 1;
                } else {
                    if (!atts.getLocalName(1).isBlank()
                            && atts.getLocalName(1).equals(X_ATTRIBUTE_NAME)
                            && isInteger(atts.getValue(1))) {
                        if (!atts.getLocalName(2).isBlank()
                                && atts.getLocalName(2).equals(Y_ATTRIBUTE_NAME)
                                && isInteger(atts.getValue(2))) {
                            graphService.getCurrentGraph().getVertices().get(vertexPosition).setX(
                                    Integer.parseInt(atts.getValue(1)));
                            graphService.getCurrentGraph().getVertices().get(vertexPosition).setY(
                                    Integer.parseInt(atts.getValue(2)));
                        } else {
                            Logger.getLogger(XMLPersistence.class.getName())
                                    .log(Level.SEVERE, "\"y\" attribute should go after \"x\"");
                        }
                    } else {
                        Logger.getLogger(XMLPersistence.class.getName())
                                .log(Level.SEVERE, "\"x\" attribute should go after \"name\"");
                    }
                    currentVertex1 = vertexPosition;
                }
            } else {
                Logger.getLogger(XMLPersistence.class.getName())
                        .log(Level.SEVERE, "\"name\" attribute should go first");
            }
        }

        // If a vertex link is present
        if (localName.equals(VERTEX_LINK_ELEMENT_NAME)) {
            if (atts.getLocalName(0).equals(LINK_ATTRIBUTE_NAME)) {
                int vertexPosition = isExistingVertex(
                        atts.getValue(0),
                        graphService.getCurrentGraph().getVertices());
                int currentVertex2;
                if (vertexPosition == -1) {
                    double radius = VertexTextSizer.computeRadius(
                            VertexFont.VERTICES_FONT, atts.getValue(0));
                    graphService.getCurrentGraph().getVertices().add(
                            new Vertex(0, 0, radius, atts.getValue(0)));
                    currentVertex2 = graphService.getCurrentGraph().getVertices().size() - 1;
                } else {
                    currentVertex2 = vertexPosition;
                }
                // Avoid self-links and duplicates (A-B or B-A)
                boolean addition = currentVertex1 != currentVertex2
                                && graphService.getCurrentGraph().getEdges().stream().noneMatch(edge ->
                                (edge.from() == currentVertex1 && edge.to() == currentVertex2)
                                        || (edge.from() == currentVertex2 && edge.to() == currentVertex1));
                // If such a VertexLink doesn't exist, add it
                if (addition) {
                    graphService.getCurrentGraph().getEdges().add(
                            new Edge(currentVertex1, currentVertex2));
                }
            } else {
                Logger.getLogger(XMLPersistence.class.getName())
                        .log(Level.SEVERE, "\"link\" attribute should go first");
            }
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) {
    }

    @Override
    public void characters(char[] ch, int start, int length) {
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) {
        Logger.getLogger(XMLPersistence.class.getName())
                .log(Level.INFO, getLocation() + " start prefix mapping /" + prefix + "/" + uri + "/");
    }

    @Override
    public void endPrefixMapping(String prefix) {
        Logger.getLogger(XMLPersistence.class.getName())
                .log(Level.INFO, getLocation() + "end prefix mapping /" + prefix + "/");
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) {
        Logger.getLogger(XMLPersistence.class.getName())
                .log(Level.INFO, getLocation() + " ignorable whitespace of length " + length);
    }

    @Override
    public void processingInstruction(String target, String data) {
        Logger.getLogger(XMLPersistence.class.getName())
                .log(Level.INFO, getLocation() + " processing instruction /" + target + "/" + data + "/");
    }

    @Override
    public void skippedEntity(String name) {
        Logger.getLogger(XMLPersistence.class.getName())
                .log(Level.INFO, getLocation() + " skipped entity /" + name + "/");
    }
    //endregion

    private int isExistingVertex(String vertexName, List<Vertex> vertices) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getVertexName().equals(vertexName)) {
                return i;
            }
        }
        return -1;
    }

    public String getLocation() {
        if (locator == null) {
            return "";
        } else {
            return "(" + locator.getLineNumber() + ", " + locator.getColumnNumber() + ")";
        }
    }
}
