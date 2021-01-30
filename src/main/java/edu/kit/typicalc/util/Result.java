package edu.kit.typicalc.util;

/**
 * Can be a value of type T or an error of type E (if the computation failed).
 *
 * @param <T> value type
 * @param <E> error type
 */
public class Result<T, E> {
    private final T value;
    private final E error;

    /**
     * Creates a new result containing the given value.
     *
     * @param value the value
     */
    public Result(T value) {
        this.value = value;
        this.error = null;
    }

    /**
     * Creates a new result by copying another one.
     *
     * @param other the result
     */
    public Result(Result other) {
        this.value = (T) other.value;
        this.error = (E) other.error;
    }

    /**
     * Creates a new result containing the given value or error.
     * Only one of the parameters may be non-null.
     *
     * @param value the value
     * @param error the error
     * @throws IllegalArgumentException if more or less than one parameter is null
     */
    public Result(T value, E error) {
        if ((value != null) == (error != null)) {
            throw new IllegalArgumentException("value xor error must be null in constructor!");
        }
        this.value = value;
        this.error = error;
    }

    /**
     * @return whether the result contains a regular value
     */
    public boolean isOk() {
        return value != null;
    }

    /**
     * @return whether the result contains an error
     */
    public boolean isError() {
        return error != null;
    }

    /**
     * If the result contains a regular value, returns that value.
     * Otherwise an IllegalStateException is thrown.
     *
     * @return the value
     * @throws IllegalStateException if this is an error
     */
    public T unwrap() throws IllegalStateException {
        if (value == null) {
            throw new IllegalStateException("tried to unwrap an error");
        }
        return value;
    }

    /**
     * If the result contains an error, returns that error.
     * Otherwise an IllegalStateException is thrown.
     *
     * @return the error
     * @throws IllegalStateException if this is a regular value
     */
    public E unwrapError() {
        if (error == null) {
            throw new IllegalStateException("tried to unwrap a value");
        }
        return error;
    }
}
