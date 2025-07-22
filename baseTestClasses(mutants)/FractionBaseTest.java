package experimento;

import experimento.FractionConversionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FractionBaseTest {

    @Test
    void testConstructors() throws FractionConversionException {
        Fraction f1 = new Fraction(3);
        Fraction f2 = new Fraction(3, 4);
        Fraction f3 = new Fraction(0.75);
        Fraction f4 = new Fraction(0.3333, 1.0e-4, 100);
        Fraction f5 = new Fraction(0.25, 10);

        assertNotNull(f1);
        assertNotNull(f2);
        assertNotNull(f3);
        assertNotNull(f4);
        assertNotNull(f5);
    }

    @Test
    void testArithmeticOperations() {
        Fraction a = new Fraction(3, 4);
        Fraction b = new Fraction(2, 5);

        assertEquals(new Fraction(23, 20), a.add(b));
        assertEquals(new Fraction(7, 20), a.subtract(b));

        assertEquals(new Fraction(3, 10), a.multiply(b));
        assertEquals(new Fraction(15, 8), a.divide(b));

        assertEquals(new Fraction(7, 4), a.add(1));
        assertEquals(new Fraction(-1, 4), a.subtract(1));
        assertEquals(new Fraction(6, 4), a.multiply(2));
        assertEquals(new Fraction(3, 8), a.divide(2));
    }

    @Test
    void testNegateAndReciprocalAndAbs() {
        Fraction f = new Fraction(3, 4);

        assertEquals(new Fraction(3, 4), f.abs());
        assertEquals(new Fraction(-3, 4), f.negate());
        assertEquals(new Fraction(4, 3), f.reciprocal());
    }


    @Test
    void testGettersAndMeta() {
        Fraction f = new Fraction(7, 3);

        assertEquals(7, f.getNumerator());
        assertEquals(3, f.getDenominator());

        assertTrue(f.doubleValue() > 2.0);
        assertTrue(f.floatValue() > 2.0f);
        assertEquals(2, f.intValue());
        assertEquals(2L, f.longValue());
        assertEquals(233.33, f.percentageValue(), 1.0);
        assertNotNull(f.toString());
        assertNotNull(f.getField());
    }

    @Test
    void testEqualityAndComparison() {
        Fraction f1 = new Fraction(2, 4); // will reduce to 1/2
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(3, 4);

        assertEquals(f1, f2);
        assertNotEquals(f1, f3);
        assertEquals(0, f1.compareTo(f2));
        assertTrue(f1.compareTo(f3) < 0);
        assertTrue(f3.compareTo(f2) > 0);
        assertEquals(f1.hashCode(), f2.hashCode());
    }
}

