package com.atlan.model.serde;

/**
 * A class to capture a value that can be explicitly set to null, so that we can
 * serialize it accordingly.
 */
public class Removable<T> {

    /**
     * Single null value that can be used for any removable type.
     */
    public static final Removable<?> NULL = new Removable<>(true);

    /**
     * Quickly create a new removable value from whatever object type you like.
     * @param value to create
     * @return a removable representation of that value
     * @param <T> type of the value
     */
    public static <T> Removable<T> of(T value) {
        return new Removable<>(value);
    }

    private T value;
    private boolean jsonNull = false;

    private Removable(boolean jsonNull) {
        this.jsonNull = jsonNull;
    }

    private Removable(T value) {
        this.setValue(value);
    }

    private void setValue(T value) {
        this.value = value;
        jsonNull = (value == null);
    }

    /**
     * Retrieve the underlying value for this object.
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * Indicates whether we should explicitly set a null value in JSON serialization
     * or not.
     * @return true if we should serialize the value as null
     */
    public boolean isJsonNull() {
        return jsonNull;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
