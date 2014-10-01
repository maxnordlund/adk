import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MainTest extends Main {
	@Parameters(name = "Example {0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { "testmedordlista1", false },
				{ "testmedordlista2", false }, { "testord1", true },
				{ "testord2", true } });
	}

	@Parameter(value = 0)
	public String baseName;
	@Parameter(value = 1)
	public boolean prependWordList;
	private String expected;
	private InputStream stdin;
	private ByteArrayOutputStream stdout;

	@Before
	public void setUp() throws Exception {
		this.stdin = Files.newInputStream(Paths.get("testfall", this.baseName
				+ ".indata"));
		if (this.prependWordList) {
			this.stdin = new SequenceInputStream(Files.newInputStream(Paths
					.get("ordlista.txt")), this.stdin);
		}
		this.expected = new String(Files.readAllBytes(Paths.get("testfall",
				this.baseName + ".utdata")), "UTF-8");
		this.stdout = new ByteArrayOutputStream();
		Main.sb.setLength(0);
		Main.wordList.clear();
	}

	@Test
	public void testExample() {
		try {
			MainTest.run(new BufferedReader(new InputStreamReader(this.stdin)),
					new PrintStream(this.stdout));
		} catch (IOException err) {
			fail(err.toString());
		}
		try {
			assertEquals(this.expected, this.stdout.toString("UTF-8"));
		} catch (UnsupportedEncodingException err) {
			fail(err.toString());
		}
	}

	@After
	public void tearDown() throws Exception {
		this.stdin.close();
	}
}
