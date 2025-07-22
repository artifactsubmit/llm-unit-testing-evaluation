package experimento.chatgpt.range;

import experimento.deepseek.range.Range;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT3RangeTest {

    @Test
    void testOfWithNaturalOrder() {
        Range<Integer> range = Range.of(1, 5);
        assertEquals(1, range.getMinimum());
        assertEquals(5, range.getMaximum());
        assertTrue(range.isNaturalOrdering());
    }

    @Test
    void testOfWithReversedOrder() {
        Range<Integer> range = Range.of(5, 1);
        assertEquals(1, range.getMinimum());
        assertEquals(5, range.getMaximum());
    }

    @Test
    void testOfWithComparator() {
        Comparator<Integer> reverse = Comparator.reverseOrder();
        Range<Integer> range = Range.of(1, 5, reverse);
        assertEquals(1, range.getMaximum()); // com comparador reverso
        assertEquals(5, range.getMinimum());
        assertFalse(range.isNaturalOrdering());
    }

    @Test
    void testIsFactory() {
        Range<Integer> range = Range.is(42);
        assertEquals(42, range.getMinimum());
        assertEquals(42, range.getMaximum());
    }

    @Test
    void testContains() {
        Range<Integer> range = Range.of(10, 20);
        assertTrue(range.contains(10));
        assertTrue(range.contains(15));
        assertTrue(range.contains(20));
        assertFalse(range.contains(25));
        assertFalse(range.contains(null));
    }

    @Test
    void testContainsRange() {
        Range<Integer> range = Range.of(10, 20);
        assertTrue(range.containsRange(Range.of(11, 19)));
        assertTrue(range.containsRange(Range.of(10, 20)));
        assertFalse(range.containsRange(Range.of(5, 15)));
        assertFalse(range.containsRange(null));
    }

    @Test
    void testElementCompareTo() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals(-1, range.elementCompareTo(5));
        assertEquals(0, range.elementCompareTo(15));
        assertEquals(1, range.elementCompareTo(25));
        assertThrows(NullPointerException.class, () -> range.elementCompareTo(null));
    }

    @Test
    void testFit() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals(10, range.fit(5));
        assertEquals(10, range.fit(10));
        assertEquals(15, range.fit(15));
        assertEquals(20, range.fit(20));
        assertEquals(20, range.fit(25));
        assertThrows(NullPointerException.class, () -> range.fit(null));
    }

    @Test
    void testIsAfterAndBefore() {
        Range<Integer> range = Range.of(10, 20);
        assertTrue(range.isAfter(5));
        assertFalse(range.isAfter(10));
        assertFalse(range.isAfter(25));

        assertFalse(range.isBefore(5));
        assertFalse(range.isBefore(20));
        assertTrue(range.isBefore(25));

        assertFalse(range.isAfter(null));
        assertFalse(range.isBefore(null));
    }

    @Test
    void testIsStartedByAndEndedBy() {
        Range<Integer> range = Range.of(10, 20);
        assertTrue(range.isStartedBy(10));
        assertTrue(range.isEndedBy(20));
        assertFalse(range.isStartedBy(9));
        assertFalse(range.isEndedBy(21));
        assertFalse(range.isStartedBy(null));
        assertFalse(range.isEndedBy(null));
    }

    @Test
    void testIsOverlappedBy() {
        Range<Integer> r1 = Range.of(10, 20);
        Range<Integer> r2 = Range.of(15, 25);
        Range<Integer> r3 = Range.of(21, 30);
        assertTrue(r1.isOverlappedBy(r2));
        assertFalse(r1.isOverlappedBy(r3));
        assertFalse(r1.isOverlappedBy(null));
    }

    @Test
    void testIntersectionWithValidOverlap() {
        Range<Integer> r1 = Range.of(10, 20);
        Range<Integer> r2 = Range.of(15, 25);
        Range<Integer> intersection = r1.intersectionWith(r2);
        assertEquals(15, intersection.getMinimum());
        assertEquals(20, intersection.getMaximum());
    }

    @Test
    void testIntersectionWithNoOverlapThrowsException() {
        Range<Integer> r1 = Range.of(10, 20);
        Range<Integer> r2 = Range.of(21, 30);
        assertThrows(IllegalArgumentException.class, () -> r1.intersectionWith(r2));
    }

    @Test
    void testToStringAndFormat() {
        Range<Integer> range = Range.of(3, 7);
        assertEquals("[3..7]", range.toString());
        assertEquals("Min: 3, Max: 7", range.toString("Min: %1$s, Max: %2$s"));
    }

    @Test
    void testEqualsAndHashCode() {
        Range<Integer> r1 = Range.of(1, 10);
        Range<Integer> r2 = Range.of(1, 10);
        Range<Integer> r3 = Range.of(2, 10);

        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testIsBeforeRangeAndIsAfterRange() {
        Range<Integer> r1 = Range.of(10, 20);
        Range<Integer> r2 = Range.of(21, 30);
        Range<Integer> r3 = Range.of(5, 9);

        assertTrue(r1.isBeforeRange(r2));
        assertTrue(r1.isAfterRange(r3));
        assertFalse(r1.isBeforeRange(null));
        assertFalse(r1.isAfterRange(null));
    }
}

