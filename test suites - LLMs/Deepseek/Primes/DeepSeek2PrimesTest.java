package experimento.deepseek.primes;

import experimento.Primes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

class DeepSeek2PrimesTest {

    // Testes para o método isPrime()

    @ParameterizedTest(name = "isPrime should return false for non-prime {0}")
    @ValueSource(ints = {0, 1, 4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25})
    void isPrime_ShouldReturnFalseForNonPrimes(int number) {
        Assertions.assertFalse(Primes.isPrime(number));
    }

    @ParameterizedTest(name = "isPrime should return true for prime {0}")
    @ValueSource(ints = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97})
    void isPrime_ShouldReturnTrueForPrimes(int number) {
        assertTrue(Primes.isPrime(number));
    }

    @Test
    void isPrime_ShouldReturnFalseForNegativeNumbers() {
        assertFalse(Primes.isPrime(-1));
        assertFalse(Primes.isPrime(-2));
        assertFalse(Primes.isPrime(-3));
        assertFalse(Primes.isPrime(Integer.MIN_VALUE));
    }

    @ParameterizedTest(name = "isPrime should handle large prime {0}")
    @ValueSource(ints = {104729, 1299709, 15485863})
    void isPrime_ShouldReturnTrueForLargePrimes(int number) {
        assertTrue(Primes.isPrime(number));
    }

    @ParameterizedTest(name = "isPrime should handle large non-prime {0}")
    @ValueSource(ints = {104730, 1299710, 15485864})
    void isPrime_ShouldReturnFalseForLargeNonPrimes(int number) {
        assertFalse(Primes.isPrime(number));
    }

    // Testes para o método nextPrime()

    @Test
    void nextPrime_ShouldThrowExceptionForNegativeNumbers() {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.nextPrime(-1));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.nextPrime(-100));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.nextPrime(Integer.MIN_VALUE));
    }

    @ParameterizedTest(name = "nextPrime after {0} should be {1}")
    @MethodSource("provideNextPrimeTestCases")
    void nextPrime_ShouldReturnCorrectNextPrime(int input, int expected) {
        assertEquals(expected, Primes.nextPrime(input));
    }

    private static Stream<Arguments> provideNextPrimeTestCases() {
        return Stream.of(
                arguments(0, 2),
                arguments(1, 2),
                arguments(2, 2),
                arguments(3, 3),
                arguments(4, 5),
                arguments(5, 5),
                arguments(6, 7),
                arguments(7, 7),
                arguments(8, 11),
                arguments(9, 11),
                arguments(10, 11),
                arguments(11, 11),
                arguments(12, 13),
                arguments(13, 13),
                arguments(14, 17),
                arguments(15, 17),
                arguments(16, 17),
                arguments(17, 17),
                arguments(18, 19),
                arguments(19, 19),
                arguments(20, 23),
                arguments(100, 101),
                arguments(1000, 1009),
                arguments(10000, 10007),
                arguments(100000, 100003),
                arguments(15485862, 15485863),
                arguments(Integer.MAX_VALUE - 100, Integer.MAX_VALUE) // Edge case near max int
        );
    }

    @Test
    void nextPrime_ShouldHandleBoundaryCases() {
        assertEquals(2, Primes.nextPrime(0));
        assertEquals(2, Primes.nextPrime(1));
        assertEquals(2, Primes.nextPrime(2));
        assertEquals(3, Primes.nextPrime(3));
    }

    @Test
    void nextPrime_ShouldHandleLargeNumbers() {
        assertEquals(1000003, Primes.nextPrime(1000000));
        assertEquals(10000019, Primes.nextPrime(10000000));
    }

    // Testes para o método primeFactors()

    @Test
    void primeFactors_ShouldThrowExceptionForNumbersLessThan2() {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(1));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(0));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(-1));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(Integer.MIN_VALUE));
    }

    @ParameterizedTest(name = "primeFactors of {0} should be {1}")
    @MethodSource("providePrimeFactorsTestCases")
    void primeFactors_ShouldReturnCorrectFactors(int number, List<Integer> expectedFactors) {
        assertEquals(expectedFactors, Primes.primeFactors(number));
    }

    private static Stream<Arguments> providePrimeFactorsTestCases() {
        return Stream.of(
                arguments(2, List.of(2)),
                arguments(3, List.of(3)),
                arguments(4, List.of(2, 2)),
                arguments(5, List.of(5)),
                arguments(6, List.of(2, 3)),
                arguments(7, List.of(7)),
                arguments(8, List.of(2, 2, 2)),
                arguments(9, List.of(3, 3)),
                arguments(10, List.of(2, 5)),
                arguments(11, List.of(11)),
                arguments(12, List.of(2, 2, 3)),
                arguments(13, List.of(13)),
                arguments(14, List.of(2, 7)),
                arguments(15, List.of(3, 5)),
                arguments(16, List.of(2, 2, 2, 2)),
                arguments(17, List.of(17)),
                arguments(18, List.of(2, 3, 3)),
                arguments(19, List.of(19)),
                arguments(20, List.of(2, 2, 5)),
                arguments(23 * 23, List.of(23, 23)),
                arguments(2 * 3 * 5 * 7 * 11, List.of(2, 3, 5, 7, 11)),
                arguments(997 * 997, List.of(997, 997)), // Large prime squared
                arguments(Integer.MAX_VALUE, List.of(Integer.MAX_VALUE)) // Edge case with max prime
        );
    }

    @Test
    void primeFactors_ShouldHandlePrimeNumbers() {
        assertEquals(List.of(2), Primes.primeFactors(2));
        assertEquals(List.of(3), Primes.primeFactors(3));
        assertEquals(List.of(5), Primes.primeFactors(5));
        assertEquals(List.of(7), Primes.primeFactors(7));
        assertEquals(List.of(11), Primes.primeFactors(11));
        assertEquals(List.of(13), Primes.primeFactors(13));
        assertEquals(List.of(17), Primes.primeFactors(17));
        assertEquals(List.of(19), Primes.primeFactors(19));
        assertEquals(List.of(23), Primes.primeFactors(23));
        assertEquals(List.of(29), Primes.primeFactors(29));
    }

    @Test
    void primeFactors_ShouldHandlePowersOfPrimes() {
        assertEquals(List.of(2, 2, 2, 2, 2), Primes.primeFactors(32));
        assertEquals(List.of(3, 3, 3, 3), Primes.primeFactors(81));
        assertEquals(List.of(5, 5, 5), Primes.primeFactors(125));
        assertEquals(List.of(7, 7, 7, 7), Primes.primeFactors(7*7*7*7));
    }

    @Test
    void primeFactors_ShouldHandleLargeCompositeNumbers() {
        assertEquals(List.of(2, 2, 2, 3, 3, 5, 7, 11, 13),
                Primes.primeFactors(2*2*2*3*3*5*7*11*13));
        assertEquals(List.of(2, 3, 5, 7, 11, 13, 17, 19),
                Primes.primeFactors(2*3*5*7*11*13*17*19));
    }
}