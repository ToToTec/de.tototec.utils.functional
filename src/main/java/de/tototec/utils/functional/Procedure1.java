package de.tototec.utils.functional;

public interface Procedure1<P> {

	public void apply(P param);

	public static class NoOp<P> implements Procedure1<P> {

		@SuppressWarnings("unused")
		public void apply(final P p) {
			// no op
		}

	}
}
