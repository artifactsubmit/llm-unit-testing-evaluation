package experimento.chatgpt.fraction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT3FractionTest {

    @Test
    @DisplayName("Criação de fração com numerador e denominador positivos")
    void testCreationWithPositiveValues() {
        Fraction fraction = new Fraction(6, 8);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    @DisplayName("Criação de fração com denominador zero deve lançar exceção")
    void testCreationWithZeroDenominator() {
        assertThrows(ArithmeticException.class, () -> new Fraction(1, 0));
    }

    @Test
    @DisplayName("Negação de fração positiva")
    void testNegatePositiveFraction() {
        Fraction fraction = new Fraction(2, 5);
        Fraction negated = fraction.negate();
        assertEquals(-2, negated.getNumerator());
        assertEquals(5, negated.getDenominator());
    }

    @Test
    @DisplayName("Valor absoluto de fração negativa")
    void testAbsNegativeFraction() {
        Fraction fraction = new Fraction(-3, 7);
        Fraction abs = fraction.abs();
        assertEquals(3, abs.getNumerator());
        assertEquals(7, abs.getDenominator());
    }

    @Test
    @DisplayName("Recíproco de uma fração")
    void testReciprocal() {
        Fraction fraction = new Fraction(3, 4);
        Fraction reciprocal = fraction.reciprocal();
        assertEquals(4, reciprocal.getNumerator());
        assertEquals(3, reciprocal.getDenominator());
    }

    @Test
    @DisplayName("Soma entre frações")
    void testAddFractions() {
        Fraction a = new Fraction(1, 4);
        Fraction b = new Fraction(1, 2);
        Fraction result = a.add(b);
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    @DisplayName("Soma de fração com inteiro")
    void testAddInt() {
        Fraction a = new Fraction(3, 5);
        Fraction result = a.add(2);
        assertEquals(13, result.getNumerator());
        assertEquals(5, result.getDenominator());
    }

    @Test
    @DisplayName("Subtração entre frações")
    void testSubtractFractions() {
        Fraction a = new Fraction(3, 4);
        Fraction b = new Fraction(1, 2);
        Fraction result = a.subtract(b);
        assertEquals(1, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    @DisplayName("Multiplicação entre frações")
    void testMultiplyFractions() {
        Fraction a = new Fraction(2, 3);
        Fraction b = new Fraction(3, 4);
        Fraction result = a.multiply(b);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    @DisplayName("Divisão entre frações")
    void testDivideFractions() {
        Fraction a = new Fraction(2, 3);
        Fraction b = new Fraction(4, 5);
        Fraction result = a.divide(b);
        assertEquals(5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    @DisplayName("Divisão por zero deve lançar exceção")
    void testDivideByZeroFraction() {
        Fraction a = new Fraction(1, 2);
        Fraction zero = new Fraction(0, 1);
        assertThrows(ArithmeticException.class, () -> a.divide(zero));
    }

    @Test
    @DisplayName("Conversão para double")
    void testDoubleValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(0.5, fraction.doubleValue(), 0.00001);
    }

    @Test
    @DisplayName("Comparação entre frações")
    void testCompareTo() {
        Fraction a = new Fraction(1, 3);
        Fraction b = new Fraction(2, 3);
        assertTrue(a.compareTo(b) < 0);
    }

    @Test
    @DisplayName("Verificação de igualdade entre frações")
    void testEquals() {
        Fraction a = new Fraction(2, 4);
        Fraction b = new Fraction(1, 2);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("toString para fração própria e inteira")
    void testToString() {
        assertEquals("1 / 2", new Fraction(1, 2).toString());
        assertEquals("3", new Fraction(3, 1).toString());
        assertEquals("0", new Fraction(0, 5).toString());
    }

    @Test
    @DisplayName("getReducedFraction reduz corretamente")
    void testGetReducedFraction() {
        Fraction fraction = Fraction.getReducedFraction(50, 100);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    @DisplayName("Criação a partir de double com precisão")
    void testFractionFromDoubleEpsilon() throws FractionConversionException {
        Fraction fraction = new Fraction(0.333, 1e-4, 100);
        assertEquals(1, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test
    @DisplayName("percentageValue retorna valor esperado")
    void testPercentageValue() {
        Fraction fraction = new Fraction(1, 4);
        assertEquals(25.0, fraction.percentageValue(), 0.001);
    }

    @Test
    @DisplayName("Teste de hashCode consistente com equals")
    void testHashCodeConsistency() {
        Fraction a = new Fraction(3, 6);
        Fraction b = new Fraction(1, 2);
        assertEquals(a.hashCode(), b.hashCode());
    }
}

