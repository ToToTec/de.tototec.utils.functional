package de.tototec.utils.functional;

public class ChainedFunction1<P, P0, R> implements F1<P, R> {

	private final F1<P, P0> inner;
	private final F1<P0, R> outer;

	public ChainedFunction1(F1<P, P0> inner, F1<P0, R> outer) {
		this.inner = inner;
		this.outer = outer;
	}

	public R apply(P param) {
		return outer.apply(inner.apply(param));
	}

}
