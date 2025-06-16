package com.ai;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestCaseGenerator {

    private static final Logger logger = Logger.getLogger(TestCaseGenerator.class.getName());

    public static String generateTestCases(String url) {
        StringBuilder testCases = new StringBuilder();

        logger.info("🔍 Generating test cases for URL: " + url);

        try {
            Document doc = fetchHtml(url);
            if (doc != null) {
                Elements buttons = doc.select("button, input[type=button], input[type=submit]");
                Elements links = doc.select("a[href]");

                logger.info("✅ Total Buttons Found: " + buttons.size());
                logger.info("✅ Total Links Found: " + links.size());

                testCases.append("📋 Test Cases Generated:\n\n");

                int step = 1;

                for (Element button : buttons) {
                    String text = button.text();
                    if (text.isEmpty()) {
                        text = button.attr("value");
                    }
                    if (text.isEmpty()) {
                        text = "Unnamed Button";
                    }
                    testCases.append("Step ").append(step++).append(": Click on button: ").append(text).append("\n");
                }

                for (Element link : links) {
                    String text = link.text();
                    if (text.isEmpty()) {
                        text = link.attr("href");
                    }
                    testCases.append("Step ").append(step++).append(": Click on link: ").append(text).append("\n");
                }

                logger.info("✅ Test case generation completed successfully.");
            }

        } catch (Exception e) {
            String errorMsg = "❌ Failed to generate test cases: " + e.getMessage();
            logger.log(Level.SEVERE, errorMsg, e);
            testCases.append(errorMsg);
        }
        return testCases.toString();
    }

    private static Document fetchHtml(String url) {
        try {
            logger.info("🌐 Connecting to: " + url);
            return (Document) Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (LunaTestBot)")
                    .timeout(5000)
                    .get();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "❌ Failed to fetch URL: " + url, e);
            return null;
        }
    }

    private static String formatStep(int id, String action) {
        return "Step " + id + ": " + action;
    }
}
