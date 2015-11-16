package de.tototec.utils.functional;

public interface Procedure2<P1, P2> {

	public void apply(P1 param1, P2 param2);

	public static class NoOp<P1, P2> implements Procedure2<P1, P2> {

		@SuppressWarnings("unused")
		public void apply(final P1 p1, final P2 p2) {
			// no op
		}

	}
}
