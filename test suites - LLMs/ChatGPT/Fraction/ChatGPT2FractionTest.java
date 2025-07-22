package experimento.chatgpt.fraction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.apache.commons.math3.exception.MathArithmeticException;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT2FractionTest {

    @Test
    void testConstructorWithValidIntegers() {
        Fraction f = new Fraction(6, 8);
        assertEquals(3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    void testConstructorWithZeroNumerator() {
        Fraction f = new Fraction(0, 5);
        assertEquals(0, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    void testConstructorThrowsWithZeroDenominator() {
        assertThrows(MathArithmeticException.class, () -> new Fraction(1, 0));
    }

    @Test
    void testNegate() {
        Fraction f = new Fraction(3, 4).negate();
        assertEquals(-3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    void testAbsPositiveAndNegative() {
        assertEquals(new Fraction(3, 4), new Fraction(3, 4).abs());
        assertEquals(new Fraction(3, 4), new Fraction(-3, 4).abs());
    }

    @Test
    void testReciprocal() {
        Fraction f = new Fraction(2, 3).reciprocal();
        assertEquals(3, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    void testAdditionFraction() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 3);
        Fraction result = a.add(b);
        assertEquals(new Fraction(5, 6), result);
    }

    @Test
    void testAdditionInt() {
        Fraction f = new Fraction(3, 4).add(1);
        assertEquals(new Fraction(7, 4), f);
    }

    @Test
    void testSubtractionFraction() {
        Fraction result = new Fraction(3, 4).subtract(new Fraction(1, 2));
        assertEquals(new Fraction(1, 4), result);
    }

    @Test
    void testSubtractionInt() {
        Fraction result = new Fraction(5, 2).subtract(2);
        assertEquals(new Fraction(1, 2), result);
    }

    @Test
    void testMultiplicationFraction() {
        Fraction result = new Fraction(2, 3).multiply(new Fraction(3, 4));
        assertEquals(new Fraction(1, 2), result);
    }

    @Test
    void testMultiplicationInt() {
        Fraction result = new Fraction(3, 5).multiply(2);
        assertEquals(new Fraction(6, 5), result);
    }

    @Test
    void testDivisionFraction() {
        Fraction result = new Fraction(3, 4).divide(new Fraction(2, 5));
        assertEquals(new Fraction(15, 8), result);
    }

    @Test
    void testDivisionByZeroFractionThrows() {
        Fraction f = new Fraction(1, 2);
        Fraction zero = new Fraction(0, 1);
        assertThrows(MathArithmeticException.class, () -> f.divide(zero));
    }

    @Test
    void testDivisionInt() {
        Fraction result = new Fraction(3, 4).divide(2);
        assertEquals(new Fraction(3, 8), result);
    }

    @Test
    void testDoubleValue() {
        assertEquals(0.75, new Fraction(3, 4).doubleValue(), 0.0001);
    }

    @Test
    void testFloatValue() {
        assertEquals(0.75f, new Fraction(3, 4).floatValue(), 0.0001f);
    }

    @Test
    void testIntValue() {
        assertEquals(1, new Fraction(9, 8).intValue());
    }

    @Test
    void testLongValue() {
        assertEquals(1L, new Fraction(9, 8).longValue());
    }

    @Test
    void testPercentageValue() {
        assertEquals(75.0, new Fraction(3, 4).percentageValue(), 0.001);
    }

    @Test
    void testCompareTo() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(2, 3);
        Fraction c = new Fraction(1, 2);
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(c));
    }

    @Test
    void testEqualsAndHashCode() {
        Fraction a = new Fraction(2, 4);
        Fraction b = new Fraction(1, 2);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToStringFormat() {
        assertEquals("3 / 4", new Fraction(3, 4).toString());
        assertEquals("0", new Fraction(0, 1).toString());
        assertEquals("2", new Fraction(2, 1).toString());
    }

    @Test
    void testGetReducedFraction() {
        Fraction reduced = Fraction.getReducedFraction(4, 8);
        assertEquals(new Fraction(1, 2), reduced);
    }

    @Test
    void testGetReducedFractionWithZeroNumerator() {
        Fraction reduced = Fraction.getReducedFraction(0, 7);
        assertEquals(Fraction.ZERO, reduced);
    }

    @Test
    void testConstructorWithDoubleExact() throws FractionConversionException {
        Fraction f = new Fraction(0.5);
        assertEquals(new Fraction(1, 2), f);
    }

    @Test
    void testConstructorWithDoubleApproximation() throws FractionConversionException {
        Fraction f = new Fraction(0.3333333, 1.0e-6, 100);
        assertEquals(new Fraction(1, 3), f);
    }

    @Test
    void testConstructorWithDoubleAndMaxDenominator() throws FractionConversionException {
        Fraction f = new Fraction(0.75, 10);
        assertEquals(new Fraction(3, 4), f);
    }

    @Test
    void testGetFieldInstance() {
        assertNotNull(Fraction.ONE.getField());
    }
}

