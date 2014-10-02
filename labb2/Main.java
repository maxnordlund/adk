/* Labb 2 i DD1352 Algoritmer, datastrukturer och komplexitet    */
/* Se labbanvisning under kurswebben https://www.kth.se/social/course/DD1352 */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Main {
	protected static final StringBuilder sb = new StringBuilder();

	public static void main(String args[]) throws IOException {
		final BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in, "UTF-8"));
		// Säkrast att specificera att UTF-8 ska användas, för vissa system har
		// annan
		// standardinställning för teckenkodningen.
		run(stdin, System.out);
	}

	protected static void run(BufferedReader stdin, PrintStream stdout)
			throws IOException {
		int i;
		String word = stdin.readLine();
		while (word.charAt(0) != '#') {
			ClosestWords.wordList.add(word);
			word = stdin.readLine();
		}

		// char[][] words = (char[][]) wordList.toArray();

		for (i = 0; i < ClosestWords.MAX_LENGTH; ++i) {
			ClosestWords.matrix[i][0] = ClosestWords.matrix[0][i] = i;
		}

		while ((word = stdin.readLine()) != null) {
			final ClosestWords cw = new ClosestWords(word);
			sb.append(cw.toString());
		}
		stdout.print(sb.toString());
	}
}
