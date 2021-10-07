package edu.kit.typicalc.util;

import java.util.Objects;
import java.util.function.Function;

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
    @SuppressWarnings("unchecked")
    public Result(Result<?, ?> other) {
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
            throw new IllegalArgumentException("value or error must be null in constructor!");
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
            throw new IllegalStateException("tried to unwrap a result, but error = " + error);
        }
        return value;
    }

    /**
     * If the result contains a regular value, returns that value.
     * Otherwise return the supplied alternative value.
     *
     * @return the value or the alternative
     */
    public T unwrapOr(T alternative) {
        if (value == null) {
            return alternative;
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
            throw new IllegalStateException("tried to unwrap the error, but value = " + value);
        }
        return error;
    }

    /**
     * Apply a function to the value of this result if it exists.
     *
     * @param mapping function to apply
     * @param <U> new type of the value
     * @return new result
     */
    public <U> Result<U, E> map(Function<T, U> mapping) {
        return new Result<>(value != null ? mapping.apply(value) : null, error);
    }

    /**
     * This function should only be used on error results and will change the second type parameter to any desired
     * value.
     *
     * @param <U> the desired type
     * @return a copy of this object
     */
    public <U> Result<U, E> castError() {
        return new Result<>(null, this.error);
    }

    @Override
    public String toString() {
        if (isOk()) {
            return "Ok(" + value + ")";
        } else {
            return "Error(" + error + ")";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Result<?, ?> result = (Result<?, ?>) o;
        return Objects.equals(value, result.value) && Objects.equals(error, result.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, error);
    }
}
