package de.softknk;

public class InputSizeTooLargeException extends Exception {

    private long data_size;

    public InputSizeTooLargeException(long data_size) {
        this.data_size = data_size;
        System.err.println("Input size is too large for SHA-256 algorithm. Maximum size: 2^64-1 bits. Input size: " + data_size + " bits.");
    }

    public long getData_size() {
        return data_size;
    }
}
