package de.tototec.utils.functional;

import static de.tobiasroeser.lambdatest.Expect.*;

import de.tobiasroeser.lambdatest.testng.FreeSpec;

public class CanEqualsSupportTest extends FreeSpec {

	public CanEqualsSupportTest() {

		class NoFields {
			private final CanEqualsSupport<NoFields> eq = new CanEqualsSupport<>(NoFields.class);

			@Override
			public boolean equals(Object other) {
				return eq._equals(this, other);
			}

			@Override
			public int hashCode() {
				return eq._hashCode(this);
			}

			public boolean canEqual(Object other) {
				return eq._canEqual(other);
			}
		}

		test("Immutable class with no fields", () -> {
			final NoFields a = new NoFields();
			final NoFields b = new NoFields();
			checkEquals(Tuple2.of(a, a), Tuple2.of(a, b));
		});

		class NoFieldsDerived extends NoFields {
		}

		test("Immutable class with no fields is equal to it's derivation", () -> {
			final NoFields a = new NoFields();
			final NoFieldsDerived a1 = new NoFieldsDerived();
			checkEquals(Tuple2.of(a, a1));
		});


		class NoFieldsWithoutCanEqual {
			private final CanEqualsSupport<NoFieldsWithoutCanEqual> eq = new CanEqualsSupport<>(NoFieldsWithoutCanEqual.class);

			@Override
			public boolean equals(Object other) {
				return eq._equals(this, other);
			}

			@Override
			public int hashCode() {
				return eq._hashCode(this);
			}
		}

		test("Immutable class with no fields and no canEqual method", () -> {
			final NoFieldsWithoutCanEqual a = new NoFieldsWithoutCanEqual();
			final NoFieldsWithoutCanEqual b = new NoFieldsWithoutCanEqual();
			checkEquals(Tuple2.of(a, a), Tuple2.of(a, b));
		});

		class NoFieldsWithoutCanEqualDerived {
		}

		test("Immutable class with no fields and no canEqual method is not equal to it's derivation", () -> {
			final NoFieldsWithoutCanEqual a = new NoFieldsWithoutCanEqual();
			final NoFieldsWithoutCanEqualDerived a1 = new NoFieldsWithoutCanEqualDerived();
			checkNonEquals(Tuple2.of(a, a1));
		});

		class SingleFields {
			private final CanEqualsSupport<SingleFields> eq = new CanEqualsSupport<>(SingleFields.class, SingleFields::getA);
			private final String a;

			public SingleFields(String a) {
				this.a = a;
			}

			public String getA() {
				return a;
			}

			@Override
			public boolean equals(Object other) {
				return eq._equals(this, other);
			}

			@Override
			public int hashCode() {
				return eq._hashCode(this);
			}

			public boolean canEqual(Object other) {
				return eq._canEqual(other);
			}
		}


		test("Immutable class with single fields", () -> {
			final SingleFields a = new SingleFields("Hello");
			final SingleFields b = new SingleFields("Hello");
			final SingleFields c = new SingleFields("hello");
			checkEquals(
					Tuple2.of(a, a),
					Tuple2.of(a, b)
			);
			checkNonEquals(
					Tuple2.of(a, c),
					Tuple2.of(b, c)
			);
		});

		class DoubleFields {
			private final CanEqualsSupport<DoubleFields> eq = new CanEqualsSupport<>(DoubleFields.class, DoubleFields::getA, DoubleFields::getB);
			private final String a;
			private final long b;

			public DoubleFields(String a, long b) {
				this.a = a;
				this.b = b;
			}

			public String getA() {
				return a;
			}

			public long getB() {
				return b;
			}

			@Override
			public boolean equals(Object other) {
				return eq._equals(this, other);
			}

			@Override
			public int hashCode() {
				return eq._hashCode(this);
			}

			public boolean canEqual(Object other) {
				return eq._canEqual(other);
			}
		}

		test("Immutable class with two fields", () -> {
			final DoubleFields a = new DoubleFields("Hello", 1L);
			final DoubleFields b = new DoubleFields("Hello", 1L);
			final DoubleFields c = new DoubleFields("hello", 1L);
			final DoubleFields d = new DoubleFields("Hello", 2L);
			final DoubleFields e = new DoubleFields("hello", 2L);
			final DoubleFields f = new DoubleFields(null, 2L);
			final DoubleFields g = new DoubleFields(null, 2L);
			checkEquals(
					Tuple2.of(a, a),
					Tuple2.of(a, b),
					Tuple2.of(f, g)
			);
			checkNonEquals(
					Tuple2.of(a, c),
					Tuple2.of(a, d),
					Tuple2.of(a, e),
					Tuple2.of(a, f),
					Tuple2.of(c, d),
					Tuple2.of(c, e),
					Tuple2.of(c, f),
					Tuple2.of(d, e),
					Tuple2.of(d, f)
			);
		});

		class DoubleFieldsFieldAccess {
			private final CanEqualsSupport<DoubleFieldsFieldAccess> eq = new CanEqualsSupport<>(DoubleFieldsFieldAccess.class, c -> c.a, c -> c.b);
			private final String a;
			private final long b;

			public DoubleFieldsFieldAccess(String a, long b) {
				this.a = a;
				this.b = b;
			}

			@Override
			public boolean equals(Object other) {
				return eq._equals(this, other);
			}

			@Override
			public int hashCode() {
				return eq._hashCode(this);
			}

			public boolean canEqual(Object other) {
				return eq._canEqual(other);
			}
		}

		test("Immutable class with two fields (field access)", () -> {
			final DoubleFieldsFieldAccess a = new DoubleFieldsFieldAccess("Hello", 1L);
			final DoubleFieldsFieldAccess b = new DoubleFieldsFieldAccess("Hello", 1L);
			final DoubleFieldsFieldAccess c = new DoubleFieldsFieldAccess("hello", 1L);
			final DoubleFieldsFieldAccess d = new DoubleFieldsFieldAccess("Hello", 2L);
			final DoubleFieldsFieldAccess e = new DoubleFieldsFieldAccess("hello", 2L);
			final DoubleFieldsFieldAccess f = new DoubleFieldsFieldAccess(null, 2L);
			final DoubleFieldsFieldAccess g = new DoubleFieldsFieldAccess(null, 2L);
			final DoubleFieldsFieldAccess h = new DoubleFieldsFieldAccess(null, 1L);
			checkEquals(
					Tuple2.of(a, a),
					Tuple2.of(a, b),
					Tuple2.of(f, g)
			);
			checkNonEquals(
					Tuple2.of(a, c),
					Tuple2.of(a, d),
					Tuple2.of(a, e),
					Tuple2.of(a, f),
					Tuple2.of(c, d),
					Tuple2.of(c, e),
					Tuple2.of(c, f),
					Tuple2.of(d, e),
					Tuple2.of(d, f),
					Tuple2.of(g, h)
			);
		});

		class Point {

			private final int x;
			private final int y;

			public Point(int x, int y) {
				this.x = x;
				this.y = y;
			}

			public int getX() {
				return x;
			}

			public int getY() {
				return y;
			}

			private final CanEqualsSupport<Point> eq = new CanEqualsSupport<>(Point.class, c -> c.getX(), c -> c.getY());

			@Override
			public boolean equals(Object other) {
//				boolean result = false;
//				if (other instanceof Point) {
//					Point that = (Point) other;
//					result = (
//						that.canEqual(this) &&
//						this.getX() == that.getX() && this.getY() == that.getY());
//				}
//				return result;
				return eq._equals(this, other);
			}

			@Override
			public int hashCode() {
//				return (41 * (41 + getX()) + getY());
				return eq._hashCode(this);
			}

			public boolean canEqual(Object other) {
//				return (other instanceof Point);
				return eq._canEqual(other);
			}
		}

		class ColoredPoint extends Point { // No longer violates symmetry requirement

			private final String color;

			public ColoredPoint(int x, int y, String color) {
				super(x, y);
				this.color = color;
			}

			private final CanEqualsSupport<ColoredPoint> eq = new CanEqualsSupport<>(ColoredPoint.class, c -> c.color);

			@Override
			public boolean equals(Object other) {
//				boolean result = false;
//				if (other instanceof ColoredPoint) {
//					ColoredPoint that = (ColoredPoint) other;
//					result = (
//						that.canEqual(this) &&
//							this.color.equals(that.color) && super.equals(that));
//				}
//				return result;
				return eq._equals(this, other) && super.equals(other);
			}

			@Override
			public int hashCode() {
//				return (41 * super.hashCode() + color.hashCode());
				return eq._hashCode(this, super.hashCode());
			}

			@Override
			public boolean canEqual(Object other) {
//				return (other instanceof ColoredPoint);
				return eq._canEqual(other);
			}

		}

		test("Derived data class has proper equals contract", () -> {
			Point p1 = new Point(1, 1);
			Point p2 = new Point(1, 1);
			Point p3 = new Point(2, 2);
			ColoredPoint cp1 = new ColoredPoint(1, 1, "red");
			ColoredPoint cp2 = new ColoredPoint(1, 1, "red");
			ColoredPoint cp3 = new ColoredPoint(2, 2, "blue");

			checkEquals(
					Tuple2.of(p1, p2),
					Tuple2.of(cp1, cp2)
			);
			checkNonEquals(
					Tuple2.of(p1, p3),
					Tuple2.of(p1, cp1),
					Tuple2.of(p3, cp3),
					Tuple2.of(cp1, cp3)
			);
		});

	}

	void checkEquals(Tuple2<?, ?>... tuples) {
		for (Tuple2 tuple : tuples) {
			expectTrue(tuple.a().equals(tuple.b()), "Expected [" + tuple.a() + "] equals [" + tuple.b() + "]");
			expectTrue(tuple.b().equals(tuple.a()), "Expected [" + tuple.b() + "] equals [" + tuple.b() + "]");
			expectEquals(tuple.a().hashCode(), tuple.b().hashCode(), "Expected [" + tuple.a() + "] and [" + tuple.b() + "] to have same hashCode");
		}
	}

	void checkNonEquals(Tuple2<?, ?>... tuples) {
		for (Tuple2 tuple : tuples) {
			expectFalse(tuple.a().equals(tuple.b()), "Expected [" + tuple.a() + "] not equals [" + tuple.b() + "]");
			expectFalse(tuple.b().equals(tuple.a()), "Expected [" + tuple.b() + "] not equals [" + tuple.a() + "]");
		}
	}

}
