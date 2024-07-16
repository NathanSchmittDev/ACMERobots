package dados;

import dados.Clientes.Cliente;
import dados.Clientes.Empresarial;
import dados.Clientes.Individual;
import dados.Robos.Agricola;
import dados.Robos.Domestico;
import dados.Robos.Industrial;
import dados.Robos.Robo;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

public class XMLMaster {

    private static final String ARQUIVO_DADOS = "dados.xml";

    public static Map<Integer, Robo> carregarRobos() throws Exception {
        Map<Integer, Robo> robos = new TreeMap<>();
        Document doc = getDocument();

        NodeList nodeList = doc.getElementsByTagName("robo");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            int id = Integer.parseInt(element.getAttribute("id"));
            String modelo = getTagValue("modelo", element);
            double valorDiario = Double.parseDouble(getTagValue("valorDiario", element));
            String tipo = getTagValue("tipo", element);

            Robo robo;
            switch (tipo) {
                case "Domestico":
                    int nivel = Integer.parseInt(getTagValue("nivel", element));
                    robo = new Domestico(id, modelo, valorDiario, nivel);
                    break;
                case "Industrial":
                    String setor = getTagValue("setor", element);
                    robo = new Industrial(id, modelo, valorDiario, setor);
                    break;
                case "Agricola":
                    double area = Double.parseDouble(getTagValue("area", element));
                    String uso = getTagValue("uso", element);
                    robo = new Agricola(id, modelo, valorDiario, area, uso);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de robô desconhecido: " + tipo);
            }
            robos.put(id, robo);
        }
        return robos;
    }

    public static void salvarRobos(Map<Integer, Robo> robos) throws Exception {
        Document doc = getDocument();
        Element rootElement = doc.getDocumentElement();

        if (rootElement == null) {
            rootElement = doc.createElement("robos");
            doc.appendChild(rootElement);
        }

        NodeList nodeList = rootElement.getElementsByTagName("robo");
        for (int i = 0; i < nodeList.getLength(); i++) {
            rootElement.removeChild(nodeList.item(i));
        }

        for (Robo robo : robos.values()) {
            Element roboElement = doc.createElement("robo");
            roboElement.setAttribute("id", Integer.toString(robo.getId()));
            createElement(doc, roboElement, "modelo", robo.getModelo());
            createElement(doc, roboElement, "valorDiario", Double.toString(robo.getValorDiario()));
            if (robo instanceof Domestico) {
                Domestico domestico = (Domestico) robo;
                createElement(doc, roboElement, "tipo", "Domestico");
                createElement(doc, roboElement, "nivel", Integer.toString(domestico.getNivel()));
            } else if (robo instanceof Industrial) {
                Industrial industrial = (Industrial) robo;
                createElement(doc, roboElement, "tipo", "Industrial");
                createElement(doc, roboElement, "setor", industrial.getSetor());
            } else if (robo instanceof Agricola) {
                Agricola agricola = (Agricola) robo;
                createElement(doc, roboElement, "tipo", "Agricola");
                createElement(doc, roboElement, "area", Double.toString(agricola.getArea()));
                createElement(doc, roboElement, "uso", agricola.getUso());
            }
            rootElement.appendChild(roboElement);
        }

        saveDocument(doc);
    }

    public static Map<Integer, Cliente> carregarClientes() throws Exception {
        Map<Integer, Cliente> clientes = new TreeMap<>();
        Document doc = getDocument();

        NodeList nodeList = doc.getElementsByTagName("cliente");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            int id = Integer.parseInt(element.getAttribute("id"));
            String nome = getTagValue("nome", element);
            String tipo = getTagValue("tipo", element);

            Cliente cliente;
            if (tipo.equals("Individual")) {
                String cpf = getTagValue("cpf", element);
                cliente = new Individual(id, nome, cpf);
            } else {
                int ano = Integer.parseInt(getTagValue("ano", element));
                cliente = new Empresarial(id, nome, ano);
            }
            clientes.put(id, cliente);
        }
        return clientes;
    }

    public static void salvarClientes(Map<Integer, Cliente> clientes) throws Exception {
        Document doc = getDocument();
        Element rootElement;

        NodeList nodeList = doc.getElementsByTagName("clientes");
        if (nodeList.getLength() == 0) {
            rootElement = doc.createElement("clientes");
            doc.appendChild(rootElement);
        } else {
            rootElement = (Element) nodeList.item(0);
        }

        for (Cliente cliente : clientes.values()) {
            Element clienteElement = doc.createElement("cliente");
            clienteElement.setAttribute("id", Integer.toString(cliente.getCodigo()));
            createElement(doc, clienteElement, "nome", cliente.getNome());
            if (cliente instanceof Individual) {
                Individual individual = (Individual) cliente;
                createElement(doc, clienteElement, "tipo", "Individual");
                createElement(doc, clienteElement, "cpf", individual.getCpf());
            } else if (cliente instanceof Empresarial) {
                Empresarial empresarial = (Empresarial) cliente;
                createElement(doc, clienteElement, "tipo", "Empresarial");
                createElement(doc, clienteElement, "ano", Integer.toString(empresarial.getAno()));
            }
            rootElement.appendChild(clienteElement);
        }

        saveDocument(doc);
    }

    public static Map<Integer, Locacao> carregarLocacoes(Map<Integer, Cliente> clientes, Map<Integer, Robo> robos) throws Exception {
        Map<Integer, Locacao> locacoes = new HashMap<>();
        Document doc = getDocument();

        NodeList nodeList = doc.getElementsByTagName("locacao");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            int id = Integer.parseInt(element.getAttribute("id"));
            int clienteCodigo = Integer.parseInt(getTagValue("clienteCodigo", element));
            String[] robosIds = getTagValue("robosIds", element).split(",");

            Date dataInicio = new Date();
            Date dataFim = new Date();

            Locacao locacao = new Locacao(id, Status.CADASTRADA, dataInicio, dataFim);
            locacao.setClientes(Collections.singleton(clientes.get(clienteCodigo)));

            for (String roboId : robosIds) {
                locacao.addRobo(robos.get(Integer.parseInt(roboId)));
            }

            locacoes.put(id, locacao);
        }
        return locacoes;
    }

    public static void salvarLocacoes(Map<Integer, Locacao> locacoes) throws Exception {
        Document doc = getDocument();
        Element rootElement = doc.getDocumentElement();

        if (rootElement == null) {
            rootElement = doc.createElement("locacoes");
            doc.appendChild(rootElement);
        }

        NodeList nodeList = rootElement.getElementsByTagName("locacao");
        for (int i = 0; i < nodeList.getLength(); i++) {
            rootElement.removeChild(nodeList.item(i));
        }

        for (Locacao locacao : locacoes.values()) {
            Element locacaoElement = doc.createElement("locacao");
            locacaoElement.setAttribute("id", Integer.toString(locacao.getNumero()));
            createElement(doc, locacaoElement, "clienteCodigo", Integer.toString(locacao.getClientes().iterator().next().getCodigo()));
            createElement(doc, locacaoElement, "robosIds", formatRobosIds(locacao.getRobos()));

            rootElement.appendChild(locacaoElement);
        }

        saveDocument(doc);
    }

    private static String formatRobosIds(Set<Robo> robos) {
        List<String> roboIds = new ArrayList<>();
        for (Robo robo : robos) {
            roboIds.add(Integer.toString(robo.getId()));
        }
        return String.join(",", roboIds);
    }

    public static Document getDocument() throws Exception {
        File file = new File(ARQUIVO_DADOS);
        if (!file.exists()) {
            Document doc = createDocument();
            saveDocument(doc);
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(file);
    }

    private static Document createDocument() throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.newDocument();
    }

    private static void saveDocument(Document doc) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(ARQUIVO_DADOS));
        transformer.transform(source, result);
    }

    public static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    public static void createElement(Document doc, Element parentElement, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textContent));
        parentElement.appendChild(element);
    }
}