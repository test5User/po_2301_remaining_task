package by.itclass.utils;

import by.itclass.exceptions.CompetitionException;
import by.itclass.model.Animal;
import by.itclass.model.Cat;
import by.itclass.model.Dog;
import lombok.experimental.UtilityClass;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static by.itclass.utils.AnimalFactory.EMAIL_REGEX;
import static by.itclass.utils.AnimalFactory.FORMATTER;

@UtilityClass
public class CompetitionUtils {
    private static final String PATH_TO_FILE = "src/by/itclass/resources/animals.txt";
    private static final LocalDate AGE_DELIMITER = LocalDate.parse("30-12-2023", FORMATTER).minusYears(2);
    private static final Predicate<Animal> YOUNG_PREDICATE = it -> it.getBirthDate().isAfter(AGE_DELIMITER);
    private static final Predicate<Animal> OLD_PREDICATE = it -> it.getBirthDate().isBefore(AGE_DELIMITER);

    public static void parseFile(List<Cat> cats, List<Dog> dogs,
                                 Map<String, String> errors) {
        try (Scanner sc = new Scanner(new FileReader(PATH_TO_FILE))){
            while (sc.hasNextLine()) {
                //System.out.println(sc.nextLine());
                fillingCollections(sc.nextLine(), cats, dogs, errors);
            }
        } catch (FileNotFoundException e) {
            System.err.printf("File not found by path \"%s\"%n", PATH_TO_FILE);
        }
    }

    private static void fillingCollections(String textLine, List<Cat> cats, List<Dog> dogs,
                                           Map<String, String> errors) {
        try {
            Animal animal = AnimalFactory.getInstance(textLine);
            if (animal instanceof Cat) {
                cats.add((Cat) animal);
            } else {
                dogs.add((Dog) animal);
            }
        } catch (CompetitionException e) {
            processException(e, errors);
        }
    }

    private void processException(CompetitionException e, Map<String, String> errors) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(e.getErrorLine());
        if (matcher.find()) {
            errors.put(matcher.group(), String.format("Error in string \"%s\" - %s%n", e.getErrorLine(), e.getCause().getMessage()));
        }
    }

    public static <T> List<T> sortByBirthDate(List<T> animals) {
        return animals.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public static void printResults(List<Cat> cats, List<Dog> dogs,
                             Map<String, String> errors) {
        System.out.println("Cats list size: " + cats.size());
        printList(cats);
        System.out.println("Dogs list size: " + dogs.size());
        printList(dogs);
        printMap(errors);
    }

    private static <T> void printList(List<T> list) {
        list.forEach(System.out::println);
    }

    private static void printMap(Map<String, String> errors) {
        if (!errors.isEmpty()) {
            System.out.println("Errors quantity: " + errors.size());
            errors.forEach((key, value) -> System.out.print(key + "; " + value));
        }
    }

    public static <T extends Animal> List<T> filterAnimals(List<T> participant, boolean isYoung) {
        return participant.stream()
                .filter(isYoung ? YOUNG_PREDICATE : OLD_PREDICATE)
                .collect(Collectors.toList());
    }

    public static void printResults(List<Cat> youngCats, List<Dog> youngDogs,
                                    List<Cat> oldCats, List<Dog> oldDogs,
                                    Map<String, String> errors) {
        System.out.println("First day participants:");
        printListsParticipants(youngCats, youngDogs);
        System.out.println("Second day participants:");
        printListsParticipants(oldCats, oldDogs);
        printMap(errors);
    }

    private static void printListsParticipants(List<Cat> cats, List<Dog> dogs) {
        System.out.println("Cats list size: " + cats.size());
        printList(cats);
        System.out.println("Dogs list size: " + dogs.size());
        printList(dogs);
    }
}
