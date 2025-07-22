package experimento.deepseek.fraction;

import static org.junit.jupiter.api.Assertions.*;

import experimento.Fraction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

class DeepSeek3FractionTest {

    // Testes para construtores
    @Test
    void constructorInt_shouldCreateFractionWithDenominatorOne() {
        Fraction f = new Fraction(5);
        assertEquals(5, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    void constructorIntInt_shouldCreateReducedFraction() {
        Fraction f = new Fraction(4, 8);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    void constructorIntInt_shouldHandleNegativeDenominator() {
        Fraction f = new Fraction(3, -4);
        assertEquals(-3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    void constructorIntInt_shouldThrowWhenDenominatorZero() {
        Exception exception = assertThrows(MathArithmeticException.class, () -> {
            new Fraction(1, 0);
        });
        assertEquals(LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION, exception.getMessage());
    }

    @Test
    void constructorDouble_shouldCreateExactFraction() {
        Fraction f = new Fraction(0.5);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    void constructorDouble_shouldCreateApproximateFraction() {
        Fraction f = new Fraction(0.33333, 0.0001, 100);
        assertEquals(1, f.getNumerator());
        assertEquals(3, f.getDenominator());
    }

    @Test
    void constructorDoubleWithMaxDenominator_shouldCreateFractionWithinLimits() {
        Fraction f = new Fraction(0.33333, 100);
        assertEquals(1, f.getNumerator());
        assertEquals(3, f.getDenominator());
    }

    // Testes para operações aritméticas
    @Test
    void add_shouldAddFractionsCorrectly() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.add(f2);
        assertEquals(5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    void add_shouldAddIntegerCorrectly() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.add(1);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void subtract_shouldSubtractFractionsCorrectly() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.subtract(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    void subtract_shouldSubtractIntegerCorrectly() {
        Fraction f = new Fraction(3, 2);
        Fraction result = f.subtract(1);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void multiply_shouldMultiplyFractionsCorrectly() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 3);
        Fraction result = f1.multiply(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    void multiply_shouldMultiplyByIntegerCorrectly() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.multiply(3);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void divide_shouldDivideFractionsCorrectly() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 3);
        Fraction result = f1.divide(f2);
        assertEquals(3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    void divide_shouldDivideByIntegerCorrectly() {
        Fraction f = new Fraction(3, 2);
        Fraction result = f.divide(3);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void divide_shouldThrowWhenDividingByZero() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(0, 1);
        Exception exception = assertThrows(MathArithmeticException.class, () -> {
            f1.divide(f2);
        });
        assertEquals(LocalizedFormats.ZERO_FRACTION_TO_DIVIDE_BY, exception.getMessage());
    }

    // Testes para operações unárias
    @Test
    void abs_shouldReturnAbsoluteValue() {
        Fraction f = new Fraction(-1, 2);
        Fraction result = f.abs();
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void abs_shouldReturnSameFractionWhenPositive() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.abs();
        assertSame(f, result);
    }

    @Test
    void negate_shouldNegateFraction() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.negate();
        assertEquals(-1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void negate_shouldThrowWhenNumeratorIsMinValue() {
        Fraction f = new Fraction(Integer.MIN_VALUE, 1);
        assertThrows(MathArithmeticException.class, f::negate);
    }

    @Test
    void reciprocal_shouldReturnReciprocalFraction() {
        Fraction f = new Fraction(2, 3);
        Fraction result = f.reciprocal();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void reciprocal_shouldThrowWhenNumeratorIsZero() {
        Fraction f = new Fraction(0, 1);
        assertThrows(MathArithmeticException.class, f::reciprocal);
    }

    // Testes para comparações
    @Test
    void compareTo_shouldReturnZeroForEqualFractions() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        assertEquals(0, f1.compareTo(f2));
    }

    @Test
    void compareTo_shouldReturnPositiveWhenGreater() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        assertTrue(f1.compareTo(f2) > 0);
    }

    @Test
    void compareTo_shouldReturnNegativeWhenLess() {
        Fraction f1 = new Fraction(1, 3);
        Fraction f2 = new Fraction(1, 2);
        assertTrue(f1.compareTo(f2) < 0);
    }

    // Testes para equals e hashCode
    @Test
    void equals_shouldReturnTrueForEqualFractions() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        assertTrue(f1.equals(f2));
    }

    @Test
    void equals_shouldReturnFalseForDifferentFractions() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        assertFalse(f1.equals(f2));
    }

    @Test
    void equals_shouldReturnFalseForNonFractionObjects() {
        Fraction f = new Fraction(1, 2);
        assertFalse(f.equals("not a fraction"));
    }

    @Test
    void hashCode_shouldReturnSameValueForEqualFractions() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    // Testes para conversões
    @Test
    void doubleValue_shouldReturnCorrectDouble() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0.5, f.doubleValue(), 0.00001);
    }

    @Test
    void floatValue_shouldReturnCorrectFloat() {
        Fraction f = new Fraction(1, 3);
        assertEquals(0.33333f, f.floatValue(), 0.00001f);
    }

    @Test
    void intValue_shouldReturnTruncatedValue() {
        Fraction f = new Fraction(3, 2);
        assertEquals(1, f.intValue());
    }

    @Test
    void longValue_shouldReturnTruncatedValue() {
        Fraction f = new Fraction(Long.MAX_VALUE / 2, 1);
        assertEquals(Long.MAX_VALUE / 2, f.longValue());
    }

    @Test
    void percentageValue_shouldReturnPercentage() {
        Fraction f = new Fraction(1, 2);
        assertEquals(50.0, f.percentageValue(), 0.00001);
    }

    // Testes para constantes
    @Test
    void constants_shouldHaveCorrectValues() {
        assertEquals(0, Fraction.ZERO.getNumerator());
        assertEquals(1, Fraction.ZERO.getDenominator());
        
        assertEquals(1, Fraction.ONE.getNumerator());
        assertEquals(1, Fraction.ONE.getDenominator());
        
        assertEquals(1, Fraction.ONE_HALF.getNumerator());
        assertEquals(2, Fraction.ONE_HALF.getDenominator());
    }

    // Testes para getReducedFraction
    @Test
    void getReducedFraction_shouldReduceFraction() {
        Fraction f = Fraction.getReducedFraction(2, 4);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    void getReducedFraction_shouldHandleNegativeDenominator() {
        Fraction f = Fraction.getReducedFraction(3, -9);
        assertEquals(-1, f.getNumerator());
        assertEquals(3, f.getDenominator());
    }

    @Test
    void getReducedFraction_shouldThrowWhenDenominatorZero() {
        assertThrows(MathArithmeticException.class, () -> {
            Fraction.getReducedFraction(1, 0);
        });
    }

    // Testes para toString
    @Test
    void toString_shouldReturnNumeratorWhenDenominatorOne() {
        Fraction f = new Fraction(5, 1);
        assertEquals("5", f.toString());
    }

    @Test
    void toString_shouldReturnFractionFormat() {
        Fraction f = new Fraction(3, 4);
        assertEquals("3 / 4", f.toString());
    }

    @Test
    void toString_shouldReturnZeroForZeroNumerator() {
        Fraction f = new Fraction(0, 1);
        assertEquals("0", f.toString());
    }

    // Testes para edge cases e mutantes
    @Test
    void constructor_shouldHandleIntegerMinValue() {
        Fraction f = new Fraction(Integer.MIN_VALUE, 1);
        assertEquals(Integer.MIN_VALUE, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    void add_shouldHandleOverflow() {
        Fraction f1 = new Fraction(Integer.MAX_VALUE - 1, 1);
        Fraction f2 = new Fraction(1, 1);
        assertThrows(MathArithmeticException.class, () -> f1.add(f2));
    }

    @Test
    void multiply_shouldHandleOverflow() {
        Fraction f1 = new Fraction(Integer.MAX_VALUE / 2 + 1, 1);
        Fraction f2 = new Fraction(2, 1);
        assertThrows(MathArithmeticException.class, () -> f1.multiply(f2));
    }

    @ParameterizedTest
    @CsvSource({
        "1, 2, 0.5",
        "3, 4, 0.75",
        "5, 8, 0.625",
        "1, 3, 0.3333333333333333"
    })
    void doubleValue_shouldReturnCorrectValues(int num, int den, double expected) {
        Fraction f = new Fraction(num, den);
        assertEquals(expected, f.doubleValue(), 0.0000001);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.5, 1.0, -0.5, 0.333333, 0.666667, 0.25, 0.75})
    void constructorDouble_shouldCreateCorrectFraction(double value) {
        Fraction f = new Fraction(value);
        assertEquals(value, f.doubleValue(), 0.00001);
    }
}
