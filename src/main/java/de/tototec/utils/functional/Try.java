package de.tototec.utils.functional;

import java.io.Serializable;
import java.util.NoSuchElementException;

public final class Try<T> implements Serializable {

	// BEGIN OF STATIC PART

	private static final long serialVersionUID = 1L;

	public static <T> Try<T> success(final T success) {
		return new Try<T>(null, success, true);
	}

	public static <T> Try<T> failure(final Throwable failure) {
		if (isFatal(failure)) {
			Util.<RuntimeException, T> sneakyThrow(failure);
		}
		return new Try<T>(failure, null, false);
	}

	public static <T> Try<T> of(final CheckedF0<T> provider) {
		try {
			return success(provider.apply());
		} catch (final Throwable e) {
			return failure(e);
		}
	}

	public static boolean isFatal(final Throwable throwable) {
		return throwable instanceof InterruptedException
				|| throwable instanceof LinkageError
				|| throwable instanceof ThreadDeath
				|| throwable instanceof VirtualMachineError;
	}

	private static final class Util {
		@SuppressWarnings("unchecked")
		private static final <T extends Throwable, R> R sneakyThrow(final Throwable t) throws T {
			throw (T) t;
		}
	}

	// END OF STATIC PART

	private final Throwable failure;
	private final T value;
	private final boolean isSuccess;

	private Try(final Throwable error, final T value, final boolean isOk) {
		this.failure = error;
		this.value = value;
		this.isSuccess = isOk;
		if (isOk && error != null) {
			throw new IllegalArgumentException("Failure must be null");
		} else if (!isOk && value != null) {
			throw new IllegalArgumentException("Value value must be null");
		}
	}

	public Throwable failure() {
		if (!isSuccess) {
			return failure;
		} else {
			throw new NoSuchElementException("Failure not defined.");
		}
	}

	public Optional<Throwable> failureOption() {
		if (!isSuccess) {
			return Optional.some(failure);
		} else {
			return Optional.none();
		}
	}

	/**
	 * Get the value or throw the exception.
	 *
	 * @return
	 */
	public T get() {
		if (isSuccess) {
			return value;
		} else {
			return Util.<RuntimeException, T> sneakyThrow(failure);
		}
	}

	public Optional<T> toOption() {
		if (isSuccess) {
			return Optional.some(value);
		} else {
			return Optional.none();
		}
	}

	public Either<Throwable, T> toEither() {
		return new Either<Throwable, T>(failure, value, isSuccess);
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public boolean isFailure() {
		return !isSuccess;
	}

	@Override
	public String toString() {
		return isSuccess ? ("Success(" + value + ")") : ("Failure(" + failure + ")");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isSuccess ? 1231 : 1237);
		result = prime * result + ((failure == null) ? 0 : failure.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Try<?> other = (Try<?>) obj;
		if (isSuccess != other.isSuccess) {
			return false;
		}
		if (isSuccess) {
			if (value == null) {
				if (other.value != null) {
					return false;
				}
			} else if (!value.equals(other.value)) {
				return false;
			}
		} else {
			if (failure == null) {
				if (other.failure != null) {
					return false;
				}
			} else if (!failure.equals(other.failure)) {
				return false;
			}
		}
		return true;
	}

	public T getOrElse(final T t) {
		if (isSuccess) {
			return get();
		} else {
			return t;
		}
	}

	public T getOrElseF(final F0<T> f) {
		if (isSuccess) {
			return get();
		} else {
			return f.apply();
		}
	}

	public Try<T> orElseF(final CheckedF0<Try<T>> f) {
		if (isSuccess) {
			return this;
		} else {
			try {
				return f.apply();
			} catch (final Throwable e) {
				if (isFatal(e)) {
					Util.<RuntimeException, T> sneakyThrow(e);
				}
				return new Try<T>(e, null, false);
			}
		}
	}

	public void foreach(final Procedure1<? super T> f) {
		if (isSuccess()) {
			f.apply(get());
		}
	}

}
