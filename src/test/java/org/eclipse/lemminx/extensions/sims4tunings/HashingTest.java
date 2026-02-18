package org.eclipse.lemminx.extensions.sims4tunings;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class HashingTest {
    @Test
    void test_hash_high_bit() {
        assertEquals(-5847587120196857963L, Hashing.fnv1Hash("abcd", true));
    }
    @Test
    void test_hash() {
        assertEquals(3375784916657917845L, Hashing.fnv1Hash("abcd", false));

    }
}