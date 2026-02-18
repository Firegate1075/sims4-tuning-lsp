package org.eclipse.lemminx.extensions.sims4tunings;

public class Hashing {
    private static final long FNV_OFFSET_BASIS = 0xcbf29ce484222325L;
    private static final long FNV_prime = 0x100000001b3L;

    public static long fnv1Hash(String input, boolean highMostSignificantBit) {
        if (highMostSignificantBit) {
            return fnv1Hash(input) | (1L << 63);
        } else {
            return fnv1Hash(input);
        }
    }

    public static long fnv1Hash(String input) {
        long hash = FNV_OFFSET_BASIS;
        for (char c : input.toCharArray()) {
            hash *= FNV_prime; // Multiply by the FNV prime
            hash ^= (long) c; // XOR with the byte
        }
        return hash;
    }
}
