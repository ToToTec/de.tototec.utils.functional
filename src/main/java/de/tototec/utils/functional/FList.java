package de.tototec.utils.functional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class FList {

	// TODO: concat

	public static <T, A extends T, B extends T> List<T> concat(final A[] first, final B[] second) {
		return concat(Arrays.asList(first), Arrays.asList(second));
	}

	public static <T, A extends T, B extends T> List<T> concat(final Iterable<A> first, final Iterable<B> second) {
		final LinkedList<T> result = new LinkedList<T>();
		for (final A a : first) {
			result.add(a);
		}
		for (final B b : second) {
			result.add(b);
		}
		return result;
	}

	public static <T> boolean contains(final Iterable<T> source, final T element) {
		for (final T t : source) {
			if (t == null) {
				if (element == null) {
					return true;
				}
			} else if (t.equals(element)) {
				return true;
			}
		}
		return false;
	}

	public static <T> boolean contains(final T[] source, final T element) {
		return contains(Arrays.asList(source), element);
	}

	public static <T> boolean containsAll(final Iterable<T> source, final Iterable<T> elements) {
		for (final T e : elements) {
			if (!contains(source, e)) {
				return false;
			}
		}
		return true;
	}

	public static <T> boolean containsAll(final Iterable<T> source, final T[] elements) {
		return containsAll(source, Arrays.asList(elements));
	}

	public static <T> boolean containsAll(final T[] source, final Iterable<T> elements) {
		return containsAll(Arrays.asList(source), elements);
	}

	public static <T> boolean containsAll(final T[] source, final T[] elements) {
		return containsAll(Arrays.asList(source), Arrays.asList(elements));
	}

	public static <T> List<T> distinct(final Iterable<T> source) {
		final List<T> result = new LinkedList<T>();
		for (final T t : source) {
			if (!result.contains(source)) {
				result.add(t);
			}
		}
		return result;
	}

	public static <T> List<T> distinct(final T[] source) {
		return distinct(Arrays.asList(source));
	}

	public static <T> List<T> dropWhile(final Iterable<T> source, final F1<? super T, Boolean> accept) {
		final List<T> result = new LinkedList<T>();
		boolean drop = true;
		for (final T t : source) {
			if (drop && !accept.apply(t)) {
				drop = false;
			}
			if (!drop) {
				result.add(t);
			}
		}
		return result;
	}

	public static <T> List<T> dropWhile(final T[] source, final F1<? super T, Boolean> accept) {
		return dropWhile(Arrays.asList(source), accept);
	}

	public static <T> boolean exists(final Iterable<T> source, final F1<? super T, Boolean> exists) {
		for (final T t : source) {
			if (exists.apply(t)) {
				return true;
			}
		}
		return false;
	}

	public static <T> boolean exists(final T[] source, final F1<? super T, Boolean> exists) {
		return exists(Arrays.asList(source), exists);
	}

	public static <T> List<T> filter(final Iterable<T> source, final F1<? super T, Boolean> accept) {
		final List<T> result = new LinkedList<T>();
		for (final T t : source) {
			if (accept.apply(t)) {
				result.add(t);
			}
		}
		return result;
	}

	public static <T> List<T> filter(final T[] source, final F1<? super T, Boolean> accept) {
		return filter(Arrays.asList(source), accept);
	}

	public static <T> List<T> filterNotNull(final Iterable<?> source) {
		final List<T> result = new LinkedList<T>();
		for (final Object object : source) {
			if (object != null) {
				@SuppressWarnings("unchecked")
				final T t = (T) object;
				result.add(t);
			}
		}
		return result;
	}

	public static <T> List<T> filterNotNull(final Object[] source) {
		return filterNotNull(Arrays.asList(source));
	}

	public static <T> List<T> filterType(final Iterable<?> source, final Class<T> type) {
		final List<T> result = new LinkedList<T>();
		for (final Object object : source) {
			if (object != null && type.isAssignableFrom(object.getClass())) {
				@SuppressWarnings("unchecked")
				final T t = (T) object;
				result.add(t);
			}
		}
		return result;
	}

	public static <T> List<T> filterType(final Object[] source, final Class<T> type) {
		return filterType(Arrays.asList(source), type);
	}

	public static <T> Optional<T> find(final Iterable<T> source, final F1<? super T, Boolean> accept) {
		for (final T t : source) {
			if (accept.apply(t)) {
				return Optional.some(t);
			}
		}
		return Optional.none();
	}

	public static <T> Optional<T> find(final T[] source, final F1<? super T, Boolean> accept) {
		return find(Arrays.asList(source), accept);
	}

	public static <T, R> List<R> flatMap(final Iterable<T> source, final F1<? super T, ? extends Iterable<R>> convert) {
		final List<R> result = (source instanceof Collection<?>) ? new ArrayList<R>(((Collection<?>) source).size())
				: new LinkedList<R>();
		for (final T t : source) {
			final Iterable<R> subList = convert.apply(t);
			if (subList instanceof Collection<?>) {
				result.addAll((Collection<? extends R>) subList);
			} else {
				for (final R r : subList) {
					result.add(r);
				}
			}
		}
		return result;
	}

	public static <T, R> List<R> flatMap(final T[] source, final F1<? super T, ? extends Iterable<R>> convert) {
		return flatMap(Arrays.asList(source), convert);
	}

	public static <T, R extends Iterable<T>> List<T> flatten(final Iterable<R> source) {
		final LinkedList<T> result = new LinkedList<T>();
		for (final Iterable<T> list : source) {
			if (list instanceof Collection<?>) {
				result.addAll((Collection<? extends T>) list);
			} else {
				for (final T t : list) {
					result.add(t);
				}
			}
		}
		return result;
	}

	public static <T> List<T> flatten(final Iterable<T>[] source) {
		return flatten(Arrays.asList(source));
	}

	public static <T> List<T> flatten(final T[][] source) {
		final LinkedList<T> result = new LinkedList<T>();
		for (final T[] list : source) {
			for (final T t : list) {
				result.add(t);
			}
		}
		return result;
	}

	public static <T, R> R foldLeft(final Iterable<T> source, final R left, final F2<R, ? super T, R> fold) {
		R theLeft = left;
		for (final T t : source) {
			theLeft = fold.apply(theLeft, t);
		}
		return theLeft;
	}

	public static <T, R> R foldLeft(final T[] source, final R left, final F2<R, ? super T, R> fold) {
		return foldLeft(Arrays.asList(source), left, fold);
	}

	public static <T, R> R foldRight(final Iterable<T> source, final F2<? super T, R, R> fold, final R right) {
		final List<T> list = source instanceof List<?> ? (List<T>) source : map(source, new F1.Identity<T>());
		R theRight = right;
		for (int i = list.size() - 1; i >= 0; --i) {
			theRight = fold.apply(list.get(i), theRight);
		}
		return theRight;
	}

	public static <T, R> R foldRight(final T[] source, final F2<? super T, R, R> fold, final R right) {
		R theRight = right;
		for (int i = source.length - 1; i >= 0; --i) {
			theRight = fold.apply(source[i], theRight);
		}
		return theRight;
	}

	public static <T> boolean forall(final Iterable<T> source, final F1<? super T, Boolean> forall) {
		for (final T t : source) {
			if (!forall.apply(t)) {
				return false;
			}
		}
		return true;
	}

	public static <T> boolean forall(final T[] source, final F1<? super T, Boolean> forall) {
		return forall(Arrays.asList(source), forall);
	}

	public static <T> void foreach(final Iterable<T> source, final Procedure1<? super T> foreach) {
		for (final T t : source) {
			foreach.apply(t);
		}
	}

	public static <T> void foreach(final T[] source, final Procedure1<? super T> foreach) {
		foreach(Arrays.asList(source), foreach);
	}

	public static <T, K> Map<K, List<T>> groupBy(final T[] source, final F1<? super T, ? extends K> groupBy) {
		return groupBy(Arrays.asList(source), groupBy);
	}

	public static <T, K> Map<K, List<T>> groupBy(final Iterable<T> source, final F1<? super T, ? extends K> groupBy) {
		final Map<K, List<T>> result = new LinkedHashMap<K, List<T>>();
		for (final T t : source) {
			final K key = groupBy.apply(t);
			final List<T> list;
			if (result.containsKey(key)) {
				list = result.get(key);
			} else {
				list = new LinkedList<T>();
				result.put(key, list);
			}
			list.add(t);
		}
		return result;
	}

	public static <T> Optional<T> headOption(final Iterable<T> source) {
		final Iterator<T> it = source.iterator();
		if (it.hasNext()) {
			return Optional.some(it.next());
		} else {
			return Optional.<T> none();
		}
	}

	public static <T> Optional<T> headOption(final T[] source) {
		if (source.length == 0) {
			return Optional.<T> none();
		} else {
			return Optional.some(source[0]);
		}
	}

	public static <T, R> List<R> map(final Iterable<T> source, final F1<? super T, ? extends R> convert) {
		final List<R> result = (source instanceof Collection<?>) ? new ArrayList<R>(((Collection<?>) source).size())
				: new LinkedList<R>();
		for (final T t : source) {
			result.add(convert.apply(t));
		}
		return result;
	}

	public static <T, R> List<R> map(final T[] source, final F1<? super T, ? extends R> convert) {
		return map(Arrays.asList(source), convert);
	}

	public static String mkString(final Iterable<?> source, final String separator) {
		return mkString(source, null, separator, null);
	}

	public static String mkString(final Object[] source, final String separator) {
		return mkString(Arrays.asList(source), separator);
	}

	public static String mkString(final Iterable<?> source, final String prefix, final String separator,
			final String suffix) {
		return mkString(source, prefix, separator, suffix, null);
	}

	public static <T> String mkString(final T[] source, final String prefix, final String separator, final String suffix) {
		return mkString(Arrays.asList(source), prefix, separator, suffix);
	}

	public static <T> String mkString(final Iterable<T> source, final String prefix, final String separator,
			final String suffix, final F1<? super T, String> convert) {
		final StringBuilder result = new StringBuilder();
		if (prefix != null) {
			result.append(prefix);
		}
		boolean sep = false;
		for (final T t : source) {
			if (sep && separator != null) {
				result.append(separator);
			}
			sep = true;
			if (convert != null) {
				result.append(convert.apply(t));
			} else {
				result.append(t == null ? null : t.toString());
			}
		}
		if (suffix != null) {
			result.append(suffix);
		}
		return result.toString();
	}

	public static <T> String mkString(final T[] source, final String prefix, final String separator,
			final String suffix, final F1<? super T, String> convert) {
		return mkString(Arrays.asList(source), prefix, separator, suffix, convert);
	}

	public static <T> Tuple2<List<T>, List<T>> partition(final Iterable<T> source,
			final F1<? super T, Boolean> predicate) {
		final List<T> left = new LinkedList<T>();
		final List<T> right = new LinkedList<T>();
		for (final T t : source) {
			if (predicate.apply(t)) {
				left.add(t);
			} else {
				right.add(t);
			}
		}
		return Tuple2.of(left, right);
	}

	public static <T> Tuple2<List<T>, List<T>> partition(final T[] source, final F1<? super T, Boolean> predicate) {
		return partition(Arrays.asList(source), predicate);
	}

	public static <T> List<T> reverse(final Iterable<T> source) {
		if (source instanceof Collection<?>) {
			final ArrayList<T> result = new ArrayList<T>((Collection<T>) source);
			Collections.reverse(result);
			return result;
		} else {
			final LinkedList<T> result = new LinkedList<T>();
			for (final T t : source) {
				result.add(0, t);
			}
			return result;
		}
	}

	public static <T> List<T> reverse(final T[] source) {
		return reverse(Arrays.asList(source));
	}

	public static <T> List<T> sort(final Iterable<T> source, final Comparator<? super T> comparator) {
		final List<T> result;
		if (source instanceof Collection<?>) {
			result = new ArrayList<T>((Collection<T>) source);
		} else {
			result = new ArrayList<T>();
			for (final T t : source) {
				result.add(t);
			}
		}
		Collections.sort(result, comparator);
		return result;
	}

	public static <T> List<T> sort(final T[] source, final Comparator<? super T> comparator) {
		return sort(Arrays.asList(source), comparator);
	}

	public static <T, C extends Comparable<C>> List<T> sortWith(final Iterable<T> source, final F1<? super T, C> convert) {
		return sort(source, new Comparator<T>() {
			public int compare(final T o1, final T o2) {
				return convert.apply(o1).compareTo(convert.apply(o2));
			};
		});
	}

	public static <T, C extends Comparable<C>> List<T> sortWith(final T[] source, final F1<? super T, C> convert) {
		return sortWith(Arrays.asList(source), convert);
	}

	public static <T> List<T> tail(final Iterable<T> source) {
		final Iterator<T> it = source.iterator();
		if (it.hasNext()) {
			// drop first element
			it.next();
		}
		final List<T> result = new LinkedList<T>();
		while (it.hasNext()) {
			result.add(it.next());
		}
		return result;
	}

	public static <T> List<T> tail(final T[] source) {
		return tail(Arrays.asList(source));
	}

	public static <T> List<T> takeWhile(final Iterable<T> source, final F1<? super T, Boolean> accept) {
		final List<T> result = new LinkedList<T>();
		for (final T t : source) {
			if (accept.apply(t)) {
				result.add(t);
			} else {
				break;
			}
		}
		return result;
	}

	public static <T> List<T> takeWhile(final T[] source, final F1<? super T, Boolean> accept) {
		return takeWhile(Arrays.asList(source), accept);
	}

	public static <K, V> LinkedHashMap<K, V> toHashMap(final Iterable<Tuple2<K, V>> source) {
		final LinkedHashMap<K, V> result = new LinkedHashMap<K, V>();
		for (final Tuple2<K, V> tuple2 : source) {
			result.put(tuple2.a(), tuple2.b());
		}
		return result;
	}

	public static <K, V> LinkedHashMap<K, V> toHashMap(final Tuple2<K, V>[] source) {
		return toHashMap(Arrays.asList(source));
	}

	public static <A, B> List<Tuple2<A, B>> zip(final A[] as, final B[] bs) {
		return zip(Arrays.asList(as), Arrays.asList(bs));
	}

	public static <A, B> List<Tuple2<A, B>> zip(final Iterable<A> as, final Iterable<B> bs) {
		final List<Tuple2<A, B>> result = new LinkedList<Tuple2<A, B>>();
		final Iterator<A> aIt = as.iterator();
		final Iterator<B> bIt = bs.iterator();
		while (aIt.hasNext() && bIt.hasNext()) {
			result.add(Tuple2.of(aIt.next(), bIt.next()));
		}
		return result;
	}

	private FList() {
		// no inheritance useful
	}

}
