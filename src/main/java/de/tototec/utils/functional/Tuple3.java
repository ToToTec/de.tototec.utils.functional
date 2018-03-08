package de.tototec.utils.functional;

import java.io.Serializable;

/**
 * Value class representing a 3-tuple.
 *
 * This class is immutable and thus thread-safe.
 *
 * @param <A>
 *            The type of the first element.
 * @param <B>
 *            The type of the second element.
 * @param <C>
 *            The type of the third element.
 */
public class Tuple3<A, B, C> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static <A, B, C> Tuple3<A, B, C> of(final A a, final B b, final C c) {
		return new Tuple3<A, B, C>(a, b, c);
	}

	public static <A, B, C> Tuple3<A, B, C> of(final Tuple2<A, B> ab, final C c) {
		return new Tuple3<A, B, C>(ab.a(), ab.b(), c);
	}

	public static <A, B, C> Tuple3<A, B, C> of(final A a, final Tuple2<B, C> bc) {
		return new Tuple3<A, B, C>(a, bc.a(), bc.b());
	}

	private final A a;
	private final B b;
	private final C c;

	protected Tuple3(final A a, final B b, final C c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public A a() {
		return a;
	}

	public B b() {
		return b;
	}

	public C c() {
		return c;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a() == null) ? 0 : a().hashCode());
		result = prime * result + ((b() == null) ? 0 : b().hashCode());
		result = prime * result + ((c() == null) ? 0 : c().hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}

		if (!(other instanceof Tuple3)) {
			return false;
		}

		final Tuple3<?, ?, ?> that = (Tuple3<?, ?, ?>) other;
		if (!that.canEqual(this)) {
			return false;
		}

		if (this.a() == null) {
			if (that.a() != null) {
				return false;
			}
		} else if (!this.a().equals(that.a())) {
			return false;
		}
		if (this.b() == null) {
			if (that.b() != null) {
				return false;
			}
		} else if (!this.b().equals(that.b())) {
			return false;
		}
		if (this.c() == null) {
			if (that.c() != null) {
				return false;
			}
		} else if (!this.c().equals(that.c())) {
			return false;
		}
		return true;
	}

	public boolean canEqual(final Object other) {
		return other instanceof Tuple3;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + a() + "," + b() + "," + c() + ")";
	}
}
