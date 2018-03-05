package de.tototec.utils.functional;

public interface Procedure4<P1, P2, P3, P4> {

	public void apply(P1 param1, P2 param2, P3 param3, P4 param4);

	public static final class NoOp<P1, P2, P3, P4> implements Procedure4<P1, P2, P3, P4> {

		@SuppressWarnings("unused")
		public void apply(final P1 p1, final P2 p2, final P3 p3, P4 p4) {
			// no op
		}

	}
}
