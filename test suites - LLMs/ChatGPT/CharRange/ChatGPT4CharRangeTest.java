package experimento.chatgpt.charrange;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT4CharRangeTest {

    // Testes de criação básica
    @Test
    void testIsSingleCharacterRange() {
        CharRange range = CharRange.is('a');
        assertEquals('a', range.getStart());
        assertEquals('a', range.getEnd());
        assertFalse(range.isNegated());
        assertTrue(range.contains('a'));
        assertFalse(range.contains('b'));
    }

    @Test
    void testIsInCharacterRange() {
        CharRange range = CharRange.isIn('a', 'e');
        assertEquals('a', range.getStart());
        assertEquals('e', range.getEnd());
        assertFalse(range.isNegated());
        assertTrue(range.contains('c'));
        assertFalse(range.contains('z'));
    }

    @Test
    void testIsInReversedOrder() {
        CharRange range = CharRange.isIn('e', 'a');
        assertEquals('a', range.getStart());
        assertEquals('e', range.getEnd());
        assertTrue(range.contains('b'));
    }

    @Test
    void testIsNotSingleCharacter() {
        CharRange range = CharRange.isNot('x');
        assertTrue(range.isNegated());
        assertFalse(range.contains('x'));
        assertTrue(range.contains('y'));
    }

    @Test
    void testIsNotInRange() {
        CharRange range = CharRange.isNotIn('m', 'p');
        assertTrue(range.isNegated());
        assertFalse(range.contains('n'));
        assertTrue(range.contains('z'));
    }

    // Testes de limites
    @Test
    void testContainsStartAndEnd() {
        CharRange range = CharRange.isIn('a', 'f');
        assertTrue(range.contains('a'));
        assertTrue(range.contains('f'));
    }

    @Test
    void testContainsWithNegatedFullRange() {
        CharRange range = CharRange.isNotIn((char) 0, Character.MAX_VALUE);
        assertFalse(range.contains('a'));
        assertFalse(range.contains('z'));
    }

    // Testes de contains(CharRange)
    @Test
    void testContainsRangePositive() {
        CharRange range = CharRange.isIn('a', 'z');
        CharRange subRange = CharRange.isIn('d', 'f');
        assertTrue(range.contains(subRange));
    }

    @Test
    void testContainsRangeNegative() {
        CharRange range = CharRange.isIn('a', 'f');
        CharRange subRange = CharRange.isIn('d', 'k');
        assertFalse(range.contains(subRange));
    }

    @Test
    void testContainsRangeNegatedInsidePositive() {
        CharRange range = CharRange.isIn('a', 'z');
        CharRange negated = CharRange.isNot('x');
        assertFalse(range.contains(negated));
    }

    @Test
    void testNegatedContainsNegated() {
        CharRange range = CharRange.isNotIn('c', 'f');
        CharRange sub = CharRange.isNotIn('b', 'g');
        assertFalse(range.contains(sub));
    }

    @Test
    void testNegatedContainsNormalRange() {
        CharRange range = CharRange.isNotIn('m', 'p');
        CharRange subRange = CharRange.isIn('a', 'l');
        assertTrue(range.contains(subRange));
    }

    // Testes de igualdade e hashCode
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

    // Testes de toString
    @Test
    void testToStringSingle() {
        CharRange range = CharRange.is('x');
        assertEquals("x", range.toString());
    }

    @Test
    void testToStringRange() {
        CharRange range = CharRange.isIn('a', 'd');
        assertEquals("a-d", range.toString());
    }

    @Test
    void testToStringNegated() {
        CharRange range = CharRange.isNotIn('a', 'c');
        assertEquals("^a-c", range.toString());
    }

    // Testes de iterator
    @Test
    void testIteratorNormalRange() {
        CharRange range = CharRange.isIn('a', 'c');
        Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals('a', it.next());
        assertEquals('b', it.next());
        assertEquals('c', it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testIteratorThrowsOnEmpty() {
        CharRange range = CharRange.isNotIn((char) 0, Character.MAX_VALUE);
        Iterator<Character> it = range.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void testIteratorRemoveUnsupported() {
        CharRange range = CharRange.is('x');
        Iterator<Character> it = range.iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    void testFullAsciiRange() {
        CharRange range = CharRange.isIn((char) 0, (char) 127);
        assertTrue(range.contains('A'));
        assertTrue(range.contains('z'));
        assertFalse(range.contains((char) 128));
    }

    @Test
    void testNegatedIteratorSkipsRange() {
        CharRange range = CharRange.isNotIn('b', 'd');
        StringBuilder builder = new StringBuilder();
        for (char c : range) {
            if (c >= 'a' && c <= 'e') {
                builder.append(c);
            }
        }
        assertEquals("ae", builder.toString());
    }
}

