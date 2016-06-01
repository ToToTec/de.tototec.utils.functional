package de.tototec.utils.functional;

import static de.tobiasroeser.lambdatest.Expect.expectEquals;
import static de.tobiasroeser.lambdatest.Expect.expectNotEquals;

import de.tobiasroeser.lambdatest.testng.FreeSpec;

public class OptionalTest extends FreeSpec {

	public OptionalTest() {

		final Object o1 = new Object();

		test("Optional.getOrElse", () -> {
			expectEquals(Optional.none().getOrElse(o1), o1);
			expectEquals(Optional.some(o1).getOrElse(o1), o1);
			expectNotEquals(Optional.some(new Object()).getOrElse(o1), o1);
		});

		test("Optional.getOrElseF", () -> {
			expectEquals(Optional.none().getOrElseF(new F0<Object>() {
				@Override
				public Object apply() {
					return o1;
				}
			}), o1);
			expectEquals(Optional.some(o1).getOrElseF(new F0<Object>() {
				@Override
				public Object apply() {
					return o1;
				}
			}), o1);
			expectNotEquals(Optional.some(new Object()).getOrElseF(new F0<Object>() {
				@Override
				public Object apply() {
					return o1;
				}
			}), o1);
		});

	}

}
