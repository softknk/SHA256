package de.softknk;

import static de.softknk.Functions.sigma_zero;
import static de.softknk.Functions.sigma_one;
import static de.softknk.Functions.Sigma_zero;
import static de.softknk.Functions.Sigma_one;
import static de.softknk.Functions.choice;
import static de.softknk.Functions.majority;

import static de.softknk.Operations.add;

import static de.softknk.BinaryTool.scale_binary;
import static de.softknk.BinaryTool.scale_hex;
import static de.softknk.BinaryTool.fill_binary;

import static de.softknk.Constants.constant;
import static de.softknk.Constants.n_prime_numbers;

public class SHA256 {

    private String raw_data;
    private String binary_data;
    private String padded_data;

    private String[] message_blocks;
    private String[][] message_schedules; // first index indicates the corresponding message block

    /**
     * hash data via SHA256
     *
     * @param data raw input data
     * @return corresponding hash value
     * @throws InputSizeTooLargeException
     */
    public String hash(String data) throws InputSizeTooLargeException {
        if (data == null || data == "") return "";

        raw_to_binary(data);
        padding();
        build_message_blocks();
        build_message_schedules();

        return compression(0, null);
    }

    /**
     * convert raw input data into binary data
     *
     * @param data raw data
     */
    private void raw_to_binary(String data) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.length(); i++) builder.append(scale_binary(data.charAt(i), 8));
        binary_data = builder.toString();
    }

    /**
     * scale binary data to a multiple of 512 bits and add the size of the data (size in bits)
     * as the last 64 bits
     *
     * @throws InputSizeTooLargeException
     */
    private void padding() throws InputSizeTooLargeException {
        if (binary_data.length() > Math.pow(2, 64) - 1) throw new InputSizeTooLargeException(binary_data.length());

        String data_bit_size = scale_binary(binary_data.length(), 64);

        int rest = 512 - (binary_data.length() % 512);

        if (rest >= 64) binary_data = fill_binary(binary_data, rest - 64);
        else binary_data = fill_binary(binary_data, rest + 512 - 64);

        padded_data = binary_data + data_bit_size;
    }

    /**
     * build message blocks where one block consists of 512 bit of data
     */
    private void build_message_blocks() {
        message_blocks = new String[padded_data.length() / 512];

        for (int i = 0; i < message_blocks.length; i++) {
            StringBuilder block_builder = new StringBuilder();
            for (int j = 0; j < 512; j++) block_builder.append(padded_data.charAt(i * 512 + j));
            message_blocks[i] = block_builder.toString();
        }
    }

    /**
     * build the message schedule for every single message block consisting of 64 words (one word = 32 bit)
     */
    private void build_message_schedules() {
        message_schedules = new String[message_blocks.length][64];

        for (int i = 0; i < message_blocks.length; i++) {
            for (int j = 0; j < 16; j++) {
                StringBuilder schedule_builder = new StringBuilder();
                for (int a = 0; a < 32; a++) schedule_builder.append(message_blocks[i].charAt(j * 32 + a));
                message_schedules[i][j] = schedule_builder.toString();
            }
            // compute new words till 64 words are reached in the message schedule
            for (int j = 16; j < 64; j++) {
                message_schedules[i][j] = add(32,
                        scale_binary(sigma_one((int) Long.parseLong(message_schedules[i][j - 2], 2)), 32),
                        message_schedules[i][j - 7],
                        scale_binary(sigma_zero((int) Long.parseLong(message_schedules[i][j - 15], 2)), 32),
                        message_schedules[i][j - 16]
                );
            }
        }
    }

    /**
     * compute the default state registers with the first 8 prime numbers (defines
     * the initial hash value)
     *
     * @return default state registers array
     */
    private String[] default_state_registers() {
        String[] default_state_registers = new String[8]; // A(0) B(1) C(2) D(3) E(4) F(5) G(6) H(7)
        int[] first_eight_primes = n_prime_numbers(8);

        for (int i = 0; i < default_state_registers.length; i++) {
            double a = Math.sqrt(first_eight_primes[i]);
            while (a >= 1.0) a -= 1.0; // take the fractional part
            long n = (long) (a * Math.pow(2, 32));
            default_state_registers[i] = scale_binary(Integer.toBinaryString((int) n), 32);
        }

        return default_state_registers;
    }

    /**
     * compress the data and compute the final hash
     *
     * @param message_block_index  current message block
     * @param curr_state_registers current used state registers
     * @return final hash value
     */
    private String compression(int message_block_index, String[] curr_state_registers) {
        if (curr_state_registers == null) curr_state_registers = default_state_registers();

        String[] state_registers_copy = curr_state_registers.clone();

        String w = message_schedules[message_block_index][0]; // current used word
        String k = constant(0); // current used constant

        String t1 = add(32,
                scale_binary(Sigma_one((int) Long.parseLong(curr_state_registers[4], 2)), 32),
                scale_binary(choice(
                        (int) Long.parseLong(curr_state_registers[4], 2),
                        (int) Long.parseLong(curr_state_registers[5], 2),
                        (int) Long.parseLong(curr_state_registers[6], 2)), 32),
                curr_state_registers[7],
                w,
                k
        );

        String t2 = add(32,
                scale_binary(Sigma_zero((int) Long.parseLong(curr_state_registers[0], 2)), 32),
                scale_binary(majority(
                        (int) Long.parseLong(curr_state_registers[0], 2),
                        (int) Long.parseLong(curr_state_registers[1], 2),
                        (int) Long.parseLong(curr_state_registers[2], 2)), 32)
        );

        // computation
        for (int i = 0; i < 64; i++) {
            // move state registers down by one
            for (int j = curr_state_registers.length - 1; j > 0; j--) {
                curr_state_registers[j] = curr_state_registers[j - 1];
            }
            // update register A
            curr_state_registers[0] = add(32, t1, t2);
            // update register E
            curr_state_registers[4] = add(32, curr_state_registers[4], t1);

            if (i == 63) break;

            // update w and k
            w = message_schedules[message_block_index][i + 1];
            k = constant(i + 1);

            t1 = add(32,
                    scale_binary(Sigma_one((int) Long.parseLong(curr_state_registers[4], 2)), 32),
                    scale_binary(choice(
                            (int) Long.parseLong(curr_state_registers[4], 2),
                            (int) Long.parseLong(curr_state_registers[5], 2),
                            (int) Long.parseLong(curr_state_registers[6], 2)), 32),
                    curr_state_registers[7],
                    w,
                    k
            );

            t2 = add(32,
                    scale_binary(Sigma_zero((int) Long.parseLong(curr_state_registers[0], 2)), 32),
                    scale_binary(majority(
                            (int) Long.parseLong(curr_state_registers[0], 2),
                            (int) Long.parseLong(curr_state_registers[1], 2),
                            (int) Long.parseLong(curr_state_registers[2], 2)), 32)
            );
        }

        // build final hash
        for (int i = 0; i < state_registers_copy.length; i++) {
            state_registers_copy[i] = add(32, state_registers_copy[i], curr_state_registers[i]);
        }

        StringBuilder hash_builder = new StringBuilder();
        for (int i = 0; i < state_registers_copy.length; i++) {
            hash_builder.append(scale_hex(Integer.toHexString((int) Long.parseLong(state_registers_copy[i], 2)), 8));
        }

        if (message_block_index == message_blocks.length - 1) return hash_builder.toString();
        else return compression(message_block_index + 1, state_registers_copy);
    }
}