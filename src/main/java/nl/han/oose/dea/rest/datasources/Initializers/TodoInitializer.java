package nl.han.oose.dea.rest.datasources.Initializers;

import nl.han.oose.dea.rest.datasources.exceptions.DBConnectionException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TodoInitializer {

    private Connection connection;

    private static final String[] TODOS = {
            "Boodschappen doen",
            "Stofzuigen",
            "Ramen Zemen",
            "Gras maaien",
            "Kamer opruimen",
            "Vaatwasser uitruimen",
            "Fietsband plakken",
            "Planten water geven"};

    public TodoInitializer(Connection connection) {
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
        var CreateQuery = "CREATE TABLE TODO(id int auto_increment primary key, item varchar(255), dueDate date)";

        var createPreparedStatement = connection.prepareStatement(CreateQuery);
        createPreparedStatement.executeUpdate();
        createPreparedStatement.close();
    }

    private void insertData() throws SQLException {
        List<String> todos = Arrays.asList(TODOS);

        for (String todoItem : todos) {
            var InsertQuery = "INSERT INTO TODO" + "(item, dueDate) values" + "(?,?)";
            var insertPreparedStatement = connection.prepareStatement(InsertQuery);

            insertPreparedStatement.setString(1, todoItem);
            insertPreparedStatement.setDate(2, Date.valueOf(createRandomDate()));

            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();
        }
    }

    public void logDBContent() throws SQLException {
        var SelectQuery = "select * from TODO";

        var selectPreparedStatement = connection.prepareStatement(SelectQuery);

        ResultSet rs = selectPreparedStatement.executeQuery();

        System.out.println("| Table Todo:");
        System.out.println("------------------------------------------------------------------");
        while (rs.next()) {
            System.out.println(
                    "| " + rs.getInt("id") +
                            "  " + rs.getString("item") +
                            " voor " + rs.getDate("dueDate"));
        }
        System.out.println("------------------------------------------------------------------");
        selectPreparedStatement.close();
    }

    private String createRandomDate() {
        Random random = new Random();
        var minDay = (int) LocalDate.now().toEpochDay();
        var maxDay = (int) LocalDate.now().plusYears(1).toEpochDay();
        var randomDay = minDay + random.nextInt(maxDay - minDay);

        var randomDate = LocalDate.ofEpochDay(randomDay);

        return randomDate.toString();
    }
}
