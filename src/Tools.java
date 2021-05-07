import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class Tools {
    static Connection connect() {
        final String url = "jdbc:postgresql://localhost:5432/Logbook";
        Connection result = null;
        try {
             result = DriverManager.getConnection(url, "postgres", "admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert result != null;
        return result;
    }

    static Connection connect(int isolationLevel) {
        Connection result = connect();
        try {
            result.setTransactionIsolation(isolationLevel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    static void disconnect(Connection db) {
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static ArrayList<String> getFileContent(String path) {
        final ArrayList<String> result = new ArrayList<>();
        final BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        }
        String line;
        try {
            line = reader.readLine();
            while (line != null) {
                result.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    static void writeTimeResultsToFile(String path, ArrayList<Long> data) {
        try (FileWriter writer = new FileWriter(path)) {
            for (Long item : data) {
                writer.write(item / 1000000.0 + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}