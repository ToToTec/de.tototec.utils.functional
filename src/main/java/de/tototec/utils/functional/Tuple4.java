package de.tototec.utils.functional;

import java.io.Serializable;

public class Tuple4<A, B, C, D> implements Serializable {

	private static final long serialVersionUID = 1L;

	private final A a;
	private final B b;
	private final C c;
	private final D d;

	public Tuple4(final A a, final B b, final C c, D d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public A a() {
		return a;
	}

	public B b() {
		return b;
	}

	public C c() {
		return c;
	}

	public D d() {
		return d;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a() == null) ? 0 : a().hashCode());
		result = prime * result + ((b() == null) ? 0 : b().hashCode());
		result = prime * result + ((c() == null) ? 0 : c().hashCode());
		result = prime * result + ((d() == null) ? 0 : d().hashCode());
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
		final Tuple4<?, ?, ?, ?> other = (Tuple4<?, ?, ?, ?>) obj;
		if (a() == null) {
			if (other.a() != null) {
				return false;
			}
		} else if (!a().equals(other.a())) {
			return false;
		}
		if (b() == null) {
			if (other.b() != null) {
				return false;
			}
		} else if (!b().equals(other.b())) {
			return false;
		}
		if (c() == null) {
			if (other.c() != null) {
				return false;
			}
		} else if (!c().equals(other.c())) {
			return false;
		}
		if (d() == null) {
			if (other.d() != null) {
				return false;
			}
		} else if (!d().equals(other.d())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + a() + "," + b() + "," + c() + "," + d() + ")";
	}
}
