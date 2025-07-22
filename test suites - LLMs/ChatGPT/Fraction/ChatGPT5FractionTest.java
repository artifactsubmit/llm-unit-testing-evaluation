package experimento.chatgpt.fraction;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT5FractionTest {

    // ----------------------------
    // Testes de Construtores
    // ----------------------------

    @Test
    void testConstructorWithIntAndDenominatorReduction() {
        Fraction fraction = new Fraction(6, 8);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    void testConstructorWithNegativeDenominator() {
        Fraction fraction = new Fraction(1, -2);
        assertEquals(-1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    void testConstructorZeroDenominatorThrowsException() {
        assertThrows(MathArithmeticException.class, () -> new Fraction(1, 0));
    }

    @Test
    void testConstructorOnlyNumerator() {
        Fraction fraction = new Fraction(5);
        assertEquals(5, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    // ----------------------------
    // Testes de Operações Aritméticas
    // ----------------------------

    @Test
    void testAddFraction() {
        Fraction result = new Fraction(1, 4).add(new Fraction(1, 4));
        assertEquals(new Fraction(1, 2), result);
    }

    @Test
    void testAddFractionNullThrowsException() {
        assertThrows(NullArgumentException.class, () -> new Fraction(1, 4).add(null));
    }

    @Test
    void testAddInteger() {
        Fraction result = new Fraction(3, 4).add(1);
        assertEquals(new Fraction(7, 4), result);
    }

    @Test
    void testSubtractFraction() {
        Fraction result = new Fraction(3, 4).subtract(new Fraction(1, 4));
        assertEquals(new Fraction(1, 2), result);
    }

    @Test
    void testSubtractInteger() {
        Fraction result = new Fraction(7, 4).subtract(1);
        assertEquals(new Fraction(3, 4), result);
    }

    @Test
    void testMultiplyFraction() {
        Fraction result = new Fraction(2, 3).multiply(new Fraction(3, 4));
        assertEquals(new Fraction(1, 2), result);
    }

    @Test
    void testMultiplyInteger() {
        Fraction result = new Fraction(2, 5).multiply(3);
        assertEquals(new Fraction(6, 5), result);
    }

    @Test
    void testDivideFraction() {
        Fraction result = new Fraction(1, 2).divide(new Fraction(1, 4));
        assertEquals(new Fraction(2, 1), result);
    }

    @Test
    void testDivideInteger() {
        Fraction result = new Fraction(3, 4).divide(2);
        assertEquals(new Fraction(3, 8), result);
    }

    @Test
    void testDivideByZeroFractionThrowsException() {
        assertThrows(MathArithmeticException.class, () -> new Fraction(1, 2).divide(Fraction.ZERO));
    }

    // ----------------------------
    // Testes de Conversão
    // ----------------------------

    @Test
    void testDoubleValue() {
        assertEquals(0.5, new Fraction(1, 2).doubleValue(), 1e-10);
    }

    @Test
    void testFloatValue() {
        assertEquals(0.5f, new Fraction(1, 2).floatValue(), 1e-6f);
    }

    @Test
    void testIntValue() {
        assertEquals(2, new Fraction(5, 2).intValue());
    }

    @Test
    void testLongValue() {
        assertEquals(2L, new Fraction(5, 2).longValue());
    }

    @Test
    void testPercentageValue() {
        assertEquals(50.0, new Fraction(1, 2).percentageValue(), 1e-10);
    }

    // ----------------------------
    // Testes de Propriedades
    // ----------------------------

    @Test
    void testAbsPositive() {
        Fraction f = new Fraction(3, 4);
        assertEquals(f, f.abs());
    }

    @Test
    void testAbsNegative() {
        Fraction f = new Fraction(-3, 4);
        assertEquals(new Fraction(3, 4), f.abs());
    }

    @Test
    void testNegate() {
        Fraction f = new Fraction(3, 4);
        assertEquals(new Fraction(-3, 4), f.negate());
    }

    @Test
    void testReciprocal() {
        Fraction f = new Fraction(3, 4);
        assertEquals(new Fraction(4, 3), f.reciprocal());
    }

    @Test
    void testCompareTo() {
        assertTrue(new Fraction(1, 2).compareTo(new Fraction(2, 3)) < 0);
        assertTrue(new Fraction(3, 4).compareTo(new Fraction(3, 4)) == 0);
        assertTrue(new Fraction(5, 4).compareTo(new Fraction(1, 2)) > 0);
    }

    @Test
    void testEqualsAndHashCode() {
        Fraction f1 = new Fraction(2, 4);
        Fraction f2 = new Fraction(1, 2);
        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("1 / 2", new Fraction(1, 2).toString());
        assertEquals("0", new Fraction(0, 1).toString());
        assertEquals("5", new Fraction(5, 1).toString());
    }

    @Test
    void testGetReducedFraction() {
        Fraction reduced = Fraction.getReducedFraction(8, 12);
        assertEquals(new Fraction(2, 3), reduced);
    }

    @Test
    void testGetFieldNotNull() {
        assertNotNull(new Fraction(1, 2).getField());
    }
}
