package experimento.chatgpt.range;

import experimento.deepseek.range.Range;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT1RangeTest {

    @Test
    void of_withValidComparableElements_shouldReturnCorrectMinAndMax() {
        Range<Integer> range = Range.of(5, 10);
        assertEquals(5, range.getMinimum());
        assertEquals(10, range.getMaximum());
    }

    @Test
    void of_withSwappedOrder_shouldStillAssignMinAndMaxCorrectly() {
        Range<Integer> range = Range.of(10, 5);
        assertEquals(5, range.getMinimum());
        assertEquals(10, range.getMaximum());
    }

    @Test
    void of_withNullElement_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> Range.of(null, 5));
        assertThrows(NullPointerException.class, () -> Range.of(5, null));
    }

    @Test
    void is_shouldCreateSingletonRange() {
        Range<String> range = Range.is("X");
        assertEquals("X", range.getMinimum());
        assertEquals("X", range.getMaximum());
        assertTrue(range.contains("X"));
        assertFalse(range.contains("Y"));
    }

    @Test
    void contains_shouldReturnTrueForValuesWithinRange() {
        Range<Integer> range = Range.of(1, 5);
        assertTrue(range.contains(1));
        assertTrue(range.contains(3));
        assertTrue(range.contains(5));
    }

    @Test
    void contains_shouldReturnFalseForValuesOutsideRange() {
        Range<Integer> range = Range.of(1, 5);
        assertFalse(range.contains(0));
        assertFalse(range.contains(6));
        assertFalse(range.contains(null));
    }

    @Test
    void containsRange_shouldReturnTrueIfOtherIsWithinRange() {
        Range<Integer> base = Range.of(1, 10);
        Range<Integer> inner = Range.of(3, 7);
        assertTrue(base.containsRange(inner));
    }

    @Test
    void containsRange_shouldReturnFalseIfNotContained() {
        Range<Integer> base = Range.of(1, 5);
        Range<Integer> outside = Range.of(6, 8);
        assertFalse(base.containsRange(outside));
    }

    @Test
    void elementCompareTo_shouldReturnCorrectValues() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals(-1, range.elementCompareTo(5));  // before
        assertEquals(0, range.elementCompareTo(15));  // inside
        assertEquals(1, range.elementCompareTo(25));  // after
    }

    @Test
    void fit_shouldClampToBoundariesWhenOutside() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals(10, range.fit(5));
        assertEquals(20, range.fit(25));
        assertEquals(15, range.fit(15));
    }

    @Test
    void intersectionWith_shouldReturnCorrectIntersection() {
        Range<Integer> r1 = Range.of(10, 30);
        Range<Integer> r2 = Range.of(20, 40);
        Range<Integer> expected = Range.of(20, 30);
        assertEquals(expected, r1.intersectionWith(r2));
    }

    @Test
    void intersectionWith_shouldThrowExceptionWhenDisjoint() {
        Range<Integer> r1 = Range.of(1, 5);
        Range<Integer> r2 = Range.of(6, 10);
        assertThrows(IllegalArgumentException.class, () -> r1.intersectionWith(r2));
    }

    @Test
    void isAfter_shouldBehaveAsExpected() {
        Range<Integer> range = Range.of(10, 20);
        assertTrue(range.isAfter(5));
        assertFalse(range.isAfter(15));
        assertFalse(range.isAfter(25));
    }

    @Test
    void isBefore_shouldBehaveAsExpected() {
        Range<Integer> range = Range.of(10, 20);
        assertTrue(range.isBefore(25));
        assertFalse(range.isBefore(10));
        assertFalse(range.isBefore(5));
    }

    @Test
    void isStartedByAndEndedBy_shouldBehaveCorrectly() {
        Range<Integer> range = Range.of(1, 5);
        assertTrue(range.isStartedBy(1));
        assertFalse(range.isStartedBy(2));
        assertTrue(range.isEndedBy(5));
        assertFalse(range.isEndedBy(4));
    }

    @Test
    void equalsAndHashCode_shouldConsiderSameMinAndMaxEqual() {
        Range<Integer> r1 = Range.of(1, 5);
        Range<Integer> r2 = Range.of(1, 5);
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void equals_shouldReturnFalseForDifferentRanges() {
        Range<Integer> r1 = Range.of(1, 5);
        Range<Integer> r2 = Range.of(2, 5);
        assertNotEquals(r1, r2);
    }

    @Test
    void isNaturalOrdering_shouldReturnTrueForDefaultOrdering() {
        Range<Integer> range = Range.of(1, 2);
        assertTrue(range.isNaturalOrdering());
    }

    @Test
    void of_withCustomComparator_shouldRespectComparatorLogic() {
        Comparator<String> reverse = Comparator.reverseOrder();
        Range<String> range = Range.of("a", "z", reverse);
        assertEquals("z", range.getMinimum()); // because comparator is reversed
        assertEquals("a", range.getMaximum());
    }

    @Test
    void toString_shouldReturnFormattedRange() {
        Range<Integer> range = Range.of(10, 20);
        assertEquals("[10..20]", range.toString());
    }

    @Test
    void toString_withFormat_shouldReturnFormattedString() {
        Range<Integer> range = Range.of(10, 20);
        String formatted = range.toString("Range from %1$s to %2$s using %3$s");
        assertTrue(formatted.contains("10"));
        assertTrue(formatted.contains("20"));
    }
}

