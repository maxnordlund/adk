/* Labb 2 i DD1352 Algoritmer, datastrukturer och komplexitet    */
/* Se labbanvisning under kurswebben https://www.kth.se/social/course/DD1352 */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
	// Kattis promise to not have word longer then 40 characters long
	private static final int MAX_LENGTH = 41;
	private static final int[][] matrix = new int[MAX_LENGTH][MAX_LENGTH];
	protected static final List<String> wordList = new ArrayList<String>();
	protected static final StringBuilder sb = new StringBuilder();

	public static void main(String args[]) throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in, "UTF-8"));
		// Säkrast att specificera att UTF-8 ska användas, för vissa system har
		// annan
		// standardinställning för teckenkodningen.
		run(stdin, System.out);
	}

	protected static void run(BufferedReader stdin, PrintStream stdout)
			throws IOException {
		int i;
		int maxLength = Integer.MIN_VALUE;
		String word = stdin.readLine();
		while (word.charAt(0) != '#') {
			if (word.length() > maxLength) {
				maxLength = word.length();
			}
			wordList.add(word);
			word = stdin.readLine();
		}

		// char[][] words = (char[][]) wordList.toArray();

		for (i = 0; i < MAX_LENGTH; ++i) {
			matrix[i][0] = matrix[0][i] = i;
		}

		while ((word = stdin.readLine()) != null) {
			int minDist = Integer.MAX_VALUE;
			char[] wordAsChars = word.toCharArray();
			char[] oldCandidate = new char[0];
			List<char[]> minWords = new LinkedList<char[]>();

			for (String cand : wordList) {
				char[] candidate = cand.toCharArray();
				int dist = distance(wordAsChars, candidate, oldCandidate);
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
		stdout.print(sb.toString());
	}

	// Marked `private static final` to encourage inlining
	private static final int distance(char[] word, char[] candidate,
			char[] oldCandidate) {
		int i, j;
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
				min = matrix[i - 1][j - 1];
				if (word[i - 1] != candidate[j - 1]) {
					++min;
				}
				if (min > matrix[i][j - 1]) {
					min = 1 + matrix[i][j - 1];
				}
				if (min > matrix[i - 1][j]) {
					min = 1 + matrix[i - 1][j];
				}
				matrix[i][j] = min;
			}
		}
		return matrix[word.length][candidate.length];
	}
}
