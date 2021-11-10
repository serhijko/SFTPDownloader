package by.sp.sftpdownloader;

import org.sqlite.core.DB;

import java.sql.*;
import java.time.LocalDateTime;

public class SQLiteMediator {

    private final String sql_user;
    private final String sql_password;
    private final String DB_NAME;
    private final String DB_DIR = "~/db/";

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public SQLiteMediator(String sql_user, String sql_password, String sql_database) {
        this.sql_user = sql_user;
        this.sql_password = sql_password;
        DB_NAME = sql_database;
    }

    public void connectDB() throws ClassNotFoundException, SQLException {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        DirectoriesCreator.createDir(DB_DIR);
        connection = DriverManager.getConnection("jdbc:sqlite:" + DB_DIR + DB_NAME);
    }

    public void deleteTable() throws SQLException {
        statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS files");
    }

    public void createTable() throws SQLException {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE files (" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'filename' VARCHAR(255) NOT NULL," +
                "'datetime' datetime NOT NULL);");
    }

    public void insert(String filename, LocalDateTime datetime) throws SQLException {
        String query = "INSERT INTO files (filename, datetime) " +
                "VALUES ('" + filename + "', '" + datetime + "')";
        statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    public void readDB() throws SQLException {
        resultSet = statement.executeQuery("SELECT * FROM files");

        System.out.println("These files have been copied (date and time of its copying):");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String filename = resultSet.getString("filename");
            String datetime = resultSet.getString("datetime");
            System.out.println(id + ": " + filename + "\t(" + datetime + ")");
        }
    }

    public void closeDB() throws SQLException {
        connection.close();
        statement.close();
        resultSet.close();
    }
}
