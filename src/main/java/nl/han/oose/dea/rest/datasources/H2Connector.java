package nl.han.oose.dea.rest.datasources;

import nl.han.oose.dea.rest.datasources.Initializers.ShoppingListInitializer;
import nl.han.oose.dea.rest.datasources.Initializers.TodoInitializer;
import nl.han.oose.dea.rest.datasources.exceptions.DBConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class H2Connector {

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    private TodoInitializer todoInitializer;
    private ShoppingListInitializer shoppingListInitializer;

    private H2Connector() {
        // private construction to prevent instantiation. Use the static create() method.
    }

    static Connection create() {
        var h2Connector = new H2Connector();

        var dbConnection = h2Connector.createConnection();
        h2Connector.initializeDB(dbConnection);

        h2Connector.logDBContent();

        return dbConnection;
    }

    private Connection createConnection() {
        loadDriver();
        Connection dbConnection = getConnection();
        return dbConnection;
    }

    private Connection getConnection() {
        Connection dbConnection;
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);

        } catch (SQLException e) {
            throw new DBConnectionException(e);
        }
        return dbConnection;
    }

    private void loadDriver() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new DBConnectionException(e);
        }
    }

    private void initializeDB(Connection dbConnection) {
        todoInitializer = new TodoInitializer(dbConnection);
        todoInitializer.initialize();
        shoppingListInitializer = new ShoppingListInitializer(dbConnection);
        shoppingListInitializer.initialize();
    }

    private void logDBContent() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("| H2 In-Memory Database created and configured");
        System.out.println("------------------------------------------------------------------");

        try {
            todoInitializer.logDBContent();
            shoppingListInitializer.logDBContent();
        } catch (SQLException e) {
            throw new DBConnectionException(e);
        }
    }
}
