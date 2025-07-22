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

class   DeepSeek1PrimesTest {

    // Testes para o método isPrime()

    @ParameterizedTest(name = "isPrime should return false for non-prime number {0}")
    @ValueSource(ints = {0, 1, 4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28})
    void isPrime_ShouldReturnFalseForNonPrimeNumbers(int number) {
        Assertions.assertFalse(Primes.isPrime(number));
    }

    @ParameterizedTest(name = "isPrime should return true for prime number {0}")
    @ValueSource(ints = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71})
    void isPrime_ShouldReturnTrueForPrimeNumbers(int number) {
        assertTrue(Primes.isPrime(number));
    }

    @ParameterizedTest(name = "isPrime should return false for negative number {0}")
    @ValueSource(ints = {-1, -2, -3, -10, -100})
    void isPrime_ShouldReturnFalseForNegativeNumbers(int number) {
        assertFalse(Primes.isPrime(number));
    }

    @Test
    void isPrime_ShouldHandleLargePrimeNumber() {
        assertTrue(Primes.isPrime(104729)); // 10000th prime number
    }

    @Test
    void isPrime_ShouldHandleLargeNonPrimeNumber() {
        assertFalse(Primes.isPrime(104730)); // 104729 + 1
    }

    // Testes para o método nextPrime()

    @ParameterizedTest(name = "nextPrime({0}) should return {1}")
    @MethodSource("nextPrimeTestCases")
    void nextPrime_ShouldReturnCorrectPrime(int input, int expected) {
        assertEquals(expected, Primes.nextPrime(input));
    }

    static Stream<Arguments> nextPrimeTestCases() {
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
                arguments(101, 101),
                arguments(1000, 1009),
                arguments(10000, 10007)
        );
    }

    @ParameterizedTest(name = "nextPrime should throw exception for negative input {0}")
    @ValueSource(ints = {-1, -2, -10, -100})
    void nextPrime_ShouldThrowExceptionForNegativeInput(int number) {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.nextPrime(number));
    }

    @Test
    void nextPrime_ShouldHandleLargeInput() {
        assertEquals(104729, Primes.nextPrime(104728));
        assertEquals(104743, Primes.nextPrime(104730));
    }

    // Testes para o método primeFactors()

    @ParameterizedTest(name = "primeFactors({0}) should return {1}")
    @MethodSource("primeFactorsTestCases")
    void primeFactors_ShouldReturnCorrectFactors(int input, List<Integer> expected) {
        assertEquals(expected, Primes.primeFactors(input));
    }

    static Stream<Arguments> primeFactorsTestCases() {
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
                arguments(25, List.of(5, 5)),
                arguments(27, List.of(3, 3, 3)),
                arguments(30, List.of(2, 3, 5)),
                arguments(36, List.of(2, 2, 3, 3)),
                arguments(100, List.of(2, 2, 5, 5)),
                arguments(101, List.of(101)),
                arguments(1024, List.of(2, 2, 2, 2, 2, 2, 2, 2, 2, 2)),
                arguments(104729, List.of(104729)) // large prime number
        );
    }

    @ParameterizedTest(name = "primeFactors should throw exception for invalid input {0}")
    @ValueSource(ints = {0, 1, -1, -2, -10, -100})
    void primeFactors_ShouldThrowExceptionForInvalidInput(int number) {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(number));
    }

    @Test
    void primeFactors_ShouldHandleLargeCompositeNumber() {
        assertEquals(List.of(2, 3, 5, 7, 11, 13), Primes.primeFactors(2*3*5*7*11*13));
    }
}
