package experimento.chatgpt.range;

import experimento.deepseek.range.Range;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT2RangeTest {

    @Test
    void testOfWithNaturalOrderingShouldSetCorrectMinAndMax() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals(10, range.getMinimum());
        assertEquals(20, range.getMaximum());
        assertTrue(range.isNaturalOrdering());
    }

    @Test
    void testOfWithInvertedOrderShouldSwapMinAndMax() {
        Range<Integer> range = Range.of(20, 10);
        assertEquals(10, range.getMinimum());
        assertEquals(20, range.getMaximum());
    }

    @Test
    void testIsMethodCreatesSingleValueRange() {
        Range<String> range = Range.is("abc");
        assertEquals("abc", range.getMinimum());
        assertEquals("abc", range.getMaximum());
        assertTrue(range.contains("abc"));
        assertFalse(range.contains("def"));
    }

    @Test
    void testContainsElementWithinRange() {
        Range<Integer> range = Range.of(5, 15);
        assertTrue(range.contains(10));
    }

    @Test
    void testContainsElementOnBoundaries() {
        Range<Integer> range = Range.of(5, 15);
        assertTrue(range.contains(5));
        assertTrue(range.contains(15));
    }

    @Test
    void testContainsElementOutsideRange() {
        Range<Integer> range = Range.of(5, 15);
        assertFalse(range.contains(4));
        assertFalse(range.contains(16));
    }

    @Test
    void testContainsNullShouldReturnFalse() {
        Range<Integer> range = Range.of(1, 5);
        assertFalse(range.contains(null));
    }

    @Test
    void testContainsRangeCompletelyWithin() {
        Range<Integer> outer = Range.of(1, 10);
        Range<Integer> inner = Range.of(3, 7);
        assertTrue(outer.containsRange(inner));
    }

    @Test
    void testContainsRangeWithEqualBounds() {
        Range<Integer> a = Range.of(5, 5);
        Range<Integer> b = Range.of(5, 5);
        assertTrue(a.containsRange(b));
    }

    @Test
    void testFitWithElementInsideRange() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals(15, range.fit(15));
    }

    @Test
    void testFitWithElementBelowRange() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals(10, range.fit(5));
    }

    @Test
    void testFitWithElementAboveRange() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals(20, range.fit(25));
    }

    @Test
    void testIsBeforeShouldReturnTrue() {
        Range<Integer> range = Range.of(10, 20);
        assertTrue(range.isBefore(25));
    }

    @Test
    void testIsBeforeShouldReturnFalse() {
        Range<Integer> range = Range.of(10, 20);
        assertFalse(range.isBefore(15));
    }

    @Test
    void testIsAfterShouldReturnTrue() {
        Range<Integer> range = Range.of(10, 20);
        assertTrue(range.isAfter(5));
    }

    @Test
    void testIsAfterShouldReturnFalse() {
        Range<Integer> range = Range.of(10, 20);
        assertFalse(range.isAfter(15));
    }

    @Test
    void testIsStartedByAndIsEndedBy() {
        Range<Integer> range = Range.of(10, 20);
        assertTrue(range.isStartedBy(10));
        assertFalse(range.isStartedBy(15));
        assertTrue(range.isEndedBy(20));
        assertFalse(range.isEndedBy(15));
    }

    @Test
    void testIntersectionWithOverlappingRanges() {
        Range<Integer> r1 = Range.of(5, 15);
        Range<Integer> r2 = Range.of(10, 20);
        Range<Integer> intersection = r1.intersectionWith(r2);
        assertEquals(10, intersection.getMinimum());
        assertEquals(15, intersection.getMaximum());
    }

    @Test
    void testIntersectionWithEqualRangesReturnsSameInstance() {
        Range<Integer> r1 = Range.of(5, 15);
        Range<Integer> intersection = r1.intersectionWith(r1);
        assertSame(r1, intersection);
    }

    @Test
    void testIntersectionWithNonOverlappingThrowsException() {
        Range<Integer> r1 = Range.of(1, 5);
        Range<Integer> r2 = Range.of(6, 10);
        assertThrows(IllegalArgumentException.class, () -> r1.intersectionWith(r2));
    }

    @Test
    void testIsOverlappedByWhenOverlapping() {
        Range<Integer> r1 = Range.of(5, 15);
        Range<Integer> r2 = Range.of(10, 20);
        assertTrue(r1.isOverlappedBy(r2));
    }

    @Test
    void testIsOverlappedByWhenDisjoint() {
        Range<Integer> r1 = Range.of(1, 5);
        Range<Integer> r2 = Range.of(6, 10);
        assertFalse(r1.isOverlappedBy(r2));
    }

    @Test
    void testToStringDefaultFormat() {
        Range<Integer> range = Range.of(1, 10);
        assertEquals("[1..10]", range.toString());
    }

    @Test
    void testToStringCustomFormat() {
        Range<Integer> range = Range.of(1, 10);
        String formatted = range.toString("Start: %1$s | End: %2$s | Comparator: %3$s");
        assertTrue(formatted.contains("Start: 1"));
        assertTrue(formatted.contains("End: 10"));
    }

    @Test
    void testEqualsAndHashCode() {
        Range<Integer> r1 = Range.of(5, 10);
        Range<Integer> r2 = Range.of(5, 10);
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testEqualsShouldReturnFalseForDifferentRange() {
        Range<Integer> r1 = Range.of(5, 10);
        Range<Integer> r2 = Range.of(6, 10);
        assertNotEquals(r1, r2);
    }

    @Test
    void testElementCompareToInsideRange() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals(0, range.elementCompareTo(15));
    }

    @Test
    void testElementCompareToBelowRange() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals(-1, range.elementCompareTo(5));
    }

    @Test
    void testElementCompareToAboveRange() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals(1, range.elementCompareTo(25));
    }

    @Test
    void testComparatorWithCustomLogic() {
        Comparator<String> reverse = Comparator.reverseOrder();
        Range<String> range = Range.of("d", "a", reverse);
        assertEquals("d", range.getMinimum());
        assertEquals("a", range.getMaximum());
        assertSame(reverse, range.getComparator());
    }
}

