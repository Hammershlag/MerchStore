package com.example.merchstore.components.utilities;

import com.example.merchstore.components.enums.Language;
import com.example.merchstore.components.models.LanguageText;
import com.example.merchstore.components.models.PreTranslatedTexts;
import com.example.merchstore.repositories.PreTranslatedTextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;

import static com.example.merchstore.components.utilities.Defaults.TRANSLATION_OVER_DUE_DAYS;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 22.09.2024
 */

@Service
public class Translator {

    @Autowired
    private PreTranslatedTextRepository preTranslatedTextRepository;

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

    public static LanguageText translate(LanguageText input, Language outputLanguage) throws IOException {
        if (!shouldTranslate) {
            return input; // Return original text if translation is not allowed
        }

        String encodedText = URLEncoder.encode(input.getText(), "UTF-8");

        String urlString = GOOGLE_TRANSLATE_URL + "?client=gtx&sl=" + input.getLanguage().getCode() + "&tl=" + outputLanguage.getCode() +
                "&dt=t&q=" + encodedText;

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Get the response
        Scanner scanner = new Scanner(conn.getInputStream());
        String response = scanner.useDelimiter("\\A").next();
        scanner.close();

        return new LanguageText(outputLanguage, parseTranslation(response));
    }

    // New method to translate multi-sentence text
    public static LanguageText translateText(LanguageText input, Language outputLanguage) {
        // Split text into sentences using punctuation
        String[] sentences = input.getText().split("(?<=[.!?])\\s+");
        StringBuilder translatedText = new StringBuilder();

        // Translate each sentence and concatenate results
        Arrays.stream(sentences).forEach(sentence -> {
            try {
                String translatedSentence = translate(input.getLanguage(), outputLanguage, sentence);
                translatedText.append(translatedSentence).append(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return new LanguageText(outputLanguage, translatedText.toString().trim());  // Return the concatenated result
    }

    public LanguageText translateText(LanguageText input, Language outputLanguage, String className, String fieldName, Long entityId) {
        // Check pre-translated texts
        boolean isOverDue = false;
        PreTranslatedTexts preTranslatedText = preTranslatedTextRepository.findByClassNameAndFieldNameAndLanguageAndEntityId(className, fieldName, outputLanguage, entityId);
        if (preTranslatedText != null) {
            isOverDue = Duration.between(preTranslatedText.getLastUpdate(), LocalDateTime.now()).toDays() > TRANSLATION_OVER_DUE_DAYS;
        }
        if (preTranslatedText != null && !isOverDue) {
            return new LanguageText(outputLanguage, preTranslatedText.getText());
        } else if (isOverDue && preTranslatedText != null) {
            preTranslatedTextRepository.delete(preTranslatedText);
        }

        // Split text into sentences using punctuation
        String[] sentences = input.getText().split("(?<=[.!?])\\s+");
        StringBuilder translatedText = new StringBuilder();

        // Translate each sentence and concatenate results
        Arrays.stream(sentences).forEach(sentence -> {
            try {
                String translatedSentence = translate(input.getLanguage(), outputLanguage, sentence);
                translatedText.append(translatedSentence).append(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Save pre-translated text
        preTranslatedTextRepository.save(new PreTranslatedTexts(null, className, fieldName, outputLanguage, translatedText.toString().trim(), entityId, LocalDateTime.now()));
        return new LanguageText(outputLanguage, translatedText.toString().trim());  // Return the concatenated result
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
        String[] sentences = text.split("(?<=[.!?])\\s+");
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

        System.out.println((balance > 0 ? "At least 50% of tests passed" : "More than 50% of tests failed") + ". Balance: " + balance + "\n\n");
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
                //System.out.printf("Test passed for '%s': expected and got '%s'%n", originalText, actualTranslation);
                return true;
            }
        } catch (IOException e) {
            System.out.printf("Test failed for '%s': IOException occurred.%n", originalText);
            return false;
        }
    }
}
