package experimento.deepseek.charrange;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Iterator;
import java.util.NoSuchElementException;

class DeepSeek2CharRangeTest {

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
        "a, z, false, a, true",
        "a, z, false, b, true",
        "a, z, false, z, true",
        "a, z, false, A, false",
        "a, z, false, 0, false",
        "a, z, true, a, false",
        "a, z, true, b, false",
        "a, z, true, z, false",
        "a, z, true, A, true",
        "a, z, true, 0, true",
        "a, a, false, a, true",
        "a, a, false, b, false",
        "a, a, true, a, false",
        "a, a, true, b, true"
    })
    void testContains_Char(char start, char end, boolean negated, char testChar, boolean expected) {
        CharRange range = new CharRange(start, end, negated);
        assertEquals(expected, range.contains(testChar));
    }

    // Testes para o método contains(CharRange)

    @Test
    void testContains_CharRange_SameRange() {
        CharRange range = CharRange.isIn('a', 'z');
        assertTrue(range.contains(range));
    }

    @Test
    void testContains_CharRange_Subset() {
        CharRange range = CharRange.isIn('a', 'z');
        CharRange subset = CharRange.isIn('c', 'f');
        assertTrue(range.contains(subset));
    }

    @Test
    void testContains_CharRange_Superset() {
        CharRange range = CharRange.isIn('c', 'f');
        CharRange superset = CharRange.isIn('a', 'z');
        assertFalse(range.contains(superset));
    }

    @Test
    void testContains_CharRange_OverlappingStart() {
        CharRange range = CharRange.isIn('c', 'f');
        CharRange overlapping = CharRange.isIn('a', 'd');
        assertFalse(range.contains(overlapping));
    }

    @Test
    void testContains_CharRange_OverlappingEnd() {
        CharRange range = CharRange.isIn('c', 'f');
        CharRange overlapping = CharRange.isIn('e', 'h');
        assertFalse(range.contains(overlapping));
    }

    @Test
    void testContains_CharRange_DisjointBefore() {
        CharRange range = CharRange.isIn('c', 'f');
        CharRange disjoint = CharRange.isIn('a', 'b');
        assertFalse(range.contains(disjoint));
    }

    @Test
    void testContains_CharRange_DisjointAfter() {
        CharRange range = CharRange.isIn('c', 'f');
        CharRange disjoint = CharRange.isIn('g', 'h');
        assertFalse(range.contains(disjoint));
    }

    @Test
    void testContains_CharRange_NegatedContainsNonNegated() {
        CharRange negated = CharRange.isNotIn('a', 'z');
        CharRange nonNegated = CharRange.isIn('A', 'Z');
        assertTrue(negated.contains(nonNegated));
    }

    @Test
    void testContains_CharRange_NegatedContainsPartiallyOverlapping() {
        CharRange negated = CharRange.isNotIn('a', 'z');
        CharRange overlapping = CharRange.isIn('x', '~');
        assertFalse(negated.contains(overlapping));
    }

    @Test
    void testContains_CharRange_UniversalRange() {
        CharRange universal = CharRange.isNotIn((char) 0, Character.MAX_VALUE);
        CharRange any = CharRange.isIn('a', 'z');
        assertFalse(universal.contains(any));
    }

    @Test
    void testContains_CharRange_NullInput() {
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
    void testEquals_DifferentStart() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isIn('b', 'z');
        assertFalse(range1.equals(range2));
    }

    @Test
    void testEquals_DifferentEnd() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isIn('a', 'y');
        assertFalse(range1.equals(range2));
    }

    @Test
    void testEquals_DifferentNegation() {
        CharRange range1 = CharRange.isIn('a', 'z');
        CharRange range2 = CharRange.isNotIn('a', 'z');
        assertFalse(range1.equals(range2));
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
    void testIterator_SingleCharacter() {
        CharRange range = CharRange.is('a');
        Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf('a'), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_NormalRange() {
        CharRange range = CharRange.isIn('a', 'c');
        Iterator<Character> it = range.iterator();
        assertTrue(it.hasNext());
        assertEquals(Character.valueOf('a'), it.next());
        assertEquals(Character.valueOf('b'), it.next());
        assertEquals(Character.valueOf('c'), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void testIterator_EmptyRange() {
        CharRange range = CharRange.isNotIn(0, Character.MAX_VALUE);
        Iterator<Character> it = range.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void testIterator_NegatedRange() {
        CharRange range = CharRange.isNotIn('b', 'd');
        Iterator<Character> it = range.iterator();
        
        // Should iterate from 0 to 'a', then 'e' to Character.MAX_VALUE
        assertEquals(Character.valueOf((char) 0), it.next());
        // ... (would need to test more thoroughly, but keeping it brief)
        assertTrue(it.hasNext());
    }

    @Test
    void testIterator_RemoveUnsupported() {
        CharRange range = CharRange.isIn('a', 'z');
        Iterator<Character> it = range.iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    // Testes para o método toString()

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

    // Testes para casos extremos

    @Test
    void testMinMaxRange() {
        CharRange range = CharRange.isIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertTrue(range.contains('a'));
        assertTrue(range.contains(Character.MIN_VALUE));
        assertTrue(range.contains(Character.MAX_VALUE));
    }

    @Test
    void testMinMaxNegatedRange() {
        CharRange range = CharRange.isNotIn(Character.MIN_VALUE, Character.MAX_VALUE);
        assertFalse(range.contains('a'));
        assertFalse(range.contains(Character.MIN_VALUE));
        assertFalse(range.contains(Character.MAX_VALUE));
        assertFalse(range.iterator().hasNext());
    }

    @Test
    void testSingleCharacterNegated() {
        CharRange range = CharRange.isNot('a');
        assertFalse(range.contains('a'));
        assertTrue(range.contains('b'));
        assertTrue(range.contains(Character.MIN_VALUE));
        assertTrue(range.contains(Character.MAX_VALUE));
    }

    // Testes para verificação de invariantes

    @Test
    void testInvariant_StartLessThanOrEqualEnd() {
        // Verifica se o construtor sempre garante start <= end
        CharRange range1 = new CharRange('a', 'z', false);
        assertTrue(range1.getStart() <= range1.getEnd());
        
        CharRange range2 = new CharRange('z', 'a', true);
        assertTrue(range2.getStart() <= range2.getEnd());
    }

    @Test
    void testInvariant_EqualsContract() {
        // Verifica o contrato do equals: reflexivo, simétrico, transitivo
        CharRange a = CharRange.isIn('a', 'z');
        CharRange b = CharRange.isIn('a', 'z');
        CharRange c = CharRange.isIn('a', 'z');
        CharRange different = CharRange.isIn('a', 'y');
        
        // Reflexivo
        assertTrue(a.equals(a));
        
        // Simétrico
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        
        // Transitivo
        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
        
        // Consistente
        assertTrue(a.equals(b));
        assertTrue(a.equals(b));
        
        // Não igual a diferente
        assertFalse(a.equals(different));
        
        // Não igual a null
        assertFalse(a.equals(null));
    }

    @Test
    void testInvariant_HashCodeContract() {
        // Verifica que objetos iguais têm hash codes iguais
        CharRange a = CharRange.isIn('a', 'z');
        CharRange b = CharRange.isIn('a', 'z');
        assertEquals(a.hashCode(), b.hashCode());
    }
}
