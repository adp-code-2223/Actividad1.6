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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author mfernandez
 */
public class MainB {

    private static final String PERSONAS_NS_DEFAULT_URI = "http://www.personas.com";
    private static final String PERSONAS_NS_ACTIVE_URI = "http://www.personas.com/active";

    private static final String PERSONA_TAG = "persona";

    private static final String PERSONA_NOMBRE_TAG = "nombre";
    private static final String PERSONA_EDAD_TAG = "edad";
    private static final String PERSONA_DNI_TAG = "dni";
    private static final String PERSONA_SALARIO_TAG = "salario";
    private static final String PERSONA_ATT_ID = "id";
    private static final String PERSONA_ATT_BORRADO = "borrado";

    private static final String PERSONAS_INPUT_FILE = Paths.get("src", "docs", "personas_ns.xml").toString();

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

            NodeList nListNSPorDefecto = doc.getElementsByTagName(PERSONA_TAG);
            //También se podría utilizar con el ns por defecto
            //NodeList nListNSPorDefecto = doc.getElementsByTagNameNS(PERSONAS_NS_DEFAULT_URI, PERSONA_TAG);

            NodeList nListNSActive = doc.getElementsByTagNameNS(PERSONAS_NS_ACTIVE_URI, PERSONA_TAG);

            personasNoActivas = toPersonaList(nListNSPorDefecto);
            personasActivas = toPersonaList(nListNSActive);

            System.out.println("Personas activas:");
            mostrarPersonas(personasActivas);
            System.out.println("Personas NO activas:");
            mostrarPersonas(personasNoActivas);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ha ocurrido una exception: " + e.getMessage());
        }

    }

    private static void mostrarPersonas(ArrayList<Persona> personas) {
        int contador = 1;
        for (Persona p : personas) {
            System.out.println("Persona: " + contador + " " + p);
            contador++;
        }
    }

    private static ArrayList<Persona> toPersonaList(NodeList nList) {

        double numero = 0;
        int edad = 0;
        Long id = 0l;
        String nombre = "", dni = "";
        float salario = 0;
        Persona persona = null;
        boolean borrado = false;
        ArrayList<Persona> personas = new ArrayList<>();

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                id = Long.valueOf(eElement.getAttribute(PERSONA_ATT_ID));
                borrado = Boolean.parseBoolean(eElement.getAttribute(PERSONA_ATT_BORRADO));

                nombre = eElement.getElementsByTagName(PERSONA_NOMBRE_TAG).item(0).getTextContent();
                edad = Integer.parseInt(eElement.getElementsByTagName(PERSONA_EDAD_TAG).item(0).getTextContent());
                dni = eElement.getElementsByTagName(PERSONA_DNI_TAG).item(0).getTextContent();
                salario = Float.parseFloat(eElement.getElementsByTagName(PERSONA_SALARIO_TAG).item(0).getTextContent());

                persona = new Persona(id, dni, edad, salario, nombre);
                persona.setBorrado(borrado);

                personas.add(persona);
            }
        }
        return personas;
    }

}
