package com.cww.veille_springboot.util;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Base64Decoder {
    public static byte[] decodeBase64Image(Object imageData) {
        // If already a byte array, return as-is
        if (imageData instanceof byte[]) {
            return (byte[]) imageData;
        }

        // Convert to string
        String base64String = imageData.toString().trim();

        // If empty, return null
        if (base64String.isEmpty()) {
            return null;
        }

        try {
            // Remove potential data URL prefixes
            base64String = extractBase64Content(base64String);

            // Decode the Base64 string
            return Base64.getDecoder().decode(base64String);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Base64 encoding: " + e.getMessage(), e);
        }
    }

    private static String extractBase64Content(String input) {
        // Regular expressions to match different Base64 formats
        String[] patterns = {
                "data:image/jpeg;base64,(.+)",
                "data:image/png;base64,(.+)",
                "data:image/gif;base64,(.+)",
                "data:application/pdf;base64,(.+)"
        };

        for (String patternStr : patterns) {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(input);

            if (matcher.matches()) {
                return matcher.group(1);
            }
        }

        // If no prefix found, return the original input
        return input;
    }

    // Optional: File size validation
    public static void validateFileSize(byte[] fileBytes, long maxSizeInBytes) {
        if (fileBytes != null && fileBytes.length > maxSizeInBytes) {
            throw new IllegalArgumentException(
                    "File size exceeds maximum limit of " +
                            (maxSizeInBytes / (1024 * 1024)) + "MB"
            );
        }
    }
}