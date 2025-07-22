package experimento.deepseek.charrange;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Iterator;
import java.util.NoSuchElementException;

class DeepSeek1CharRangeTest {

    // Testes para métodos de fábrica estáticos

    @Test
    void testIs_SingleCharacter() {
        CharRange range = CharRange.is('a');
        assertAll(
            () -> assertEquals('a', range.getStart()),
            () -> assertEquals('a', range.getEnd()),
            () -> assertFalse(range.isNegated())
        );
    }

    @Test
    void testIsIn_NormalRange() {
        CharRange range = CharRange.isIn('a', 'z');
        assertAll(
            () -> assertEquals('a', range.getStart()),
            () -> assertEquals('z', range.getEnd()),
            () -> assertFalse(range.isNegated())
        );
    }

    @Test
    void testIsIn_ReversedRange() {
        CharRange range = CharRange.isIn('z', 'a');
        assertAll(
            () -> assertEquals('a', range.getStart()),
            () -> assertEquals('z', range.getEnd()),
            () -> assertFalse(range.isNegated())
        );
    }

    @Test
    void testIsNot_SingleCharacter() {
        CharRange range = CharRange.isNot('a');
        assertAll(
            () -> assertEquals('a', range.getStart()),
            () -> assertEquals('a', range.getEnd()),
            () -> assertTrue(range.isNegated())
        );
    }

    @Test
    void testIsNotIn_NormalRange() {
        CharRange range = CharRange.isNotIn('a', 'z');
        assertAll(
            () -> assertEquals('a', range.getStart()),
            () -> assertEquals('z', range.getEnd()),
            () -> assertTrue(range.isNegated())
        );
    }

    @Test
    void testIsNotIn_ReversedRange() {
        CharRange range = CharRange.isNotIn('z', 'a');
        assertAll(
            () -> assertEquals('a', range.getStart()),
            () -> assertEquals('z', range.getEnd()),
            () -> assertTrue(range.isNegated())
        );
    }

    // Testes para o método contains(char)

    @ParameterizedTest
    @ValueSource(chars = {'a', 'b', 'c', 'd', 'e'})
    void testContains_Char_InRange(char ch) {
        CharRange range = CharRange.isIn('a', 'e');
        assertTrue(range.contains(ch));
    }

    @ParameterizedTest
    @ValueSource(chars = {' ', '0', 'f', 'z', Character.MAX_VALUE})
    void testContains_Char_OutOfRange(char ch) {
        CharRange range = CharRange.isIn('a', 'e');
        assertFalse(range.contains(ch));
    }

    @ParameterizedTest
    @ValueSource(chars = {' ', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'z'})
    void testContains_Char_InNegatedRange(char ch) {
        CharRange range = CharRange.isNotIn('a', 'e');
        assertEquals(ch < 'a' || ch > 'e', range.contains(ch));
    }

    @Test
    void testContains_Char_EntireUnicodeRangeNegated() {
        CharRange range = CharRange.isNotIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertFalse(range.contains(Character.MIN_VALUE));
        assertFalse(range.contains('a'));
        assertFalse(range.contains(Character.MAX_VALUE));
    }

    @Test
    void testContains_Char_EmptyRange() {
        CharRange range = CharRange.isNotIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertFalse(range.contains(Character.MIN_VALUE));
        assertFalse(range.contains('a'));
        assertFalse(range.contains(Character.MAX_VALUE));
    }

    // Testes para o método contains(CharRange)

    @Test
    void testContains_Range_SameRange() {
        CharRange range = CharRange.isIn('a', 'z');
        assertTrue(range.contains(range));
    }

    @Test
    void testContains_Range_SubRange() {
        CharRange range = CharRange.isIn('a', 'z');
        CharRange subRange = CharRange.isIn('c', 'f');
        assertTrue(range.contains(subRange));
    }

    @Test
    void testContains_Range_OverlappingRange() {
        CharRange range = CharRange.isIn('a', 'z');
        CharRange overlapping = CharRange.isIn('x', '~');
        assertFalse(range.contains(overlapping));
    }

    @Test
    void testContains_Range_DisjointRange() {
        CharRange range = CharRange.isIn('a', 'z');
        CharRange disjoint = CharRange.isIn('0', '9');
        assertFalse(range.contains(disjoint));
    }

