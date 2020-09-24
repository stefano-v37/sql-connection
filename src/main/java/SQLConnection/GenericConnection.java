package SQLConnection;

import lombok.Getter;
import lombok.Setter;

import java.sql.*;
import java.util.*;

@Getter
@Setter
public class GenericConnection {

    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url;
    private Connection connection;


    // constructors
    // ask user and password in CL
    public GenericConnection(String host, String port, String database) throws Exception {
        this.host = host;
        this.port = port;
        this.database = database;

        this.url = this.host + ":" + this.port + "/" + this.database;
    }

    // pass everything as parameter
    public GenericConnection(String host, String port, String database, String user, String password) throws Exception {
        this.host = host;
        this.port = port;
        this.database = database;

        this.url = this.host + ":" + this.port + "/" + this.database;
    }


    // create connection method, not working for generic class but needed for polymorphism
    public Connection getConnection()  throws Exception{
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter username and password");
            String username = scanner.next();
            String password = scanner.next();

            Class.forName(this.driver);

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected");
            return conn;
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }

    // override for second constructor
    public Connection getConnection(String username, String password)  throws Exception{
        try {
            Class.forName(this.driver);

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected");
            return conn;
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void close() throws SQLException {
        // it doesn't seem necessary
        try{
            this.connection.close();
            System.out.println("Connection closed");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }


    // SQL actions
    public void executeSQL(String sqlcode){
        try {
            PreparedStatement sql = this.connection.prepareStatement(sqlcode);
            sql.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public ResultSet executeSQL(String sqlcode, Boolean _return){
        if (_return){
            try {
                Statement statement = this.connection.createStatement();
                ResultSet result = statement.executeQuery(sqlcode);
                return result;
            }
            catch (SQLException e){
                System.out.println(e);
                return null;
            }
        }
        else{
            executeSQL(sqlcode);
            return null;
        }

    }

    // static methods to process ResultSet
    public static int getColumnsNumber(ResultSet result) throws SQLException {
        ResultSetMetaData metadata = result.getMetaData();
        int columnNumber = metadata.getColumnCount();
        return columnNumber;
    }

    public static List<String> getColumnsLabels(ResultSet result) throws SQLException {
        List <String> columnsLabels = new ArrayList<String>();
        // please note that columns count starts from 1 (JDBC documentation)
        for (int i = 1; i <= getColumnsNumber(result); i ++){
            columnsLabels.add(result.getMetaData().getColumnLabel(i));
        }
        return columnsLabels;
    }

    public static List<String> getColumnsTypeName(ResultSet result) throws SQLException {
        List <String> columnsTypeName = new ArrayList<String>();
        // please note that columns count starts from 1 (JDBC documentation)
        for (int i = 1; i <= getColumnsNumber(result); i ++){
            columnsTypeName.add(result.getMetaData().getColumnTypeName(i));
        }
        return columnsTypeName;
    }

    public Map<String, ArrayList<Object>> getColumns(ResultSet result) throws SQLException{
        int columns = getColumnsNumber(result);
        List columnsLabels = getColumnsLabels(result);
        System.out.println(columns);


        Map<String, ArrayList<Object>> df = new LinkedHashMap<String, ArrayList<Object>>(columns);
        for (int i = 0; i < columns; i++) {
            df.put((String)columnsLabels.get(i), new ArrayList<>());
        }

        while (result.next()){
            for (int i = 0; i < columns; i++){
                df.get(columnsLabels.get(i)).add(getItem(result,i+1));
            }
        }

        return df;
    }

    public Object getItem(ResultSet result, String columnName) throws SQLException {
        Object item = result.getObject(columnName);
        return item;
    }

    public Object getItem(ResultSet result, int columnIndex) throws SQLException {
        Object item = result.getObject(columnIndex);
        return item;
    }

    // tricky, because ResultSet cursor cannot be set back on .beforeFirst(), it's possible using a different statement which happens to be reducing perfomance
    public <T> List<T> getColumn(ResultSet result, String columnName) throws SQLException{
        ArrayList column = new ArrayList<T>();
        // check condition and apply method that moves
            while (result.next()){
                System.out.println("ok");
                column.add(result.getObject(columnName));
            }
        return column;
    }

}
