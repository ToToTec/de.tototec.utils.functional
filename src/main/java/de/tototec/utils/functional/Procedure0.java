package de.tototec.utils.functional;

public interface Procedure0 {

	public void apply();

	public static class AsRunnable implements Runnable {
		private final Procedure0 procedure;

		public AsRunnable(final Procedure0 procedure) {
			this.procedure = procedure;
		}

		@Override
		public void run() {
			procedure.apply();
		}
	}

	public static class FromRunnable implements Procedure0 {
		private final Runnable runnable;

		public FromRunnable(final Runnable runnable) {
			this.runnable = runnable;
		}

		@Override
		public void apply() {
			runnable.run();
		}
	}

	public static class NoOp implements Procedure0 {

		@Override
		public void apply() {
			// No op
		}

	}
}
