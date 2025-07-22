package experimento.deepseek.range;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullSource;

class DeepSeek1RangeTest {

    @Nested
    @DisplayName("Testes de construção da Range")
    class ConstructionTests {
        
        @Test
        @DisplayName("Criação de Range com elementos comparáveis")
        void testBetweenWithComparable() {
            Range<Integer> range = Range.between(10, 20);
            assertEquals(10, range.getMinimum());
            assertEquals(20, range.getMaximum());
            assertTrue(range.isNaturalOrdering());
        }
        
        @Test
        @DisplayName("Criação de Range com comparator")
        void testBetweenWithComparator() {
            Comparator<Integer> reverseComparator = Comparator.reverseOrder();
            Range<Integer> range = Range.between(20, 10, reverseComparator);
            assertEquals(20, range.getMinimum());
            assertEquals(10, range.getMaximum());
            assertFalse(range.isNaturalOrdering());
        }
        
        @Test
        @DisplayName("Criação de Range singleton")
        void testIs() {
            Range<Integer> range = Range.is(15);
            assertEquals(15, range.getMinimum());
            assertEquals(15, range.getMaximum());
        }
        
        @Test
        @DisplayName("Criação de Range com elementos invertidos")
        void testBetweenWithInvertedElements() {
            Range<Integer> range = Range.between(20, 10);
            assertEquals(10, range.getMinimum());
            assertEquals(20, range.getMaximum());
        }
        
        @Test
        @DisplayName("Criação de Range.of equivalente a between")
        void testOf() {
            Range<Integer> range = Range.of(10, 20);
            assertEquals(10, range.getMinimum());
            assertEquals(20, range.getMaximum());
        }
        
        @Test
        @DisplayName("Lançamento de NullPointerException para elemento nulo")
        void testNullElementThrowsException() {
            assertThrows(NullPointerException.class, () -> Range.between(null, 10));
            assertThrows(NullPointerException.class, () -> Range.between(10, null));
            assertThrows(NullPointerException.class, () -> Range.is(null));
        }
        
        @Test
        @DisplayName("Teste com tipos customizados e comparator")
        void testWithCustomTypeAndComparator() {
            class Person {
                String name;
                int age;
                Person(String name, int age) { this.name = name; this.age = age; }
            }
            
            Comparator<Person> ageComparator = Comparator.comparingInt(p -> p.age);
            Person young = new Person("Young", 20);
            Person old = new Person("Old", 60);
            
            Range<Person> range = Range.between(young, old, ageComparator);
            assertEquals(young, range.getMinimum());
            assertEquals(old, range.getMaximum());
        }
    }

    @Nested
    @DisplayName("Testes de métodos de verificação")
    class VerificationTests {
        private final Range<Integer> range = Range.between(10, 20);
        
        @ParameterizedTest
        @ValueSource(ints = {10, 15, 20})
        @DisplayName("Elemento dentro do range deve retornar true para contains")
        void testContainsWithinRange(int value) {
            assertTrue(range.contains(value));
        }
        
        @ParameterizedTest
        @ValueSource(ints = {9, 21})
        @DisplayName("Elemento fora do range deve retornar false para contains")
        void testContainsOutsideRange(int value) {
            assertFalse(range.contains(value));
        }
        
        @Test
        @DisplayName("Elemento nulo deve retornar false para contains")
        void testContainsNull() {
            assertFalse(range.contains(null));
        }
        
        @Test
        @DisplayName("Range contido deve retornar true para containsRange")
        void testContainsRange() {
            Range<Integer> subRange = Range.between(12, 18);
            assertTrue(range.containsRange(subRange));
        }
        
        @Test
        @DisplayName("Range não contido deve retornar false para containsRange")
        void testDoesNotContainRange() {
            Range<Integer> outsideRange = Range.between(5, 15);
            assertFalse(range.containsRange(outsideRange));
        }
        
        @Test
        @DisplayName("Range nulo deve retornar false para containsRange")
        void testContainsNullRange() {
            assertFalse(range.containsRange(null));
        }
        
        @ParameterizedTest
        @CsvSource({"5,1", "10,0", "15,0", "20,0", "25,-1"})
        @DisplayName("elementCompareTo deve retornar valores corretos")
        void testElementCompareTo(int value, int expected) {
            assertEquals(expected, range.elementCompareTo(value));
        }
        
        @Test
        @DisplayName("elementCompareTo com elemento nulo deve lançar exceção")
        void testElementCompareToNull() {
            assertThrows(NullPointerException.class, () -> range.elementCompareTo(null));
        }
    }

    @Nested
    @DisplayName("Testes de métodos de posicionamento relativo")
    class PositionTests {
        private final Range<Integer> range = Range.between(10, 20);
        
