package experimento;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class RangeBaseTest {

    private final Range<Integer> r1 = Range.of(10, 20);
    private final Range<Integer> r2 = Range.of(15, 25);
    private final Range<Integer> r3 = Range.of(30, 40);
    private final Comparator<Integer> reversed = Comparator.reverseOrder();

    @Test
    void testFactoryMethods() {
        Range<Integer> a = Range.of(5, 15);
        Range<Integer> b = Range.of(15, 5); // inverted
        Range<Integer> c = Range.of(10, 10); // same min/max

        Range<Integer> d = Range.of(1, 3, reversed);
        Range<Integer> e = Range.is(9);
        Range<Integer> f = Range.is(7, Comparator.naturalOrder());

        assertNotNull(a);
        assertNotNull(b);
        assertNotNull(c);
        assertNotNull(d);
        assertNotNull(e);
        assertNotNull(f);
    }

    @Test
    void testContainmentAndComparison() {
        assertTrue(r1.contains(15));
        assertFalse(r1.contains(25));
        assertTrue(r1.containsRange(Range.of(12, 18)));
        assertFalse(r1.containsRange(r3));
        assertEquals(0, r1.elementCompareTo(15));
        assertEquals(-1, r1.elementCompareTo(5));
        assertEquals(1, r1.elementCompareTo(25));
    }

    @Test
    void testIntersectionAndFit() {
        Range<Integer> intersection = r1.intersectionWith(r2);
        assertEquals(Range.of(15, 20), intersection);
        assertEquals(10, r1.fit(5));
        assertEquals(20, r1.fit(25));
        assertEquals(15, r1.fit(15));
    }

    @Test
    void testPositionChecks() {
        assertTrue(r1.isAfter(5));
        assertFalse(r1.isAfter(15));
        assertTrue(r1.isBefore(25));
        assertFalse(r1.isBefore(10));
        assertTrue(r1.isEndedBy(20));
        assertTrue(r1.isStartedBy(10));
    }

    @Test
    void testRangeRelationChecks() {
        assertTrue(r1.isOverlappedBy(r2));
        assertFalse(r1.isOverlappedBy(r3));
        assertFalse(r1.isBeforeRange(r2));
        assertTrue(r1.isBeforeRange(r3));
        assertTrue(r3.isAfterRange(r1));
        assertTrue(r3.isAfterRange(r2));
    }

    @Test
    void testAccessorsAndMeta() {
        assertEquals(10, r1.getMinimum());
        assertEquals(20, r1.getMaximum());
        assertNotNull(r1.getComparator());
        assertTrue(r1.isNaturalOrdering());
        assertTrue(r1.toString().contains(".."));
        assertNotNull(r1.toString("%1$s-%2$s"));
        assertEquals(r1.hashCode(), r1.hashCode());
        assertEquals(r1, Range.of(10, 20));
        assertNotEquals(r1, r2);
    }
}
