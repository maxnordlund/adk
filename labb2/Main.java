/* Labb 2 i DD1352 Algoritmer, datastrukturer och komplexitet    */
/* Se labbanvisning under kurswebben https://www.kth.se/social/course/DD1352 */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Main {
  public static void main(String args[]) throws IOException {
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
    // Säkrast att specificera att UTF-8 ska användas, för vissa system har annan
    // standardinställning för teckenkodningen.

    LinkedList<String> words = new LinkedList<String>();
    String word = stdin.readLine();
    while (!word.equals("#")) {
      words.add(word);
      word = stdin.readLine();
    }

    while ((word = stdin.readLine()) != null) {
      int minDist = Integer.MAX_VALUE;
      LinkedList<String> minWords = new LinkedList<String>();
      for (String candidate : words) {
        int dist = distance(word, candidate);
        if (dist < minDist) {
          minDist = dist;
          minWords = new LinkedList<String>();
          minWords.add(candidate);
        } else if (dist == minDist) {
          minWords.add(candidate);
        }
      }

      System.out.print(word + " (" + minDist + ")");
      for (String candidate : minWords) {
        System.out.print(" " + candidate);
      }
      System.out.println();
    }
  }

  // Marked `private static final` to encourage inlining
  private static final int distance(String w1, String w2) {
    int i;
    int j;
    int min;
    int w1len = w1.length();
    int w2len = w2.length();
    int[][] matrix = new int[w1len+1][w2len+1];

    for (i = 0; i <= w1len; i++) {
      matrix[i][0] = i;
    }
    for (j = 0; j <= w2len; j++) {
      matrix[0][j] = j;
    }

    for (i = 1; i <= w1len; i++) {
      for (j = 1; j <= w2len; j++) {
        min = matrix[i-1][j-1];
        if (w1.charAt(i-1) != w2.charAt(j-1)) {
          min++;
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
    return matrix[w1len][w2len];
  }
}
