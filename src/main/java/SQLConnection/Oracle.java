package SQLConnection;

public class Oracle extends GenericConnection {

    public Oracle(String host, String port, String serviceName) throws Exception {
        super(host, port, serviceName);
        // does not supply Oracle-specific connection properties
        super.setUrl("jdbc:oracle:thin:@//" + super.getUrl());

        super.setConnection(super.getConnection());
    }

    // pass everything via parameters
    public Oracle(String host, String port, String serviceName, String username, String password) throws Exception {
        super(host, port, serviceName);
        // does not supply Oracle-specific connection properties
        super.setUrl("jdbc:oracle:thin:@//" + super.getUrl());

        super.setConnection(super.getConnection(username, password));
    }
}