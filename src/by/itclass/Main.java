package by.itclass;

import by.itclass.model.Cat;
import by.itclass.model.Dog;
import by.itclass.model.Genus;
import by.itclass.utils.CompetitionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.itclass.utils.CompetitionUtils.*;

public class Main {
    private static List<Cat> cats = new ArrayList<>();
    private static List<Dog> dogs = new ArrayList<>();
    private static Map<String, String> errors = new HashMap<>();

    public static void main(String[] args) {
        //        Genus animal = Genus.of("cat");
        //        System.out.println("I am a " + animal + " and my value is " + animal.getValue() );

        parseFile(cats, dogs, errors);

        List<Cat> sortedCats = sortByBirthDate(cats);
        List<Dog> sortedDogs = sortByBirthDate(dogs);
        printResults(sortedCats, sortedDogs, errors);

        System.out.println("__________________________________________________");

        List<Cat> youngCats = filterAnimals(sortByBirthDate(cats), true);
        List<Dog> youngDogs = filterAnimals(sortByBirthDate(dogs), true);
        List<Cat> oldCats = filterAnimals(sortByBirthDate(cats), false);
        List<Dog> oldDogs = filterAnimals(sortByBirthDate(dogs), false);

        printResults(youngCats, youngDogs, oldCats, oldDogs, errors);
    }
}
