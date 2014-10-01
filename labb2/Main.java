/* Labb 2 i DD1352 Algoritmer, datastrukturer och komplexitet    */
/* Se labbanvisning under kurswebben https://www.kth.se/social/course/DD1352 */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
  private static int[][] matrix;

  // Kattis promise to not have word longer then 40 characters long
  private static final int MAX_LENGTH = 41;

  public static void main(String args[]) throws IOException {
    int i;
    int j;

    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
    // Säkrast att specificera att UTF-8 ska användas, för vissa system har annan
    // standardinställning för teckenkodningen.

    int maxLength = Integer.MIN_VALUE;
    char[] word = readLine(stdin);
    StringBuilder sb = new StringBuilder();
    List<char[]> wordList = new ArrayList<char[]>();
    while (word[0] != '#') {
      if (word.length > maxLength) {
        maxLength = word.length;
      }
      wordList.add(word);
      word = readLine(stdin);
    }

    char[][] words = wordList.toArray(new char[0][0]);

    matrix = new int[MAX_LENGTH][MAX_LENGTH];
    for (i = 0; i < MAX_LENGTH; ++i) {
      matrix[i][0] = matrix[0][i] = i;
    }

    while ((word = readLine(stdin)).length > 0) {
      int minDist = Integer.MAX_VALUE;
      char[] oldCandidate = new char[0];
      List<char[]> minWords = new LinkedList<char[]>();

      for (i = 0; i < words.length; ++i) {
        char[] candidate = words[i];
        int dist = distance(word, candidate, oldCandidate);
        if (dist < minDist) {
          minDist = dist;
          minWords = new LinkedList<char[]>();
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
      for (char[] candidate : minWords) {
        sb.append(" ");
        sb.append(candidate);
      }
      sb.append('\n');
    }
    System.out.print(sb.toString());
  }

  // Marked `private static final` to encourage inlining
  private static final char[] readLine(BufferedReader stdin) throws IOException {
    String str = stdin.readLine();
    if (str == null) {
      return new char[0];
    } else {
      return str.toCharArray();
    }
  }

  // Marked `private static final` to encourage inlining
  private static final int distance(char[] word, char[] candidate, char[] oldCandidate) {
    int i;
    int j;
    int start = 1;
    int min;

    if (candidate.length < oldCandidate.length) {
      min = candidate.length;
    } else {
      min = oldCandidate.length;
    }

    for (i = 0; i < min && candidate[i] == oldCandidate[i]; ++i) {
      ++start;
    }

    for (i = 1; i <= word.length; ++i) {
      for (j = start; j <= candidate.length; ++j) {
        min = matrix[i-1][j-1];
        if (word[i-1] != candidate[j-1]) {
          ++min;
        }
        if (min > matrix[i][j-1]) {
          min = 1 + matrix[i][j-1];
        }
        if (min > matrix[i-1][j]) {
          min = 1 + matrix[i-1][j];
        }
        matrix[i][j] = min;
      }
    }
    return matrix[word.length][candidate.length];
  }
}
