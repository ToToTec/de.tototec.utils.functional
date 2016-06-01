package de.tototec.utils.functional;

import static de.tobiasroeser.lambdatest.Expect.expectEquals;
import static de.tobiasroeser.lambdatest.Expect.expectFalse;
import static de.tobiasroeser.lambdatest.Expect.expectTrue;
import static de.tototec.utils.functional.FList.foldLeft;
import static de.tototec.utils.functional.FList.mkString;
import java.util.Arrays;
import java.util.List;

import de.tobiasroeser.lambdatest.testng.FreeSpec;

public class FListTest extends FreeSpec {

	public FListTest() {

		final F2<Integer, Integer, Integer> add = (p1, p2) -> p1 + p2;

		testFoldLeft(0, 0, add);
		testFoldLeft(1, 0, add, 1);
		testFoldLeft(3, 0, add, 1, 2);
		testFoldLeft(6, 0, add, 1, 2, 3);

		final F2<Integer, Integer, Integer> multiply = (p1, p2) -> p1 * p2;

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

		final F1<String, String> nullSafeConvert = param -> param == null ? "" : param;

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

		test("containsAll", () -> {
			final String[] array = new String[] { "A", "B", "C" };
			final List<String> list = Arrays.asList(array);

			expectTrue(FList.containsAll(array, new String[] { "C", "A" }));
			expectTrue(FList.containsAll(list, Arrays.asList("C")));
			expectFalse(FList.containsAll(array, Arrays.asList("A", "D")));
			expectFalse(FList.containsAll(list, new String[] { "A", "D" }));
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

		test("filterNotNull removes null-entries", () -> {
			expectEquals(FList.filterNotNull(new String[] { "1", null, "3", null }), Arrays.asList("1", "3"));
			expectEquals(FList.filterNotNull(Arrays.asList("1", null, "3", null)), Arrays.asList("1", "3"));
		});

		testConcat();
		testConcat("1");
		testConcat("1", "2");
		testConcat("1", "2", "3", "4");

		testDistinct(Arrays.asList("1", "2", "3"), "1", "2", "3");
		testDistinct(Arrays.asList("1", "2"), "1", "2", "1");
		testDistinct(Arrays.asList("1", "2"), "1", "2", "1");

	}

	private <T> void testDistinct(final List<T> expected, final T... test) {
		test("distinct for array " + mkString(test, "[", ",", "]") + " results in " + mkString(expected, "[", ",", "]"),
				() -> {
					expectEquals(FList.distinct(test), expected);
				});
		test("distinct for iterable " + mkString(test, "[", ",", "]") + " results in "
				+ mkString(expected, "[", ",", "]"), () -> {
					expectEquals(FList.distinct(Arrays.asList(test)), expected);
				});
	}

	public <T> void testContains(final T test, final boolean contains, final T... elements) {
		test("contains " + (contains ? "" : "not ") + test + " in " + Arrays.deepToString(elements),
				() -> {
					expectEquals(FList.contains(elements, test), contains);
					expectEquals(FList.contains(Arrays.asList(elements), test), contains);
				});
	}

	public <T> void testFoldLeft(final T expected, final T left, final F2<T, T, T> function, final T... elements) {
		test("FList.foldLeft: " + Arrays.deepToString(elements), () -> {
			expectEquals(foldLeft(elements, left, function), expected);
			expectEquals(foldLeft(Arrays.asList(elements), left, function), expected);
		});
	}

	public <T> void testMkString(final String expected, final F1<T, String> convert, final String prefix,
			final String sep, final String suffix, final T... elements) {
		test("FList.mkString should result in " + expected, () -> {
			expectEquals(mkString(Arrays.asList(elements), prefix, sep, suffix, convert), expected);
			expectEquals(mkString(elements, prefix, sep, suffix, convert), expected);
			if (convert == null) {
				expectEquals(mkString(Arrays.asList(elements), prefix, sep, suffix), expected);
				expectEquals(mkString(elements, prefix, sep, suffix), expected);
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

	public <T> void testConcat(final T... ts) {
		test("concat for two works", () -> {
			final int half = ts.length / 2;
			final T[] left = Arrays.copyOfRange(ts, 0, half);
			final T[] right = Arrays.copyOfRange(ts, half, ts.length);
			expectEquals(FList.concat(left, right), Arrays.asList(ts));
			expectEquals(FList.concat(Arrays.asList(left), Arrays.asList(right)), Arrays.asList(ts));
		});
	}

}
