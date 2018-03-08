package de.tototec.utils.functional;

import java.io.Serializable;

/**
 * Value class representing a 4-tuple.
 *
 * This class is immutable and thus thread-safe.
 *
 * @param <A>
 *            The type of the first element.
 * @param <B>
 *            The type of the second element.
 * @param <C>
 *            The type of the third element.
 * @param <D>
 *            The type of the fourth element.
 */
public class Tuple4<A, B, C, D> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static <A, B, C, D> Tuple4<A, B, C, D> of(final A a, final B b, final C c, final D d) {
		return new Tuple4<A, B, C, D>(a, b, c, d);
	}

	public static <A, B, C, D> Tuple4<A, B, C, D> of(final Tuple3<A, B, C> abc, final D d) {
		return new Tuple4<A, B, C, D>(abc.a(), abc.b(), abc.c(), d);
	}

	public static <A, B, C, D> Tuple4<A, B, C, D> of(final A a, final Tuple3<B, C, D> bcd) {
		return new Tuple4<A, B, C, D>(a, bcd.a(), bcd.b(), bcd.c());
	}

	private final A a;
	private final B b;
	private final C c;
	private final D d;

	protected Tuple4(final A a, final B b, final C c, final D d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
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

	public D d() {
		return d;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a() == null) ? 0 : a().hashCode());
		result = prime * result + ((b() == null) ? 0 : b().hashCode());
		result = prime * result + ((c() == null) ? 0 : c().hashCode());
		result = prime * result + ((d() == null) ? 0 : d().hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}

		if (!(other instanceof Tuple4)) {
			return false;
		}

		final Tuple4<?, ?, ?, ?> that = (Tuple4<?, ?, ?, ?>) other;
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
		if (this.d() == null) {
			if (that.d() != null) {
				return false;
			}
		} else if (!this.d().equals(that.d())) {
			return false;
		}
		return true;
	}

	public boolean canEqual(final Object other) {
		return other instanceof Tuple4;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + a() + "," + b() + "," + c() + "," + d() + ")";
	}
}
