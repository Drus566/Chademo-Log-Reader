package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ChargerTemplate {

    Map<Integer, ArrayList<Map<Byte, ArrayList<Bit>>>> chargerMessages = new HashMap<Integer, ArrayList<Map<Byte, ArrayList<Bit>>>>();


    public ChargerTemplate() {
        readConfig();
    }

    public class Metadata {

        Metadata(int position, String ru_item, String en_item) {
            this.position = position;
            this.ru_item = ru_item;
            this.en_item = en_item;
        }

        protected int position;
        protected String ru_item;
        protected String en_item;
    }

    public class Byte extends Metadata {
        Byte(int position, String ru_item, String en_item) {
            super(position, ru_item, en_item);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass())
                return false;

            Byte that = (Byte) o;
            return position == that.position && ru_item == that.ru_item && en_item == that.en_item;
        }

        @Override
        public int hashCode() {
            return this.position;
        }
    }

    public class Bit extends Metadata {
        Bit(int position, String ru_item, String en_item) {
            super(position, ru_item, en_item);
        }
    }

    private void readConfig() {
        Document doc;

        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(new File("resources/Charger.xml")));
            NodeList nodes = doc.getElementsByTagName("Message");

            // Перебор Can сообщений
            if (nodes.getLength() > 0) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    if (nodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }

                    Node messageNode = nodes.item(i);
                    Element messageElement = (Element) messageNode;
                    int messageId = Integer.decode(messageElement.getAttribute("id"));

                    // Перебор байтов
                    ArrayList<Map<Byte, ArrayList<Bit>>> byteArray = new ArrayList<Map<Byte, ArrayList<Bit>>>(0);
                    NodeList byteNodes = messageNode.getChildNodes();
                    if (byteNodes.getLength() > 0) {
                        Map<Byte, ArrayList<Bit>> byteMap = new HashMap<Byte, ArrayList<Bit>>(0);
                        for (int j = 0; j < byteNodes.getLength(); j++) {
                            if (byteNodes.item(j).getNodeType() != Node.ELEMENT_NODE) {
                                continue;
                            }

                            Node byteNode = byteNodes.item(j);
                            System.out.println(byteNode);
                            Element byteElement = (Element) byteNode;
                            int positionByte = Integer.decode(byteElement.getAttribute("position"));
                            String ruItemByte = byteElement.getAttribute("ru_item");
                            String enItemByte = byteElement.getAttribute("en_item");

                            // Перебор битов
                            ArrayList<Bit> bits = new ArrayList<Bit>(0);
                            NodeList bitNodes = byteNode.getChildNodes();
                            if (bitNodes.getLength() > 0) {
                                for (int k = 0; k < bitNodes.getLength(); k++) {
                                    if (bitNodes.item(k).getNodeType() != Node.ELEMENT_NODE) {
                                        continue;
                                    }

                                    Node bitNode = byteNodes.item(i);
                                    Element bitElement = (Element) bitNode;
                                    int positionBit = Integer.decode(bitElement.getAttribute("position"));
                                    String ruItemBit = bitElement.getAttribute("ru_item");
                                    String enItemBit = bitElement.getAttribute("en_item");

                                    bits.add(new Bit(positionBit, ruItemBit, enItemBit));
                                }
                            }
                            byteMap.put(new Byte(positionByte, ruItemByte, enItemByte), bits);
                            byteArray.add(byteMap);
                        }
                        chargerMessages.put(messageId, byteArray);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
