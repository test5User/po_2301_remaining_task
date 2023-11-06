package by.itclass.utils;

import by.itclass.exceptions.CompetitionException;
import by.itclass.model.Animal;
import by.itclass.model.Cat;
import by.itclass.model.Dog;
import by.itclass.model.Genus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AnimalFactory {
    private static final String DELIMITER = "[,;]";
    private static final String CHIP_REGEX = "(?=\\d{15}\\b)(?:112|643)09(?:81|56)\\d{8}";
    protected static final String EMAIL_REGEX = "[\\w!#$%&*+/=?^'`{|}~\\-]+(?:\\.[\\w!#$%&*+/=?^'`{|}~\\-]+)*@(?:[a-zA-Z\\d](?:[a-zA-Z\\d\\-]*[a-zA-Z\\d])?\\.)+[a-zA-Z\\d][a-zA-Z\\d\\-]*[a-zA-Z\\d]";
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static Animal getInstance(String textString) throws CompetitionException {
        String[] stringArray = textString.split(DELIMITER);
        try {
            long chipNumber = Long.parseLong(validateStringMatches(stringArray[0], CHIP_REGEX));
            String name = validateStringByEmpty(stringArray[2]);
            LocalDate birthDate = LocalDate.parse(stringArray[3], FORMATTER);
            String breed = validateStringByEmpty(stringArray[4]);
            String email = validateStringMatches(stringArray[5], EMAIL_REGEX);
//            if ("cat".equalsIgnoreCase(stringArray[1])) {
//                return new Cat(chipNumber, Genus.CAT, name, birthDate, breed, email);
//            } else {
//                return new Dog(chipNumber, Genus.of(stringArray[1]), name, birthDate, breed, email);
//            }
            return "cat".equals(stringArray[1])
                    ? new Cat(chipNumber, Genus.CAT, name, birthDate, breed, email)
                    : new Dog(chipNumber, Genus.of(stringArray[1]), name, birthDate, breed, email);
        } catch (IllegalStateException e) {
            throw new CompetitionException(e, textString);
        }
    }

    private static String validateStringMatches(String value, String regex) {
        if (value.matches(regex)) {
            return value;
        }
        throw new IllegalStateException("Chip number or email has invalid format");
    }

    private static String validateStringByEmpty(String value) {
        if (!value.isEmpty()) {
            return value;
        }
        throw new IllegalStateException("Name or Breed is empty");
    }
}
