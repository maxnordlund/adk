/* Labb 2 i DD1352 Algoritmer, datastrukturer och komplexitet    */
/* Se labbanvisning under kurswebben https://www.kth.se/social/course/DD1352 */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Main {
  private static int[][] matrix;

  public static void main(String args[]) throws IOException {
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
    // Säkrast att specificera att UTF-8 ska användas, för vissa system har annan
    // standardinställning för teckenkodningen.

    int maxLength = Integer.MIN_VALUE;
    String word = stdin.readLine();
    StringBuilder sb = new StringBuilder();
    LinkedList<String> words = new LinkedList<String>();
    while (!word.equals("#")) {
      if (word.length() > maxLength) {
        maxLength = word.length();
      }
      words.add(word);
      word = stdin.readLine();
    }

    while ((word = stdin.readLine()) != null) {
      int i;
      int j;
      int wordLength = word.length();
      int minDist = Integer.MAX_VALUE;
      String oldCandidate = "";
      LinkedList<String> minWords = new LinkedList<String>();

      matrix = new int[wordLength+1][maxLength+1];

      for (i = 0; i <= wordLength; ++i) {
        matrix[i][0] = i;
      }
      for (j = 0; j <= maxLength; ++j) {
        matrix[0][j] = j;
      }

      for (String candidate : words) {
        int dist = distance(word, candidate, oldCandidate, wordLength);
        if (dist < minDist) {
          minDist = dist;
          minWords = new LinkedList<String>();
          minWords.add(candidate);
        } else if (dist == minDist) {
          minWords.add(candidate);
        }
        oldCandidate = candidate;
      }

      sb.append(word);
      sb.append(" (");
      sb.append(minDist);
      sb.append(")");
      for (String candidate : minWords) {
        sb.append(" ");
        sb.append(candidate);
      }
      sb.append('\n');
    }
    System.out.print(sb.toString());
  }

  // Marked `private static final` to encourage inlining
  private static final int distance(String word, String candidate, String oldCandidate, int wordLength) {
    int i;
    int j;
    int start = 1;
    int candidateLength = candidate.length();
    int min;

    if (candidateLength < oldCandidate.length()) {
      min = candidateLength;
    } else {
      min = oldCandidate.length();
    }

    for (i = 0; i < min; ++i) {
      if (candidate.charAt(i) != oldCandidate.charAt(i)) {
        break;
      }
      ++start;
    }

    for (i = 1; i <= wordLength; ++i) {
      for (j = start; j <= candidateLength; ++j) {
        min = matrix[i-1][j-1];
        if (word.charAt(i-1) != candidate.charAt(j-1)) {
          ++min;
        }
        if (min > matrix[i-1][j]) {
          min = 1 + matrix[i-1][j];
        }
        if (min > matrix[i][j-1]) {
          min = 1 + matrix[i][j-1];
        }
        matrix[i][j] = min;
      }
    }
    return matrix[wordLength][candidateLength];
  }
}
