package de.tototec.utils.functional;

public interface Procedure3<P1, P2, P3> {

	public void apply(P1 param1, P2 param2, P3 param3);

	public static final class NoOp<P1, P2, P3> implements Procedure3<P1, P2, P3> {

		@SuppressWarnings("unused")
		public void apply(final P1 p1, final P2 p2, final P3 p3) {
			// no op
		}

	}
}
