package org.adoptopenjdk.lambda;

import org.adoptopenjdk.lambda.tutorial.exercise1.Shape;
import org.adoptopenjdk.lambda.tutorial.exercise2.Person;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.adoptopenjdk.lambda.tutorial.exercise1.Color.*;

/**
 * The Lambda Cheat sheet is by no means thorough but should be enough to get you through the Lambda Tutorial along
 * with the excellent description provided with the Exercises and classes.
 *
 * Some of the examples were taken from Oracle's Lambda Tutorial by Stuart Marks given at JavaOne 2013.
 * @see <http://stuartmarks.files.wordpress.com/2013/09/tut3877_marks-jumpstartinglambda-v6.pdf>
 */
public class LambdaCheatSheet {

  public void differentFormsOfFunctionalInterfaces() {

    OptionalLong lastClicked = OptionalLong.empty();

    // Old Way
    ActionListener al = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.out.printf("Event happened at %d%n", e.getWhen());
      }
    };

    /* Lambda Expressions take the form

           (params) -> methodCall(params)

       But you can have more */

    ActionListener alSimple =
        (event) -> System.out.printf("Event happened at %d%n", event.getWhen());

    ActionListener alWithTypes =
        (ActionEvent event) -> System.out.printf("Event happened at %d%n", event.getWhen());

    ActionListener alWithBody =
        (ActionEvent event) -> {
          System.out.printf("Event happened at %d%n", event.getWhen());
          System.out.printf("Event command %s%n", event.getActionCommand());
        };


    /**
     * What about methods that take more than one operator and return something?
     */
    // inner class
    Comparator<ActionEvent> comparator = new Comparator<ActionEvent>() {
      @Override
      public int compare(ActionEvent o1, ActionEvent o2) {
        Objects.requireNonNull(o1); Objects.requireNonNull(o2);
        return o1.getWhen() < o2.getWhen() ? -1 : o1.getWhen() == o2.getWhen() ? 0 : 1;
      }
    };

    // this comparator takes two params and returns an int, all in one line
    Comparator<ActionEvent> comparatorNoNulls =
        (o1, o2) -> o1.getWhen() < o2.getWhen() ? -1 : o1.getWhen() == o2.getWhen() ? 0 : 1;

    // lambda body can be surrounded with braces for multiple lines
    Comparator<ActionEvent> comparatorLambda = (o1, o2) -> {
      Objects.requireNonNull(o1); Objects.requireNonNull(o2);
      return o1.getWhen() < o2.getWhen() ? -1 : o1.getWhen() == o2.getWhen() ? 0 : 1;
      // need a return statement too whereas before it was implied
    };

  }


  public void methodsOnCollections() {
    // New methods have been added to collections class to let us do things with streams

    List<Shape> shapes = asList(new Shape(RED), new Shape(BLACK), new Shape(YELLOW));
    shapes.forEach((s) -> s.setColor(RED));
    shapes.removeIf((s) -> s.getColor() == RED);


  }


  public void functionalInterfaces() {

    // Predicates return a boolean
    Predicate<Person> over18test = person -> person.getAge() >= 18;
    // Consumers are functions that return void
    Consumer<Person> p = person -> person.toString();
    // Functions turn one form into another
    Function<Person, Integer> personsAgeNextYear = person -> person.getAge() + 1;
    // Suppliers
    Supplier<String> nameSupplier = () -> asList("Tommy", "Ozzy", "Bill", "Geezer").get(new Random().nextInt(4));

    // See the Java Util Function package for many more
    // BiFunction, Unary Operator

    /**
     * They are declared with @FunctionalInterface, meaning if someone modifies those
     * interfaces to have more than one undeclared method, the compiler will issue an
     * error.
     */

  }


  public void streamOperations() {
    String phrase = "Every problem in computer science can be solved by adding another level of indirection";
    List<String> list = Arrays.asList(phrase.split(" "));

    // map
    list.stream()
        .map(s -> s.toUpperCase())
        .forEach(s -> System.out.print(s + " "));

    // filter
    list.stream()
        .filter(s -> (s.length() & 1) == 0)
        .forEach(s -> System.out.print(s + " "));

/*
    // substream
    list.stream()
        .substream(3, 8)
        .forEach(s -> System.out.print(s + " "));

        TODO: doesnt work in b114
*/

    // limit the stream
    list.stream()
        .limit(5)
        .forEach(s -> System.out.print(s + " "));

    // sorted
    list.stream()
        .sorted()
        .forEach(s -> System.out.print(s + " "));

    // distinct
    list.stream()
        .map(s -> s.substring(0,1))
        .distinct()
        .forEach(s -> System.out.print(s + " "));

  }


  public void terminalOperations() {
    String phrase = "Every problem in computer science can be solved by adding another level of indirection";
    List<String> list = Arrays.asList(phrase.split(" "));

    List<String> output =
        list.stream()
            .map(s -> s.toUpperCase())
            .collect(toList());

    String[] mapOut =
        list.stream()
            .map(s -> s.toUpperCase())
            .toArray(n -> new String[n]);
    System.out.println(mapOut);

    long count =
        list.stream()
            .filter(s -> s.length() <= 3)
            .count();
    System.out.println(count);

    boolean anyMatch =
        list.stream()
            .anyMatch(s -> s.startsWith("c"));
    System.out.println(anyMatch);

    Optional<String> findFirst =
        list.stream()
            .filter(s -> s.startsWith("c"))
            .findFirst();
    System.out.println(findFirst.get());

    Optional<String> findFirstWithElse =
        list.stream()
            .filter(s -> s.startsWith("x"))
            .findFirst();
    System.out.println(findFirstWithElse.orElse("not found"));
  }

  public void primitives() {
    /**
     * Each functional interface uses generics to work on a range of any types.
     * To support primitives there are versions of the stream methods that work on Int, Long and Double types.
     */
    List<Person> persons = asList(new Person("Joey", 18), new Person("Phil", 27));
    persons.stream().mapToInt(Person::getAge).sum();

    // Look for methods suffixed ToInt, ToLong, ToDouble if you need to deal with primitives
  }
}
