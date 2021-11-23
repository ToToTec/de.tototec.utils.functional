package de.tototec.utils.functional;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Value class representing exactly one of two options, left or right.
 * <p>
 * This class is immutable and thus threadsafe.
 *
 * @param <L> The type of the left option.
 * @param <R> The type of the right option.
 */
public final class Either<L, R> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final L left;
    private final R right;
    private final boolean isRight;

    public static <L, R> Either<L, R> left(final L left) {
        return new Either<L, R>(left, null, false);
    }

    public static <L, R> Either<L, R> right(final R right) {
        return new Either<L, R>(null, right, true);
    }

    /* package private */ Either(final L left, final R right, final boolean isRight) {
        this.left = left;
        this.right = right;
        this.isRight = isRight;
        if (isRight && left != null) {
            throw new IllegalArgumentException("Left value must be null");
        } else if (!isRight && right != null) {
            throw new IllegalArgumentException("Right value must be null");
        }
    }

    public L left() {
        if (!isRight) {
            return left;
        } else {
            throw new NoSuchElementException("Left value not defined.");
        }
    }

    public Optional<L> leftOption() {
        if (!isRight) {
            return Optional.some(left);
        } else {
            return Optional.none();
        }
    }

    public Try<L> leftTry() {
        if (!isRight) {
            return Try.success(left);
        } else {
            return Try.failure(new NoSuchElementException("Either.left.value on Right"));
        }
    }

    public R right() {
        if (isRight) {
            return right;
        } else {
            throw new NoSuchElementException("Right value not defined.");
        }
    }

    public Optional<R> rightOption() {
        if (isRight) {
            return Optional.some(right);
        } else {
            return Optional.none();
        }
    }

    public Try<R> rightTry() {
        if (isRight) {
            return Try.success(right);
        } else {
            return Try.failure(new NoSuchElementException("Either.right.value on Left"));
        }
    }

    public boolean isLeft() {
        return !isRight;
    }

    public boolean isRight() {
        return isRight;
    }

    @Override
    public String toString() {
        return isRight ? ("Right(" + right + ")") : ("Left(" + left + ")");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isRight ? 1231 : 1237);
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        result = prime * result + ((right == null) ? 0 : right.hashCode());
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
        final Either<?, ?> other = (Either<?, ?>) obj;
        if (isRight != other.isRight) {
            return false;
        }
        if (isRight) {
            if (right == null) {
                if (other.right != null) {
                    return false;
                }
            } else if (!right.equals(other.right)) {
                return false;
            }
        } else {
            if (left == null) {
                if (other.left != null) {
                    return false;
                }
            } else if (!left.equals(other.left)) {
                return false;
            }
        }
        return true;
    }

    public Either<R, L> swap() {
        return new Either<R, L>(right, left, !isRight);
    }

    public <C> C fold(F1<L, C> l, F1<R, C> r) {
        return isRight ? r.apply(right) : l.apply(left);
    }

    public <R1> Either<L, R1> map(F1<R, R1> f) {
        return isRight ? Either.<L, R1>right(f.apply(right)) : (Either<L, R1>) left;
    }

    public <R1> Either<L, R1> flatMap(F1<R, Either<L, R1>> f) {
        return isRight ? f.apply(right) : (Either<L, R1>) left;
    }

    public void foreach(final Procedure1<? super R> f) {
        if (isRight) {
            f.apply(right);
        }
    }

    public R getOrElse(final R t) {
        return isRight ? right : t;
    }

    public R getOrElseF(final F0<R> f) {
        return isRight ? right : f.apply();
    }

    public Either<L, R> orElse(Either<L, R> or) {
        return isRight ? this : or;
    }

    public Either<L, R> orElseF(F0<Either<L, R>> or) {
        return isRight ? this : or.apply();
    }

    public List<R> toList() {
        return isRight ? Collections.singletonList(right) : Collections.<R>emptyList();
    }


}
