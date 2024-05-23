package group.projetcybooks.serveur;

import group.projetcybooks.serveur.model.Book;

import java.sql.*;

/**
 * This class is for have a connexion to a MySQL database
 * Attributes :
 *  String <b>url</b>    The URL of the database.
 *  String <b>user</b>   The username for the database connexion.
 *  String <b>pwd</b>    The password for the database connexion.
 */
public class ConnectDB {
    private String url;
    private String user;
    private String pwd;

    /**
     * Initializes the database connection parameters.
     *
     * @throws Exception If an error occurs while initializing the parameters.
     */
    public ConnectDB() throws Exception {
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
     * Executes a SELECT SQL query on the database.
     *
     * @param request The SQL query to be executed.
     * @return A string containing the results of the query, with columns separated by semicolons and rows separated by slashes.
     * @throws Exception If an error occurs while executing the query.
     */
    public String RequestSelectDB(String request) throws Exception {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(request);
        ResultSetMetaData rsmd = rs.getMetaData();

        String output = "";
        int columnCount = rsmd.getColumnCount();

        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                output += rs.getString(i);
                if (i < columnCount) {
                    output += ";";
                }
            }
            output += "/";
        }

        rs.close();
        stmt.close();
        conn.close();

        return output;
    }

    /**
     * Executes an INSERT SQL query on the database.
     *
     * @param request The SQL query to be executed.
     * @throws Exception If an error occurs while executing the query.
     */
    public void requestInsertDB(String request) throws Exception {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement stmt = conn.createStatement();

        stmt.executeUpdate(request);

        stmt.close();
        conn.close();
    }
}