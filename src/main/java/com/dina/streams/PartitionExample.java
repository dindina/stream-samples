package com.dina.streams;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PartitionExample {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8);

        Map<Boolean, List<Integer>> evenOddCount = numbers.stream().collect(
                Collectors.partitioningBy(num -> num % 2 == 0));

        System.out.println("even numbers " + evenOddCount.get(true));
        System.out.println("even numbers " + evenOddCount.get(false));

        // Partition numbers into even/odd and count them
        Map<Boolean, Long> countPartitionedNumbers = numbers.stream()
                .collect(Collectors.partitioningBy(num -> num % 2 == 0, Collectors.counting()));

        System.out.println("Count of even numbers (true): " + countPartitionedNumbers.get(true));
        System.out.println("Count of odd numbers (false): " + countPartitionedNumbers.get(false));

    }

}
