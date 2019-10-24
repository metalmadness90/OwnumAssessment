/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ownumassessment;

/**
 *
 * @author James
 */
public class OwnumAssessment {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DocumentParser parser = new DocumentParser();
        parser.printWordCount();
        parser.printMostUsedWords();
        parser.printLastSentence();
    }
}