        @ParameterizedTest
        @ValueSource(ints = {5, 9})
        @DisplayName("Elemento antes do range deve retornar true para isAfter")
        void testIsAfter(int value) {
            assertTrue(range.isAfter(value));
        }
        
        @ParameterizedTest
        @ValueSource(ints = {10, 15, 20, 21})
        @DisplayName("Elemento não antes do range deve retornar false para isAfter")
        void testIsNotAfter(int value) {
            assertFalse(range.isAfter(value));
        }
        
        @ParameterizedTest
        @ValueSource(ints = {21, 25})
        @DisplayName("Elemento depois do range deve retornar true para isBefore")
        void testIsBefore(int value) {
            assertTrue(range.isBefore(value));
        }
        
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20})
        @DisplayName("Elemento não depois do range deve retornar false para isBefore")
        void testIsNotBefore(int value) {
            assertFalse(range.isBefore(value));
        }
        
        @Test
        @DisplayName("Range antes deve retornar true para isAfterRange")
        void testIsAfterRange() {
            Range<Integer> beforeRange = Range.between(1, 5);
            assertTrue(range.isAfterRange(beforeRange));
        }
        
        @Test
        @DisplayName("Range depois deve retornar true para isBeforeRange")
        void testIsBeforeRange() {
            Range<Integer> afterRange = Range.between(25, 30);
            assertTrue(range.isBeforeRange(afterRange));
        }
        
        @Test
        @DisplayName("Range sobreposto não deve retornar true para isBeforeRange ou isAfterRange")
        void testOverlappingRange() {
            Range<Integer> overlappingRange = Range.between(15, 25);
            assertFalse(range.isBeforeRange(overlappingRange));
            assertFalse(range.isAfterRange(overlappingRange));
        }
        
        @Test
        @DisplayName("isStartedBy deve retornar true apenas para o mínimo")
        void testIsStartedBy() {
            assertTrue(range.isStartedBy(10));
            assertFalse(range.isStartedBy(15));
            assertFalse(range.isStartedBy(20));
            assertFalse(range.isStartedBy(null));
        }
        
        @Test
        @DisplayName("isEndedBy deve retornar true apenas para o máximo")
        void testIsEndedBy() {
            assertTrue(range.isEndedBy(20));
            assertFalse(range.isEndedBy(15));
            assertFalse(range.isEndedBy(10));
            assertFalse(range.isEndedBy(null));
        }
    }

    @Nested
    @DisplayName("Testes de métodos de sobreposição e interseção")
    class OverlapTests {
        private final Range<Integer> range = Range.between(10, 20);
        
        @Test
        @DisplayName("Range sobreposto deve retornar true para isOverlappedBy")
        void testIsOverlappedBy() {
            Range<Integer> overlapping1 = Range.between(5, 15);
            Range<Integer> overlapping2 = Range.between(15, 25);
            Range<Integer> overlapping3 = Range.between(18, 19);
            
            assertTrue(range.isOverlappedBy(overlapping1));
            assertTrue(range.isOverlappedBy(overlapping2));
            assertTrue(range.isOverlappedBy(overlapping3));
        }
        
        @Test
        @DisplayName("Range não sobreposto deve retornar false para isOverlappedBy")
        void testIsNotOverlappedBy() {
            Range<Integer> beforeRange = Range.between(1, 5);
            Range<Integer> afterRange = Range.between(25, 30);
            
            assertFalse(range.isOverlappedBy(beforeRange));
            assertFalse(range.isOverlappedBy(afterRange));
        }
        
        @Test
        @DisplayName("Interseção com range sobreposto deve retornar range correto")
        void testIntersectionWith() {
            Range<Integer> other = Range.between(15, 25);
            Range<Integer> expected = Range.between(15, 20);
            
            assertEquals(expected, range.intersectionWith(other));
        }
        
        @Test
        @DisplayName("Interseção com range idêntico deve retornar o mesmo range")
        void testIntersectionWithSameRange() {
            assertEquals(range, range.intersectionWith(range));
        }
        
        @Test
        @DisplayName("Interseção com range não sobreposto deve lançar exceção")
        void testIntersectionWithNonOverlappingRange() {
            Range<Integer> nonOverlapping = Range.between(1, 5);
            assertThrows(IllegalArgumentException.class, () -> range.intersectionWith(nonOverlapping));
        }
    }

    @Nested
    @DisplayName("Testes de métodos utilitários")
    class UtilityTests {
        private final Range<Integer> range = Range.between(10, 20);
        
        @ParameterizedTest
        @CsvSource({"5,10", "15,15", "25,20"})
        @DisplayName("fit deve ajustar valores fora do range")
        void testFit(int input, int expected) {
            assertEquals(expected, range.fit(input));
        }
        
        @Test
        @DisplayName("fit com elemento nulo deve lançar exceção")
        void testFitNull() {
            assertThrows(NullPointerException.class, () -> range.fit(null));
        }
        
        @Test
        @DisplayName("getComparator deve retornar o comparator correto")
        void testGetComparator() {
            Comparator<Integer> customComparator = Comparator.reverseOrder();
            Range<Integer> customRange = Range.between(10, 20, customComparator);
            
            assertSame(customComparator, customRange.getComparator());
            assertNotSame(customComparator, range.getComparator());
        }
        
        @Test
        @DisplayName("isNaturalOrdering deve retornar true para ordem natural")
        void testIsNaturalOrdering() {
            assertTrue(range.isNaturalOrdering());
            
            Comparator<Integer> customComparator = Comparator.reverseOrder();
            Range<Integer> customRange = Range.between(10, 20, customComparator);
            assertFalse(customRange.isNaturalOrdering());
        }
    }

    @Nested
    @DisplayName("Testes de métodos de objeto")
    class ObjectMethodsTests {
        private final Range<Integer> range1 = Range.between(10, 20);
        private final Range<Integer> range2 = Range.between(10, 20);
        private final Range<Integer> range3 = Range.between(15, 25);
        private final Range<String> stringRange = Range.between("a", "z");
        
        @Test
        @DisplayName("equals deve retornar true para ranges iguais")
        void testEquals() {
            assertEquals(range1, range2);
        }
        
        @Test
        @DisplayName("equals deve retornar false para ranges diferentes")
        void testNotEquals() {
            assertNotEquals(range1, range3);
            assertNotEquals(range1, stringRange);
        }
        
        @Test
        @DisplayName("equals deve retornar false para objetos de tipos diferentes")
        void testEqualsWithDifferentType() {
            assertNotEquals(range1, "not a range");
        }
        
        @Test
        @DisplayName("equals deve retornar true para a mesma instância")
        void testEqualsSameInstance() {
            assertEquals(range1, range1);
        }
        
        @Test
        @DisplayName("hashCode deve retornar o mesmo valor para ranges iguais")
        void testHashCode() {
            assertEquals(range1.hashCode(), range2.hashCode());
        }
        
        @Test
        @DisplayName("toString deve retornar representação string correta")
        void testToString() {
            assertEquals("[10..20]", range1.toString());
        }
        
        @Test
        @DisplayName("toString com formato customizado deve retornar string correta")
        void testToStringWithFormat() {
            String format = "Range from %1$s to %2$s with comparator %3$s";
            String result = range1.toString(format);
            assertTrue(result.startsWith("Range from 10 to 20 with comparator"));
        }
    }

    @Nested
    @DisplayName("Testes de borda e casos especiais")
    class EdgeCaseTests {
        @Test
        @DisplayName("Range com mínimo igual ao máximo")
        void testRangeWithSameMinMax() {
            Range<Integer> single = Range.between(10, 10);
            assertEquals(10, single.getMinimum());
            assertEquals(10, single.getMaximum());
            assertTrue(single.contains(10));
            assertFalse(single.contains(9));
            assertFalse(single.contains(11));
        }
        
        @Test
        @DisplayName("Teste com Double e valores especiais")
        void testDoubleSpecialValues() {
            Range<Double> doubleRange = Range.between(1.5, 3.5);
            assertFalse(doubleRange.contains(Double.NaN));
            assertFalse(doubleRange.contains(Double.POSITIVE_INFINITY));
            assertFalse(doubleRange.contains(Double.NEGATIVE_INFINITY));
        }
        
        @Test
        @DisplayName("Teste com Strings e ordenação lexicográfica")
        void testStringRange() {
            Range<String> stringRange = Range.between("apple", "banana");
            assertTrue(stringRange.contains("apricot"));
            assertFalse(stringRange.contains("cherry"));
            assertTrue(stringRange.isStartedBy("apple"));
            assertTrue(stringRange.isEndedBy("banana"));
        }
        
        @Test
        @DisplayName("Teste com comparator customizado que trata null como menor")
        void testCustomComparatorWithNulls() {
            Comparator<Integer> nullsFirstComparator = Comparator.nullsFirst(Comparator.naturalOrder());
            Range<Integer> range = Range.between(10, 20, nullsFirstComparator);
            
            // O comportamento esperado pode variar dependendo da implementação da classe Range
            // Este teste assume que a classe Range não permite elementos null internamente
            // mas pode aceitar null em métodos como contains quando usando um comparator que trata null
            assertTrue(range.contains(null));
        }
    }
}
