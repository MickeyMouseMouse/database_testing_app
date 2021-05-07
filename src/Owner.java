public class Owner {
    private final String driverLicenseNumber;
    private final String name;
    private final String surname;
    private final String patronymic;

    public Owner(String driverLicenseNumber, String name, String surname, String patronymic) {
        this.driverLicenseNumber = driverLicenseNumber;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
    }

    public String getDriverLicenseNumber() { return driverLicenseNumber; }

    public String getName() { return name; }

    public String getSurname() { return surname; }

    public String getPatronymic() { return patronymic; }
}
