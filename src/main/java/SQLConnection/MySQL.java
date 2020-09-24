package SQLConnection;

public class MySQL extends GenericConnection{

    // constructors
    // ask username and password via CL
    public MySQL(String host, String port, String database) throws Exception {
        super(host, port, database);
        super.setUrl("jdbc:mysql://" + super.getUrl() + "?serverTimezone=UTC");

        super.setConnection(super.getConnection());
    }

    // pass everything via parameters
    public MySQL(String host, String port, String database, String username, String password) throws Exception {
        super(host, port, database);
        super.setUrl("jdbc:mysql://" + super.getUrl() + "?serverTimezone=UTC");

        super.setConnection(super.getConnection(username, password));
    }
}