    @Test
    void testContains_Range_NegatedRange() {
        CharRange range = CharRange.isIn('a', 'z');
        CharRange negated = CharRange.isNotIn('c', 'f');
        assertFalse(range.contains(negated));
    }

    @Test
    void testContains_Range_EntireUnicodeRange() {
        CharRange range = CharRange.isIn(Character.MIN_VALUE, Character.MAX_VALUE);
        CharRange any = CharRange.isIn('a', 'z');
        assertTrue(range.contains(any));
    }

    @Test
    void testContains_Range_NegatedContainsNonNegated() {
        CharRange negated = CharRange.isNotIn('a', 'z');
        CharRange nonNegated = CharRange.isIn('0', '9');
        assertTrue(negated.contains(nonNegated));
    }

    @Test
    void testContains_Range_NegatedContainsNegated() {
        CharRange negated1 = CharRange.isNotIn('a', 'z');
        CharRange negated2 = CharRange.isNotIn('c', 'f');
        assertFalse(negated1.contains(negated2));
    }

    @Test
    void testContains_Range_NullInput() {
        CharRange range = CharRange.isIn('a', 'z');
        assertThrows(NullPointerException.class, () -> range.contains(null));
    }

    // Testes para equals e hashCode

    @Test
    void testEquals_SameInstance() {
        CharRange range = CharRange.isIn('a', 'z');
        assertTrue(range.equals(range));
    }

