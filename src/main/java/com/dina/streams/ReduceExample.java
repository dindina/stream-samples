package com.dina.streams;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

class Product {
    String name;
    double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}

public class ReduceExample {

    public static void main(String[] args) {

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        List<String> words = List.of("hello", " ", "world", " ", "java", " ", "streams");
        List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");

        // Example 1: Sum of numbers using reduce(identity, accumulator)
        // The identity is 0 (the starting sum).
        // The accumulator (a, b) -> a + b takes the current sum 'a' and the next element 'b' and adds them.

        int sum = numbers.stream().reduce(0, (a,b) -> a+b);
        System.out.println(sum);

        // Example 1.1: Sum of numbers using method reference Integer::sum
        int sumWithMethodRef = numbers.stream()
                                      .reduce(0, Integer::sum);
        System.out.println("Example 1.1 - Sum with method reference: " + sumWithMethodRef);

        // Example 2: max of the array reduce(accumulator)
        // The identity is 1 (the starting product).

        int max = numbers.stream().reduce(Integer :: max ).orElse(0);
        System.out.println(max);

        // Example 3: Product of numbers using reduce(identity, accumulator)
        // The identity is 1 (the starting product).
        int product = numbers.stream()
                             .reduce(1, (currentProduct, number) -> currentProduct * number);
        System.out.println("Example 2 - Product of numbers: " + product);

        // Example 4: Concatenate strings using reduce(identity, accumulator)

        String concatString = words.stream().reduce("", (a,b) -> a.concat(b));
        System.out.println(concatString);

        // Example 7: Sum of lengths of strings using reduce(identity, accumulator, combiner)
        // This form is particularly useful for parallel streams or when the accumulator
        // transforms elements into a type different from the stream elements.
        // Here, U is Integer (for sum of lengths), T is String (stream elements).
        // identity = 0 (initial sum of lengths)
        // accumulator = (partialSum, str) -> partialSum + str.length()
        //   - Takes current partialSum (Integer) and a string (str), returns new partialSum (Integer).
        // combiner = (sum1, sum2) -> sum1 + sum2 (or Integer::sum)
        //   - Combines two partial sums (both Integers) if the stream is processed in parallel.
        //   - For sequential streams, the combiner might not be called if the accumulator's result type
        //     is the same as the identity type and the stream element type. However, it's essential
        //     when the accumulator changes types or for parallelism.

        int totalLengthOfNames = names.stream()
                    .reduce(0,(a,b)-> a+b.length() , Integer::sum);
        System.out.println("Example 7 - Total length of all names: " + totalLengthOfNames);


        // Example 8: Calculating total price of products (illustrating combiner with objects)
        List<Product> products = Arrays.asList(
                new Product("Laptop", -1200.00),
                new Product("Mouse", 25.00),
                new Product("Keyboard", 75.00),
                new Product("Monitor", 300.00)
        );

        //double totalPrice = products.stream().reduce(0.0,(partialTotal, product) -> partialTotal + product.getPrice() , Double :: sum );
        double totalPrice = products.stream()
                .reduce(0.0, // Identity: initial total price
                        (partialTotal, currentProd) -> partialTotal + currentProd.getPrice(), // Accumulator
                        Double::sum); // Combiner
        System.out.println(totalPrice);

         // Example 8.1: Same as above, but using map before reduce (often clearer for such cases)
        double totalPriceMapped = products.stream()
                                          .mapToDouble(Product::getPrice) // map to a DoubleStream
                                          .sum(); // sum is a specialized reduction on numeric streams
        System.out.println("Example 8.1 - Total price using mapToDouble().sum(): " + totalPriceMapped);

        System.out.println("\n--- Examples with Parallel Streams ---");

        // Example 9: Sum of numbers in parallel
        // The identity, accumulator, and combiner are the same as for sequential sum.
        // The stream is processed in parallel, and the combiner (Integer::sum) merges partial sums.
        System.out.println("Available processors: " + ForkJoinPool.commonPool().getParallelism()); // Shows default parallelism
        long startTimeParallelSum = System.nanoTime();
        int parallelSum = numbers.parallelStream() // Note: .parallelStream() or .stream().parallel()
                                 .reduce(0, Integer::sum, Integer::sum);
        long endTimeParallelSum = System.nanoTime();
        System.out.println("Example 9 - Parallel sum: " + parallelSum + " (took " + (endTimeParallelSum - startTimeParallelSum) + " ns)");

        long startTimeSequentialSum = System.nanoTime();
        int sequentialSum = numbers.stream()
                                   .reduce(0, Integer::sum, Integer::sum); // Combiner is still valid, though less critical for sequential
        long endTimeSequentialSum = System.nanoTime();
        System.out.println("For comparison - Sequential sum: " + sequentialSum + " (took " + (endTimeSequentialSum - startTimeSequentialSum) + " ns)");
        // For small datasets, parallel overhead might make it slower.





        // Example 10: Concatenate strings in parallel using a mutable container (StringBuilder)
        // This is a more complex scenario for parallel reduce.
        // Identity: new StringBuilder()
        // Accumulator: (sb, str) -> sb.append(str).append(" ") - appends to a thread-local StringBuilder
        // Combiner: (sb1, sb2) -> sb1.append(sb2) - merges StringBuilders from different threads
        // Note: For simple string concatenation, a sequential reduce or Collectors.joining() is often better.
        // This example illustrates the mechanics if you *had* to use a mutable reduction target in parallel.
        StringBuilder parallelConcatenatedWords = names.parallelStream()
                .reduce(new StringBuilder(), // Identity: new StringBuilder for each thread's partial result
                        (stringBuilder, name) -> {
                            // This accumulator will be called for each element within a parallel task
                            if (stringBuilder.length() > 0) stringBuilder.append(", ");
                            stringBuilder.append(name);
                            return stringBuilder;
                        },
                        (sb1, sb2) -> {
                            // This combiner merges the StringBuilders from different tasks
                            if (sb1.length() > 0 && sb2.length() > 0) sb1.append(", ");
                            sb1.append(sb2);
                            return sb1;
                        });
        System.out.println("Example 10 - Parallel concatenated names: " + parallelConcatenatedWords.toString());
        String joinedNames = names.parallelStream()
                          .collect(Collectors.joining(", "));
        System.out.println("Joined names using Collectors.joining: " + joinedNames);


        // Example 11: Sum of product prices in parallel (similar to Example 8)
        double parallelTotalPrice = products.parallelStream()
                .reduce(0.0,
                        (partialTotal, currentProd) -> partialTotal + currentProd.getPrice(),
                        Double::sum); // Combiner is essential here
        System.out.println("Example 11 - Parallel total price of products: " + parallelTotalPrice);
    }

}
