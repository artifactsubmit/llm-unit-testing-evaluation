package experimento.chatgpt.range;

import experimento.deepseek.range.Range;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT4RangeTest {

    @Test
    void testOfWithNaturalOrderingCreatesCorrectRange() {
        Range<Integer> range = Range.of(5, 10);
        assertEquals(5, range.getMinimum());
        assertEquals(10, range.getMaximum());
        assertTrue(range.isNaturalOrdering());
    }

    @Test
    void testOfWithReversedOrderSwapsValues() {
        Range<Integer> range = Range.of(10, 5);
        assertEquals(5, range.getMinimum());
        assertEquals(10, range.getMaximum());
    }

    @Test
    void testOfWithComparatorCreatesCorrectRange() {
        Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;
        Range<String> range = Range.of("b", "A", comparator);
        assertEquals("A", range.getMinimum());
        assertEquals("b", range.getMaximum());
        assertSame(comparator, range.getComparator());
    }

    @Test
    void testIsCreatesRangeWithSameMinMax() {
        Range<Integer> range = Range.is(42);
        assertEquals(42, range.getMinimum());
        assertEquals(42, range.getMaximum());
    }

    @Test
    void testContainsElementInsideRange() {
        Range<Integer> range = Range.of(10, 20);
        assertTrue(range.contains(15));
    }

    @Test
    void testContainsElementOnBounds() {
        Range<Integer> range = Range.of(10, 20);
        assertTrue(range.contains(10));
        assertTrue(range.contains(20));
    }

    @Test
    void testContainsElementOutsideRange() {
        Range<Integer> range = Range.of(10, 20);
        assertFalse(range.contains(9));
        assertFalse(range.contains(21));
    }

    @Test
    void testContainsNullReturnsFalse() {
        Range<Integer> range = Range.of(1, 5);
        assertFalse(range.contains(null));
    }

    @Test
    void testContainsRangeFullyInside() {
        Range<Integer> outer = Range.of(1, 10);
        Range<Integer> inner = Range.of(3, 7);
        assertTrue(outer.containsRange(inner));
    }

    @Test
    void testContainsRangePartiallyOutsideReturnsFalse() {
        Range<Integer> outer = Range.of(1, 10);
        Range<Integer> inner = Range.of(9, 11);
        assertFalse(outer.containsRange(inner));
    }

    @Test
    void testElementCompareToReturnsCorrectValues() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals(-1, range.elementCompareTo(5));
        assertEquals(0, range.elementCompareTo(15));
        assertEquals(1, range.elementCompareTo(25));
    }

    @Test
    void testFitReturnsBoundedValues() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals(10, range.fit(5));
        assertEquals(15, range.fit(15));
        assertEquals(20, range.fit(25));
    }

    @Test
    void testIsAfterAndIsBefore() {
        Range<Integer> range = Range.of(10, 20);
        assertTrue(range.isAfter(5));
        assertFalse(range.isAfter(10));
        assertTrue(range.isBefore(25));
        assertFalse(range.isBefore(20));
    }

    @Test
    void testIsAfterRangeAndIsBeforeRange() {
        Range<Integer> range1 = Range.of(10, 20);
        Range<Integer> range2 = Range.of(1, 5);
        Range<Integer> range3 = Range.of(25, 30);
        assertTrue(range1.isAfterRange(range2));
        assertTrue(range1.isBeforeRange(range3));
    }

    @Test
    void testIsStartedByAndIsEndedBy() {
        Range<Integer> range = Range.of(10, 20);
        assertTrue(range.isStartedBy(10));
        assertTrue(range.isEndedBy(20));
        assertFalse(range.isStartedBy(15));
        assertFalse(range.isEndedBy(15));
    }

    @Test
    void testIsOverlappedByWithOverlap() {
        Range<Integer> range1 = Range.of(10, 20);
        Range<Integer> range2 = Range.of(15, 25);
        assertTrue(range1.isOverlappedBy(range2));
    }

    @Test
    void testIsOverlappedByWithoutOverlap() {
        Range<Integer> range1 = Range.of(10, 15);
        Range<Integer> range2 = Range.of(16, 20);
        assertFalse(range1.isOverlappedBy(range2));
    }

    @Test
    void testIntersectionWithOverlappingRange() {
        Range<Integer> range1 = Range.of(10, 20);
        Range<Integer> range2 = Range.of(15, 25);
        Range<Integer> intersection = range1.intersectionWith(range2);
        assertEquals(15, intersection.getMinimum());
        assertEquals(20, intersection.getMaximum());
    }

    @Test
    void testIntersectionWithNonOverlappingThrows() {
        Range<Integer> range1 = Range.of(10, 15);
        Range<Integer> range2 = Range.of(16, 20);
        assertThrows(IllegalArgumentException.class, () -> range1.intersectionWith(range2));
    }

    @Test
    void testEqualsAndHashCode() {
        Range<Integer> range1 = Range.of(10, 20);
        Range<Integer> range2 = Range.of(10, 20);
        assertEquals(range1, range2);
        assertEquals(range1.hashCode(), range2.hashCode());
    }

    @Test
    void testToStringDefaultFormat() {
        Range<Integer> range = Range.of(5, 10);
        assertEquals("[5..10]", range.toString());
    }

    @Test
    void testToStringWithCustomFormat() {
        Range<Integer> range = Range.of(5, 10);
        String formatted = range.toString("From %1$s to %2$s with comparator %3$s");
        assertTrue(formatted.contains("From 5 to 10"));
    }
}

