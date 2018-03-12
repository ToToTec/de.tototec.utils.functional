package de.tototec.utils.functional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Value class representing an optional value.
 *
 * This class is immutable and thus thread-safe.
 *
 * @param <T>
 *            The type of the optional value.
 */
public final class Optional<T> implements Iterable<T>, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Create a defined {@link Optional} with the given value +some+.
	 */
	public static <S> Optional<S> some(final S some) {
		return new Optional<S>(some);
	}

	/**
	 * Create an empty {@link Optional}.
	 * 
	 * Use {@link #of(S)} instead.
	 * 
	 * @see #of(S)
	 * 
	 * @deprecated Use {@link #getOrElseF(F0)} instead.
	 */
	public static <S> Optional<S> lift(final S someOrNull) {
		if (someOrNull == null) {
			return new Optional<S>();
		} else {
			return new Optional<S>(someOrNull);
		}
	}

	/**
	 * Create an empty {@link Optional}.
	 */
	public static <S> Optional<S> of(final S someOrNull) {
		if (someOrNull == null) {
			return new Optional<S>();
		} else {
			return new Optional<S>(someOrNull);
		}
	}

	@SuppressWarnings("unchecked")
	public static <N> Optional<N> none() {
		return (Optional<N>) NONE;
	}

	@SuppressWarnings("rawtypes")
	private static final Optional<?> NONE = new Optional();

	private final boolean isNone;
	private final T optional;

	private Optional() {
		this.isNone = true;
		this.optional = null;
	}

	private Optional(final T optional) {
		this.isNone = false;
		this.optional = optional;
	}

	public T get() {
		if (isDefined()) {
			return optional;
		} else {
			throw new NoSuchElementException("Optional value not defined.");
		}
	}

	public T getOrElse(final T t) {
		if (isDefined()) {
			return optional;
		} else {
			return t;
		}
	}

	public T getOrElseF(final F0<T> f) {
		if (isDefined()) {
			return optional;
		} else {
			return f.apply();
		}
	}

	public Optional<T> orElse(final Optional<T> t) {
		if (isDefined()) {
			return this;
		} else {
			return t;
		}
	}

	public Optional<T> orElseF(final F0<Optional<T>> f) {
		if (isDefined()) {
			return this;
		} else {
			return f.apply();
		}
	}

	public T orNull() {
		return isDefined() ? optional : null;
	}

	public <L> Either<L, T> toRight(final F0<L> left) {
		if (isDefined()) {
			return Either.right(get());
		} else {
			return Either.left(left.apply());
		}
	}

	public <R> Either<T, R> toLeft(final F0<R> right) {
		if (isDefined()) {
			return Either.left(get());
		} else {
			return Either.right(right.apply());
		}
	}

	public boolean isDefined() {
		return !isNone;
	}

	public boolean isEmpty() {
		return isNone;
	}

	public Optional<?> adapt() {
		return isDefined() ? Optional.some(get()) : Optional.none();
	}

	@SuppressWarnings("unchecked")
	public List<T> toList() {
		return isDefined() ? Arrays.<T> asList(optional) : Collections.<T> emptyList();
	}

	public Iterator<T> iterator() {
		return toList().iterator();
	}

	@SuppressWarnings("unchecked")
	public <R> Optional<R> map(final F1<? super T, ? extends R> f) {
		return (Optional<R>) (isEmpty() ? Optional.none() : Optional.some(f.apply(get())));
	}

	@SuppressWarnings("unchecked")
	public <R> Optional<R> flatMap(final F1<? super T, ? extends Optional<R>> f) {
		return (Optional<R>) (isEmpty() ? Optional.none() : f.apply(get()));
	}

	public void foreach(final Procedure1<? super T> f) {
		if (isDefined()) {
			f.apply(get());
		}
	}

	public Optional<T> filter(final F1<T, Boolean> predicate) {
		return (isDefined() && predicate.apply(get())) ? this : Optional.<T> none();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + (isEmpty() ? ".NONE" : "(" + optional + ")");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isNone ? 1231 : 1237);
		result = prime * result + ((optional == null) ? 0 : optional.hashCode());
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
		final Optional<?> other = (Optional<?>) obj;
		if (isNone != other.isNone) {
			return false;
		}
		if (optional == null) {
			if (other.optional != null) {
				return false;
			}
		} else if (!optional.equals(other.optional)) {
			return false;
		}
		return true;
	}

}
