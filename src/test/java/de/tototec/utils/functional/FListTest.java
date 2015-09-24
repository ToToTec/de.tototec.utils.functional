package de.tototec.utils.functional;

import static de.tototec.utils.functional.FList.foldLeft;
import static de.tototec.utils.functional.FList.mkString;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FListTest {

	@DataProvider
	public Iterator<Object[]> dataFoldLeft() {
		@SuppressWarnings("serial")
		class Data extends LinkedList<Object[]> {
			<T> void data(final T expected, final T left, final F2<T, T, T> function, final T... list) {
				add(new Object[] { expected, left, function, list });
			}
		}
		;

		final Data data = new Data();

		final F2<Integer, Integer, Integer> add = new F2<Integer, Integer, Integer>() {
			public Integer apply(final Integer p1, final Integer p2) {
				return p1 + p2;
			}
		};
		final F2<Integer, Integer, Integer> multiply = new F2<Integer, Integer, Integer>() {
			public Integer apply(final Integer p1, final Integer p2) {
				return p1 * p2;
			}
		};

		data.data(6, 0, add, 1, 2, 3);
		data.data(6, 1, multiply, 1, 2, 3);

		return data.iterator();
	}

	@Test(dataProvider = "dataFoldLeft")
	public <T> void testFoldLeft(final T expected, final T left, final F2<T, T, T> function, final T[] list) {
		assertEquals(foldLeft(list, left, function), expected);
		assertEquals(foldLeft(Arrays.asList(list), left, function), expected);
	}

	@DataProvider
	public Iterator<Object[]> dataMkString() {
		@SuppressWarnings("serial")
		class Data extends LinkedList<Object[]> {
			void data(final String expected, final String prefix, final String sep, final String suffix,
					final Object... elements) {
				add(new Object[] { expected, null, prefix, sep, suffix, elements });
			}

			void dataConvert(final String expected, final F1<?, String> convert, final String prefix, final String sep,
					final String suffix,
					final Object... elements) {
				add(new Object[] { expected, convert, prefix, sep, suffix, elements });
			}
		}
		;

		final Data data = new Data();

		data.data("ABC", null, null, null, "A", "B", "C");
		data.data("ABC", null, "", null, "A", "B", "C");
		data.data("ABC", "", null, null, "A", "B", "C");
		data.data("ABC", null, null, "", "A", "B", "C");
		data.data("ABC", "", "", "", "A", "B", "C");
		data.data("A,B,C", null, ",", "", "A", "B", "C");
		data.data("[A,B,C]", "[", ",", "]", "A", "B", "C");
		data.data("[A,B,null]", "[", ",", "]", "A", "B", null);

		final F1<String, String> convert = new F1<String, String>() {
			public String apply(final String param) {
				return param == null ? "" : param;
			}
		};

		data.dataConvert("[A,B,]", convert, "[", ",", "]", "A", "B", null);

		return data.iterator();
	}

	@Test(dataProvider = "dataMkString")
	public <T> void testMkString(final String expected, final F1<T, String> convert, final String prefix,
			final String sep, final String suffix,
			final T[] elements) {
		assertEquals(mkString(Arrays.asList(elements), prefix, sep, suffix, convert), expected);
		assertEquals(mkString(elements, prefix, sep, suffix, convert), expected);
		if (convert == null) {
			assertEquals(mkString(Arrays.asList(elements), prefix, sep, suffix), expected);
			assertEquals(mkString(elements, prefix, sep, suffix), expected);
		}
	}
}
