package experimento.chatgpt.charrange;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT1CharRangeTest {

    @Test
    void testIsSingleCharContainsItself() {
        CharRange range = CharRange.is('a');
        assertTrue(range.contains('a'));
        assertFalse(range.contains('b'));
        assertFalse(range.isNegated());
    }

    @Test
    void testIsInRangeInclusive() {
        CharRange range = CharRange.isIn('a', 'e');
        assertTrue(range.contains('c'));
        assertTrue(range.contains('a'));
        assertTrue(range.contains('e'));
        assertFalse(range.contains('f'));
    }

    @Test
    void testIsInRangeReversedInput() {
        CharRange range = CharRange.isIn('e', 'a');
        assertTrue(range.contains('b'));
        assertTrue(range.contains('e'));
        assertFalse(range.contains('z'));
    }

    @Test
    void testIsNotSingleCharExcludesItself() {
        CharRange range = CharRange.isNot('a');
        assertFalse(range.contains('a'));
        assertTrue(range.contains('b'));
        assertTrue(range.isNegated());
    }

    @Test
    void testIsNotInRangeExcludesRange() {
        CharRange range = CharRange.isNotIn('a', 'e');
        assertFalse(range.contains('a'));
        assertFalse(range.contains('c'));
        assertFalse(range.contains('e'));
        assertTrue(range.contains('f'));
    }

    @Test
    void testContainsRange_PositiveToPositive() {
        CharRange outer = CharRange.isIn('a', 'z');
        CharRange inner = CharRange.isIn('d', 'f');
        assertTrue(outer.contains(inner));
    }

    @Test
    void testContainsRange_NegativeToNegative() {
        CharRange outer = CharRange.isNotIn('b', 'y');
        CharRange inner = CharRange.isNotIn('c', 'x');
        assertTrue(outer.contains(inner));
    }

    @Test
    void testContainsRange_NegativeToPositive_ShouldReturnFalse() {
        CharRange negated = CharRange.isNotIn('b', 'd');
        CharRange positive = CharRange.isIn('c', 'c');
        assertFalse(negated.contains(positive));
    }

    @Test
    void testContainsRange_PositiveToNegative_ShouldReturnFalse() {
        CharRange positive = CharRange.isIn('a', 'z');
        CharRange negated = CharRange.isNot('x');
        assertFalse(positive.contains(negated));
    }

    @Test
    void testContainsRange_PositiveToFullNegated_ShouldReturnTrue() {
        CharRange positive = CharRange.isIn(Character.MIN_VALUE, Character.MAX_VALUE);
        CharRange negated = CharRange.isNot('a');
        assertTrue(positive.contains(negated));
    }

    @Test
    void testIteratorPositiveRange() {
        CharRange range = CharRange.isIn('a', 'c');
        Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals('a', it.next());
        assertTrue(it.hasNext());
        assertEquals('b', it.next());
        assertTrue(it.hasNext());
        assertEquals('c', it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testIteratorNegatedSingleChar() {
        CharRange range = CharRange.isNot('a');
        Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf((char) 0), it.next());

        while (it.hasNext()) {
            char ch = it.next();
            assertNotEquals('a', ch);
        }
    }

    @Test
    void testIteratorEmptyNegatedFullRange() {
        CharRange range = CharRange.isNotIn((char) 0, Character.MAX_VALUE);
        assertFalse(range.iterator().hasNext());
    }

    @Test
    void testEqualsAndHashCode() {
        CharRange r1 = CharRange.isIn('a', 'z');
        CharRange r2 = CharRange.isIn('a', 'z');
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testNotEqualsDifferentNegation() {
        CharRange r1 = CharRange.is('a');
        CharRange r2 = CharRange.isNot('a');
        assertNotEquals(r1, r2);
    }

    @Test
    void testToStringPositiveSingle() {
        CharRange r = CharRange.is('a');
        assertEquals("a", r.toString());
    }

    @Test
    void testToStringPositiveRange() {
        CharRange r = CharRange.isIn('a', 'c');
        assertEquals("a-c", r.toString());
    }

    @Test
    void testToStringNegatedRange() {
        CharRange r = CharRange.isNotIn('a', 'c');
        assertEquals("^a-c", r.toString());
    }

    @Test
    void testGetStartAndEnd() {
        CharRange range = CharRange.isIn('m', 'z');
        assertEquals('m', range.getStart());
        assertEquals('z', range.getEnd());
    }

    @Test
    void testIteratorThrowsOnExceedingNext() {
        CharRange range = CharRange.is('x');
        Iterator<Character> it = range.iterator();
        assertEquals('x', it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void testIteratorThrowsUnsupportedRemove() {
        CharRange range = CharRange.is('x');
        Iterator<Character> it = range.iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    void testNullInputToContainsShouldThrowNPE() {
        CharRange range = CharRange.is('a');
        assertThrows(NullPointerException.class, () -> range.contains(null));
    }
}

