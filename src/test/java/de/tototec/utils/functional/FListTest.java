package de.tototec.utils.functional;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static de.tototec.utils.functional.FList.*;

public class FListTest extends Assert {

	@DataProvider
	public Iterator<Object[]> dataFoldLeft() {
		@SuppressWarnings("serial")
		class Data extends LinkedList<Object[]> {
			<T> void data(T expected, T left, F2<T, T, T> function, T... list) {
				add(new Object[] { expected, left, function, list });
			}
		}
		;

		Data data = new Data();

		F2<Integer, Integer, Integer> add = new F2<Integer, Integer, Integer>() {
			public Integer apply(Integer p1, Integer p2) {
				return p1 + p2;
			}
		};
		F2<Integer, Integer, Integer> multiply = new F2<Integer, Integer, Integer>() {
			public Integer apply(Integer p1, Integer p2) {
				return p1 * p2;
			}
		};

		data.data(6, 0, add, 1, 2, 3);
		data.data(6, 1, multiply, 1, 2, 3);

		return data.iterator();
	}

	@Test(dataProvider = "dataFoldLeft")
	public <T> void testFoldLeft(T expected, T left, F2<T, T, T> function, T[] list) {
		assertEquals(foldLeft(list, left, function), expected);
		assertEquals(foldLeft(Arrays.asList(list), left, function), expected);
	}

	@DataProvider
	public Iterator<Object[]> dataMkString() {
		@SuppressWarnings("serial")
		class Data extends LinkedList<Object[]> {
			void data(String expected, String prefix, String sep, String suffix, Object... elements) {
				add(new Object[] { expected, null, prefix, sep, suffix, elements });
			}

			void dataConvert(String expected, F1<?, String> convert, String prefix, String sep, String suffix,
					Object... elements) {
				add(new Object[] { expected, convert, prefix, sep, suffix, elements });
			}
		}
		;

		Data data = new Data();

		data.data("ABC", null, null, null, "A", "B", "C");
		data.data("ABC", null, "", null, "A", "B", "C");
		data.data("ABC", "", null, null, "A", "B", "C");
		data.data("ABC", null, null, "", "A", "B", "C");
		data.data("ABC", "", "", "", "A", "B", "C");
		data.data("A,B,C", null, ",", "", "A", "B", "C");
		data.data("[A,B,C]", "[", ",", "]", "A", "B", "C");
		data.data("[A,B,null]", "[", ",", "]", "A", "B", null);

		F1<String, String> convert = new F1<String, String>() {
			public String apply(String param) {
				return param == null ? "" : param;
			}
		};

		data.dataConvert("[A,B,]", convert, "[", ",", "]", "A", "B", null);

		return data.iterator();
	}

	@Test(dataProvider = "dataMkString")
	public <T> void testMkString(String expected, F1<T, String> convert, String prefix, String sep, String suffix,
			T[] elements) {
		assertEquals(mkString(Arrays.asList(elements), prefix, sep, suffix, convert), expected);
		assertEquals(mkString(elements, prefix, sep, suffix, convert), expected);
		if (convert == null) {
			assertEquals(mkString(Arrays.asList(elements), prefix, sep, suffix), expected);
			assertEquals(mkString(elements, prefix, sep, suffix), expected);
		}
	}
}