    @Test
    void testEquals_EqualRanges() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isIn('a', 'z');
        assertTrue(range1.equals(range2));
        assertEquals(range1.hashCode(), range2.hashCode());
    }

    @Test
    void testEquals_DifferentRanges() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isIn('a', 'y');
        assertFalse(range1.equals(range2));
    }

    @Test
    void testEquals_NegatedRange() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isNotIn('a', 'z');
        assertFalse(range1.equals(range2));
    }

    @Test
    void testEquals_ReversedRange() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isIn('z', 'a');
        assertTrue(range1.equals(range2));
    }

    @Test
    void testEquals_NonCharRangeObject() {
        CharRange range = CharRange.isIn('a', 'z');
        assertFalse(range.equals("not a char range"));
    }

    @Test
    void testEquals_Null() {
        CharRange range = CharRange.isIn('a', 'z');
        assertFalse(range.equals(null));
    }

    // Testes para o método iterator()

    @Test
    void testIterator_NormalRange() {
        CharRange range = CharRange.isIn('a', 'c');
        Iterator<Character> it = range.iterator();
        assertAll(
            () -> assertTrue(it.hasNext()),
            () -> assertEquals('a', it.next()),
            () -> assertTrue(it.hasNext()),
            () -> assertEquals('b', it.next()),
            () -> assertTrue(it.hasNext()),
            () -> assertEquals('c', it.next()),
            () -> assertFalse(it.hasNext()),
            () -> assertThrows(NoSuchElementException.class, it::next)
        );
    }

    @Test
    void testIterator_SingleCharacter() {
        CharRange range = CharRange.is('a');
        Iterator<Character> it = range.iterator();
        assertAll(
            () -> assertTrue(it.hasNext()),
            () -> assertEquals('a', it.next()),
            () -> assertFalse(it.hasNext())
        );
    }

    @Test
    void testIterator_NegatedRange() {
        CharRange range = CharRange.isNotIn('b', 'y');
        Iterator<Character> it = range.iterator();
        
        // Should include everything from MIN_VALUE to 'a' and 'z' to MAX_VALUE
        assertTrue(it.hasNext());
        assertEquals(Character.MIN_VALUE, it.next());
        
        // Skip to 'z'
        char current = Character.MIN_VALUE;
        while (current < 'a') {
            current++;
            assertEquals(current, it.next());
        }
        
        // Now should jump to 'z'
        assertEquals('z', it.next());
        
        // Then continue to MAX_VALUE
        current = 'z';
        while (current < Character.MAX_VALUE) {
            current++;
            assertEquals(current, it.next());
        }
        
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_NegatedSingleCharacter() {
        CharRange range = CharRange.isNot('a');
        Iterator<Character> it = range.iterator();
        
        assertTrue(it.hasNext());
        assertEquals(Character.MIN_VALUE, it.next());
        
        char current = Character.MIN_VALUE;
        while (current < Character.MAX_VALUE) {
            if (current != 'a' - 1) { // Skip 'a'
                current++;
                assertEquals(current, it.next());
            } else {
                current += 2; // Skip 'a'
                assertEquals(current, it.next());
            }
        }
        
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_EmptyRange() {
        CharRange range = CharRange.isNotIn(Character.MIN_VALUE, Character.MAX_VALUE);
        Iterator<Character> it = range.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void testIterator_RemoveUnsupported() {
        CharRange range = CharRange.isIn('a', 'c');
        Iterator<Character> it = range.iterator();
        it.next();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    // Testes para toString()

    @Test
    void testToString_SingleCharacter() {
        CharRange range = CharRange.is('a');
        assertEquals("a", range.toString());
    }

    @Test
    void testToString_Range() {
        CharRange range = CharRange.isIn('a', 'z');
        assertEquals("a-z", range.toString());
    }

    @Test
    void testToString_NegatedSingleCharacter() {
        CharRange range = CharRange.isNot('a');
        assertEquals("^a", range.toString());
    }

    @Test
    void testToString_NegatedRange() {
        CharRange range = CharRange.isNotIn('a', 'z');
        assertEquals("^a-z", range.toString());
    }

    // Testes de borda/canto

    @Test
    void testMinValueRange() {
        CharRange range = CharRange.isIn(Character.MIN_VALUE, 'a');
        assertAll(
            () -> assertTrue(range.contains(Character.MIN_VALUE)),
            () -> assertTrue(range.contains('a')),
            () -> assertFalse(range.contains('b'))
        );
    }

    @Test
    void testMaxValueRange() {
        CharRange range = CharRange.isIn('a', Character.MAX_VALUE);
        assertAll(
            () -> assertTrue(range.contains('a')),
            () -> assertTrue(range.contains(Character.MAX_VALUE)),
            () -> assertFalse(range.contains(' '))
        );
    }

    @Test
    void testFullUnicodeRange() {
        CharRange range = CharRange.isIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertAll(
            () -> assertTrue(range.contains(Character.MIN_VALUE)),
            () -> assertTrue(range.contains('a')),
            () -> assertTrue(range.contains(Character.MAX_VALUE))
        );
    }

    @Test
    void testFullUnicodeRangeNegated() {
        CharRange range = CharRange.isNotIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertAll(
            () -> assertFalse(range.contains(Character.MIN_VALUE)),
            () -> assertFalse(range.contains('a')),
            () -> assertFalse(range.contains(Character.MAX_VALUE))
        );
    }

    // Testes paramétricos para maior cobertura

    @ParameterizedTest
    @CsvSource({
        "a, a, false, a, a, false, true",
        "a, z, false, a, z, false, true",
        "a, z, false, a, y, false, false",
        "a, z, false, b, z, false, false",
        "a, z, true, a, z, true, true",
        "a, z, true, a, z, false, false",
        "a, z, false, b, y, false, true",
        "a, z, true, b, y, false, false",
        "a, z, true, 0, ~, true, false"
    })
    void testEquals_Parametric(char s1, char e1, boolean n1, char s2, char e2, boolean n2, boolean expected) {
        CharRange range1 = new CharRange(s1, e1, n1);
        CharRange range2 = new CharRange(s2, e2, n2);
        assertEquals(expected, range1.equals(range2));
    }

    @ParameterizedTest
    @CsvSource({
        "a, b, a, true",
        "a, b, b, true",
        "a, b, c, false",
        "a, a, a, true",
        "a, a, b, false",
        "^a, a, a, false",
        "^a, a, b, true",
        "^a-z, a, a, false",
        "^a-z, a, z, false",
        "^a-z, 0, 0, true",
        "^a-z, [, [, true"
    })
    void testContains_Char_Parametric(String rangeDesc, char testChar, boolean expected) {
        CharRange range;
        if (rangeDesc.startsWith("^")) {
            if (rangeDesc.length() == 2) { // single char negated
                range = CharRange.isNot(rangeDesc.charAt(1));
            } else { // range negated
                range = CharRange.isNotIn(rangeDesc.charAt(1), rangeDesc.charAt(3));
            }
        } else {
            if (rangeDesc.length() == 1) { // single char
                range = CharRange.is(rangeDesc.charAt(0));
            } else { // range
                range = CharRange.isIn(rangeDesc.charAt(0), rangeDesc.charAt(2));
            }
        }
        assertEquals(expected, range.contains(testChar));
    }
}
