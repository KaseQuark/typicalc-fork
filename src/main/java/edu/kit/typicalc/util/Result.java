package edu.kit.typicalc.util;

public class Result<T, E> {
	private final T value;
	private final E error;

	public Result(T value) {
		this.value = value;
		this.error = null;
	}

	public Result(T value, E error) { // TODO: Java does not allow both constructors otherwise
		this.value = value;
		this.error = error;
	}

	public boolean isError() {
		return error != null;
	}

	public T unwrap() {
		return value;
	}

	public E unwrapError() {
		return error;
	}
}
