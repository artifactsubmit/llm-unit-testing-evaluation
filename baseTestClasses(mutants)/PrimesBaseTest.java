package experimento;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrimesBaseTest {

    @Test
    void coverIsPrime() {
        Primes.isPrime(2);  // esperado: true
        Primes.isPrime(9);  // esperado: false
    }

    @Test
    void coverNextPrime() {
        Primes.nextPrime(14);  // ativa laço de busca por próximo primo
    }

    @Test
    void coverPrimeFactors() {
        Primes.primeFactors(18); // ativa fatoração com múltiplas divisões
    }
}