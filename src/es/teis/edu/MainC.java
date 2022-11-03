/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.teis.edu;

import es.teis.edu.model.Persona;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author mfernandez
 */
public class MainC {

    private static final String PERSONA_TAG = "persona";

    private static final String PERSONAS_INPUT_FILE = Paths.get("src", "docs", "personas_ns.xml").toString();
    private static final String PERSONAS_OUTPUT_FILE = Paths.get("src", "docs", "personas_output.xml").toString();

    public static void main(String[] args) {

        ArrayList<Persona> personasNoActivas = new ArrayList<>();
        ArrayList<Persona> personasActivas = new ArrayList<>();

        try {
            File inputFile = new File(PERSONAS_INPUT_FILE);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            //elimina hijos con texto vacío y fusiona en un único nodo de texto varios adyacentes.
            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();

         
            NodeList nListNSPorDefecto = doc.getElementsByTagName(PERSONA_TAG);

         
            for (int i = (nListNSPorDefecto.getLength() - 1); i >= 0; i--) {
//                System.out.println("Hijos length: " + root.getChildNodes().getLength());
                Node hijo = nListNSPorDefecto.item(i);
                Node deleted =root.removeChild(hijo);
                //  Node deleted = personas.removeChild(hijo);
                if (deleted != null) {
                    System.out.println("Se ha eliminado un hijo");
                } else {
                    System.out.println("NO se ha eliminado un hijo");
                }
            }

            TransformerFactory fabricaTransformador = TransformerFactory.newInstance();
            //Espacios para indentar cada línea
            fabricaTransformador.setAttribute("indent-number", 4);
            Transformer transformador = fabricaTransformador.newTransformer();
            //Insertar saltos de línea al final de cada línea
            //https://docs.oracle.com/javase/8/docs/api/javax/xml/transform/OutputKeys.html
            transformador.setOutputProperty(OutputKeys.INDENT, "yes");

            //El origen de la transformación es el document
            Source origen = new DOMSource(doc);
            //El destino será un stream a un fichero 
            Result destino = new StreamResult(PERSONAS_OUTPUT_FILE);
            transformador.transform(origen, destino);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ha ocurrido una exception: " + e.getMessage());
        }

    }

}
