package de.softknk;

import static de.softknk.BinaryTool.scale_binary;

public interface Constants {

    int[] prime_numbers = n_prime_numbers(64);

    /**
     * gives the n-th constant out of 64 used for hash computation
     *
     * @param n constant number
     * @return n-th constant as binary string
     */
    static String constant(int n) {
        double value = Math.pow(prime_numbers[n], 1 / (double) 3);
        while (value >= 1.0) value -= 1;

        StringBuilder binary_builder = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            value *= 16;
            binary_builder.append(scale_binary(((int) Math.floor(value)), 4));
            value -= Math.floor(value);
        }

        return binary_builder.toString();
    }

    /**
     * computes the first n prime numbers
     *
     * @param n number of prime numbers
     * @return array of prime numbers (sorted asc)
     */
    static int[] n_prime_numbers(int n) {
        int[] primes = new int[n];
        int counter = 0, i = 0, a = 0;
        while (counter != n) {
            if (is_prime(a)) {
                primes[i] = a;
                i++;
                counter++;
            }
            a++;
        }
        return primes;
    }

    /**
     * tells whether a given number is a prime number
     *
     * @param n number to check
     * @return true if n is a prime number, otherwise false
     */
    static boolean is_prime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= n / 2; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
