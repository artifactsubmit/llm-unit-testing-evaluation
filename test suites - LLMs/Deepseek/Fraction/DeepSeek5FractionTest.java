package experimento.deepseek.fraction;

import static org.junit.jupiter.api.Assertions.*;

import experimento.Fraction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;

class DeepSeek5FractionTest {

    // Testes para construtores
    @Test
    void constructorInt_shouldCreateFractionWithDenominatorOne() {
        Fraction f = new Fraction(5);
        assertEquals(5, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    void constructorIntInt_shouldCreateReducedFraction() {
        Fraction f = new Fraction(6, 8);
        assertEquals(3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    void constructorIntInt_shouldHandleNegativeDenominator() {
        Fraction f = new Fraction(3, -4);
        assertEquals(-3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    void constructorIntInt_shouldThrowWhenDenominatorZero() {
        assertThrows(MathArithmeticException.class, () -> new Fraction(1, 0));
    }

    @Test
    void constructorIntInt_shouldHandleMinValueEdgeCases() {
        assertThrows(MathArithmeticException.class, () -> new Fraction(Integer.MIN_VALUE, Integer.MIN_VALUE));
        Fraction f = new Fraction(Integer.MIN_VALUE/2, Integer.MIN_VALUE);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    void constructorDouble_shouldCreateExactFractionForIntegers() {
        Fraction f = new Fraction(5.0);
        assertEquals(5, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    void constructorDoubleEpsilonMaxIterations_shouldCreateApproximateFraction() {
        Fraction f = new Fraction(0.3333333333, 0.000001, 100);
        assertEquals(1, f.getNumerator());
        assertEquals(3, f.getDenominator());
    }

    @Test
    void constructorDoubleMaxDenominator_shouldCreateFractionWithLimitedDenominator() {
        Fraction f = new Fraction(0.333333, 100);
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
    void add_shouldThrowWhenNullArgument() {
        Fraction f = new Fraction(1, 2);
        assertThrows(NullArgumentException.class, () -> f.add(null));
    }

    @Test
    void add_shouldHandleOverflow() {
        Fraction f1 = new Fraction(Integer.MAX_VALUE - 1, 1);
        Fraction f2 = new Fraction(1, 1);
        assertThrows(MathArithmeticException.class, () -> f1.add(f2));
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
    void multiply_shouldMultiplyFractionsCorrectly() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 3);
        Fraction result = f1.multiply(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    void multiply_shouldReturnZeroWhenMultiplyingByZero() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = Fraction.ZERO;
        assertEquals(Fraction.ZERO, f1.multiply(f2));
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
    void divide_shouldThrowWhenDividingByZero() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = Fraction.ZERO;
        assertThrows(MathArithmeticException.class, () -> f1.divide(f2));
    }

    @Test
    void reciprocal_shouldReturnReciprocalFraction() {
        Fraction f = new Fraction(2, 3);
        Fraction result = f.reciprocal();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void reciprocal_shouldThrowWhenNumeratorZero() {
        Fraction f = Fraction.ZERO;
        assertThrows(MathArithmeticException.class, () -> f.reciprocal());
    }

    // Testes para operações de comparação
    @Test
    void compareTo_shouldReturnZeroForEqualFractions() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        assertEquals(0, f1.compareTo(f2));
    }

    @Test
    void compareTo_shouldReturnNegativeWhenThisIsSmaller() {
        Fraction f1 = new Fraction(1, 3);
        Fraction f2 = new Fraction(1, 2);
        assertTrue(f1.compareTo(f2) < 0);
    }

    @Test
    void compareTo_shouldReturnPositiveWhenThisIsLarger() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        assertTrue(f1.compareTo(f2) > 0);
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

    // Testes para métodos de conversão
    @Test
    void doubleValue_shouldReturnCorrectDoubleValue() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0.5, f.doubleValue(), 0.00001);
    }

    @Test
    void floatValue_shouldReturnCorrectFloatValue() {
        Fraction f = new Fraction(1, 3);
        assertEquals(0.33333f, f.floatValue(), 0.00001f);
    }

    @Test
    void intValue_shouldReturnTruncatedValue() {
        Fraction f = new Fraction(5, 2);
        assertEquals(2, f.intValue());
    }

    @Test
    void longValue_shouldReturnTruncatedValue() {
        Fraction f = new Fraction(5, 2);
        assertEquals(2L, f.longValue());
    }

    @Test
    void percentageValue_shouldReturnPercentage() {
        Fraction f = new Fraction(1, 2);
        assertEquals(50.0, f.percentageValue(), 0.00001);
    }

    // Testes para métodos utilitários
    @Test
    void abs_shouldReturnAbsoluteValue() {
        Fraction f = new Fraction(-1, 2);
        Fraction result = f.abs();
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void negate_shouldReturnNegatedValue() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.negate();
        assertEquals(-1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void negate_shouldThrowWhenNumeratorIsMinValue() {
        Fraction f = new Fraction(Integer.MIN_VALUE, 1);
        assertThrows(MathArithmeticException.class, () -> f.negate());
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
        assertThrows(MathArithmeticException.class, () -> Fraction.getReducedFraction(1, 0));
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
        assertEquals("0", Fraction.ZERO.toString());
    }

    // Testes parametrizados para cobrir mais casos
    @ParameterizedTest
    @CsvSource({
        "1, 2, 1, 3, 5, 6",    // 1/2 + 1/3 = 5/6
        "1, 4, 1, 4, 1, 2",     // 1/4 + 1/4 = 1/2
        "-1, 2, 1, 2, 0, 1"     // -1/2 + 1/2 = 0
    })
    void add_shouldHandleVariousCases(int num1, int den1, int num2, int den2, int expectedNum, int expectedDen) {
        Fraction f1 = new Fraction(num1, den1);
        Fraction f2 = new Fraction(num2, den2);
        Fraction result = f1.add(f2);
        assertEquals(expectedNum, result.getNumerator());
        assertEquals(expectedDen, result.getDenominator());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.5, 0.333333, 0.25, 0.2, 0.166666})
    void constructorDouble_shouldCreateCorrectFraction(double value) {
        Fraction f = new Fraction(value);
        assertEquals(value, f.doubleValue(), 0.00001);
    }
}
