package nl.han.oose.dea.rest.datasources;

import java.sql.Connection;

/**
 * A Simple Factory for creating instances of {@link Connection}.
 */
public class ConnectorFactory {

    private static Connection connector;

    /**
     * Create a new instance of {@link Connection}.
     *
     * @return An instance of {@link Connection} configured with a filled in-memory H2 database.
     */
    public static Connection createH2Connection() {
        if (connector == null) {
            connector = H2Connector.create();
        }

        return connector;
    }
}
