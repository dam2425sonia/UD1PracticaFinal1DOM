import java.io.*;
import java.nio.file.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.*;

class Alumno implements Serializable {
    private String nombre;
    private int edad;

    public Alumno(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    @Override
    public String toString() {
        return "Alumno{" + "nombre='" + nombre + '\'' + ", edad=" + edad + '}';
    }
}

public class App {

    public static void main(String[] args) {
        try {
            // 1. Crear directorio ejercicios
            Path ejerciciosDir = Paths.get("ejercicios");
            Files.createDirectories(ejerciciosDir);

            // 1.1 Crear fichero pizzas.xml y rellenarlo
            crearPizzasXML(ejerciciosDir.resolve("pizzas.xml").toFile());

            /* NOTA
            ejerciciosDir: Es una instancia de Path que representa el directorio 
            base donde quieres guardar el archivo. En este caso, es el directorio ejercicios.
            resolve("pizzas.xml"): El método resolve de la clase Path toma un String 
            que representa el nombre del archivo o subdirectorio, y lo concatena con 
            el Path de ejerciciosDir. Esto construye la ruta completa al archivo 
            pizzas.xml dentro del directorio ejercicios. 
            Es equivalente a ejerciciosDir + "/pizzas.xml".
            toFile(): Convierte el objeto Path resultante en una instancia de File. 
            Esto es necesario porque algunas operaciones de entrada/salida en Java, 
            como la creación o escritura de archivos con FileWriter, 
            requieren un objeto File en lugar de un objeto Path.
            */

            // 1.2 Leer el fichero libros.xml (supón que ya existe) y mostrar los autores
            leerLibrosXML(ejerciciosDir.resolve("libros.xml").toFile());

            // 2. Crear directorio alumnos y guardar objetos Alumno
            Path alumnosDir = Paths.get("alumnos");
            Files.createDirectories(alumnosDir);

            // Crear 3 objetos Alumno y guardarlos
            guardarAlumnos(alumnosDir);

            // 3. Crear directorio profesores y escribir el fichero profesores.txt
            Path profesoresDir = Paths.get("profesores");
            Files.createDirectories(profesoresDir);

            // Escribir el listado de profesores en un fichero de texto
            escribirProfesores(profesoresDir.resolve("profesores.txt").toFile());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Función para crear el archivo pizzas.xml
    public static void crearPizzasXML(File file) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Crear documento XML
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("pizzas");
        doc.appendChild(rootElement);

        // Crear elementos de pizza
        Element pizza1 = doc.createElement("pizza");
        pizza1.setAttribute("nombre", "Margarita");
        pizza1.appendChild(crearIngrediente(doc, "tomate"));
        pizza1.appendChild(crearIngrediente(doc, "queso"));
        rootElement.appendChild(pizza1);

        Element pizza2 = doc.createElement("pizza");
        pizza2.setAttribute("nombre", "Pepperoni");
        pizza2.appendChild(crearIngrediente(doc, "tomate"));
        pizza2.appendChild(crearIngrediente(doc, "queso"));
        pizza2.appendChild(crearIngrediente(doc, "pepperoni"));
        rootElement.appendChild(pizza2);

        // Escribir el contenido en el archivo XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);

        System.out.println("Archivo pizzas.xml creado correctamente.");
    }

    // Función auxiliar para crear elementos de ingrediente
    public static Element crearIngrediente(Document doc, String nombre) {
        Element ingrediente = doc.createElement("ingrediente");
        ingrediente.appendChild(doc.createTextNode(nombre));
        return ingrediente;
    }

    // Función para leer el archivo libros.xml y mostrar los autores
    public static void leerLibrosXML(File file) throws Exception {
        if (!file.exists()) {
            System.out.println("El archivo libros.xml no existe.");
            return;
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("libro");

        System.out.println("Autores en libros.xml:");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String autor = eElement.getElementsByTagName("autor").item(0).getTextContent();
                System.out.println(autor);
            }
        }
    }

    // Función para guardar objetos de alumnos en ficheros
    public static void guardarAlumnos(Path alumnosDir) throws IOException {
        Alumno alumno1 = new Alumno("Juan", 20);
        Alumno alumno2 = new Alumno("María", 22);
        Alumno alumno3 = new Alumno("Pedro", 21);

        guardarAlumno(alumnosDir.resolve("alumno1.dat").toFile(), alumno1);
        guardarAlumno(alumnosDir.resolve("alumno2.dat").toFile(), alumno2);
        guardarAlumno(alumnosDir.resolve("alumno3.dat").toFile(), alumno3);

        System.out.println("Alumnos guardados correctamente.");
    }

    // Función auxiliar para guardar un alumno en un archivo
    public static void guardarAlumno(File file, Alumno alumno) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(alumno);
        }
    }

    // Función para escribir el listado de profesores en un fichero de texto
    public static void escribirProfesores(File file) throws IOException {
        List<String> profesores = Arrays.asList("Profesor A", "Profesor B", "Profesor C");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String profesor : profesores) {
                writer.write(profesor);
                writer.newLine();
            }
        }
        System.out.println("Listado de profesores guardado correctamente.");
    }
}

