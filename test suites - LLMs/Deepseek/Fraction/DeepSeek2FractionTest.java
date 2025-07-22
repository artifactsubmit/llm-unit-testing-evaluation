package experimento.deepseek.fraction;

import static org.junit.jupiter.api.Assertions.*;

import experimento.Fraction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;

class DeepSeek2FractionTest {

    // Testes para construtores
    @Test
    void testConstructorInt() {
        Fraction f = new Fraction(3);
        assertEquals(3, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    void testConstructorIntInt() {
        Fraction f = new Fraction(3, 4);
        assertEquals(3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    void testConstructorIntIntReduces() {
        Fraction f = new Fraction(6, 8);
        assertEquals(3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    void testConstructorIntIntDenominatorZero() {
        assertThrows(MathArithmeticException.class, () -> new Fraction(1, 0));
    }

    @Test
    void testConstructorIntIntNegativeDenominator() {
        Fraction f = new Fraction(3, -4);
        assertEquals(-3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    void testConstructorIntIntMinValue() {
        assertThrows(MathArithmeticException.class, () -> new Fraction(Integer.MIN_VALUE, -1));
    }

    // Testes para getReducedFraction
    @Test
    void testGetReducedFraction() {
        Fraction f = Fraction.getReducedFraction(6, 8);
        assertEquals(3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    void testGetReducedFractionDenominatorZero() {
        assertThrows(MathArithmeticException.class, () -> Fraction.getReducedFraction(1, 0));
    }

    @Test
    void testGetReducedFractionNumeratorZero() {
        Fraction f = Fraction.getReducedFraction(0, 5);
        assertSame(Fraction.ZERO, f);
    }

    @Test
    void testGetReducedFractionMinValue() {
        assertThrows(MathArithmeticException.class, () -> Fraction.getReducedFraction(Integer.MIN_VALUE, -1));
    }

    // Testes para operações aritméticas
    @Test
    void testAdd() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.add(f2);
        assertEquals(5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    void testAddNull() {
        Fraction f1 = new Fraction(1, 2);
        assertThrows(NullArgumentException.class, () -> f1.add(null));
    }

    @Test
    void testAddOverflow() {
        Fraction f1 = new Fraction(Integer.MAX_VALUE - 1, 1);
        Fraction f2 = new Fraction(2, 1);
        assertThrows(MathArithmeticException.class, () -> f1.add(f2));
    }

    @Test
    void testSubtract() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.subtract(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    void testMultiply() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 3);
        Fraction result = f1.multiply(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    void testMultiplyByZero() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = Fraction.ZERO;
        Fraction result = f1.multiply(f2);
        assertSame(Fraction.ZERO, result);
    }

    @Test
    void testDivide() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 3);
        Fraction result = f1.divide(f2);
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    void testDivideByZero() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = Fraction.ZERO;
        assertThrows(MathArithmeticException.class, () -> f1.divide(f2));
    }

    // Testes para operações unárias
    @Test
    void testAbsPositive() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.abs();
        assertSame(f, result);
    }

    @Test
    void testAbsNegative() {
        Fraction f = new Fraction(-1, 2);
        Fraction result = f.abs();
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void testNegate() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.negate();
        assertEquals(-1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void testNegateMinValue() {
        Fraction f = new Fraction(Integer.MIN_VALUE, 1);
        assertThrows(MathArithmeticException.class, () -> f.negate());
    }

    @Test
    void testReciprocal() {
        Fraction f = new Fraction(2, 3);
        Fraction result = f.reciprocal();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void testReciprocalZero() {
        Fraction f = Fraction.ZERO;
        assertThrows(MathArithmeticException.class, () -> f.reciprocal());
    }

    // Testes para conversões
    @Test
    void testDoubleValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0.5, f.doubleValue(), 0.00001);
    }

    @Test
    void testFloatValue() {
        Fraction f = new Fraction(1, 3);
        assertEquals(0.33333f, f.floatValue(), 0.00001f);
    }

    @Test
    void testIntValue() {
        Fraction f = new Fraction(5, 2);
        assertEquals(2, f.intValue());
    }

    @Test
    void testLongValue() {
        Fraction f = new Fraction(5, 2);
        assertEquals(2L, f.longValue());
    }

    @Test
    void testPercentageValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(50.0, f.percentageValue(), 0.00001);
    }

    // Testes para comparação
    @Test
    void testCompareTo() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        assertTrue(f1.compareTo(f2) > 0);
        assertTrue(f2.compareTo(f1) < 0);
        assertEquals(0, f1.compareTo(new Fraction(2, 4)));
    }

    @Test
    void testEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(1, 3);
        assertEquals(f1, f2);
        assertNotEquals(f1, f3);
        assertNotEquals(f1, null);
        assertNotEquals(f1, "not a fraction");
    }

    @Test
    void testHashCode() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    // Testes para toString
    @Test
    void testToString() {
        assertEquals("1 / 2", new Fraction(1, 2).toString());
        assertEquals("3", new Fraction(3, 1).toString());
        assertEquals("0", Fraction.ZERO.toString());
    }

    // Testes para constantes
    @Test
    void testConstants() {
        assertEquals(0, Fraction.ZERO.getNumerator());
        assertEquals(1, Fraction.ZERO.getDenominator());
        
        assertEquals(1, Fraction.ONE.getNumerator());
        assertEquals(1, Fraction.ONE.getDenominator());
        
        assertEquals(1, Fraction.ONE_HALF.getNumerator());
        assertEquals(2, Fraction.ONE_HALF.getDenominator());
    }

    // Testes parametrizados para cobertura mais ampla
    @ParameterizedTest
    @CsvSource({
        "1, 2, 1, 3, 5, 6",    // 1/2 + 1/3 = 5/6
        "1, 4, 1, 4, 1, 2",     // 1/4 + 1/4 = 1/2
        "-1, 2, 1, 2, 0, 1"     // -1/2 + 1/2 = 0
    })
    void testAddParameterized(int num1, int den1, int num2, int den2, int expectedNum, int expectedDen) {
        Fraction f1 = new Fraction(num1, den1);
        Fraction f2 = new Fraction(num2, den2);
        Fraction result = f1.add(f2);
        assertEquals(expectedNum, result.getNumerator());
        assertEquals(expectedDen, result.getDenominator());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, -1, 5, Integer.MAX_VALUE, Integer.MIN_VALUE})
    void testAddIntParameterized(int value) {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.add(value);
        assertEquals(1 + value * 2, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    // Testes para construtores com double
    @Test
    void testConstructorDouble() {
        Fraction f = new Fraction(0.5);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    void testConstructorDoubleWithEpsilon() {
        Fraction f = new Fraction(0.33333, 0.0001, 100);
        assertEquals(1, f.getNumerator());
        assertEquals(3, f.getDenominator());
    }

    @Test
    void testConstructorDoubleWithMaxDenominator() {
        Fraction f = new Fraction(0.142857, 100);
        assertEquals(1, f.getNumerator());
        assertEquals(7, f.getDenominator());
    }

    @Test
    void testConstructorDoubleNonTerminating() {
        assertThrows(FractionConversionException.class, () -> new Fraction(Math.PI, 0.000001, 1000));
    }

    // Testes para getField
    @Test
    void testGetField() {
        Fraction f = new Fraction(1, 2);
        assertNotNull(f.getField());
        assertEquals(FractionField.getInstance(), f.getField());
    }
}
