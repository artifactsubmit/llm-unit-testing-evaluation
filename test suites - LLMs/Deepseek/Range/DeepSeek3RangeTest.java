package experimento.deepseek.range;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DeepSeek3RangeTest {

    // Testes para métodos de criação/fábrica

    @Test
    void between_shouldCreateRangeWithNaturalOrdering() {
        Range<Integer> range = Range.between(10, 20);
        assertEquals(10, range.getMinimum());
        assertEquals(20, range.getMaximum());
        assertTrue(range.isNaturalOrdering());
    }

    @Test
    void between_shouldCreateRangeWithComparator() {
        Comparator<String> lengthComparator = Comparator.comparingInt(String::length);
        Range<String> range = Range.between("a", "zz", lengthComparator);
        assertEquals("a", range.getMinimum());
        assertEquals("zz", range.getMaximum());
        assertSame(lengthComparator, range.getComparator());
    }

    @Test
    void between_shouldSwapElementsWhenNeeded() {
        Range<Integer> range = Range.between(20, 10);
        assertEquals(10, range.getMinimum());
        assertEquals(20, range.getMaximum());
    }

    @Test
    void between_shouldThrowNPEWhenFromIsNull() {
        assertThrows(NullPointerException.class, () -> Range.between(null, 10));
    }

    @Test
    void between_shouldThrowNPEWhenToIsNull() {
        assertThrows(NullPointerException.class, () -> Range.between(10, null));
    }

    @Test
    void is_shouldCreateSingleElementRange() {
        Range<Integer> range = Range.is(15);
        assertEquals(15, range.getMinimum());
        assertEquals(15, range.getMaximum());
    }

    @Test
    void of_shouldCreateRangeWithNaturalOrdering() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals(10, range.getMinimum());
        assertEquals(20, range.getMaximum());
        assertTrue(range.isNaturalOrdering());
    }

    // Testes para métodos de consulta

    @ParameterizedTest
    @ValueSource(ints = {10, 15, 20})
    void contains_shouldReturnTrueForElementsInRange(int value) {
        Range<Integer> range = Range.between(10, 20);
        assertTrue(range.contains(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {9, 21})
    void contains_shouldReturnFalseForElementsOutsideRange(int value) {
        Range<Integer> range = Range.between(10, 20);
        assertFalse(range.contains(value));
    }

    @Test
    void contains_shouldReturnFalseForNull() {
        Range<Integer> range = Range.between(10, 20);
        assertFalse(range.contains(null));
    }

    @Test
    void containsRange_shouldReturnTrueForContainedRange() {
        Range<Integer> outer = Range.between(10, 30);
        Range<Integer> inner = Range.between(15, 25);
        assertTrue(outer.containsRange(inner));
    }

    @Test
    void containsRange_shouldReturnFalseForNonContainedRange() {
        Range<Integer> range1 = Range.between(10, 20);
        Range<Integer> range2 = Range.between(15, 25);
        assertFalse(range1.containsRange(range2));
    }

    @Test
    void containsRange_shouldReturnFalseForNull() {
        Range<Integer> range = Range.between(10, 20);
        assertFalse(range.containsRange(null));
    }

    @Test
    void elementCompareTo_shouldReturnZeroForContainedElement() {
        Range<Integer> range = Range.between(10, 20);
        assertEquals(0, range.elementCompareTo(15));
    }

    @Test
    void elementCompareTo_shouldReturnNegativeOneForElementAfterRange() {
        Range<Integer> range = Range.between(10, 20);
        assertEquals(-1, range.elementCompareTo(5));
    }

    @Test
    void elementCompareTo_shouldReturnOneForElementBeforeRange() {
        Range<Integer> range = Range.between(10, 20);
        assertEquals(1, range.elementCompareTo(25));
    }

    @Test
    void elementCompareTo_shouldThrowNPEForNull() {
        Range<Integer> range = Range.between(10, 20);
        assertThrows(NullPointerException.class, () -> range.elementCompareTo(null));
    }

    @Test
    void fit_shouldReturnElementWhenInRange() {
        Range<Integer> range = Range.between(10, 20);
        assertEquals(15, range.fit(15));
    }

    @Test
    void fit_shouldReturnMinimumWhenBelowRange() {
        Range<Integer> range = Range.between(10, 20);
        assertEquals(10, range.fit(5));
    }

    @Test
    void fit_shouldReturnMaximumWhenAboveRange() {
        Range<Integer> range = Range.between(10, 20);
        assertEquals(20, range.fit(25));
    }

    @Test
    void fit_shouldThrowNPEForNull() {
        Range<Integer> range = Range.between(10, 20);
        assertThrows(NullPointerException.class, () -> range.fit(null));
    }

    // Testes para métodos de comparação

    @Test
    void isAfter_shouldReturnTrueForElementBeforeMinimum() {
        Range<Integer> range = Range.between(10, 20);
        assertTrue(range.isAfter(5));
    }

    @Test
    void isAfter_shouldReturnFalseForElementInRange() {
        Range<Integer> range = Range.between(10, 20);
        assertFalse(range.isAfter(15));
    }

    @Test
    void isAfter_shouldReturnFalseForNull() {
        Range<Integer> range = Range.between(10, 20);
        assertFalse(range.isAfter(null));
    }

    @Test
    void isAfterRange_shouldReturnTrueForRangeBeforeThis() {
        Range<Integer> range1 = Range.between(10, 20);
        Range<Integer> range2 = Range.between(1, 5);
        assertTrue(range1.isAfterRange(range2));
    }

    @Test
    void isBefore_shouldReturnTrueForElementAfterMaximum() {
        Range<Integer> range = Range.between(10, 20);
        assertTrue(range.isBefore(25));
    }

    @Test
    void isBefore_shouldReturnFalseForElementInRange() {
        Range<Integer> range = Range.between(10, 20);
        assertFalse(range.isBefore(15));
    }

    @Test
    void isBefore_shouldReturnFalseForNull() {
        Range<Integer> range = Range.between(10, 20);
        assertFalse(range.isBefore(null));
    }

    @Test
    void isBeforeRange_shouldReturnTrueForRangeAfterThis() {
        Range<Integer> range1 = Range.between(10, 20);
        Range<Integer> range2 = Range.between(25, 30);
        assertTrue(range1.isBeforeRange(range2));
    }

    @Test
    void isEndedBy_shouldReturnTrueForMaximumElement() {
        Range<Integer> range = Range.between(10, 20);
        assertTrue(range.isEndedBy(20));
    }

    @Test
    void isEndedBy_shouldReturnFalseForNonMaximumElement() {
        Range<Integer> range = Range.between(10, 20);
        assertFalse(range.isEndedBy(15));
    }

    @Test
    void isEndedBy_shouldReturnFalseForNull() {
        Range<Integer> range = Range.between(10, 20);
        assertFalse(range.isEndedBy(null));
    }

    @Test
    void isNaturalOrdering_shouldReturnTrueForNaturalOrdering() {
        Range<Integer> range = Range.between(10, 20);
        assertTrue(range.isNaturalOrdering());
    }

    @Test
    void isNaturalOrdering_shouldReturnFalseForCustomComparator() {
        Comparator<String> lengthComparator = Comparator.comparingInt(String::length);
        Range<String> range = Range.between("a", "zz", lengthComparator);
        assertFalse(range.isNaturalOrdering());
    }

    @Test
    void isOverlappedBy_shouldReturnTrueForOverlappingRange() {
        Range<Integer> range1 = Range.between(10, 20);
        Range<Integer> range2 = Range.between(15, 25);
        assertTrue(range1.isOverlappedBy(range2));
    }

    @Test
    void isOverlappedBy_shouldReturnFalseForNonOverlappingRange() {
        Range<Integer> range1 = Range.between(10, 20);
        Range<Integer> range2 = Range.between(25, 30);
        assertFalse(range1.isOverlappedBy(range2));
    }

    @Test
    void isOverlappedBy_shouldReturnFalseForNull() {
        Range<Integer> range = Range.between(10, 20);
        assertFalse(range.isOverlappedBy(null));
    }

    @Test
    void isStartedBy_shouldReturnTrueForMinimumElement() {
        Range<Integer> range = Range.between(10, 20);
        assertTrue(range.isStartedBy(10));
    }

    @Test
    void isStartedBy_shouldReturnFalseForNonMinimumElement() {
        Range<Integer> range = Range.between(10, 20);
        assertFalse(range.isStartedBy(15));
    }

    @Test
    void isStartedBy_shouldReturnFalseForNull() {
        Range<Integer> range = Range.between(10, 20);
        assertFalse(range.isStartedBy(null));
    }

    // Testes para intersectionWith

    @Test
    void intersectionWith_shouldReturnIntersectionForOverlappingRanges() {
        Range<Integer> range1 = Range.between(10, 20);
        Range<Integer> range2 = Range.between(15, 25);
        Range<Integer> intersection = range1.intersectionWith(range2);
        assertEquals(15, intersection.getMinimum());
        assertEquals(20, intersection.getMaximum());
    }

    @Test
    void intersectionWith_shouldReturnSameRangeForEqualRanges() {
        Range<Integer> range1 = Range.between(10, 20);
        Range<Integer> range2 = Range.between(10, 20);
        Range<Integer> intersection = range1.intersectionWith(range2);
        assertSame(range1, intersection);
    }

    @Test
    void intersectionWith_shouldThrowForNonOverlappingRanges() {
        Range<Integer> range1 = Range.between(10, 20);
        Range<Integer> range2 = Range.between(25, 30);
        assertThrows(IllegalArgumentException.class, () -> range1.intersectionWith(range2));
    }

    // Testes para equals e hashCode

    @Test
    void equals_shouldReturnTrueForSameRange() {
        Range<Integer> range1 = Range.between(10, 20);
        Range<Integer> range2 = Range.between(10, 20);
        assertEquals(range1, range2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentRanges() {
        Range<Integer> range1 = Range.between(10, 20);
        Range<Integer> range2 = Range.between(15, 25);
        assertNotEquals(range1, range2);
    }

    @Test
    void equals_shouldReturnFalseForNull() {
        Range<Integer> range = Range.between(10, 20);
        assertNotEquals(null, range);
    }

    @Test
    void equals_shouldReturnFalseForDifferentClass() {
        Range<Integer> range = Range.between(10, 20);
        assertNotEquals("not a range", range);
    }

    @Test
    void hashCode_shouldBeConsistent() {
        Range<Integer> range = Range.between(10, 20);
        int initialHashCode = range.hashCode();
        assertEquals(initialHashCode, range.hashCode());
    }

    @Test
    void hashCode_shouldBeEqualForEqualRanges() {
        Range<Integer> range1 = Range.between(10, 20);
        Range<Integer> range2 = Range.between(10, 20);
        assertEquals(range1.hashCode(), range2.hashCode());
    }

    // Testes para toString

    @Test
    void toString_shouldReturnCorrectFormat() {
        Range<Integer> range = Range.between(10, 20);
        assertEquals("[10..20]", range.toString());
    }

    @Test
    void toString_withFormat_shouldFormatCorrectly() {
        Range<Integer> range = Range.between(10, 20);
        String formatted = range.toString("From %1$s to %2$s with comparator %3$s");
        assertTrue(formatted.startsWith("From 10 to 20 with comparator"));
    }

    // Testes com comparador customizado

    @Test
    void withCustomComparator_shouldOrderCorrectly() {
        Comparator<String> lengthComparator = Comparator.comparingInt(String::length);
        Range<String> range = Range.between("a", "zzzz", lengthComparator);
        
        assertTrue(range.contains("bb"));
        assertFalse(range.contains(""));
        assertFalse(range.contains("aaaaa"));
    }

    // Testes de borda/canto

    @Test
    void singleElementRange_shouldContainOnlyThatElement() {
        Range<Integer> range = Range.is(15);
        assertTrue(range.contains(15));
        assertFalse(range.contains(14));
        assertFalse(range.contains(16));
    }

    @ParameterizedTest
    @CsvSource({
        "10, 20, 15, 15",
        "10, 20, 5, 10",
        "10, 20, 25, 20"
    })
    void fit_shouldCorrectlyFitValues(int min, int max, int input, int expected) {
        Range<Integer> range = Range.between(min, max);
        assertEquals(expected, range.fit(input));
    }

    @Test
    void rangeWithNullComparator_shouldUseNaturalOrdering() {
        Range<Integer> range = Range.between(10, 20, null);
        assertTrue(range.isNaturalOrdering());
    }
}
