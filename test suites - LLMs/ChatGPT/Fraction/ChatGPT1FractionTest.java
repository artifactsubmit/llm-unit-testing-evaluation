package experimento.chatgpt.fraction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.math3.exception.MathArithmeticException;

class ChatGPT1FractionTest {

    @Test
    void testConstructorWithIntAndReduction() {
        Fraction fraction = new Fraction(2, 4);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    void testConstructorWithZeroDenominatorShouldThrow() {
        assertThrows(MathArithmeticException.class, () -> new Fraction(1, 0));
    }

    @Test
    void testAdditionWithAnotherFraction() {
        Fraction f1 = new Fraction(1, 3);
        Fraction f2 = new Fraction(1, 6);
        Fraction result = f1.add(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void testAdditionWithInteger() {
        Fraction f = new Fraction(3, 4);
        Fraction result = f.add(1);
        assertEquals(7, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    void testSubtractionWithAnotherFraction() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(1, 4);
        Fraction result = f1.subtract(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void testMultiplicationByFraction() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(3, 4);
        Fraction result = f1.multiply(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void testDivisionByFraction() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 4);
        Fraction result = f1.divide(f2);
        assertEquals(2, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    void testNegateAndAbs() {
        Fraction f = new Fraction(-3, 4);
        assertEquals(3, f.abs().getNumerator());
        assertEquals(-3, f.negate().getNumerator());
    }

    @Test
    void testReciprocal() {
        Fraction f = new Fraction(2, 3);
        Fraction r = f.reciprocal();
        assertEquals(3, r.getNumerator());
        assertEquals(2, r.getDenominator());
    }

    @Test
    void testEqualsAndHashCode() {
        Fraction f1 = new Fraction(2, 4);
        Fraction f2 = new Fraction(1, 2);
        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    void testDoubleFloatIntLongValues() {
        Fraction f = new Fraction(3, 2);
        assertEquals(1.5, f.doubleValue(), 0.00001);
        assertEquals(1.5f, f.floatValue(), 0.00001f);
        assertEquals(1, f.intValue());
        assertEquals(1L, f.longValue());
    }

    @Test
    void testCompareTo() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(3, 4);
        assertTrue(f1.compareTo(f2) < 0);
    }

    @Test
    void testToString() {
        assertEquals("1 / 2", new Fraction(1, 2).toString());
        assertEquals("0", new Fraction(0, 3).toString());
        assertEquals("3", new Fraction(3, 1).toString());
    }
}

