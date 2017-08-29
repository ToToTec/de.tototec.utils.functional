package de.tototec.utils.functional;

public final class ChainedFunction1<P, P0, R> implements F1<P, R> {

	private final F1<P, P0> inner;
	private final F1<P0, R> outer;

	public ChainedFunction1(final F1<P, P0> inner, final F1<P0, R> outer) {
		this.inner = inner;
		this.outer = outer;
	}

	public R apply(final P param) {
		return outer.apply(inner.apply(param));
	}

}
