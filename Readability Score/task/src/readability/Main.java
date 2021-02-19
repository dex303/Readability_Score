package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static int polysyllablessCounter = 0;
    public static int wordCounter = 0;
    public static int syllablesCounter = 0;
    public static int charCounter = 0;
    public static double scoreARI;
    public static double scoreFK;
    public static double scoreSMOG;
    public static double scoreCL;
    public static double ageAV;
    public static int ageARI;
    public static int ageFK;
    public static int ageSMOG;
    public static int ageCL;
    public static String[] sentences;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String pathToFile = args[0];
        String text = "";
        try {
            text = readFileAsString(pathToFile);
        } catch (IOException e) {
            System.out.println("Cannot read file: " + e.getMessage());
        }

        sentences = text.split("[\\.!\\?] ");
        String[] words;

        for (int i = 0; i < sentences.length; i++) {
            String sentence = sentences[i];
            wordCounter += sentence.split(" ").length;
            words = sentence.split(" ");
            syllablesCounter += syllables(words);
        }

        charCounter = text.replace(" ", "").length();

        System.out.println("java Main " + args[0]);
        System.out.println("The text is: \n" + text);
        System.out.println();
        System.out.println("Words: " + wordCounter);
        System.out.println("Sentences: " + sentences.length);
        System.out.println("Characters: " + charCounter);
        System.out.println("Syllables: " + syllablesCounter);
        System.out.println("Polysyllables: " + polysyllablessCounter);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String choice = scanner.nextLine();
        System.out.println();
        menu(choice.toUpperCase());

    }

    private static void menu(String choice) {
        switch (choice) {
            case "ARI":
                ari();
                break;
            case "FK":
                fk();
                break;
            case "SMOG":
                smog();
                break;
            case "CL":
                cl();
                break;
            case "ALL":
                ari();
                fk();
                smog();
                cl();
                System.out.println();
                ageAV = (ageARI + ageFK + ageSMOG + ageCL) / 4.0;
                System.out.println("This text should be understood in average by " + ageAV + " year olds.");
                break;
        }
    }

    private static void cl() {
        scoreCL = 0.0588 * (((double) charCounter / wordCounter) * 100) - 0.296 * (((double) sentences.length / wordCounter) * 100) - 15.8;
        ageCL = Integer.parseInt(age(scoreCL));

        System.out.println("Coleman–Liau index: " + scoreCL
                + " (about " + ageCL + " year olds).");
    }

    private static void smog() {
        scoreSMOG = 1.043 * Math.sqrt(polysyllablessCounter * (30.0 / sentences.length)) + 3.1291;
        ageSMOG = Integer.parseInt(age(scoreSMOG));

        System.out.println("Simple Measure of Gobbledygook: " + scoreSMOG
                + " (about " + ageSMOG + " year olds).");
    }

    private static void fk() {
        scoreFK = 0.39 * ((double) wordCounter / sentences.length) + 11.8 * ((double) syllablesCounter / wordCounter) - 15.59;
        ageFK = Integer.parseInt(age(scoreFK));

        System.out.println("Flesch–Kincaid readability tests: " + scoreFK
                + " (about " + ageFK + " year olds).");
    }

    private static void ari() {
        scoreARI = 4.71 * charCounter / wordCounter + 0.5 * wordCounter / sentences.length - 21.43;
        ageARI = Integer.parseInt(age(scoreARI));

        System.out.println("Automated Readability Index: " + scoreARI
                + " (about " + ageARI + " year olds).");
    }

    private static int syllables(String[] words) {
        int numSyllables = 0;
        int allSyllables;
        char ch;
        char c;
        char a;
        char b;

        for (int i = 0; i < words.length; i++) {
            allSyllables = 0;
            //Set everything to upper case
            words[i] = words[i].toUpperCase();
            //The loop will run from 0 to the character before the last
            for (int j = 0; j < words[i].length() - 1; j++) {
                ch = words[i].charAt(j);
                c = words[i].charAt(j + 1);
                //Only adds if the char is in the index AND if there is no
                //other letter in the index fore j
                if ("AEIOUY".indexOf(ch) >= 0 && "AEIOUY".indexOf(c) == -1) {
                    allSyllables++;
                }
            }

            //Check the last character
            if (words[i].length() > 1) {
                a = words[i].charAt((words[i].length() - 1));
                b = words[i].charAt((words[i].length() - 2));
                if ("AEIOUY".indexOf(a) >= 0) {
                    allSyllables++;
                }

                //Not count if 'E' is the last char
                if (a == 'E' || (b == 'D' && a == 'E')) {
                    allSyllables--;
                }
            }

            //There must be atleast one syllable
            if (allSyllables == 0) {
                allSyllables = 1;
            }
            // check pollysyllables
            if (allSyllables > 2) {
                polysyllablessCounter++;
            }
            numSyllables += allSyllables;
        }
            return numSyllables;
    }

    private static String age(double score) {
        switch ((int) Math.round(score)) {
            case 1:
                return "6";
            case 2:
                return "7";
            case 3:
                return "9";
            case 4:
                return "10";
            case 5:
                return "11";
            case 6:
                return "12";
            case 7:
                return "13";
            case 8:
                return "14";
            case 9:
                return "15";
            case 10:
                return "16";
            case 11:
                return "17";
            case 12:
                return "18";
            case 13:
            case 14:
                return "24";
            default:
                return "0";
        }
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}