package experimento.deepseek.charrange;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Iterator;
import java.util.NoSuchElementException;

class DeepSeek5CharRangeTest {

    // Testes para métodos de fábrica estáticos

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
    @ValueSource(chars = {'a', 'b', 'c', 'd', 'e'})
    void testContains_NonNegatedRange(char ch) {
        CharRange range = CharRange.isIn('a', 'e');
        assertTrue(range.contains(ch));
    }

    @ParameterizedTest
    @ValueSource(chars = {' ', 'A', 'f', '0', 'Z'})
    void testDoesNotContain_NonNegatedRange(char ch) {
        CharRange range = CharRange.isIn('a', 'e');
        assertFalse(range.contains(ch));
    }

    @ParameterizedTest
    @ValueSource(chars = {' ', 'A', 'f', '0', 'Z'})
    void testContains_NegatedRange(char ch) {
        CharRange range = CharRange.isNotIn('a', 'e');
        assertTrue(range.contains(ch));
    }

    @ParameterizedTest
    @ValueSource(chars = {'a', 'b', 'c', 'd', 'e'})
    void testDoesNotContain_NegatedRange(char ch) {
        CharRange range = CharRange.isNotIn('a', 'e');
        assertFalse(range.contains(ch));
    }

    @Test
    void testContains_EntireCharacterRange() {
        CharRange range = CharRange.isNotIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertFalse(range.contains(Character.MIN_VALUE));
        assertFalse(range.contains(Character.MAX_VALUE));
        assertFalse(range.contains('a'));
        assertFalse(range.contains('0'));
    }

    @Test
    void testContains_EmptyNegatedRange() {
        CharRange range = CharRange.isNotIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertFalse(range.contains('a'));
        assertFalse(range.contains(Character.MIN_VALUE));
        assertFalse(range.contains(Character.MAX_VALUE));
    }

    // Testes para o método contains(CharRange)

    @Test
    void testContainsRange_NonNegatedWithinNonNegated() {
        CharRange outer = CharRange.isIn('a', 'z');
        CharRange inner = CharRange.isIn('c', 'f');
        assertTrue(outer.contains(inner));
    }

    @Test
    void testContainsRange_NonNegatedNotWithinNonNegated() {
        CharRange outer = CharRange.isIn('a', 'z');
        CharRange inner = CharRange.isIn('x', '~');
        assertFalse(outer.contains(inner));
    }

    @Test
    void testContainsRange_NegatedWithinNegated() {
        CharRange outer = CharRange.isNotIn('a', 'z');
        CharRange inner = CharRange.isNotIn('c', 'f');
        assertTrue(outer.contains(inner));
    }

    @Test
    void testContainsRange_NegatedNotWithinNegated() {
        CharRange outer = CharRange.isNotIn('a', 'z');
        CharRange inner = CharRange.isNotIn('x', '~');
        assertFalse(outer.contains(inner));
    }

    @Test
    void testContainsRange_NonNegatedWithinNegated() {
        CharRange negated = CharRange.isNotIn('a', 'z');
        CharRange nonNegated = CharRange.isIn('0', '9');
        assertTrue(negated.contains(nonNegated));
    }

    @Test
    void testContainsRange_NonNegatedNotWithinNegated() {
        CharRange negated = CharRange.isNotIn('a', 'z');
        CharRange nonNegated = CharRange.isIn('m', 'p');
        assertFalse(negated.contains(nonNegated));
    }

    @Test
    void testContainsRange_NegatedWithinNonNegated() {
        CharRange nonNegated = CharRange.isIn(Character.MIN_VALUE, Character.MAX_VALUE);
        CharRange negated = CharRange.isNotIn('a', 'z');
        assertTrue(nonNegated.contains(negated));
    }

    @Test
    void testContainsRange_NonNegatedWithinFullRange() {
        CharRange full = CharRange.isIn(Character.MIN_VALUE, Character.MAX_VALUE);
        CharRange any = CharRange.isIn('a', 'z');
        assertTrue(full.contains(any));
    }

    @Test
    void testContainsRange_NullRange() {
        CharRange range = CharRange.is('a');
        assertThrows(NullPointerException.class, () -> range.contains(null));
    }

    // Testes para equals e hashCode

    @Test
    void testEquals_SameInstance() {
        CharRange range = CharRange.is('a');
        assertEquals(range, range);
    }

