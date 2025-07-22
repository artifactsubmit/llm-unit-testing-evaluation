package experimento;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT5PrimesTest {

    // ---------- Testes para isPrime(int n) ----------

    @Test
    void testIsPrimeWithPrimes() {
        assertTrue(Primes.isPrime(2));
        assertTrue(Primes.isPrime(3));
        assertTrue(Primes.isPrime(5));
        assertTrue(Primes.isPrime(97));
        assertTrue(Primes.isPrime(7919)); // primo grande
    }

    @Test
    void testIsPrimeWithNonPrimes() {
        assertFalse(Primes.isPrime(0));
        assertFalse(Primes.isPrime(1));
        assertFalse(Primes.isPrime(4));
        assertFalse(Primes.isPrime(100));
        assertFalse(Primes.isPrime(9999));
    }

    @Test
    void testIsPrimeWithNegativeNumbers() {
        assertFalse(Primes.isPrime(-1));
        assertFalse(Primes.isPrime(-17));
    }

    @Test
    void testIsPrimeWithPrimeDivisibleByItself() {
        // Teste que exercita n == p dentro do laço
        assertTrue(Primes.isPrime(13));
    }

    // ---------- Testes para nextPrime(int n) ----------

    @Test
    void testNextPrimeWhenInputIsNegative() {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.nextPrime(-1));
    }

    @Test
    void testNextPrimeWhenInputIsZeroOrOne() {
        assertEquals(2, Primes.nextPrime(0));
        assertEquals(2, Primes.nextPrime(1));
    }

    @Test
    void testNextPrimeWhenInputIsAlreadyPrime() {
        assertEquals(2, Primes.nextPrime(2));
        assertEquals(7, Primes.nextPrime(7));
    }

    @Test
    void testNextPrimeWithNonPrimeInput() {
        assertEquals(11, Primes.nextPrime(10));
        assertEquals(17, Primes.nextPrime(15));
        assertEquals(101, Primes.nextPrime(100));
    }

    @Test
    void testNextPrimeWithLargeNonPrime() {
        assertEquals(10007, Primes.nextPrime(10000));
    }

    // ---------- Testes para primeFactors(int n) ----------

    @Test
    void testPrimeFactorsOfPrimeNumber() {
        List<Integer> factors = Primes.primeFactors(13);
        assertEquals(List.of(13), factors);
    }

    @Test
    void testPrimeFactorsOfCompositeNumber() {
        List<Integer> factors = Primes.primeFactors(60);
        assertEquals(List.of(2, 2, 3, 5), factors);
    }

    @Test
    void testPrimeFactorsWithPowerOfPrimes() {
        List<Integer> factors = Primes.primeFactors(49); // 7^2
        assertEquals(List.of(7, 7), factors);
    }

    @Test
    void testPrimeFactorsThrowsForInvalidInput() {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(1));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(0));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(-10));
    }

    @Test
    void testPrimeFactorsWithLargeNumber() {
        List<Integer> factors = Primes.primeFactors(2310); // 2 * 3 * 5 * 7 * 11
        assertEquals(List.of(2, 3, 5, 7, 11), factors);
    }
}

