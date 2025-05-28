package com.dina.streams;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FilterExample {

    public static void main(String[] args) {
        List<Integer> list = List.of(12,2,4,5,5,6);
        // Example  1 filter and get even numbers in aother list
        List<Integer> even  = list.stream().filter(no -> no%2 == 0).collect(Collectors.toList());
        System.out.println(even);

        List<String> names = List.of("Alice", "Bob", "Charlie", "Dave", "Eve","Charlie");
        // example 2 filter string by length
        Set<String> namesSet4 = names.stream().filter(name -> name.length()>4).collect(Collectors.toSet());
        List<String> namesList4 = names.stream().filter(name -> name.length()>4).toList();
        System.out.println(namesSet4);
        System.out.println(namesList4);


        // Example 3: Filter strings starting with a specific letter

        List<String> namesWithA = names.stream().filter(name -> name.startsWith("C")).collect(Collectors.toList());
        System.out.println(namesWithA);

        // Example 4: Filter out null values
        List<String> namesWithNulls = Arrays.asList("John", null, "Jane", null, "Peter");
        List<String> namesWithOutNulls = namesWithNulls.stream().filter(name -> name!=null).toList();
        System.out.println(namesWithOutNulls);

    }
}
