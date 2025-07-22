package experimento.chatgpt.fraction;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChatGPT4FractionTest {

    @Test
    @DisplayName("Criação de fração com numerador e denominador positivos")
    void testFractionConstructorValid() {
        Fraction f = new Fraction(3, 9);
        assertEquals(1, f.getNumerator());
        assertEquals(3, f.getDenominator());
    }

    @Test
    @DisplayName("Criação de fração com denominador zero deve lançar exceção")
    void testFractionConstructorZeroDenominator() {
        assertThrows(MathArithmeticException.class, () -> new Fraction(1, 0));
    }

    @Test
    @DisplayName("Criação de fração com sinal negativo no denominador")
    void testFractionConstructorNegativeDenominator() {
        Fraction f = new Fraction(1, -2);
        assertEquals(-1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    @DisplayName("Soma de duas frações com mesmo denominador")
    void testAddSameDenominator() {
        Fraction f1 = new Fraction(1, 3);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.add(f2);
        assertEquals(2, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    @DisplayName("Subtração de frações com denominadores diferentes")
    void testSubtractDifferentDenominators() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(1, 2);
        Fraction result = f1.subtract(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    @DisplayName("Multiplicação por inteiro")
    void testMultiplyByInteger() {
        Fraction f = new Fraction(2, 3);
        Fraction result = f.multiply(3);
        assertEquals(2, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    @DisplayName("Divisão por fração nula deve lançar exceção")
    void testDivideByZeroFraction() {
        Fraction f = new Fraction(1, 2);
        Fraction zero = new Fraction(0, 1);
        assertThrows(MathArithmeticException.class, () -> f.divide(zero));
    }

    @Test
    @DisplayName("Valor decimal da fração")
    void testDoubleValue() {
        Fraction f = new Fraction(1, 4);
        assertEquals(0.25, f.doubleValue(), 1e-10);
    }

    @Test
    @DisplayName("Valor percentual da fração")
    void testPercentageValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(50.0, f.percentageValue(), 1e-10);
    }

    @Test
    @DisplayName("Valor absoluto de fração negativa")
    void testAbsNegative() {
        Fraction f = new Fraction(-3, 4);
        Fraction result = f.abs();
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    @DisplayName("Inverso aditivo da fração")
    void testNegate() {
        Fraction f = new Fraction(3, 5);
        Fraction result = f.negate();
        assertEquals(-3, result.getNumerator());
        assertEquals(5, result.getDenominator());
    }

    @Test
    @DisplayName("Inverso multiplicativo da fração")
    void testReciprocal() {
        Fraction f = new Fraction(2, 5);
        Fraction result = f.reciprocal();
        assertEquals(5, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    @DisplayName("Comparação entre frações: menor, igual e maior")
    void testCompareTo() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        Fraction f3 = new Fraction(1, 2);
        assertTrue(f1.compareTo(f2) < 0);
        assertTrue(f2.compareTo(f1) > 0);
        assertEquals(0, f1.compareTo(f3));
    }

    @Test
    @DisplayName("Testa toString para diferentes representações")
    void testToStringRepresentations() {
        assertEquals("2 / 3", new Fraction(2, 3).toString());
        assertEquals("0", new Fraction(0, 5).toString());
        assertEquals("3", new Fraction(3, 1).toString());
    }

    @Test
    @DisplayName("Criação de fração reduzida usando método estático")
    void testGetReducedFraction() {
        Fraction f = Fraction.getReducedFraction(4, 8);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    @DisplayName("Verifica igualdade de frações")
    void testEquals() {
        Fraction f1 = new Fraction(2, 4);
        Fraction f2 = new Fraction(1, 2);
        assertEquals(f1, f2);
    }

    @Test
    @DisplayName("Soma com fração nula deve retornar a original")
    void testAddZero() {
        Fraction f = new Fraction(3, 5);
        Fraction result = f.add(Fraction.ZERO);
        assertEquals(f, result);
    }

    @Test
    @DisplayName("Subtração com resultado negativo")
    void testSubtractToNegative() {
        Fraction f1 = new Fraction(1, 4);
        Fraction f2 = new Fraction(3, 4);
        Fraction result = f1.subtract(f2);
        assertEquals(-1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }
}

