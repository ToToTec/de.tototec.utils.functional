package de.tototec.utils.functional;

import java.io.Serializable;

/**
 * Value class representing a 5-tuple.
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
 * @param <E>
 *            The type of the fifth element.
 */
public class Tuple5<A, B, C, D, E> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static <A, B, C, D, E> Tuple5<A, B, C, D, E> of(final A a, final B b, final C c, final D d, final E e) {
		return new Tuple5<A, B, C, D, E>(a, b, c, d, e);
	}

	public static <A, B, C, D, E> Tuple5<A, B, C, D, E> of(final Tuple4<A, B, C, D> abcd, final E e) {
		return new Tuple5<A, B, C, D, E>(abcd.a(), abcd.b(), abcd.c(), abcd.d(), e);
	}

	public static <A, B, C, D, E> Tuple5<A, B, C, D, E> of(final A a, final Tuple4<B, C, D, E> bcde) {
		return new Tuple5<A, B, C, D, E>(a, bcde.a(), bcde.b(), bcde.c(), bcde.d());
	}

	private final A a;
	private final B b;
	private final C c;
	private final D d;
	private final E e;

	protected Tuple5(final A a, final B b, final C c, final D d, final E e) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
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

	public E e() {
		return e;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a() == null) ? 0 : a().hashCode());
		result = prime * result + ((b() == null) ? 0 : b().hashCode());
		result = prime * result + ((c() == null) ? 0 : c().hashCode());
		result = prime * result + ((d() == null) ? 0 : d().hashCode());
		result = prime * result + ((e() == null) ? 0 : e().hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}

		if (!(other instanceof Tuple5)) {
			return false;
		}

		final Tuple5<?, ?, ?, ?, ?> that = (Tuple5<?, ?, ?, ?, ?>) other;
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
		if (this.e() == null) {
			if (that.e() != null) {
				return false;
			}
		} else if (!this.e().equals(that.e())) {
			return false;
		}
		return true;
	}

	public boolean canEqual(final Object other) {
		return other instanceof Tuple5;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + a() + "," + b() + "," + c() + "," + d() + "," + e() + ")";
	}
}
