package experimento.deepseek.fraction;

import static org.junit.jupiter.api.Assertions.*;

import experimento.Fraction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;

class DeepSeek1FractionTest {

    // Testes para construtores
    @Test
    void constructorInt_shouldCreateFractionWithDenominatorOne() {
        Fraction f = new Fraction(3);
        assertEquals(3, f.getNumerator());
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
    void constructorDouble_shouldCreateFractionFromDouble() {
        Fraction f = new Fraction(0.5);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    void constructorDoubleEpsilonMaxIterations_shouldCreateApproximateFraction() {
        Fraction f = new Fraction(0.33333, 0.0001, 100);
        assertEquals(1, f.getNumerator());
        assertEquals(3, f.getDenominator());
    }

    @Test
    void constructorDoubleMaxDenominator_shouldCreateFractionWithLimitedDenominator() {
        Fraction f = new Fraction(0.33333, 10);
        assertEquals(1, f.getNumerator());
        assertEquals(3, f.getDenominator());
    }

    // Testes para operações aritméticas
    @Test
    void add_shouldAddTwoFractions() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.add(f2);
        assertEquals(5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    void add_shouldAddIntegerToFraction() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.add(1);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void subtract_shouldSubtractTwoFractions() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.subtract(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    void subtract_shouldSubtractIntegerFromFraction() {
        Fraction f = new Fraction(3, 2);
        Fraction result = f.subtract(1);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void multiply_shouldMultiplyTwoFractions() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 3);
        Fraction result = f1.multiply(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    void multiply_shouldMultiplyFractionByInteger() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.multiply(3);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void divide_shouldDivideTwoFractions() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.divide(f2);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void divide_shouldDivideFractionByInteger() {
        Fraction f = new Fraction(3, 2);
        Fraction result = f.divide(3);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void divide_shouldThrowWhenDividingByZero() {
        Fraction f = new Fraction(1, 2);
        Fraction zero = new Fraction(0, 1);
        assertThrows(MathArithmeticException.class, () -> f.divide(zero));
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
    void negate_shouldNegateFraction() {
        Fraction f = new Fraction(1, 2);
        Fraction result = f.negate();
        assertEquals(-1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void reciprocal_shouldReturnReciprocal() {
        Fraction f = new Fraction(2, 3);
        Fraction result = f.reciprocal();
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    void reciprocal_shouldThrowWhenNumeratorZero() {
        Fraction zero = new Fraction(0, 1);
        assertThrows(MathArithmeticException.class, () -> zero.reciprocal());
    }

    // Testes para conversões
    @Test
    void doubleValue_shouldConvertToDouble() {
        Fraction f = new Fraction(1, 2);
        assertEquals(0.5, f.doubleValue(), 0.0001);
    }

    @Test
    void floatValue_shouldConvertToFloat() {
        Fraction f = new Fraction(1, 3);
        assertEquals(0.3333f, f.floatValue(), 0.0001f);
    }

    @Test
    void intValue_shouldConvertToInt() {
        Fraction f = new Fraction(3, 2);
        assertEquals(1, f.intValue());
    }

    @Test
    void longValue_shouldConvertToLong() {
        Fraction f = new Fraction(Long.MAX_VALUE / 2, 1);
        assertEquals(Long.MAX_VALUE / 2, f.longValue());
    }

    @Test
    void percentageValue_shouldConvertToPercentage() {
        Fraction f = new Fraction(1, 2);
        assertEquals(50.0, f.percentageValue(), 0.0001);
    }

    // Testes para comparação
    @Test
    void compareTo_shouldCompareFractions() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        assertTrue(f1.compareTo(f2) > 0);
        assertTrue(f2.compareTo(f1) < 0);
        assertEquals(0, f1.compareTo(new Fraction(2, 4)));
    }

    @Test
    void equals_shouldTestEquality() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 4);
        Fraction f3 = new Fraction(1, 3);
        assertEquals(f1, f2);
        assertNotEquals(f1, f3);
        assertNotEquals(f1, null);
        assertNotEquals(f1, new Object());
    }

    @Test
    void hashCode_shouldConsistentWithEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 4);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    // Testes para métodos estáticos
    @Test
    void getReducedFraction_shouldReduceFraction() {
        Fraction f = Fraction.getReducedFraction(2, 4);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
    }

    @Test
    void getReducedFraction_shouldHandleNegativeDenominator() {
        Fraction f = Fraction.getReducedFraction(3, -4);
        assertEquals(-3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    // Testes para representação string
    @Test
    void toString_shouldReturnProperRepresentation() {
        assertEquals("1/2", new Fraction(1, 2).toString());
        assertEquals("3", new Fraction(3, 1).toString());
        assertEquals("0", new Fraction(0, 1).toString());
    }

    // Testes para casos limite e exceções
    @Test
    void constructor_shouldHandleIntegerMinValue() {
        assertThrows(MathArithmeticException.class, () -> new Fraction(Integer.MIN_VALUE, -1));
        
        Fraction f = new Fraction(Integer.MIN_VALUE, 1);
        assertEquals(Integer.MIN_VALUE, f.getNumerator());
        assertEquals(1, f.getDenominator());
    }

    @Test
    void add_shouldThrowOnOverflow() {
        Fraction f1 = new Fraction(Integer.MAX_VALUE - 1, 1);
        Fraction f2 = new Fraction(2, 1);
        assertThrows(MathArithmeticException.class, () -> f1.add(f2));
    }

    @Test
    void multiply_shouldThrowOnOverflow() {
        Fraction f1 = new Fraction(Integer.MAX_VALUE / 2 + 1, 1);
        Fraction f2 = new Fraction(2, 1);
        assertThrows(MathArithmeticException.class, () -> f1.multiply(f2));
    }

    // Testes paramétricos para maior cobertura
    @ParameterizedTest
    @CsvSource({
        "1, 2, 1, 3, 5, 6",    // 1/2 + 1/3 = 5/6
        "1, 4, 1, 4, 1, 2",     // 1/4 + 1/4 = 1/2
        "3, 4, -1, 4, 1, 2"     // 3/4 + (-1/4) = 1/2
    })
    void add_parameterizedTests(int num1, int den1, int num2, int den2, int expectedNum, int expectedDen) {
        Fraction f1 = new Fraction(num1, den1);
        Fraction f2 = new Fraction(num2, den2);
        Fraction result = f1.add(f2);
        assertEquals(expectedNum, result.getNumerator());
        assertEquals(expectedDen, result.getDenominator());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.5, 1.0, -0.25, 0.333333, 0.666667})
    void constructorDouble_parameterizedTests(double value) {
        Fraction f = new Fraction(value);
        assertEquals(value, f.doubleValue(), 0.0001);
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

    // Testes para null
    @Test
    void operations_shouldThrowWhenNull() {
        Fraction f = new Fraction(1, 2);
        assertThrows(NullArgumentException.class, () -> f.add(null));
        assertThrows(NullArgumentException.class, () -> f.subtract(null));
        assertThrows(NullArgumentException.class, () -> f.multiply(null));
        assertThrows(NullArgumentException.class, () -> f.divide(null));
    }
}	
