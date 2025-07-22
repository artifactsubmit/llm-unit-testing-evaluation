package experimento.deepseek.charrange;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Iterator;
import java.util.NoSuchElementException;

class DeepSeek3CharRangeTest {

    // Testes para métodos factory estáticos

    @Test
    void testIs_SingleCharacter() {
        CharRange range = CharRange.is('a');
        assertFalse(range.isNegated());
        assertEquals('a', range.getStart());
        assertEquals('a', range.getEnd());
    }

    @Test
    void testIsIn_NormalRange() {
        CharRange range = CharRange.isIn('a', 'z');
        assertFalse(range.isNegated());
        assertEquals('a', range.getStart());
        assertEquals('z', range.getEnd());
    }

    @Test
    void testIsIn_ReversedRange() {
        CharRange range = CharRange.isIn('z', 'a');
        assertFalse(range.isNegated());
        assertEquals('a', range.getStart());
        assertEquals('z', range.getEnd());
    }

    @Test
    void testIsNot_SingleCharacter() {
        CharRange range = CharRange.isNot('a');
        assertTrue(range.isNegated());
        assertEquals('a', range.getStart());
        assertEquals('a', range.getEnd());
    }

    @Test
    void testIsNotIn_NormalRange() {
        CharRange range = CharRange.isNotIn('a', 'z');
        assertTrue(range.isNegated());
        assertEquals('a', range.getStart());
        assertEquals('z', range.getEnd());
    }

    @Test
    void testIsNotIn_ReversedRange() {
        CharRange range = CharRange.isNotIn('z', 'a');
        assertTrue(range.isNegated());
        assertEquals('a', range.getStart());
        assertEquals('z', range.getEnd());
    }

    // Testes para o método contains(char)

    @ParameterizedTest
    @CsvSource({
        "a, z, false, b, true",
        "a, z, false, a, true",
        "a, z, false, z, true",
        "a, z, false, `, false",
        "a, z, false, {, false",
        "a, z, true, b, false",
        "a, z, true, a, false",
        "a, z, true, z, false",
        "a, z, true, `, true",
        "a, z, true, {, true",
        "A, A, false, A, true",
        "A, A, false, B, false",
        "A, A, true, A, false",
        "A, A, true, B, true"
    })
    void testContainsChar(char start, char end, boolean negated, char testChar, boolean expected) {
        CharRange range = new CharRange(start, end, negated);
        assertEquals(expected, range.contains(testChar));
    }

    // Testes para o método contains(CharRange)

    @Test
    void testContainsRange_NonNegatedContainsNonNegated() {
        CharRange outer = CharRange.isIn('a', 'z');
        CharRange inner = CharRange.isIn('c', 'f');
        assertTrue(outer.contains(inner));
    }

    @Test
    void testContainsRange_NonNegatedDoesNotContainNonNegated() {
        CharRange range1 = CharRange.isIn('a', 'm');
        CharRange range2 = CharRange.isIn('n', 'z');
        assertFalse(range1.contains(range2));
    }

    @Test
    void testContainsRange_NonNegatedContainsNonNegatedEdgeCases() {
        CharRange outer = CharRange.isIn('a', 'z');
        CharRange inner1 = CharRange.isIn('a', 'a');
        CharRange inner2 = CharRange.isIn('z', 'z');
        assertTrue(outer.contains(inner1));
        assertTrue(outer.contains(inner2));
    }

    @Test
    void testContainsRange_NonNegatedContainsNegated() {
        CharRange nonNegated = CharRange.isIn(Character.MIN_VALUE, Character.MAX_VALUE);
        CharRange negated = CharRange.isNotIn('a', 'z');
        assertTrue(nonNegated.contains(negated));
    }

    @Test
    void testContainsRange_NegatedContainsNegated() {
        CharRange outer = CharRange.isNotIn('a', 'f');
        CharRange inner = CharRange.isNotIn('b', 'e');
        assertTrue(outer.contains(inner));
    }

    @Test
    void testContainsRange_NegatedDoesNotContainNegated() {
        CharRange range1 = CharRange.isNotIn('a', 'm');
        CharRange range2 = CharRange.isNotIn('l', 'z');
        assertFalse(range1.contains(range2));
    }

    @Test
    void testContainsRange_NegatedDoesNotContainNonNegated() {
        CharRange negated = CharRange.isNotIn('a', 'z');
        CharRange nonNegated = CharRange.isIn('b', 'y');
        assertFalse(negated.contains(nonNegated));
    }

    @Test
    void testContainsRange_NonNegatedDoesNotContainLargerNonNegated() {
        CharRange small = CharRange.isIn('c', 'f');
        CharRange large = CharRange.isIn('a', 'z');
        assertFalse(small.contains(large));
    }

    @Test
    void testContainsRange_NullInput() {
        CharRange range = CharRange.is('a');
        assertThrows(NullPointerException.class, () -> range.contains(null));
    }

