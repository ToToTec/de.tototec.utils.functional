package de.tototec.utils.functional;

public interface CheckedF1<P, R> {
	public R apply(P param) throws Throwable;

	public final class Identity<I> implements CheckedF1<I, I> {

		public I apply(final I param) throws Throwable {
			return param;
		}

	}

}
