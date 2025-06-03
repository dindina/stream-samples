package com.dina.streams;

import java.util.Arrays;
import java.util.List;

public class FlatMapExample {
    public static void main(String[] args) {
        List<List<Integer>> nestedLists = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4, 5),
                Arrays.asList(6));

        List<Integer> allNumbers = nestedLists.stream().flatMap(list -> list.stream()).toList();
        System.out.println(allNumbers);

        // extracting all words from list of sentences
        List<String> sentences = Arrays.asList(
                "Hello world",
                "Java streams are powerful in java world",
                "Hello flatMap");

        List<String> wordList = sentences.stream().flatMap(word -> Arrays.stream(word.split(" "))).toList();
        System.out.println(wordList);

        // extracting all unique words from list of sentences
        wordList = sentences.stream().flatMap(sentence -> Arrays.stream(sentence.split(" ")))
                .map(String::toLowerCase)
                .distinct()
                .sorted()
                .toList();
        System.out.println(wordList);

    }

}
