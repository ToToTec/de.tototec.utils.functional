package de.tototec.utils.functional;

import java.lang.reflect.Method;

/**
 * Convenience support to easily write correct `equals` and `hashCode` methods as well as `canEqual` methods.
 *
 * .Example
 * [source,java]
 * ----
 * class MyDataClass {
 *
 *   private final String a;
 *   private final Long b;
 *
 *   public MyDataClass(String a, Long b) {
 *     this.a = a;
 *     this.b = b;
 *   }
 *
 * private static final CanEqualsSupport<MyDataClass> eq =
 *   new CanEqualsSupport<>(MyDataClass.class,
 *     (a,b) -> a.canEqual(b),
 *     c -> c.a,
 *     c -> c.b
 *   );
 *
 *   @Override public boolean equals(Object other) {
 *     return eq._equals(this, other);
 *   }
 *
 *   @Override public int hashCode() {
 * 	   return eq._hashCode(this);
 *   }
 *
 *   public boolean canEqual(Object other) {
 *     return eq._canEqual(other);
 *   }
 * }
 * ----
 *
 * @param <T> The type of the class to which the equals and hashCode methods belongs.
 */
public class CanEqualsSupport<T> {

    private final Class<T> thisType;
    private final F2<T, T, Boolean> canEqualFunction;
    private final F1<T, Object>[] fields;

    /**
     * This creates a `ConEqualsSupport` that uses a function to access the `canEqual` method.
     *
     * @param thisType         The type of the class to which the equals and hashCode methods belong.
     * @param canEqualFunction The function that actually contains the call of the `canEqual` method.
     *                         This is typically: `(a,b) -> a.canEqual(b)`.
     *                         If you don't want to implement the `canEqual(Object)` method, give a function that returns always `true` here: `(a,b) -> true`.
     * @param fields           The accessor functions returning the values (fields/getters) that should be part of the equals/hashCode contract.
     */
    @SafeVarargs
    public CanEqualsSupport(
            Class<T> thisType,
            F2<T, T, Boolean> canEqualFunction,
            F1<T, Object>... fields
    ) {
        this.thisType = thisType;
        this.canEqualFunction = canEqualFunction;
        this.fields = fields == null ? new F1[0] : fields;
    }

    /**
     * This create a `CanEqualsSupport` that uses reflection to detect the `canEqual` method.
     *
     * @param thisType The type of the class to which the equals and hashCode methods belong.
     * @param fields   The accessor functions returning the values (fields/getters) that should be part of the equals/hashCode contract.
     */
    @SafeVarargs
    public CanEqualsSupport(Class<T> thisType, F1<T, Object>... fields) {
        this(
                thisType,
                new F2<T, T, Boolean>() {
                    @Override
                    public Boolean apply(T me, T other) {
                        try {
                            Method canEqual = other.getClass().getMethod("canEqual", Object.class);
                            Boolean canWe = (Boolean) canEqual.invoke(me, other);
                            return canWe;
                        } catch (NoSuchMethodException e) {
                            // looks like we don't (want to) support canEqual method
                            return true;
                        } catch (Exception e) {
                            return false;
                        }
                    }
                },
                fields
        );
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
        Boolean canEqual = canEqualFunction.apply(that, me);
        if (!canEqual) return false;
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
