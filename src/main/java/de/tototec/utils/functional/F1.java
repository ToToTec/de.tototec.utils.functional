package de.tototec.utils.functional;

public interface F1<P, R> {
	public R apply(P param);

	public final class Identity<I> implements F1<I, I> {

		public I apply(final I param) {
			return param;
		}

	}

}
