package experimento.chatgpt.charrange;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT2CharRangeTest {

    // Testes de criação de intervalos simples
    @Test
    void testIs_createsRangeWithSingleChar() {
        CharRange range = CharRange.is('a');
        assertFalse(range.isNegated());
        assertEquals('a', range.getStart());
        assertEquals('a', range.getEnd());
        assertEquals("a", range.toString());
    }

    @Test
    void testIsIn_createsRangeCorrectly() {
        CharRange range = CharRange.isIn('a', 'e');
        assertFalse(range.isNegated());
        assertEquals('a', range.getStart());
        assertEquals('e', range.getEnd());
        assertEquals("a-e", range.toString());
    }

    @Test
    void testIsIn_swappedCharacters() {
        CharRange range = CharRange.isIn('e', 'a');
        assertEquals('a', range.getStart());
        assertEquals('e', range.getEnd());
    }

    @Test
    void testIsNot_createsNegatedRangeWithSingleChar() {
        CharRange range = CharRange.isNot('z');
        assertTrue(range.isNegated());
        assertEquals("^z", range.toString());
    }

    @Test
    void testIsNotIn_createsNegatedRangeWithMultipleChars() {
        CharRange range = CharRange.isNotIn('x', 'z');
        assertTrue(range.isNegated());
        assertEquals("^x-z", range.toString());
    }

    // Testes de método contains(char)
    @Test
    void testContains_inclusiveRange() {
        CharRange range = CharRange.isIn('a', 'c');
        assertTrue(range.contains('a'));
        assertTrue(range.contains('b'));
        assertTrue(range.contains('c'));
        assertFalse(range.contains('d'));
    }

    @Test
    void testContains_negatedRange() {
        CharRange range = CharRange.isNotIn('a', 'c');
        assertFalse(range.contains('a'));
        assertFalse(range.contains('b'));
        assertFalse(range.contains('c'));
        assertTrue(range.contains('d'));
    }

    // Testes de método contains(CharRange)
    @Test
    void testContains_sameRange() {
        CharRange outer = CharRange.isIn('a', 'z');
        CharRange inner = CharRange.isIn('d', 'f');
        assertTrue(outer.contains(inner));
    }

    @Test
    void testContains_differentRange_returnsFalse() {
        CharRange outer = CharRange.isIn('a', 'f');
        CharRange inner = CharRange.isIn('e', 'z');
        assertFalse(outer.contains(inner));
    }

    @Test
    void testContains_negatedContainingNormal() {
        CharRange negated = CharRange.isNot('x');
        CharRange normal = CharRange.is('a');
        assertTrue(negated.contains(normal));
    }

    @Test
    void testContains_normalContainingNegated() {
        CharRange normal = CharRange.isIn((char) 0, Character.MAX_VALUE);
        CharRange negated = CharRange.isNot('x');
        assertTrue(normal.contains(negated));
    }

    @Test
    void testContains_negatedContainingNegated() {
        CharRange outer = CharRange.isNotIn('b', 'y');
        CharRange inner = CharRange.isNotIn('a', 'z');
        assertFalse(outer.contains(inner));
    }

    @Test
    void testContains_nullInput_throwsException() {
        CharRange range = CharRange.is('a');
        assertThrows(NullPointerException.class, () -> range.contains(null));
    }

    // Testes de equals e hashCode
    @Test
    void testEquals_andHashCode() {
        CharRange range1 = CharRange.isIn('a', 'f');
        CharRange range2 = CharRange.isIn('a', 'f');
        assertEquals(range1, range2);
        assertEquals(range1.hashCode(), range2.hashCode());
    }

    @Test
    void testEquals_differentObject() {
        CharRange range = CharRange.is('a');
        assertNotEquals(range, "a");
    }

    @Test
    void testEquals_differentRange() {
        CharRange range1 = CharRange.isIn('a', 'f');
        CharRange range2 = CharRange.isIn('a', 'g');
        assertNotEquals(range1, range2);
    }

    // Testes de iterator
    @Test
    void testIterator_overRange() {
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
    void testIterator_singleCharacter() {
        CharRange range = CharRange.is('z');
        Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals('z', it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_noSuchElementException() {
        CharRange range = CharRange.is('x');
        Iterator<Character> it = range.iterator();
        it.next(); // valid
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void testIterator_unsupportedRemove() {
        CharRange range = CharRange.is('a');
        Iterator<Character> it = range.iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    void testToString_cachedValue() {
        CharRange range = CharRange.isIn('m', 'n');
        String firstCall = range.toString();
        String secondCall = range.toString(); // Usa cache interno
        assertSame(firstCall, secondCall);
    }
}

