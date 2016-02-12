package de.tototec.utils.functional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import de.tobiasroeser.lambdatest.RunnableWithException;
import de.tobiasroeser.lambdatest.testng.FreeSpec;

public class OptionalTest extends FreeSpec {

	public OptionalTest() {

		final Object o1 = new Object();

		test("Optional.getOrElse", new RunnableWithException() {
			public void run() throws Exception {
				assertEquals(Optional.none().getOrElse(o1), o1);
				assertEquals(Optional.some(o1).getOrElse(o1), o1);
				assertNotEquals(Optional.some(new Object()).getOrElse(o1), o1);
			}
		});

		test("Optional.getOrElseF0", new RunnableWithException() {
			public void run() throws Exception {
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
		});

	}

}
