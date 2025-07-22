package experimento.chatgpt.charrange;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT3CharRangeTest {

    @Test
    void testIs_singleCharacterContainsItself() {
        CharRange range = CharRange.is('a');
        assertTrue(range.contains('a'));
        assertFalse(range.contains('b'));
    }

    @Test
    void testIsIn_rangeInOrder() {
        CharRange range = CharRange.isIn('a', 'd');
        assertTrue(range.contains('b'));
        assertFalse(range.contains('z'));
    }

    @Test
    void testIsIn_rangeReversedOrder() {
        CharRange range = CharRange.isIn('d', 'a');
        assertTrue(range.contains('b'));
        assertFalse(range.contains('z'));
    }

    @Test
    void testIsNot_singleCharNegation() {
        CharRange range = CharRange.isNot('x');
        assertFalse(range.contains('x'));
        assertTrue(range.contains('a'));
    }

    @Test
    void testIsNotIn_rangeNegation() {
        CharRange range = CharRange.isNotIn('a', 'c');
        assertFalse(range.contains('a'));
        assertFalse(range.contains('b'));
        assertTrue(range.contains('z'));
    }

    @Test
    void testGetStartAndEnd() {
        CharRange range = CharRange.isIn('a', 'e');
        assertEquals('a', range.getStart());
        assertEquals('e', range.getEnd());
    }

    @Test
    void testIsNegated() {
        assertFalse(CharRange.is('a').isNegated());
        assertTrue(CharRange.isNot('a').isNegated());
    }

    @Test
    void testToString_normalRange() {
        assertEquals("a", CharRange.is('a').toString());
        assertEquals("a-e", CharRange.isIn('a', 'e').toString());
    }

    @Test
    void testToString_negatedRange() {
        assertEquals("^a", CharRange.isNot('a').toString());
        assertEquals("^a-c", CharRange.isNotIn('a', 'c').toString());
    }

    @Test
    void testEqualsAndHashCode() {
        CharRange r1 = CharRange.isIn('a', 'z');
        CharRange r2 = CharRange.isIn('a', 'z');
        CharRange r3 = CharRange.isNotIn('a', 'z');
        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testContains_charactersInsideAndOutside() {
        CharRange range = CharRange.isIn('m', 'o');
        assertTrue(range.contains('m'));
        assertTrue(range.contains('o'));
        assertFalse(range.contains('l'));
        assertFalse(range.contains('p'));
    }

    @Test
    void testContains_rangeInside() {
        CharRange outer = CharRange.isIn('a', 'z');
        CharRange inner = CharRange.isIn('d', 'm');
        assertTrue(outer.contains(inner));
    }

    @Test
    void testContains_rangeOutside() {
        CharRange outer = CharRange.isIn('a', 'f');
        CharRange inner = CharRange.isIn('g', 'k');
        assertFalse(outer.contains(inner));
    }

    @Test
    void testContains_negatedContainsNegated() {
        CharRange outer = CharRange.isNotIn('a', 'z');
        CharRange inner = CharRange.isNotIn('b', 'y');
        assertTrue(outer.contains(inner));
    }

    @Test
    void testContains_normalContainsNegated_fullRange() {
        CharRange outer = CharRange.isIn((char) 0, Character.MAX_VALUE);
        CharRange inner = CharRange.isNot('x');
        assertTrue(outer.contains(inner));
    }

    @Test
    void testIterator_normalRange() {
        CharRange range = CharRange.isIn('a', 'c');
        Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals('a', it.next());
        assertEquals('b', it.next());
        assertEquals('c', it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_negatedRangeAvoidsGap() {
        CharRange range = CharRange.isNotIn('b', 'd');
        Iterator<Character> it = range.iterator();
        // Should skip from 'a' to 'e' directly
        assertEquals('a', it.next());
        assertEquals('e', it.next());
    }

    @Test
    void testIterator_throwsWhenExceeds() {
        CharRange range = CharRange.is('x');
        Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals('x', it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void testRemoveUnsupportedInIterator() {
        CharRange range = CharRange.is('z');
        Iterator<Character> it = range.iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    void testContainsNullRangeThrows() {
        CharRange range = CharRange.is('a');
        assertThrows(NullPointerException.class, () -> range.contains(null));
    }

    @Test
    void testNegatedRangeEmptySet() {
        CharRange range = CharRange.isNotIn((char) 0, Character.MAX_VALUE);
        Iterator<Character> it = range.iterator();
        assertFalse(it.hasNext()); // should be empty
    }
}

