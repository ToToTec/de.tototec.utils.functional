package de.tototec.utils.functional;

import java.lang.reflect.Method;

/**
 * Convenience support to easy writing correct `equals` and `hashCode` methods.
 *
 * .Example
 * [source,java]
 * ----
 * class MyDataClass {
 *   private static final CanEqualsSupport<MyDataClass> eq = new CanEqualsSupport<>(MyDataClass.class,
 *     c -> c.a, c -> c.b
 *   );
 *
 *   private final String a;
 *   private final Long b;
 *
 *   public MyDataClass(String a, Long b) {
 * 	   this.a = a;
 * 	   this.b = b;
 *   }
 *
 *   @Override public boolean equals(Object other) {
 * 	   return eq._equals(this, other);
 *   }
 *
 *   @Override public int hashCode() {
 * 	   return eq._hashCode(this);
 *   }
 *
 * 	 public boolean canEqual(Object other) {
 * 	   return eq._canEqual(other);
 *   }
 * }
 * ----
 *
 * @param <T> The type of the class to which the equals and hashCode methods belongs.
 */
public class CanEqualsSupport<T> {

	private final Class<T> thisType;
	private final F1<T, Object>[] fields;

	@SafeVarargs
	public CanEqualsSupport(Class<T> thisType, F1<T, Object>... fields) {
		this.thisType = thisType;
		this.fields = fields == null ? new F1[0] : fields;
	}

	public boolean _canEqual(Object other) {
		return thisType.isInstance(other);
	}

	public boolean _equals(T me, Object other) {
		if (me == null) {
			throw new IllegalArgumentException("Object me must not be null");
		}
		if (me == other) return true;
		if (!thisType.isInstance(other)) return false;
		T that = (T) other;
		try {
			Method canEqual = that.getClass().getMethod("canEqual", Object.class);
			Boolean canWe = (Boolean) canEqual.invoke(that, me);
			if (!canWe) return false;
		} catch (NoSuchMethodException e) {
			// looks like we don't (want to) support canEqual method
		} catch (Exception e) {
			return false;
		}
		for (F1<T, Object> field : fields) {
			Object l = field.apply(me);
			Object r = field.apply(that);
			if (l == null) {
				if (r != null) {
					return false;
				} else {
					continue;
				}
			} else {
				if (r == null) {
					return false;
				} else {
					if (!l.equals(r)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public int _hashCode(T me) {
		return _hashCode(me, 1);
	}

	public int _hashCode(T me, int superHashCode) {
		if (me == null) {
			throw new IllegalArgumentException("Object me must not be null");
		}
		int prime = 41;
		int code = superHashCode;
		for (F1<T, Object> field : fields) {
			Object l = field.apply(me);
			int hash = l == null ? 0 : l.hashCode();
			code = (prime * code) + hash;
		}
		return code;
	}

	;

}
