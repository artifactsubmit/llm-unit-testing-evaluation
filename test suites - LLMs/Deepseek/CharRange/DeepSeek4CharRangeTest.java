package experimento.deepseek.charrange;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Iterator;
import java.util.NoSuchElementException;

class DeepSeek4CharRangeTest {

    // Testes para métodos estáticos de criação

    @Test
    void testIs_SingleCharacter() {
        CharRange range = CharRange.is('a');
        assertFalse(range.isNegated());
        assertEquals('a', range.getStart());
        assertEquals('a', range.getEnd());
    }

    @Test
    void testIsIn_ValidRange() {
        CharRange range = CharRange.isIn('a', 'z');
        assertFalse(range.isNegated());
        assertEquals('a', range.getStart());
        assertEquals('z', range.getEnd());
    }

    @Test
    void testIsIn_ReverseRange() {
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
    void testIsNotIn_ValidRange() {
        CharRange range = CharRange.isNotIn('a', 'z');
        assertTrue(range.isNegated());
        assertEquals('a', range.getStart());
        assertEquals('z', range.getEnd());
    }

    @Test
    void testIsNotIn_ReverseRange() {
        CharRange range = CharRange.isNotIn('z', 'a');
        assertTrue(range.isNegated());
        assertEquals('a', range.getStart());
        assertEquals('z', range.getEnd());
    }

    // Testes para o método contains(char)

    @ParameterizedTest
    @CsvSource({
        "a, z, false, a, true",
        "a, z, false, m, true",
        "a, z, false, z, true",
        "a, z, false, A, false",
        "a, z, false, 0, false",
        "a, z, true, a, false",
        "a, z, true, m, false",
        "a, z, true, z, false",
        "a, z, true, A, true",
        "a, z, true, 0, true",
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
    void testContainsRange_NullArgument_ThrowsNPE() {
        CharRange range = CharRange.isIn('a', 'z');
        assertThrows(NullPointerException.class, () -> range.contains(null));
    }

    @ParameterizedTest
    @CsvSource({
        "a, z, false, a, z, false, true",    // same range
        "a, z, false, b, y, false, true",    // sub-range
        "a, z, false, a, a, false, true",    // single char at start
        "a, z, false, z, z, false, true",     // single char at end
        "a, z, false, m, m, false, true",     // single char in middle
        "a, z, false, A, Z, false, false",    // non-overlapping
        "a, z, false, 0, 9, false, false",    // completely different
        "a, z, true, a, z, true, true",      // both negated, same range
        "a, z, true, b, y, true, false",      // both negated, sub-range
        "a, z, true, a, z, false, false",    // negated contains non-negated
        "a, z, false, a, z, true, false",     // non-negated contains negated
        "a, z, true, 0, 9, false, true",     // negated contains non-overlapping
        "a, z, true, A, Z, false, false",     // negated contains partially overlapping
        "a, a, true, b, b, true, true",      // two single negated chars
        "a, a, true, a, a, false, false",     // negated contains same single char
        "a, z, false, 0, 255, false, false", // larger range
        "0, 255, false, a, z, false, true",  // contains sub-range
        "0, 127, true, 128, 255, false, true" // negated contains non-overlapping
    })
    void testContainsRange(char start1, char end1, boolean negated1, 
                         char start2, char end2, boolean negated2, 
                         boolean expected) {
        CharRange range1 = new CharRange(start1, end1, negated1);
        CharRange range2 = new CharRange(start2, end2, negated2);
        assertEquals(expected, range1.contains(range2));
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
    void testEquals_Null() {
        CharRange range = CharRange.isIn('a', 'z');
        assertNotEquals(null, range);
    }

    @Test
    void testEquals_DifferentClass() {
        CharRange range = CharRange.isIn('a', 'z');
        assertNotEquals("a-z", range);
    }

    @ParameterizedTest
    @CsvSource({
        "a, z, false, a, z, false, true",
        "a, z, false, a, z, true, false",
        "a, z, false, A, Z, false, false",
        "a, z, false, a, y, false, false",
        "a, z, false, b, z, false, false",
        "a, a, false, a, a, false, true",
        "a, a, false, a, a, true, false",
        "a, a, false, b, b, false, false"
    })
    void testEquals_VariousCases(char start1, char end1, boolean negated1, 
                                char start2, char end2, boolean negated2, 
                                boolean expected) {
        CharRange range1 = new CharRange(start1, end1, negated1);
        CharRange range2 = new CharRange(start2, end2, negated2);
        assertEquals(expected, range1.equals(range2));
    }

    @Test
    void testHashCode_ConsistentWithEquals() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isIn('a', 'z');
        assertEquals(range1.hashCode(), range2.hashCode());
    }

    @Test
    void testHashCode_DifferentForDifferentRanges() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isIn('a', 'y');
        assertNotEquals(range1.hashCode(), range2.hashCode());
    }

    @Test
    void testHashCode_DifferentForNegated() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isNotIn('a', 'z');
        assertNotEquals(range1.hashCode(), range2.hashCode());
    }

    // Testes para o método iterator()

    @Test
    void testIterator_SingleCharacter() {
        CharRange range = CharRange.is('a');
        Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf('a'), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_Range() {
        CharRange range = CharRange.isIn('a', 'c');
        Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf('a'), it.next());
        assertEquals(Character.valueOf('b'), it.next());
        assertEquals(Character.valueOf('c'), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_NegatedSingleCharacter() {
        CharRange range = CharRange.isNot('a');
        Iterator<Character> it = range.iterator();
        
        // Should iterate all chars except 'a'
        boolean foundA = false;
        char firstChar = it.next();
        assertTrue(firstChar == 0 || firstChar == 'b'); // depends on implementation
        
        while (it.hasNext()) {
            char c = it.next();
            assertNotEquals('a', c);
            if (c == 'a') foundA = true;
        }
        assertFalse(foundA);
    }

    @Test
    void testIterator_NegatedRange() {
        CharRange range = CharRange.isNotIn('a', 'c');
        Iterator<Character> it = range.iterator();
        
        // Should iterate all chars except a-c
        boolean foundInRange = false;
        while (it.hasNext()) {
            char c = it.next();
            assertFalse(c >= 'a' && c <= 'c');
            if (c >= 'a' && c <= 'c') foundInRange = true;
        }
        assertFalse(foundInRange);
    }

    @Test
    void testIterator_EmptyRange() {
        CharRange range = new CharRange((char)0, Character.MAX_VALUE, true);
        Iterator<Character> it = range.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void testIterator_RemoveUnsupported() {
        CharRange range = CharRange.is('a');
        Iterator<Character> it = range.iterator();
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

    @Test
    void testToString_Caching() {
        CharRange range = CharRange.isIn('a', 'z');
        String first = range.toString();
        String second = range.toString();
        assertSame(first, second); // should return cached value
    }

    // Testes de casos extremos

    @Test
    void testMinMaxRange() {
        CharRange range = CharRange.isIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertTrue(range.contains('a'));
        assertTrue(range.contains(Character.MIN_VALUE));
        assertTrue(range.contains(Character.MAX_VALUE));
    }

    @Test
    void testMinMaxRangeNegated() {
        CharRange range = CharRange.isNotIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertFalse(range.contains('a'));
        assertFalse(range.contains(Character.MIN_VALUE));
        assertFalse(range.contains(Character.MAX_VALUE));
        assertFalse(range.iterator().hasNext());
    }

    @Test
    void testSingleCharNegatedContains() {
        CharRange range = CharRange.isNot('a');
        assertFalse(range.contains('a'));
        assertTrue(range.contains('b'));
        assertTrue(range.contains(Character.MIN_VALUE));
        assertTrue(range.contains(Character.MAX_VALUE));
    }

    @Test
    void testContains_EdgeCases() {
        CharRange range = CharRange.isIn('a', 'a');
        assertTrue(range.contains('a'));
        assertFalse(range.contains('b'));
        
        CharRange negated = CharRange.isNotIn('a', 'a');
        assertFalse(negated.contains('a'));
        assertTrue(negated.contains('b'));
    }
}
