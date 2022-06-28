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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ChargerTemplate {

    Map<Integer, ArrayList<Map<Byte, ArrayList<Bit>>>> chargerMessages = new HashMap<Integer, ArrayList<Map<Byte, ArrayList<Bit>>>>();


    public ChargerTemplate() {
        readConfig();
        showChargerMessages();
    }

    public class Metadata {

        Metadata(int[] position, String ru_item, String en_item) {
            this.position = position;
            this.ru_item = ru_item;
            this.en_item = en_item;
        }

        protected int[] position;
        protected String ru_item;
        protected String en_item;
    }

    public class Byte extends Metadata {
        Byte(int[] position, String ru_item, String en_item) {
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
            return position[0];
        }
    }

    public class Bit extends Metadata {
        Bit(int[] position, String ru_item, String en_item) {
            super(position, ru_item, en_item);
        }
    }

    private void showChargerMessages() {
        for (Map.Entry<Integer, ArrayList<Map<Byte, ArrayList<Bit>>>> chargerMessage : chargerMessages.entrySet()) {
            System.out.println("~~~~~~~~" + chargerMessage.getKey() + "~~~~~~~~");
            ArrayList<Map<Byte, ArrayList<Bit>>> array = chargerMessage.getValue();
            for (Map<Byte, ArrayList<Bit>> entry_bytes : array) {
                System.out.println("Bytes:");
                for (Byte key : entry_bytes.keySet()) {
                    System.out.println("    Byte: ");
                    for (int i = 0; i < key.position.length; i++) {
                        if (i == 1 && key.position[1] <= key.position[0]) {
                            continue;
                        }
                        System.out.println("        position: " + key.position[i]);
                    }
                    System.out.println("        description: " + key.ru_item);

                    ArrayList<Bit> bits = entry_bytes.get(key);
                    for (Bit bit : bits) {
                        System.out.println("        Bit: " + bit.position[0]);
                        System.out.println("             " + bit.ru_item);

                    }
                }
            }
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
                    NodeList byteNodes = messageElement.getElementsByTagName("Byte");
                    if (byteNodes.getLength() > 0) {
                        Map<Byte, ArrayList<Bit>> byteMap = new HashMap<Byte, ArrayList<Bit>>(0);
                        for (int j = 0; j < byteNodes.getLength(); j++) {
                            if (byteNodes.item(j).getNodeType() != Node.ELEMENT_NODE) {
                                continue;
                            }

                            Node byteNode = byteNodes.item(j);
                            Element byteElement = (Element) byteNode;

                            String[] positionBytes = byteElement.getAttribute("position").split(",");
                            int[] positionByte = new int[] {0,0};
                            for (int l = 0; l < positionBytes.length; l++) {
                                positionByte[l] = Integer.decode(positionBytes[l]);
                            }
                            String ruItemByte = byteElement.getAttribute("ru_item");
                            String enItemByte = byteElement.getAttribute("en_item");

                            // Перебор битов
                            ArrayList<Bit> bits = new ArrayList<Bit>(0);
                            NodeList bitNodes = byteElement.getElementsByTagName("Bit");
                            if (bitNodes.getLength() > 0) {
                                for (int k = 0; k < bitNodes.getLength(); k++) {
                                    if (bitNodes.item(k).getNodeType() != Node.ELEMENT_NODE) {
                                        continue;
                                    }

//                                    System.out.println(bitNodes.getLength());
//                                    Node bitNode = byteNodes.item(k);
                                    Element bitElement = (Element) bitNodes.item(k);
//                                    System.out.println(bitElement);

                                    String[] positionBits = bitElement.getAttribute("position").split(",");
                                    int[] positionBit = new int[] {0,0};
                                    for (int h = 0; h < positionBits.length; h++) {
                                        positionBit[h] = Integer.decode(positionBits[h]);
                                    }
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
