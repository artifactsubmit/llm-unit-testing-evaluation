package experimento.chatgpt.range;

import experimento.deepseek.range.Range;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

public class ChatGPT5RangeTest {

    // Range com ordenação natural (inteiros)
    private final Range<Integer> naturalRange = Range.of(5, 10);

    // Range com Comparator reverso
    private final Range<Integer> customRange = Range.of(5, 10, Comparator.reverseOrder());

    @Test
    void testOfWithNaturalOrdering() {
        assertEquals(5, naturalRange.getMinimum());
        assertEquals(10, naturalRange.getMaximum());
        assertTrue(naturalRange.isNaturalOrdering());
    }

    @Test
    void testOfWithCustomComparator() {
        assertEquals(10, customRange.getMinimum()); // ordem invertida
        assertEquals(5, customRange.getMaximum());
        assertFalse(customRange.isNaturalOrdering());
    }

    @Test
    void testContainsElement() {
        assertTrue(naturalRange.contains(5));
        assertTrue(naturalRange.contains(7));
        assertTrue(naturalRange.contains(10));
        assertFalse(naturalRange.contains(4));
        assertFalse(naturalRange.contains(11));
        assertFalse(naturalRange.contains(null));
    }

    @Test
    void testContainsRange() {
        Range<Integer> inner = Range.of(6, 9);
        assertTrue(naturalRange.containsRange(inner));

        Range<Integer> outer = Range.of(4, 11);
        assertFalse(naturalRange.containsRange(outer));

        assertFalse(naturalRange.containsRange(null));
    }

    @Test
    void testElementCompareTo() {
        assertEquals(-1, naturalRange.elementCompareTo(3));
        assertEquals(0, naturalRange.elementCompareTo(7));
        assertEquals(1, naturalRange.elementCompareTo(15));
    }

    @Test
    void testFit() {
        assertEquals(5, naturalRange.fit(3));
        assertEquals(7, naturalRange.fit(7));
        assertEquals(10, naturalRange.fit(12));
    }

    @Test
    void testIsAfterAndBefore() {
        assertTrue(naturalRange.isAfter(2));
        assertFalse(naturalRange.isAfter(5));
        assertTrue(naturalRange.isBefore(12));
        assertFalse(naturalRange.isBefore(10));
    }

    @Test
    void testIsAfterAndBeforeRange() {
        Range<Integer> before = Range.of(1, 3);
        Range<Integer> after = Range.of(15, 20);

        assertTrue(naturalRange.isAfterRange(before));
        assertTrue(naturalRange.isBeforeRange(after));
        assertFalse(naturalRange.isAfterRange(after));
    }

    @Test
    void testIsStartedByAndEndedBy() {
        assertTrue(naturalRange.isStartedBy(5));
        assertTrue(naturalRange.isEndedBy(10));
        assertFalse(naturalRange.isStartedBy(6));
        assertFalse(naturalRange.isEndedBy(9));
    }

    @Test
    void testIsOverlappedBy() {
        Range<Integer> overlap = Range.of(9, 15);
        Range<Integer> nonOverlap = Range.of(11, 20);

        assertTrue(naturalRange.isOverlappedBy(overlap));
        assertFalse(naturalRange.isOverlappedBy(nonOverlap));
        assertFalse(naturalRange.isOverlappedBy(null));
    }

    @Test
    void testIntersectionWith() {
        Range<Integer> overlap = Range.of(8, 15);
        Range<Integer> result = naturalRange.intersectionWith(overlap);

        assertEquals(8, result.getMinimum());
        assertEquals(10, result.getMaximum());
    }

    @Test
    void testIntersectionWithThrowsException() {
        Range<Integer> nonOverlap = Range.of(11, 20);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            naturalRange.intersectionWith(nonOverlap);
        });
        assertTrue(ex.getMessage().contains("Cannot calculate intersection"));
    }

    @Test
    void testEqualsAndHashCode() {
        Range<Integer> copy = Range.of(5, 10);
        assertEquals(naturalRange, copy);
        assertEquals(naturalRange.hashCode(), copy.hashCode());

        Range<Integer> different = Range.of(6, 10);
        assertNotEquals(naturalRange, different);
    }

    @Test
    void testToStringDefaultAndCustom() {
        assertEquals("[5..10]", naturalRange.toString());
        String customFormat = naturalRange.toString("Range from %1$s to %2$s using %3$s");
        assertTrue(customFormat.contains("5"));
        assertTrue(customFormat.contains("10"));
    }

    @Test
    void testIsWithNaturalOrdering() {
        Range<Integer> singleton = Range.is(7);
        assertEquals(7, singleton.getMinimum());
        assertEquals(7, singleton.getMaximum());
    }

    @Test
    void testIsWithCustomComparator() {
        Range<Integer> singleton = Range.is(9, Comparator.reverseOrder());
        assertEquals(9, singleton.getMinimum());
        assertEquals(9, singleton.getMaximum());
    }

    @Test
    void testBetweenDelegatesToOf() {
        Range<Integer> legacy = Range.between(3, 8);
        assertEquals(3, legacy.getMinimum());
        assertEquals(8, legacy.getMaximum());

        Range<Integer> legacyWithComparator = Range.between(3, 8, Comparator.naturalOrder());
        assertEquals(3, legacyWithComparator.getMinimum());
        assertEquals(8, legacyWithComparator.getMaximum());
    }

    @Test
    void testNullElementThrowsInElementCompareToAndFit() {
        assertThrows(NullPointerException.class, () -> naturalRange.elementCompareTo(null));
        assertThrows(NullPointerException.class, () -> naturalRange.fit(null));
    }

    @Test
    void testConstructorNullsThrow() {
        assertThrows(NullPointerException.class, () -> Range.of(null, 5));
        assertThrows(NullPointerException.class, () -> Range.of(5, null));
    }

}

