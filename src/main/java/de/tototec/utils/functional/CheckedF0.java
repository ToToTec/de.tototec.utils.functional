package de.tototec.utils.functional;

public interface CheckedF0<R> {
	public R apply() throws Throwable;
}
