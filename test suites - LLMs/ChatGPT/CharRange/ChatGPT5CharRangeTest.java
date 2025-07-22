package experimento.chatgpt.charrange;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT5CharRangeTest {

    // Testes de criação de intervalos simples e acesso

    @Test
    void testIsSingleChar() {
        CharRange range = CharRange.is('a');
        assertFalse(range.isNegated());
        assertEquals('a', range.getStart());
        assertEquals('a', range.getEnd());
        assertTrue(range.contains('a'));
        assertFalse(range.contains('b'));
    }

    @Test
    void testIsInRange() {
        CharRange range = CharRange.isIn('a', 'd');
        assertFalse(range.isNegated());
        assertEquals('a', range.getStart());
        assertEquals('d', range.getEnd());
        assertTrue(range.contains('a'));
        assertTrue(range.contains('c'));
        assertTrue(range.contains('d'));
        assertFalse(range.contains('e'));
    }

    @Test
    void testIsInWithSwappedBounds() {
        CharRange range = CharRange.isIn('z', 'x');
        assertEquals('x', range.getStart());
        assertEquals('z', range.getEnd());
    }

    @Test
    void testIsNotSingleChar() {
        CharRange range = CharRange.isNot('a');
        assertTrue(range.isNegated());
        assertFalse(range.contains('a'));
        assertTrue(range.contains('b'));
    }

    @Test
    void testIsNotInRange() {
        CharRange range = CharRange.isNotIn('a', 'd');
        assertTrue(range.isNegated());
        assertFalse(range.contains('a'));
        assertFalse(range.contains('c'));
        assertFalse(range.contains('d'));
        assertTrue(range.contains('e'));
    }

    // Testes de métodos equals e hashCode

    @Test
    void testEqualsAndHashCodeSameRange() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isIn('a', 'z');
        assertEquals(range1, range2);
        assertEquals(range1.hashCode(), range2.hashCode());
    }

    @Test
    void testNotEqualsDifferentNegation() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isNotIn('a', 'z');
        assertNotEquals(range1, range2);
    }

    @Test
    void testNotEqualsDifferentBounds() {
        CharRange range1 = CharRange.is('a');
        CharRange range2 = CharRange.is('b');
        assertNotEquals(range1, range2);
    }

    @Test
    void testEqualsSameNegatedRange() {
        CharRange r1 = CharRange.isNotIn('b', 'y');
        CharRange r2 = CharRange.isNotIn('y', 'b');
        assertEquals(r1, r2);
    }

    // Testes de contains(CharRange)

    @Test
    void testContainsRangePositiveInsidePositive() {
        CharRange outer = CharRange.isIn('a', 'z');
        CharRange inner = CharRange.isIn('c', 'f');
        assertTrue(outer.contains(inner));
    }

    @Test
    void testContainsRangePositiveOutsidePositive() {
        CharRange outer = CharRange.isIn('d', 'h');
        CharRange inner = CharRange.isIn('a', 'z');
        assertFalse(outer.contains(inner));
    }

    @Test
    void testContainsRangePositiveContainsNegatedAll() {
        CharRange range = CharRange.isIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertTrue(range.contains(CharRange.isNot('a')));
    }

    @Test
    void testContainsRangeNegatedContainsPositiveOutside() {
        CharRange negated = CharRange.isNotIn('d', 'f');
        CharRange inner = CharRange.isIn('a', 'c');
        assertTrue(negated.contains(inner));
    }

    @Test
    void testContainsRangeNegatedNotContainsPositiveInside() {
        CharRange negated = CharRange.isNotIn('d', 'f');
        CharRange inner = CharRange.isIn('d', 'f');
        assertFalse(negated.contains(inner));
    }

    @Test
    void testContainsRangeNegatedInsideNegated() {
        CharRange outer = CharRange.isNotIn('b', 'y');
        CharRange inner = CharRange.isNotIn('c', 'x');
        assertTrue(outer.contains(inner));
    }

    @Test
    void testContainsThrowsOnNull() {
        CharRange range = CharRange.is('a');
        assertThrows(NullPointerException.class, () -> range.contains(null));
    }

    // Testes de toString

    @Test
    void testToStringSingleNonNegated() {
        CharRange range = CharRange.is('x');
        assertEquals("x", range.toString());
    }

    @Test
    void testToStringRangeNonNegated() {
        CharRange range = CharRange.isIn('a', 'c');
        assertEquals("a-c", range.toString());
    }

    @Test
    void testToStringSingleNegated() {
        CharRange range = CharRange.isNot('x');
        assertEquals("^x", range.toString());
    }

    @Test
    void testToStringRangeNegated() {
        CharRange range = CharRange.isNotIn('a', 'c');
        assertEquals("^a-c", range.toString());
    }

    // Testes do iterator

    @Test
    void testIteratorOverRange() {
        CharRange range = CharRange.isIn('a', 'c');
        Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals('a', it.next());
        assertEquals('b', it.next());
        assertEquals('c', it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testIteratorOverNegatedRange() {
        CharRange range = CharRange.isNotIn('b', 'd');
        Iterator<Character> it = range.iterator();
        assertEquals('a', it.next());
        while (it.hasNext()) {
            char ch = it.next();
            assertTrue(ch < 'b' || ch > 'd');
        }
    }

    @Test
    void testIteratorOverEmptyNegated() {
        CharRange range = CharRange.isNotIn((char) 0, Character.MAX_VALUE);
        Iterator<Character> it = range.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    void testIteratorNextThrowsWhenDone() {
        CharRange range = CharRange.is('x');
        Iterator<Character> it = range.iterator();
        it.next();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void testIteratorRemoveUnsupported() {
        CharRange range = CharRange.is('x');
        Iterator<Character> it = range.iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }
}

