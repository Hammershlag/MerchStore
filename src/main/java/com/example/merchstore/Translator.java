package com.example.merchstore;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 22.09.2024
 */
public class Translator {

    // Translates only one sentence at a time
    private static final String GOOGLE_TRANSLATE_URL = "https://translate.google.com/translate_a/single";

    // Variable to track if translation should proceed
    private static boolean shouldTranslate = true;

    public static String translate(Language from, Language to, String text) throws IOException {
        if (!shouldTranslate) {
            return text; // Return original text if translation is not allowed
        }

        String encodedText = URLEncoder.encode(text, "UTF-8");

        String urlString = GOOGLE_TRANSLATE_URL + "?client=gtx&sl=" + from.getCode() + "&tl=" + to.getCode() +
                "&dt=t&q=" + encodedText;

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Get the response
        Scanner scanner = new Scanner(conn.getInputStream());
        String response = scanner.useDelimiter("\\A").next();
        scanner.close();

        return parseTranslation(response);
    }

    // This method extracts the translation from the raw response
    private static String parseTranslation(String response) {
        // Check for errors in response or failure in parsing
        if (response.contains("error")) {
            shouldTranslate = false; // Set to false if there's an error
            return ""; // Return empty string for failed translations
        }

        // The response format is complex; we just need the main translation part
        String[] split = response.split("\"");
        return split.length > 1 ? split[1] : "";
    }

    // New method to translate multi-sentence text
    public static String translateText(Language from, Language to, String text) {
        // Split text into sentences using punctuation
        String[] sentences = text.split("(?<=[.!?])\\s*");
        StringBuilder translatedText = new StringBuilder();

        // Translate each sentence and concatenate results
        Arrays.stream(sentences).forEach(sentence -> {
            try {
                String translatedSentence = translate(from, to, sentence);
                translatedText.append(translatedSentence).append(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return translatedText.toString().trim();  // Return the concatenated result
    }

    // Method to run tests on translation
    public static void runTests() {
        int balance = 0;
        System.out.println("Running tests...");

        // Test cases: Original text and expected translated text
        balance += testTranslation("Hello", "Hola", Language.ENGLISH, Language.SPANISH) ? 1 : -1;
        balance += testTranslation("Hello", "Witam", Language.ENGLISH, Language.POLISH) ? 1 : -1;
        balance += testTranslation("Hello", "Hallo", Language.ENGLISH, Language.GERMAN) ? 1 : -1;
        balance += testTranslation("Hello", "Bonjour", Language.ENGLISH, Language.FRENCH) ? 1 : -1;
        balance += testTranslation("Hello", "Ahoj", Language.ENGLISH, Language.CZECH) ? 1 : -1;
        balance += testTranslation("Hello", "Привіт", Language.ENGLISH, Language.UKRAINIAN) ? 1 : -1;

        balance += testTranslation("Goodbye", "Adiós", Language.ENGLISH, Language.SPANISH) ? 1 : -1;
        balance += testTranslation("Goodbye", "Do widzenia", Language.ENGLISH, Language.POLISH) ? 1 : -1;
        balance += testTranslation("Goodbye", "Auf Wiedersehen", Language.ENGLISH, Language.GERMAN) ? 1 : -1;
        balance += testTranslation("Goodbye", "Au revoir", Language.ENGLISH, Language.FRENCH) ? 1 : -1;
        balance += testTranslation("Goodbye", "Sbohem", Language.ENGLISH, Language.CZECH) ? 1 : -1;
        balance += testTranslation("Goodbye", "До побачення", Language.ENGLISH, Language.UKRAINIAN) ? 1 : -1;

        balance += testTranslation("Thank you", "Gracias", Language.ENGLISH, Language.SPANISH) ? 1 : -1;
        balance += testTranslation("Thank you", "Dziękuję", Language.ENGLISH, Language.POLISH) ? 1 : -1;
        balance += testTranslation("Thank you", "Danke", Language.ENGLISH, Language.GERMAN) ? 1 : -1;
        balance += testTranslation("Thank you", "Merci", Language.ENGLISH, Language.FRENCH) ? 1 : -1;
        balance += testTranslation("Thank you", "Děkuji", Language.ENGLISH, Language.CZECH) ? 1 : -1;
        balance += testTranslation("Thank you", "Дякую", Language.ENGLISH, Language.UKRAINIAN) ? 1 : -1;

        System.out.println((balance > 0 ? "More tests passed than failed" : "More tests failed than passed") + ". Balance: " + balance + "\n\n");
        if (balance < 0) {
            shouldTranslate = false; // Disable translation if more tests failed
        }
    }

    // Helper method to test translations
    private static boolean testTranslation(String originalText, String expectedTranslation, Language from, Language to) {
        try {
            String actualTranslation = translate(from, to, originalText);
            if (!actualTranslation.equals(expectedTranslation)) {
                System.out.printf("Test failed for '%s': expected '%s', got '%s'%n", originalText, expectedTranslation, actualTranslation);
                return false;
            } else {
                System.out.printf("Test passed for '%s': expected and got '%s'%n", originalText, actualTranslation);
                return true;
            }
        } catch (IOException e) {
            System.out.printf("Test failed for '%s': IOException occurred.%n", originalText);
            return false;
        }
    }

    public static void main(String[] args) {
        runTests(); // Call the tests

        String text = "Magari yamebadilisha sana maisha ya watu...";
        String output = translateText(Language.AUTO, Language.ENGLISH, text);
        System.out.println("Full translated Text: " + output);
    }
}
