package com.dina.streams;

import java.util.List;

// Helper class for Example 4 & 5
class User {
    private String name;
    public User(String name) { this.name = name; }
    public String getName() { return name; }
    @Override public String toString() { return "User{name='" + name + "'}"; }
}

// Helper class for Example 6
class UserDetails {
    String name;
    int score;
    boolean startsWithA;

    public UserDetails(String name, int score, boolean startsWithA) {
        this.name = name;
        this.score = score;
        this.startsWithA = startsWithA;
    }

    @Override
    public String toString() {
        return "UserDetails{name='" + name + "', score=" + score + ", startsWithA=" + startsWithA + '}';
    }
}
public class MapExample {

    public static void main(String[] args) {

        List<String> strings = List.of("dinesh","saran","ami","avi");
        // List.of is immutable , cannot add, remove or update
        //strings.remove(1);
        // Example 1 convert to a list with uppercase

        List<String> neeList = strings.stream().map(item -> item.toUpperCase()).toList(); // using to List returns an immutable list
        System.out.println(neeList);

        // Example 2: Get the length of each string (String to Integer)
        List<Integer> lengthList = strings.stream().map(item -> item.length()).toList(); // using to List returns an immutable list
        System.out.println(lengthList);

        // Example 3: Square each number in a list of integers
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        List<Integer> squares = numbers.stream().map(num -> num*num).toList();
        System.out.println(squares);

         // Example 4: Create User objects from a list of names
        List<String> userNames = List.of("Alice", "Bob", "Charlie"); // immutable list , cannot change anything or update , delete
        System.out.println("User names: " + userNames);

        List<User> userObjects = userNames.stream().map(item -> new User(item)).toList();
        System.out.println("userObjects: " + userObjects);

        // You could then, for example, extract just the names again or other properties
        List<String> extractedNames = userObjects.stream()
                                           .map(User::getName) // Method reference for user -> user.getName()
                                           .toList();
        System.out.println("Extracted names from User objects: " + extractedNames);


        // Example 5: Chaining map operations
        // Convert strings to uppercase, then get their lengths
        List<Integer> lengthsOfUppercaseStrings = strings.stream()
                .map(String::toUpperCase)      // First map: "dinesh" -> "DINESH"
                .map(String::length)           // Second map: "DINESH" -> 6
                .toList();
        System.out.println("Lengths of uppercase strings (chained map): " + lengthsOfUppercaseStrings);

        // Example 6: Transforming to a different object type with more properties
        List<UserDetails> userDetailsList = strings.stream()
                .map(name -> new UserDetails(name, name.length() * 10, name.startsWith("a")))
                .toList();
        System.out.println("User details objects: " + userDetailsList);












    }

}
