package com.dina.streams;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + ", age=" + age + '}';
    }
}

public class PartitioningExample2 {
    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
                new Person("Alice", 25),
                new Person("Bob", 17),
                new Person("Charlie", 30),
                new Person("David", 16),
                new Person("Eve", 22));

        // Partition people into adults and minors
        Map<Boolean, List<Person>> partitionedPeople = people.stream()
                .collect(Collectors.partitioningBy(person -> person.getAge() >= 18));

        System.out.println("Adults (true):");
        partitionedPeople.get(true).forEach(System.out::println);

        System.out.println("\nMinors (false):");
        partitionedPeople.get(false).forEach(System.out::println);
    }

}
