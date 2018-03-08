package de.tototec.utils.functional;

public abstract class Tuple {

	public static <A, B> Tuple2<A, B> of(final A a, final B b) {
		return Tuple2.of(a, b);
	}

	public static <A, B, C> Tuple3<A, B, C> of(final A a, final B b, final C c) {
		return Tuple3.of(a, b, c);
	}

	public static <A, B, C> Tuple3<A, B, C> of(final Tuple2<A, B> ab, final C c) {
		return Tuple3.of(ab.a(), ab.b(), c);
	}

	public static <A, B, C> Tuple3<A, B, C> of(final A a, final Tuple2<B, C> bc) {
		return Tuple3.of(a, bc.a(), bc.b());
	}

	public static <A, B, C, D> Tuple4<A, B, C, D> of(final A a, final B b, final C c, final D d) {
		return Tuple4.of(a, b, c, d);
	}

	public static <A, B, C, D> Tuple4<A, B, C, D> of(final Tuple3<A, B, C> abc, final D d) {
		return Tuple4.of(abc.a(), abc.b(), abc.c(), d);
	}

	public static <A, B, C, D> Tuple4<A, B, C, D> of(final A a, final Tuple3<B, C, D> bcd) {
		return Tuple4.of(a, bcd.a(), bcd.b(), bcd.c());
	}

	public static <A, B, C, D, E> Tuple5<A, B, C, D, E> of(final A a, final B b, final C c, final D d, final E e) {
		return Tuple5.of(a, b, c, d, e);
	}

	public static <A, B, C, D, E> Tuple5<A, B, C, D, E> of(final Tuple4<A, B, C, D> abcd, final E e) {
		return Tuple5.of(abcd.a(), abcd.b(), abcd.c(), abcd.d(), e);
	}

	public static <A, B, C, D, E> Tuple5<A, B, C, D, E> of(final A a, final Tuple4<B, C, D, E> bcde) {
		return Tuple5.of(a, bcde.a(), bcde.b(), bcde.c(), bcde.d());
	}

	private Tuple() {
		// no inheritance useful
	}
}
