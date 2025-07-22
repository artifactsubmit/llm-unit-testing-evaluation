package experimento;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT4PrimesTest {

    // --- isPrime() ---

    @Test
    void isPrime_shouldReturnFalseForValuesLessThanTwo() {
        assertFalse(Primes.isPrime(-10));
        assertFalse(Primes.isPrime(0));
        assertFalse(Primes.isPrime(1));
    }

    @Test
    void isPrime_shouldReturnTrueForKnownPrimes() {
        assertTrue(Primes.isPrime(2));
        assertTrue(Primes.isPrime(3));
        assertTrue(Primes.isPrime(5));
        assertTrue(Primes.isPrime(7));
        assertTrue(Primes.isPrime(29));
    }

    @Test
    void isPrime_shouldReturnFalseForKnownNonPrimes() {
        assertFalse(Primes.isPrime(4));
        assertFalse(Primes.isPrime(6));
        assertFalse(Primes.isPrime(9));
        assertFalse(Primes.isPrime(100));
    }

    @Test
    void isPrime_shouldReturnTrueForLargePrimeNumber() {
        assertTrue(Primes.isPrime(7919)); // known large prime
    }

    @Test
    void isPrime_shouldReturnFalseForLargeNonPrime() {
        assertFalse(Primes.isPrime(7920)); // 7919 + 1
    }

    // --- nextPrime() ---

    @Test
    void nextPrime_shouldReturnSameNumberIfAlreadyPrime() {
        assertEquals(2, Primes.nextPrime(2));
        assertEquals(3, Primes.nextPrime(3));
        assertEquals(17, Primes.nextPrime(17));
    }

    @Test
    void nextPrime_shouldReturnNextPrimeAfterNonPrime() {
        assertEquals(2, Primes.nextPrime(1));
        assertEquals(5, Primes.nextPrime(4));
        assertEquals(11, Primes.nextPrime(10));
        assertEquals(7919, Primes.nextPrime(7918));
    }

    @Test
    void nextPrime_shouldThrowExceptionIfNegative() {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.nextPrime(-1));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.nextPrime(Integer.MIN_VALUE));
    }

    // --- primeFactors() ---

    @Test
    void primeFactors_shouldThrowExceptionForValuesLessThanTwo() {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(1));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(0));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(-10));
    }

    @Test
    void primeFactors_shouldReturnSingleElementListForPrimes() {
        assertEquals(List.of(2), Primes.primeFactors(2));
        assertEquals(List.of(3), Primes.primeFactors(3));
        assertEquals(List.of(29), Primes.primeFactors(29));
    }

    @Test
    void primeFactors_shouldReturnCorrectFactorsForComposites() {
        assertEquals(List.of(2, 2, 3), Primes.primeFactors(12));
        assertEquals(List.of(3, 3, 3), Primes.primeFactors(27));
        assertEquals(List.of(2, 2, 2, 3, 5), Primes.primeFactors(120));
    }

    @Test
    void primeFactors_shouldReturnFactorsInAscendingOrder() {
        List<Integer> factors = Primes.primeFactors(210); // 2 * 3 * 5 * 7
        assertEquals(List.of(2, 3, 5, 7), factors);
    }
}

