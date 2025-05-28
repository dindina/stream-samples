package com.dina.streams;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FilterExample {

    public static void main(String[] args) {
        List<Integer> list = List.of(12,2,4,5,5,6);
        // Example 1: filter and get even numbers in another list
        List<Integer> evenNumbers  = list.stream().filter(no -> no%2 == 0).collect(Collectors.toList()); // Mutable list
        System.out.println("Even numbers (mutable list): " + evenNumbers);
        evenNumbers.add(100); // This is allowed
        System.out.println("Even numbers after add: " + evenNumbers);

        List<String> names = List.of("Alice", "Bob", "Charlie", "Dave", "Eve","Charlie");
        // Example 2: filter string by length
        Set<String> namesSet4 = names.stream().filter(name -> name.length()>4).collect(Collectors.toSet());
        List<String> namesList4Unmodifiable = names.stream().filter(name -> name.length()>4).toList(); // Unmodifiable list
        System.out.println("Names > 4 chars (Set): " + namesSet4);
        System.out.println("Names > 4 chars (Unmodifiable List): " + namesList4Unmodifiable);
        try {
            namesList4Unmodifiable.add("ExtraLongName");
        } catch (UnsupportedOperationException e) {
            System.out.println("Attempted to add to unmodifiable list created by .toList(): " + e.getMessage());
        }


        // Example 3: Filter strings starting with a specific letter
        List<String> namesWithC = names.stream().filter(name -> name.startsWith("C")).collect(Collectors.toList());
        System.out.println("Names starting with 'C': " + namesWithC);

        // Example 4: Filter out null values
        List<String> namesWithNulls = Arrays.asList("John", null, "Jane", null, "Peter");
        // Using Objects::nonNull for clarity and conciseness
        List<String> namesWithoutNulls = namesWithNulls.stream().filter(Objects::nonNull).toList();
        System.out.println("Original list with nulls: " + namesWithNulls);
        System.out.println("List without nulls (Unmodifiable): " + namesWithoutNulls);
        try {
            namesWithoutNulls.add("AnotherName");
        } catch (UnsupportedOperationException e) {
            System.out.println("Attempted to add to unmodifiable list (from null filtering): " + e.getMessage());
        }

    }
}
