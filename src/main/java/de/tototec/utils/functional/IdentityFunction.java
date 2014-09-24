package de.tototec.utils.functional;

public class IdentityFunction<I> implements F1<I, I> {

	public I apply(I param) {
		return param;
	}

}
