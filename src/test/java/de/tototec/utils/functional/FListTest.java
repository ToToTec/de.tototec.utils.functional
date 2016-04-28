package de.tototec.utils.functional;

import static de.tobiasroeser.lambdatest.Expect.expectEquals;
import static de.tototec.utils.functional.FList.foldLeft;
import static de.tototec.utils.functional.FList.mkString;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import de.tobiasroeser.lambdatest.RunnableWithException;
import de.tobiasroeser.lambdatest.testng.FreeSpec;

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

		testContains("A", false);
		testContains("A", true, "A", "B", "C");
		testContains("B", true, "A", "B", "C");
		testContains("C", true, "A", "B", "C");
		testContains("D", false, "A", "B", "C");
		testContains("", false, "A", "B", "C");
		testContains(null, false, "A", "B", "C");
		testContains(null, false);
		testContains(null, true, "A", "B", null);

		test("containsAll", new RunnableWithException() {
			public void run() throws Exception {
				final String[] array = new String[] { "A", "B", "C" };
				final List<String> list = Arrays.asList(array);

				assertTrue(FList.containsAll(array, new String[] { "C", "A" }));
				assertTrue(FList.containsAll(list, Arrays.asList("C")));
				assertFalse(FList.containsAll(array, Arrays.asList("A", "D")));
				assertFalse(FList.containsAll(list, new String[] { "A", "D" }));
			}
		});

		testHeadOption();
		testHeadOption((Object) null);
		testHeadOption(null, "1", null);
		testHeadOption(1L, 2L);

		testTail();
		testTail((Object) null);
		testTail("1");
		testTail(1L);
		testTail(1, 2, 3);
		testTail("1", "2", "3");
		testTail(null, null, null);

	}

	public <T> void testContains(final T test, final boolean contains, final T... elements) {
		test("contains " + (contains ? "" : "not ") + test + " in " + Arrays.deepToString(elements),
				new RunnableWithException() {
					public void run() throws Exception {
						assertEquals(FList.contains(elements, test), contains);
						assertEquals(FList.contains(Arrays.asList(elements), test), contains);
					}
				});
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

	public <T> void testHeadOption(final T... elements) {
		if (elements.length == 0) {
			test("Flist.headOption given Optional.NONE for empty source", () -> {
				// array version
				expectEquals(FList.headOption(elements), Optional.none());
				// list version
				expectEquals(FList.headOption(Arrays.asList(elements)), Optional.none());
			});
		} else {
			test("Flist.headOption gives first element as Optional", () -> {
				// array version
				expectEquals(FList.headOption(elements), Optional.some(elements[0]));
				// list version
				expectEquals(FList.headOption(Arrays.asList(elements)), Optional.some(elements[0]));
			});
		}
	}

	public <T> void testTail(final T... elements) {
		if (elements.length == 0) {
			test("Flist.tail given empty list for empty source", () -> {
				// array version
				expectEquals(FList.tail(elements), Arrays.asList());
				// list version
				expectEquals(FList.tail(Arrays.asList(elements)), Arrays.asList());
			});
		} else if (elements.length == 1) {
			test("Flist.tail given empty list for single-element source", () -> {
				// array version
				expectEquals(FList.tail(elements), Arrays.asList());
				// list version
				expectEquals(FList.tail(Arrays.asList(elements)), Arrays.asList());
			});
		} else {
			test("Flist.tail gives tail list", () -> {
				final T[] copy = Arrays.copyOfRange(elements, 1, elements.length);
				// array version
				final List<T> arrayResult = FList.tail(elements);
				expectEquals(arrayResult, Arrays.asList(copy));
				expectEquals(arrayResult.size(), elements.length - 1);
				// list version
				final List<T> listResult = FList.tail(Arrays.asList(elements));
				expectEquals(listResult, Arrays.asList(copy));
				expectEquals(listResult.size(), elements.length - 1);
			});
		}
	}

}
