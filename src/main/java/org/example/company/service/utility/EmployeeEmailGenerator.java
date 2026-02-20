package org.example.company.service.utility;

import java.util.List;

/**
 * Utility class for processing employee names and generating unique corporate emails
 * in the format {@code name@ordering.com}, with automatic numbering for duplicates.
 */
public final class EmployeeEmailGenerator {

    private static final String DOMAIN = "@ordering.com";

    private EmployeeEmailGenerator() {
    }

    /**
     * Generates the next available email for the given base name, considering existing emails.
     * Example: base "bob", existing [bob@..., bob1@..., bob8@...] → "bob9@ordering.com"
     * Example: base "bob", existing [] → "bob@ordering.com"
     */
    public static String generateNextAvailableEmail(String baseName, List<String> existingEmails) {
        if (baseName == null || baseName.isBlank()) {
            throw new IllegalArgumentException("Base name cannot be null or blank");
        }
        int maxNumber = existingEmails.stream()
            .mapToInt(EmployeeEmailGenerator::extractTrailingNumberFromEmail)
            .max()
            .orElse(-1);
        int nextNumber = maxNumber + 1;
        String suffix = nextNumber == 0 ? "" : String.valueOf(nextNumber);
        return baseName + suffix + DOMAIN;
    }

    private static int extractTrailingNumberFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            return 0;
        }
        String username = email.substring(0, email.indexOf('@'));
        return extractTrailingNumber(username);
    }

    private static int extractTrailingNumber(String username) {
        if (username == null || username.isEmpty()) {
            return 0;
        }
        int i = username.length() - 1;
        while (i >= 0 && Character.isDigit(username.charAt(i))) {
            i--;
        }
        if (i == username.length() - 1) {
            return 0;
        }
        return Integer.parseInt(username.substring(i + 1));
    }

    /**
     * Returns normalized base name for email generation (letters and digits only).
     */
    public static String normalizeNameForEmail(String name) {
        String normalized = normalizeName(name);
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty after normalization");
        }
        return normalized;
    }

    private static String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            return "";
        }
        return name.trim().replaceAll("[^a-zA-Z0-9]", "");
    }


}
