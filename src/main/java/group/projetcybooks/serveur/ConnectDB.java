package group.projetcybooks.serveur;

import group.projetcybooks.serveur.model.Book;

import java.sql.*;

/**
 * This class is for have a connexion to a MySQL database
 */
public class ConnectDB {
    private String url;
    private String user;
    private String pwd;

    /**
     * initialised parameter
     * @throws Exception;
     */
    public ConnectDB() throws Exception{
        /*
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream("config.properties");
        properties.load(fis);

        url = properties.getProperty("database.url");
        user = properties.getProperty("database.user");
        pwd = properties.getProperty("database.password");
        */
        url = "jdbc:mysql://localhost:3308/cybook?serverTimezone=UTC";
        user = "root";
        pwd = "";
    }

    /**
     * Do a request to a database
     * @param request SQL request
     * @return String of return request
     * @throws Exception;
     */
    public String RequestSelectDB(String request) throws Exception{
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(request);
        ResultSetMetaData rsmd = rs.getMetaData();

        String output="";
        int columnCount = rsmd.getColumnCount();

        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                output += rs.getString(i);
                if (i < columnCount) {
                    output += ";";
                }
            }
            output+="/";
        }

        rs.close();
        stmt.close();
        conn.close();

        return output;
    }

    public void requestInsertDB(String request) throws Exception{
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement stmt = conn.createStatement();

        stmt.executeUpdate(request);

        stmt.close();
        conn.close();
    }

    public void requestInsertBookDB(Book book) throws Exception{
        requestInsertDB(STR."INSERT INTO `book` (`isbn`, `statue`, `editor`, `title`,`year`,`genre`) VALUES ('\{book.getISBN()}', '\{book.getStatue()}', '\{book.getEditor()}', '\{book.getTitle()}','\{book.getYear()}','\{book.getGenre()}');");
    }


    public static void main(String[] args){
        try {
            System.out.println(new ConnectDB().RequestSelectDB("SELECT * FROM test"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
