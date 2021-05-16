import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OwnersGenerator {
    public ArrayList<Owner> generate(int quantity) {
        final ArrayList<Owner> result = new ArrayList<>();

        final ArrayList<String> persons = Tools.getFileContent("resources/persons.txt");
        final ArrayList<String> driverLicenseNumbers = new ArrayList<>();
        for(int i = 0; i < quantity; i++) {
            // get a new driver license number
            String licenseNumber = getDriverLicenseNumber();
            while (driverLicenseNumbers.contains(licenseNumber)) {
                licenseNumber = getDriverLicenseNumber();
            }
            driverLicenseNumbers.add(licenseNumber);

            // get a new name, surname, patronymic
            String[] parts = (persons.get((int)(persons.size() * Math.random()))).split(" ");

            result.add(new Owner(licenseNumber, parts[1], parts[0], parts[2]));
        }
        return result;
    }

    public static final List<String> letters = Arrays.asList("Q", "W", "E", "R", "T",
            "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K",
            "L", "Z", "X", "C", "V", "B", "N", "M");
    public String getDriverLicenseNumber() {
        StringBuilder number = new StringBuilder();
        for(int i = 0; i < 10; i++) {
            if (Math.random() > 0.5)
                number.append(letters.get((int) (letters.size() * Math.random())));
            else
                number.append((int) (9 * Math.random()));
        }
        return number.toString();
    }
}
