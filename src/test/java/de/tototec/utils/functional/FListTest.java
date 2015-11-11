package de.tototec.utils.functional;

import static de.tototec.utils.functional.FList.foldLeft;
import static de.tototec.utils.functional.FList.mkString;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import de.tobiasroeser.lambdatest.testng.FreeSpec;
import de.tobiasroeser.lambdatest.testng.RunnableWithException;

public class FListTest extends FreeSpec {

	public FListTest() {

		final F2<Integer, Integer, Integer> add = new F2<Integer, Integer, Integer>() {
			public Integer apply(final Integer p1, final Integer p2) {
				return p1 + p2;
			}
		};

		testFoldLeft(0, 0, add);
		testFoldLeft(1, 0, add, 1);
		testFoldLeft(3, 0, add, 1, 2);
		testFoldLeft(6, 0, add, 1, 2, 3);

		final F2<Integer, Integer, Integer> multiply = new F2<Integer, Integer, Integer>() {
			public Integer apply(final Integer p1, final Integer p2) {
				return p1 * p2;
			}
		};

		testFoldLeft(1, 1, multiply);
		testFoldLeft(1, 1, multiply, 1);
		testFoldLeft(2, 1, multiply, 1, 2);
		testFoldLeft(6, 1, multiply, 1, 2, 3);
		testFoldLeft(24, 1, multiply, 1, 2, 3, 4);

		final F1<String, String> noConvert = null;

		testMkString("ABC", noConvert, null, null, null, "A", "B", "C");
		testMkString("ABC", noConvert, null, "", null, "A", "B", "C");
		testMkString("ABC", noConvert, "", null, null, "A", "B", "C");
		testMkString("ABC", noConvert, null, null, "", "A", "B", "C");
		testMkString("ABC", noConvert, "", "", "", "A", "B", "C");
		testMkString("A,B,C", noConvert, null, ",", "", "A", "B", "C");
		testMkString("[A,B,C]", noConvert, "[", ",", "]", "A", "B", "C");
		testMkString("[A,B,null]", noConvert, "[", ",", "]", "A", "B", null);

		final F1<String, String> nullSafeConvert = new F1<String, String>() {
			public String apply(final String param) {
				return param == null ? "" : param;
			}
		};

		testMkString("[A,B,]", nullSafeConvert, "[", ",", "]", "A", "B", null);

	}

	public <T> void testFoldLeft(final T expected, final T left, final F2<T, T, T> function, final T... elements) {
		test("FList.foldLeft: " + Arrays.deepToString(elements), new RunnableWithException() {
			public void run() throws Exception {
				assertEquals(foldLeft(elements, left, function), expected);
				assertEquals(foldLeft(Arrays.asList(elements), left, function), expected);
			}
		});
	}

	public <T> void testMkString(final String expected, final F1<T, String> convert, final String prefix,
			final String sep, final String suffix, final T... elements) {
		test("FList.mkString should result in " + expected, new RunnableWithException() {
			public void run() throws Exception {
				assertEquals(mkString(Arrays.asList(elements), prefix, sep, suffix, convert), expected);
				assertEquals(mkString(elements, prefix, sep, suffix, convert), expected);
				if (convert == null) {
					assertEquals(mkString(Arrays.asList(elements), prefix, sep, suffix), expected);
					assertEquals(mkString(elements, prefix, sep, suffix), expected);
				}
			}
		});
	}

}
