package experimento;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT3PrimesTest {

    // ---------------------------
    // Testes para isPrime(int n)
    // ---------------------------

    @Test
    void isPrime_shouldReturnFalseForNumbersLessThan2() {
        assertFalse(Primes.isPrime(-10));
        assertFalse(Primes.isPrime(0));
        assertFalse(Primes.isPrime(1));
    }

    @Test
    void isPrime_shouldReturnTrueForSmallPrimes() {
        assertTrue(Primes.isPrime(2));
        assertTrue(Primes.isPrime(3));
        assertTrue(Primes.isPrime(5));
        assertTrue(Primes.isPrime(7));
    }

    @Test
    void isPrime_shouldReturnFalseForSmallNonPrimes() {
        assertFalse(Primes.isPrime(4));
        assertFalse(Primes.isPrime(6));
        assertFalse(Primes.isPrime(9));
    }

    @Test
    void isPrime_shouldReturnTrueForLargePrime() {
        assertTrue(Primes.isPrime(7919)); // conhecido como primo
    }

    @Test
    void isPrime_shouldReturnFalseForLargeNonPrime() {
        assertFalse(Primes.isPrime(7920)); // 7919 + 1 (não primo)
    }

    // -------------------------------
    // Testes para nextPrime(int n)
    // -------------------------------

    @Test
    void nextPrime_shouldReturnTwoWhenInputIsOne() {
        assertEquals(2, Primes.nextPrime(1));
    }

    @Test
    void nextPrime_shouldReturnSameNumberIfPrime() {
        assertEquals(3, Primes.nextPrime(3));
        assertEquals(13, Primes.nextPrime(13));
    }

    @Test
    void nextPrime_shouldReturnNextPrimeIfInputIsNotPrime() {
        assertEquals(5, Primes.nextPrime(4));
        assertEquals(7, Primes.nextPrime(6));
        assertEquals(7919, Primes.nextPrime(7918));
    }

    @Test
    void nextPrime_shouldThrowExceptionWhenNegative() {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.nextPrime(-1));
    }

    // --------------------------------------
    // Testes para primeFactors(int n)
    // --------------------------------------

    @Test
    void primeFactors_shouldReturnExceptionWhenLessThan2() {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(1));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(0));
    }

    @Test
    void primeFactors_shouldReturnSingleFactorForPrimeInput() {
        List<Integer> result = Primes.primeFactors(13);
        assertEquals(List.of(13), result);
    }

    @Test
    void primeFactors_shouldReturnCorrectFactorsForComposite() {
        List<Integer> result = Primes.primeFactors(60);
        assertEquals(List.of(2, 2, 3, 5), result);
    }

    @Test
    void primeFactors_shouldReturnRepeatedFactorsCorrectly() {
        List<Integer> result = Primes.primeFactors(49); // 7 * 7
        assertEquals(List.of(7, 7), result);
    }

    @Test
    void primeFactors_shouldHandleLargeNumber() {
        List<Integer> result = Primes.primeFactors(97 * 89);
        assertEquals(List.of(89, 97), result); // ordem pode variar
    }
}

