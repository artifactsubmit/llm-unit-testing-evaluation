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

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import java.util.List;
import java.util.stream.Stream;

class DeepSeek5PrimesTest {

    // Testes para o método isPrime()

    @ParameterizedTest(name = "isPrime should return false for non-prime {0}")
    @ValueSource(ints = {0, 1, 4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28})
    void isPrime_ShouldReturnFalseForNonPrimes(int number) {
        Assertions.assertFalse(Primes.isPrime(number));
    }

    @ParameterizedTest(name = "isPrime should return true for prime {0}")
    @ValueSource(ints = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71})
    void isPrime_ShouldReturnTrueForPrimes(int number) {
        assertTrue(Primes.isPrime(number));
    }

    @ParameterizedTest(name = "isPrime should return false for negative {0}")
    @ValueSource(ints = {-1, -2, -3, -10, -100, Integer.MIN_VALUE})
    void isPrime_ShouldReturnFalseForNegativeNumbers(int number) {
        assertFalse(Primes.isPrime(number));
    }

    @Test
    void isPrime_ShouldHandleLargePrimes() {
        assertTrue(Primes.isPrime(104729)); // 10000th prime
        assertTrue(Primes.isPrime(2147483647)); // Largest 32-bit prime
    }

    @Test
    void isPrime_ShouldHandleLargeNonPrimes() {
        assertFalse(Primes.isPrime(104730)); // 104729 + 1
        assertFalse(Primes.isPrime(2147483646)); // Largest 32-bit even number
    }

    // Testes para o método nextPrime()

    @ParameterizedTest(name = "nextPrime after {0} should be {1}")
    @MethodSource("provideNextPrimeTestCases")
    void nextPrime_ShouldReturnCorrectNextPrime(int input, int expected) {
        assertEquals(expected, Primes.nextPrime(input));
    }

    private static Stream<Arguments> provideNextPrimeTestCases() {
        return Stream.of(
                arguments(-1, 2),
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
                arguments(101, 101),
                arguments(1000, 1009),
                arguments(104728, 104729),
                arguments(104729, 104729)
        );
    }

    @Test
    void nextPrime_ShouldHandleLargeInputs() {
        assertEquals(104743, Primes.nextPrime(104730));
        assertEquals(2147483647, Primes.nextPrime(2147483629)); // Largest 32-bit prime
    }

    @Test
    void nextPrime_ShouldThrowExceptionForNegativeInput() {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.nextPrime(-2));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.nextPrime(-100));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.nextPrime(Integer.MIN_VALUE));
    }

    // Testes para o método primeFactors()

    @ParameterizedTest(name = "primeFactors of {0} should be {1}")
    @MethodSource("providePrimeFactorsTestCases")
    void primeFactors_ShouldReturnCorrectFactors(int input, List<Integer> expected) {
        assertEquals(expected, Primes.primeFactors(input));
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
                arguments(23, List.of(23)),
                arguments(25, List.of(5, 5)),
                arguments(27, List.of(3, 3, 3)),
                arguments(28, List.of(2, 2, 7)),
                arguments(29, List.of(29)),
                arguments(30, List.of(2, 3, 5)),
                arguments(36, List.of(2, 2, 3, 3)),
                arguments(100, List.of(2, 2, 5, 5)),
                arguments(101, List.of(101)),
                arguments(104729, List.of(104729)) // large prime
        );
    }

    @Test
    void primeFactors_ShouldHandleLargeCompositeNumbers() {
        assertEquals(List.of(2, 3, 5, 7, 11, 13), Primes.primeFactors(2*3*5*7*11*13));
        assertEquals(List.of(2, 2, 2, 3, 3, 5, 7, 11, 13),
                Primes.primeFactors(2*2*2*3*3*5*7*11*13));
    }

    @ParameterizedTest(name = "primeFactors should throw exception for {0}")
    @ValueSource(ints = {-1, 0, 1, Integer.MIN_VALUE})
    void primeFactors_ShouldThrowExceptionForInvalidInput(int number) {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(number));
    }
}