    @Test
    void testEquals_EqualRanges() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isIn('a', 'z');
        assertEquals(range1, range2);
        assertEquals(range1.hashCode(), range2.hashCode());
    }

    @Test
    void testEquals_DifferentRanges() {
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
    void testEquals_NonCharRangeObject() {
        CharRange range = CharRange.is('a');
        assertNotEquals(range, "not a CharRange");
    }

    @Test
    void testEquals_Null() {
        CharRange range = CharRange.is('a');
        assertNotEquals(null, range);
    }

    // Testes para o método iterator()

    @Test
    void testIterator_NonNegatedSingleChar() {
        CharRange range = CharRange.is('a');
        Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf('a'), it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void testIterator_NonNegatedRange() {
        CharRange range = CharRange.isIn('a', 'c');
        Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf('a'), it.next());
        assertEquals(Character.valueOf('b'), it.next());
        assertEquals(Character.valueOf('c'), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_NegatedSingleChar() {
        CharRange range = CharRange.isNot('a');
        Iterator<Character> it = range.iterator();
        
        // Should iterate from MIN_VALUE to 'a'-1 and from 'a'+1 to MAX_VALUE
        char firstChar = it.next();
        assertEquals(Character.MIN_VALUE, firstChar);
        
        // Skip to after 'a'
        while (it.hasNext() && it.next() < 'a') {}
        
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf((char)('a' + 1)), it.next());
    }

    @Test
    void testIterator_NegatedRange() {
        CharRange range = CharRange.isNotIn('b', 'd');
        Iterator<Character> it = range.iterator();
        
        // Should iterate from MIN_VALUE to 'b'-1 and from 'd'+1 to MAX_VALUE
        char firstChar = it.next();
        assertEquals(Character.MIN_VALUE, firstChar);
        
        // Skip to after 'd'
        while (it.hasNext() && it.next() < 'd') {}
        
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf((char)('d' + 1)), it.next());
    }

    @Test
    void testIterator_EmptyNegatedRange() {
        CharRange range = CharRange.isNotIn(Character.MIN_VALUE, Character.MAX_VALUE);
        Iterator<Character> it = range.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void testIterator_RemoveNotSupported() {
        CharRange range = CharRange.is('a');
        Iterator<Character> it = range.iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    // Testes para toString()

    @Test
    void testToString_NonNegatedSingleChar() {
        CharRange range = CharRange.is('a');
        assertEquals("a", range.toString());
    }

    @Test
    void testToString_NonNegatedRange() {
        CharRange range = CharRange.isIn('a', 'z');
        assertEquals("a-z", range.toString());
    }

    @Test
    void testToString_NegatedSingleChar() {
        CharRange range = CharRange.isNot('a');
        assertEquals("^a", range.toString());
    }

    @Test
    void testToString_NegatedRange() {
        CharRange range = CharRange.isNotIn('a', 'z');
        assertEquals("^a-z", range.toString());
    }

    // Testes de borda/cantos

    @Test
    void testEdgeCases_MinAndMaxValues() {
        CharRange minRange = CharRange.is(Character.MIN_VALUE);
        CharRange maxRange = CharRange.is(Character.MAX_VALUE);
        
        assertTrue(minRange.contains(Character.MIN_VALUE));
        assertFalse(minRange.contains(Character.MAX_VALUE));
        
        assertTrue(maxRange.contains(Character.MAX_VALUE));
        assertFalse(maxRange.contains(Character.MIN_VALUE));
    }

    @Test
    void testEdgeCases_FullRange() {
        CharRange fullRange = CharRange.isIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertTrue(fullRange.contains(Character.MIN_VALUE));
        assertTrue(fullRange.contains(Character.MAX_VALUE));
        assertTrue(fullRange.contains('a'));
        assertTrue(fullRange.contains('0'));
    }

    @Test
    void testEdgeCases_EmptyRange() {
        CharRange emptyRange = CharRange.isNotIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertFalse(emptyRange.contains(Character.MIN_VALUE));
        assertFalse(emptyRange.contains(Character.MAX_VALUE));
        assertFalse(emptyRange.contains('a'));
    }

    // Testes parametrizados para melhor cobertura

    @ParameterizedTest
    @CsvSource({
        "a, a, false, a, a, false, true",
        "a, z, false, a, z, false, true",
        "a, z, false, a, y, false, false",
        "a, z, true, a, z, true, true",
        "a, z, true, b, y, true, false",
        "a, z, false, a, z, true, false"
    })
    void testParameterizedEquals(char s1, char e1, boolean n1, char s2, char e2, boolean n2, boolean expected) {
        CharRange range1 = new CharRange(s1, e1, n1);
        CharRange range2 = new CharRange(s2, e2, n2);
        assertEquals(expected, range1.equals(range2));
    }

    @ParameterizedTest
    @CsvSource({
        "a, a, false, 'a'",
        "a, c, false, 'a-c'",
        "a, a, true, '^a'",
        "a, c, true, '^a-c'"
    })
    void testParameterizedToString(char start, char end, boolean negated, String expected) {
        CharRange range = new CharRange(start, end, negated);
        assertEquals(expected, range.toString());
    }
}
