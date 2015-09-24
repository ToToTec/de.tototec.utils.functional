package de.tototec.utils.functional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import org.testng.annotations.Test;

public class OptionalTest {

	@Test
	public void testGetOrElseT() {
		final Object o1 = new Object();
		assertEquals(Optional.none().getOrElse(o1), o1);
		assertEquals(Optional.some(o1).getOrElse(o1), o1);
		assertNotEquals(Optional.some(new Object()).getOrElse(o1), o1);
	}

	@Test
	public void testGetOrElseF0() {
		final Object o1 = new Object();
		assertEquals(Optional.none().getOrElse(new F0<Object>() {
			public Object apply() {
				return o1;
			}
		}), o1);
		assertEquals(Optional.some(o1).getOrElse(new F0<Object>() {
			public Object apply() {
				return o1;
			}
		}), o1);
		assertNotEquals(Optional.some(new Object()).getOrElse(new F0<Object>() {
			public Object apply() {
				return o1;
			}
		}), o1);
	}

}
