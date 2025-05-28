package com.dina.streams;

import java.util.Arrays;
import java.util.List;

public class ArraysAndListOf {
    public static void main(String[] args) {
        // List.of()
        System.out.println("List.of():");
        List<String> immutableList = List.of("Apple", "Banana", "Cherry");
        System.out.println("Created with List.of(): " + immutableList);
        try {
            immutableList.add("Date"); // This will throw UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("List.of() - add operation: " + e);
        }
        try {
            immutableList.set(0, "Apricot"); // This will throw UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("List.of() - set operation: " + e);
        }
        try {
            List.of("one", null, "three"); // This will throw NullPointerException
        } catch (NullPointerException e) {
            System.out.println("List.of() - with null element: " + e);
        }


        // Arrays.asList()
        System.out.println("\nArrays.asList():");
        String[] array = {"Dog", "Cat", "Elephant"};
        List<String> fixedSizeList = Arrays.asList(array); // Backed by the 'array'
        // Or: List<String> fixedSizeList = Arrays.asList("Dog", "Cat", "Elephant");
        System.out.println("Created with Arrays.asList(): " + fixedSizeList);
        try {
            fixedSizeList.add("Fish"); // This will throw UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("Arrays.asList() - add operation: " + e);
        }
        fixedSizeList.set(0, "Puppy"); // This is allowed
        System.out.println("Arrays.asList() - after set(0, \"Puppy\"): " + fixedSizeList);
        System.out.println("Original array after set on list: " + Arrays.toString(array)); // Shows the array is also modified




        List<String> listWithNull = Arrays.asList("one", null, "three");
        System.out.println("Arrays.asList() - with null element: " + listWithNull);

    }

}
