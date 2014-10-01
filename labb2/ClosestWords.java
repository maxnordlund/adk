/* Labb 2 i DD1352 Algoritmer, datastrukturer och komplexitet    */
/* Se labbanvisning under kurswebben https://www.kth.se/social/course/DD1352 */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.util.LinkedList;
import java.util.List;

public class ClosestWords {
  LinkedList<String> closestWords = null;

  int closestDistance = -1;

  int Distance(String w1, String w2, int w1len, int w2len) {
    int i;
    int j;
    int min;
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

  public ClosestWords(String w, List<String> wordList) {
    for (String s : wordList) {
      int dist = Distance(w, s, w.length(), s.length());
      // System.out.println("d(" + w + "," + s + ")=" + dist);
      if (dist < closestDistance || closestDistance == -1) {
        closestDistance = dist;
        closestWords = new LinkedList<String>();
        closestWords.add(s);
      }
      else if (dist == closestDistance)
        closestWords.add(s);
    }
  }

  int getMinDistance() {
    return closestDistance;
  }

  List<String> getClosestWords() {
    return closestWords;
  }
}
