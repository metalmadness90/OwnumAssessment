/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ownumassessment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 *
 * @author James
 */
public class DocumentParser {

    final File ownumFile = new File(getClass().getResource("passage.txt").getPath());

    int wordCount = 0;

    HashMap<String, Integer> wordCountMap = new HashMap<>();

    List<Entry<String, Integer>> sortedMostUsedWords;

    List<String> allLines;

    String mostUsedLastSentence;

    public DocumentParser() {
        parseDocument();
    }

    private void parseDocument() {

        try {
            allLines = Files.readAllLines(ownumFile.toPath());
            for (String line : allLines) {
                wordCount += getCountForLine(line);
            }

            sortedMostUsedWords = identifyMostUsedWords();
            mostUsedLastSentence = getLastSentenceForWord(sortedMostUsedWords.get(0).getKey());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int getCountForLine(String line) {
        int count = 0;

        String currentWord = "";
        String currentCharacter;
        for (int i = 0; i < line.length(); i++) {
            currentCharacter = line.substring(i, i + 1);

            //if whitespace or punctuation is the current character, we have parsed a full word.
            //we use a pattern regex to check against 632 unicode characters as well as 32 
            //other commonly used punctuation and non-letter characters.
            //this is assuming we have correct (or correct enough) grammar and spelling, we will not be able to handle otherwise.
            if (Pattern.matches("[\\p{Punct}\\p{IsPunctuation}]", currentCharacter) || currentCharacter.equals(" ")) {
                //in case nothing was put into current word before next escape character
                if (currentWord.trim().length() > 0) {
                    //if wordCountMap already contains word, increment, else add.
                    //this map is used to see how many times each word is found,
                    //to be used later to find most used words.
                    if (wordCountMap.containsKey(currentWord.toLowerCase())) {
                        wordCountMap.put(currentWord.toLowerCase(), wordCountMap.get(currentWord.toLowerCase()) + 1);
                    } else {
                        wordCountMap.put(currentWord.toLowerCase(), 1);
                    }
                    count++;
                    currentWord = "";
                }
            } else {
                currentWord += currentCharacter;
            }

        }

        return count;
    }

    private List<Entry<String, Integer>> identifyMostUsedWords() {

        List<Entry<String, Integer>> sortedEntries = new ArrayList<>(wordCountMap.entrySet());

        //simply sort the list with a comparator in descending order
        Collections.sort(sortedEntries,
                new Comparator<Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        }
        );

        return sortedEntries;
    }

    private String getLastSentenceForWord(String word) {
        String sentence = "";
        String lastSentence = "";

        String currentCharacter;
        for (String line : allLines) {
            for (int i = 0; i < line.length(); i++) {
                currentCharacter = line.substring(i, i + 1);

                //similar to parsing for words, but since we want sentences we no longer
                //care about white space and only about ending punctuation
                if (Pattern.matches("[.?!]", currentCharacter)) {
                    String temp = sentence;
                    if (temp.toLowerCase().contains(word.toLowerCase())) {
                        lastSentence = sentence + currentCharacter;
                    }
                    sentence = "";
                } else {
                    sentence += currentCharacter;
                }
            }
        }

        return lastSentence.trim();
    }

    public void printWordCount() {
        System.out.println("Word count for document is: " + wordCount + "\n");
    }

    public void printMostUsedWords() {
        System.out.println("Most used words in descending order: \n");

        //in case we run into a situation with less than 10 most used words.
        //doesn't happen with example but it's good to check in case different
        //documents are tested.
        int size;
        if (sortedMostUsedWords.size() < 10) {
            size = sortedMostUsedWords.size();
        } else {
            size = 10;
        }

        for (int i = 0; i < size; i++) {
            System.out.println(sortedMostUsedWords.get(i).getKey() + " || times used: " + sortedMostUsedWords.get(i).getValue());
        }
    }

    public void printLastSentence() {
        System.out.println("\nLast sentence of most used word:\n" + mostUsedLastSentence);
    }
}
