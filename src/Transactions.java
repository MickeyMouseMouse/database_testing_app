import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Transactions {
    public static ArrayList<Long> selectOwners(Connection db, int transactionsNumber) {
        ArrayList<Long> result = new ArrayList<>();
        for (int i = 0; i < transactionsNumber; i++) {
            long startTime = System.nanoTime();
            while (true) {
                try (Statement statement = db.createStatement()) {
                    statement.execute("SELECT surname FROM Owners WHERE name LIKE 'А%'");
                } catch (SQLException e) {
                    if (e.getSQLState().equals("40001")) // serializable error
                        continue;
                    else
                        e.printStackTrace();
                }
                break;
            }
            result.add(System.nanoTime() - startTime);
        }
        return result;
    }

    public static ArrayList<Long> insertOwners(Connection db, int transactionsNumber, ArrayList<Owner> owners) {
        ArrayList<Long> result = new ArrayList<>();
        for (int i = 0; i < transactionsNumber; i++) {
            long startTime = System.nanoTime();
            while (true) {
                try (Statement statement = db.createStatement()) {
                    statement.execute(String.format("INSERT INTO Owners (driver_license_number, name, surname, patronymic) VALUES ('%s','%s','%s','%s')",
                            owners.get(i).getDriverLicenseNumber(), owners.get(i).getName(), owners.get(i).getSurname(), owners.get(i).getPatronymic()));
                } catch (SQLException e) {
                    if (e.getSQLState().equals("40001"))// serializable error
                        continue;
                    else
                        e.printStackTrace();
                }
                break;
            }
            result.add(System.nanoTime() - startTime);
        }
        return result;
    }

    public static ArrayList<Long> updateOwners(Connection db, int transactionsNumber) {
        ArrayList<Long> result = new ArrayList<>();
        for (int i = 0; i < transactionsNumber; i++) {
            long startTime = System.nanoTime();
            while (true) {
                try (Statement statement = db.createStatement()) {
                    statement.executeUpdate("UPDATE Owners SET surname = 'ИВАНЕНКО' WHERE name LIKE 'А%'");
                } catch (SQLException e) {
                    if (e.getSQLState().equals("40001"))// serializable error
                        continue;
                    else
                        e.printStackTrace();
                }
                break;
            }
            result.add(System.nanoTime() - startTime);
        }
        return result;
    }
}
