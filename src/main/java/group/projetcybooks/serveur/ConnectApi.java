package group.projetcybooks.serveur;

import group.projetcybooks.serveur.model.TypeStatue;
import group.projetcybooks.serveur.model.exception.NoArgumentException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import group.projetcybooks.serveur.model.Book;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static group.projetcybooks.serveur.model.TypeStatue.FREE;

/**
 * Handles connections and queries to the BNF API.
 */
public class ConnectApi {
    private final static String apiUrl = "http://catalogue.bnf.fr/api/SRU?version=1.2&operation=searchRetrieve";
    private Book book;
    private List<Book> books;


    /**
     * Do a request to BNF API with idBnf
     * @param idBnf
     * @throws Exception
     */
    public ConnectApi(String idBnf) throws Exception {
        //%20 = ' ' and %22 = '"'
        URL url = new URL(apiUrl+"&query=bib.recordid%20all%20%22"+idBnf+"%22");

        HttpURLConnection api = (HttpURLConnection) url.openConnection();
        api.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(api.getInputStream()));

        String inputLine;
        String result = "";
        while ((inputLine = in.readLine()) != null) {
            result += inputLine;
        }

        in.close();
        api.disconnect();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document parsXML = builder.parse(new java.io.ByteArrayInputStream(result.toString().getBytes()));

        parsXML.getDocumentElement().normalize();

        // Extraire les informations nécessaires
        NodeList data = parsXML.getElementsByTagName("mxc:datafield");
        String title = "";
        String author = "";
        String publicationYear = "";
        String editor="";

        for (int i = 0; i < data.getLength(); i++) {
            Element dataField = (Element) data.item(i);
            String tag = dataField.getAttribute("tag");
            if (tag.equals("200")) {
                NodeList subfields = dataField.getElementsByTagNameNS("*", "*");
                title = getSubfield(subfields,"a");
            }
            if (tag.equals("700")){
                NodeList subfields = dataField.getElementsByTagNameNS("*", "*");
                author = getSubfield(subfields,"a");
            }
            if (tag.equals("214")){
                NodeList subfields = dataField.getElementsByTagNameNS("*", "*");
                publicationYear = getSubfield(subfields,"d");
            }
        }
        book = new Book(Long.parseLong(idBnf), FREE,editor,title,author,publicationYear);
    }

    public ConnectApi(String idBnfR,String titleR,String autorR) throws Exception {
        books = new ArrayList<>();
        String fullUrl = apiUrl;
        if (idBnfR!=null){
            fullUrl += "&query=(bib.recordid%20all%20%22"+idBnfR+"%22)";
            if (titleR!=null){
                fullUrl+= "%20and%20(bib.title%20all%20%22"+titleR+"%22)";
            }
            if (autorR!=null){
                fullUrl+= "%20and%20(bib.author%20all%20%22"+autorR+"%22)";
            }
        } else if (titleR!=null) {
            fullUrl+="&query=(bib.title%20all%20%22"+titleR+"%22)";
            if (autorR!=null){
                fullUrl+= "%20and%20(bib.author%20all%20%22"+autorR+"%22)";
            }
        } else if (autorR!=null) {
            fullUrl+="&query=(bib.author%20all%20%22"+autorR+"%22)";
        }
        else{
            throw new NoArgumentException("Need idBnf or/and title or/and autor");
        }
        fullUrl+="%20and%20bib.doctype%20all%20%22a%22&maximumRecords=25";
        System.out.println(fullUrl);
        URL url = new URL(fullUrl);

        HttpURLConnection api = (HttpURLConnection) url.openConnection();
        api.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(api.getInputStream()));

        String inputLine;
        String result = "";
        while ((inputLine = in.readLine()) != null) {
            result += inputLine;
        }

        in.close();
        api.disconnect();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document parsXML = builder.parse(new java.io.ByteArrayInputStream(result.toString().getBytes()));

        parsXML.getDocumentElement().normalize();

        // Extraire les informations nécessaires
        String idBnf = "";
        String title = "";
        String author = "";
        String publicationYear = "";
        String editor="";

        NodeList records = parsXML.getElementsByTagName("srw:record");

        for (int y = 0; y < records.getLength(); y++) {
            Element recordElement = (Element) records.item(y);
            NodeList data = recordElement.getElementsByTagName("mxc:datafield");
            NodeList control = recordElement.getElementsByTagName("mxc:controlfield");
            idBnf = "";
            for (int i = 0; i < control.getLength(); i++) {
                Element controlField = (Element) control.item(i);
                String tag = controlField.getAttribute("tag");
                if (tag.equals("001")){
                    idBnf = controlField.getTextContent().substring(5);
                }
            }
            title = "";
            author = "";
            publicationYear = "";
            editor="";

            for (int i = 0; i < data.getLength(); i++) {
                Element dataField = (Element) data.item(i);
                String tag = dataField.getAttribute("tag");
                if (tag.equals("200")) {
                    NodeList subfields = dataField.getElementsByTagNameNS("*", "*");
                    title = getSubfield(subfields,"a");
                }
                if (tag.equals("700")){
                    NodeList subfields = dataField.getElementsByTagNameNS("*", "*");
                    author = getSubfield(subfields,"a");
                }
                if (tag.equals("210")){
                    NodeList subfields = dataField.getElementsByTagNameNS("*", "*");
                    publicationYear = getSubfield(subfields,"z");
                    if (publicationYear==null){
                        publicationYear = getSubfield(subfields,"d");
                    }
                    editor = getSubfield(subfields,"c");
                }
            }
            System.out.println("idBnf:"+idBnf+" editor:"+editor+" title:"+title+"");
            if (!idBnf.contains("X")) {
                book = new Book(Long.parseLong(idBnf.replaceAll("-", "")), FREE, editor, title, author, publicationYear);
                books.add(book);
            }
        }
    }

    private String getSubfield(NodeList subfields, String find){
        for (int j = 0; j < subfields.getLength(); j++) {
            Element subfield = (Element) subfields.item(j);
            String code = subfield.getAttribute("code");
            if (code.equals(find)) {
                return subfield.getTextContent().trim();
            }
        }
        return null;
    }

    public Book getBook() {
        return book;
    }

    public List<Book> getBooks() {
        return books;
    }

    public static void main(String[] args){
        try {
            List<Book> b = new ConnectApi(null,"malade",null).getBooks();
            for (Book book:b) {
                System.out.println(book.afficher());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
