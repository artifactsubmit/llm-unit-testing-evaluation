package experimento;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT1PrimesTest {

    // --- Tests for isPrime(int n) ---

    @Test
    void isPrime_ShouldReturnFalse_ForNumberLessThan2() {
        assertFalse(Primes.isPrime(-10));
        assertFalse(Primes.isPrime(0));
        assertFalse(Primes.isPrime(1));
    }

    @Test
    void isPrime_ShouldReturnTrue_ForSmallPrimeNumbers() {
        assertTrue(Primes.isPrime(2));
        assertTrue(Primes.isPrime(3));
        assertTrue(Primes.isPrime(5));
        assertTrue(Primes.isPrime(7));
    }

    @Test
    void isPrime_ShouldReturnFalse_ForSmallCompositeNumbers() {
        assertFalse(Primes.isPrime(4));
        assertFalse(Primes.isPrime(6));
        assertFalse(Primes.isPrime(9));
        assertFalse(Primes.isPrime(12));
    }

    @Test
    void isPrime_ShouldReturnTrue_ForLargePrimeNumber() {
        assertTrue(Primes.isPrime(7919)); // Known prime
    }

    @Test
    void isPrime_ShouldReturnFalse_ForLargeCompositeNumber() {
        assertFalse(Primes.isPrime(7920)); // 7919 + 1
    }

    // --- Tests for nextPrime(int n) ---

    @Test
    void nextPrime_ShouldThrowException_WhenNegativeInput() {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.nextPrime(-1));
    }

    @Test
    void nextPrime_ShouldReturnTwo_WhenInputIsZeroOrOneOrTwo() {
        assertEquals(2, Primes.nextPrime(0));
        assertEquals(2, Primes.nextPrime(1));
        assertEquals(2, Primes.nextPrime(2));
    }

    @Test
    void nextPrime_ShouldReturnNextPrime_WhenInputIsComposite() {
        assertEquals(5, Primes.nextPrime(4));
        assertEquals(11, Primes.nextPrime(9));
        assertEquals(13, Primes.nextPrime(12));
    }

    @Test
    void nextPrime_ShouldReturnSameNumber_WhenInputIsPrime() {
        assertEquals(3, Primes.nextPrime(3));
        assertEquals(7, Primes.nextPrime(7));
        assertEquals(7919, Primes.nextPrime(7919));
    }

    // --- Tests for primeFactors(int n) ---

    @Test
    void primeFactors_ShouldThrowException_WhenInputLessThanTwo() {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(1));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(0));
    }

    @Test
    void primeFactors_ShouldReturnSingleElementList_ForPrimeInput() {
        assertEquals(List.of(2), Primes.primeFactors(2));
        assertEquals(List.of(7), Primes.primeFactors(7));
    }

    @Test
    void primeFactors_ShouldReturnCorrectFactors_ForCompositeNumber() {
        assertEquals(List.of(2, 2, 3), Primes.primeFactors(12));
        assertEquals(List.of(3, 3, 11), Primes.primeFactors(99));
        assertEquals(List.of(2, 2, 2, 2), Primes.primeFactors(16));
    }

    @Test
    void primeFactors_ShouldReturnMultipleDistinctFactors() {
        assertEquals(List.of(2, 3, 5, 7), Primes.primeFactors(210)); // 2*3*5*7
    }

}

