import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* Labb 2 i DD1352 Algoritmer, datastrukturer och komplexitet    */
/* Se labbanvisning under kurssidan http://www.csc.kth.se/DD1352 */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
public class ClosestWords {
	// Kattis promise to not have word longer then 40 characters long
	public static final int MAX_LENGTH = 41;
	public static final int[][] matrix = new int[MAX_LENGTH][MAX_LENGTH];
	public static final List<String> wordList = new ArrayList<>();
	private static String lastCandidate;
	private static int lastCandidateLength;

	private final String word;
	private final StringBuilder sb;
	private final List<String> minWords;
	private int minDist;

	public ClosestWords(String word) {
		this.sb = new StringBuilder();
		this.word = word;
		this.minDist = Integer.MAX_VALUE;
		this.minWords = new LinkedList<>();

		final int wordLength = word.length();

		lastCandidate = "";
		lastCandidateLength = 0;

		for (final String candidate : wordList) {
			final int dist = distance(word, wordLength, candidate);
			if (dist < this.minDist) {
				this.minDist = dist;
				this.minWords.clear();
				this.minWords.add(candidate);
			} else if (dist == this.minDist) {
				this.minWords.add(candidate);
			}
			lastCandidate = candidate;
			lastCandidateLength = lastCandidate.length();
		}
	}

	@Override
	public String toString() {
		this.sb.append(this.word);
		this.sb.append(" (");
		this.sb.append(this.minDist);
		this.sb.append(")");
		for (final String candidate : this.minWords) {
			this.sb.append(" ");
			this.sb.append(candidate);
		}
		this.sb.append('\n');
		return this.sb.toString();
	}

	// Marked `private static final` to encourage inlining
	private static final int distance(String word, int wordLength,
			String candidate) {
		int i, j, min;
		int start = 1;
		final int candidateLength = candidate.length();

		if (candidateLength < lastCandidateLength) {
			min = candidateLength;
		} else {
			min = lastCandidateLength;
		}

		for (i = 0; i < min && candidate.charAt(i) == lastCandidate.charAt(i); ++i) {
			++start;
		}

		for (j = start; j <= candidateLength; ++j) {
			for (i = 1; i <= wordLength; ++i) {
				min = matrix[i - 1][j - 1];
				if (word.charAt(i - 1) != candidate.charAt(j - 1)) {
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
		return matrix[wordLength][candidateLength];
	}
}