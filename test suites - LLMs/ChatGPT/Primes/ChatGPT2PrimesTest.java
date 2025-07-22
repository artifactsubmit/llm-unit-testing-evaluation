package experimento;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT2PrimesTest {

    // ------------------ Tests for isPrime ------------------

    @Test
    @DisplayName("Deve retornar false para números menores que 2")
    void isPrimeShouldReturnFalseForNumbersLessThanTwo() {
        assertFalse(Primes.isPrime(-5));
        assertFalse(Primes.isPrime(0));
        assertFalse(Primes.isPrime(1));
    }

    @Test
    @DisplayName("Deve retornar true para números primos conhecidos")
    void isPrimeShouldReturnTrueForKnownPrimes() {
        assertTrue(Primes.isPrime(2));
        assertTrue(Primes.isPrime(3));
        assertTrue(Primes.isPrime(5));
        assertTrue(Primes.isPrime(7));
        assertTrue(Primes.isPrime(13));
        assertTrue(Primes.isPrime(7919)); // primo grande
    }

    @Test
    @DisplayName("Deve retornar false para números compostos")
    void isPrimeShouldReturnFalseForNonPrimes() {
        assertFalse(Primes.isPrime(4));
        assertFalse(Primes.isPrime(6));
        assertFalse(Primes.isPrime(100));
        assertFalse(Primes.isPrime(1001));
    }

    // ------------------ Tests for nextPrime ------------------

    @Test
    @DisplayName("Deve lançar exceção se o número for negativo")
    void nextPrimeShouldThrowForNegativeNumbers() {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.nextPrime(-1));
    }

    @Test
    @DisplayName("Deve retornar 2 se a entrada for 2")
    void nextPrimeShouldReturn2WhenInputIs2() {
        assertEquals(2, Primes.nextPrime(2));
    }

    @Test
    @DisplayName("Deve retornar o próximo primo igual ou maior que n")
    void nextPrimeShouldReturnNextPrimeGreaterOrEqualToN() {
        assertEquals(2, Primes.nextPrime(1));
        assertEquals(3, Primes.nextPrime(3));
        assertEquals(5, Primes.nextPrime(4));
        assertEquals(7, Primes.nextPrime(6));
        assertEquals(11, Primes.nextPrime(9));
    }

    @Test
    @DisplayName("Deve lidar com múltiplos de 3 corretamente")
    void nextPrimeShouldSkipMultiplesOfThree() {
        assertEquals(5, Primes.nextPrime(4)); // 4 não é múltiplo de 3, próximo primo é 5
        assertEquals(7, Primes.nextPrime(6)); // 6 é múltiplo de 3
        assertEquals(11, Primes.nextPrime(9)); // 9 é múltiplo de 3
    }

    // ------------------ Tests for primeFactors ------------------

    @Test
    @DisplayName("Deve lançar exceção se n < 2")
    void primeFactorsShouldThrowForInvalidInput() {
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(1));
        assertThrows(MathIllegalArgumentException.class, () -> Primes.primeFactors(0));
    }

    @Test
    @DisplayName("Deve retornar fator primo único se n for primo")
    void primeFactorsShouldReturnListWithNIfNIsPrime() {
        List<Integer> factors = Primes.primeFactors(13);
        assertEquals(List.of(13), factors);
    }

    @Test
    @DisplayName("Deve retornar fatores primos corretos para compostos")
    void primeFactorsShouldReturnCorrectFactors() {
        assertEquals(List.of(2, 2, 3), Primes.primeFactors(12));     // 2*2*3
        assertEquals(List.of(2, 2, 2, 3, 3), Primes.primeFactors(72)); // 2^3 * 3^2
        assertEquals(List.of(5, 5, 5), Primes.primeFactors(125));     // 5^3
    }

    @Test
    @DisplayName("Deve retornar fatorizações corretas para números grandes")
    void primeFactorsShouldWorkForLargerCompositeNumbers() {
        List<Integer> expected = List.of(2, 2, 2, 2, 3, 3, 5, 7); // 2^4 * 3^2 * 5 * 7 = 5040
        assertEquals(expected, Primes.primeFactors(5040));
    }

}

