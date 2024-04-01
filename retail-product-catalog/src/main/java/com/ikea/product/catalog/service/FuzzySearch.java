package com.ikea.product.catalog.service;

import com.ikea.product.catalog.entity.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FuzzySearch {

  // Function to perform fuzzy search
  public List<Product> search(String term, List<Product> products, int threshold) {
    List<Product> results = new ArrayList<>();
    for (Product product : products) {
      int distance = damerauLevenshteinDistance(term.toLowerCase(), product.getName().toLowerCase());
      if (distance <= threshold) {
        results.add(product);
      }
    }
    return results;
  }

  // Function to calculate Damerau-Levenshtein distance
  private static int damerauLevenshteinDistance(String s1, String s2) {
    int len1 = s1.length();
    int len2 = s2.length();

    // Create a matrix to store distances
    int[][] dp = new int[len1 + 1][len2 + 1];

    // Initialize the first row and column of the matrix
    for (int i = 0; i <= len1; i++) {
      dp[i][0] = i;
    }
    for (int j = 0; j <= len2; j++) {
      dp[0][j] = j;
    }

    // Fill in the matrix with minimum edit distances
    for (int i = 1; i <= len1; i++) {
      for (int j = 1; j <= len2; j++) {
        // Determine the cost of substitution
        int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;

        // Calculate the minimum distance
        dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);

        // Check for transposition (if applicable)
        if (i > 1 && j > 1 && s1.charAt(i - 1) == s2.charAt(j - 2) && s1.charAt(i - 2) == s2.charAt(j - 1)) {
          dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + cost);
        }
      }
    }

    // Return the Damerau-Levenshtein distance
    return dp[len1][len2];
  }
}
