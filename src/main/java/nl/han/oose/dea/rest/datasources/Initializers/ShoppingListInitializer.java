package nl.han.oose.dea.rest.datasources.Initializers;

import nl.han.oose.dea.rest.datasources.exceptions.DBConnectionException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ShoppingListInitializer {

    private Connection connection;

    private static final String[] SHOPPING_LIST = {
            "Pindakaas",
            "Sambal",
            "Brood",
            "Patak Curry Pasta",
            "Coca Cola 1,5l",
            "Magnum Classic",
            "Sperziebonen, 400gram",
            "Appels, Pink Lady",
            "Bananen",
            "Mandarijnen",
            "Kaas, Oud",
            "Paprika Chips"};

    public ShoppingListInitializer(Connection connection) {
        this.connection = connection;
    }

    public void initialize() {
        try {
            createSchema();
            insertData();
        } catch (SQLException e) {
            throw new DBConnectionException(e);
        }
    }

    private void createSchema() throws SQLException {
        var CreateQuery = "CREATE TABLE SHOPPINGLIST(id int auto_increment primary key, item varchar(255), amount int)";

        var createPreparedStatement = connection.prepareStatement(CreateQuery);
        createPreparedStatement.executeUpdate();
        createPreparedStatement.close();
    }

    private void insertData() throws SQLException {
        List<String> todos = Arrays.asList(SHOPPING_LIST);

        for (String todoItem : todos) {
            var InsertQuery = "INSERT INTO SHOPPINGLIST" + "(item, amount) values" + "(?,?)";
            var insertPreparedStatement = connection.prepareStatement(InsertQuery);

            insertPreparedStatement.setString(1, todoItem);
            insertPreparedStatement.setInt(2, (int) (Math.random() * 3) + 1);

            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();
        }
    }

    public void logDBContent() throws SQLException {
        var SelectQuery = "select * from SHOPPINGLIST";

        var selectPreparedStatement = connection.prepareStatement(SelectQuery);

        ResultSet rs = selectPreparedStatement.executeQuery();

        System.out.println("| Table Shopping List:");
        System.out.println("------------------------------------------------------------------");
        while (rs.next()) {
            System.out.println(
                    "| " + rs.getInt("id") +
                            "  " + rs.getString("item") +
                            " (" + rs.getInt("amount") + ")");
        }
        System.out.println("------------------------------------------------------------------");
        selectPreparedStatement.close();
    }
}