    // Testes para equals e hashCode

    @Test
    void testEquals_Reflexive() {
        CharRange range = CharRange.isIn('a', 'z');
        assertEquals(range, range);
    }

    @Test
    void testEquals_Symmetric() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isIn('a', 'z');
        assertEquals(range1, range2);
        assertEquals(range2, range1);
    }

    @Test
    void testEquals_Transitive() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isIn('a', 'z');
        CharRange range3 = CharRange.isIn('a', 'z');
        assertEquals(range1, range2);
        assertEquals(range2, range3);
        assertEquals(range1, range3);
    }

    @Test
    void testEquals_NullInput() {
        CharRange range = CharRange.is('a');
        assertFalse(range.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        CharRange range = CharRange.is('a');
        assertFalse(range.equals("string"));
    }

    @Test
    void testEquals_DifferentStart() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isIn('b', 'z');
        assertNotEquals(range1, range2);
    }

    @Test
    void testEquals_DifferentEnd() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isIn('a', 'y');
        assertNotEquals(range1, range2);
    }

    @Test
    void testEquals_DifferentNegation() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isNotIn('a', 'z');
        assertNotEquals(range1, range2);
    }

    @Test
    void testHashCode_ConsistentWithEquals() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isIn('a', 'z');
        assertEquals(range1.hashCode(), range2.hashCode());
    }

    // Testes para o método iterator()

    @Test
    void testIterator_NonNegatedSingleChar() {
        CharRange range = CharRange.is('a');
        Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf('a'), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_NonNegatedRange() {
        CharRange range = CharRange.isIn('a', 'c');
        Iterator<Character> it = range.iterator();
        assertEquals(Character.valueOf('a'), it.next());
        assertEquals(Character.valueOf('b'), it.next());
        assertEquals(Character.valueOf('c'), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_NegatedSingleChar() {
        CharRange range = CharRange.isNot('a');
        Iterator<Character> it = range.iterator();
        
        // Should iterate all chars except 'a'
        char first = it.next();
        assertTrue(first != 'a');
        
        // Skip to near the end
        while (it.hasNext() && it.next() < Character.MAX_VALUE) {
            // just advance
        }
        
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_NegatedRange() {
        CharRange range = CharRange.isNotIn('b', 'y');
        Iterator<Character> it = range.iterator();
        
        // Should start at MIN_VALUE and go up to 'b'-1
        assertEquals(Character.valueOf(Character.MIN_VALUE), it.next());
        
        // Then skip to 'y'+1
        char c = it.next();
        assertEquals((char)('y' + 1), c);
        
        // Then continue to MAX_VALUE
        while (it.hasNext()) {
            c = it.next();
        }
        assertEquals(Character.valueOf(Character.MAX_VALUE), c);
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_EmptyNegatedRange() {
        CharRange range = CharRange.isNotIn(Character.MIN_VALUE, Character.MAX_VALUE);
        Iterator<Character> it = range.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_NoSuchElementException() {
        CharRange range = CharRange.is('a');
        Iterator<Character> it = range.iterator();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void testIterator_UnsupportedOperationException() {
        CharRange range = CharRange.is('a');
        Iterator<Character> it = range.iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    // Testes para toString()

    @Test
    void testToString_SingleCharNonNegated() {
        CharRange range = CharRange.is('a');
        assertEquals("a", range.toString());
    }

    @Test
    void testToString_RangeNonNegated() {
        CharRange range = CharRange.isIn('a', 'z');
        assertEquals("a-z", range.toString());
    }

    @Test
    void testToString_SingleCharNegated() {
        CharRange range = CharRange.isNot('a');
        assertEquals("^a", range.toString());
    }

    @Test
    void testToString_RangeNegated() {
        CharRange range = CharRange.isNotIn('a', 'z');
        assertEquals("^a-z", range.toString());
    }

    // Testes para casos extremos

    @Test
    void testMinMaxValueRange() {
        CharRange range = CharRange.isIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertTrue(range.contains(Character.MIN_VALUE));
        assertTrue(range.contains(Character.MAX_VALUE));
        assertTrue(range.contains('a'));
    }

    @Test
    void testMinMaxValueNegatedRange() {
        CharRange range = CharRange.isNotIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertFalse(range.contains(Character.MIN_VALUE));
        assertFalse(range.contains(Character.MAX_VALUE));
        assertFalse(range.contains('a'));
    }

    @Test
    void testSingleCharMinValue() {
        CharRange range = CharRange.is(Character.MIN_VALUE);
        assertTrue(range.contains(Character.MIN_VALUE));
        assertFalse(range.contains((char)(Character.MIN_VALUE + 1)));
    }

    @Test
    void testSingleCharMaxValue() {
        CharRange range = CharRange.is(Character.MAX_VALUE);
        assertTrue(range.contains(Character.MAX_VALUE));
        assertFalse(range.contains((char)(Character.MAX_VALUE - 1)));
    }
}
