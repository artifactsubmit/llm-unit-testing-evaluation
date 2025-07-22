package experimento.deepseek.range;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

class DeepSeek5RangeTest {

    @Nested
    @DisplayName("Testes para criação de Range")
    class CreationTests {
        @Test
        @DisplayName("Criação com valores mínimo e máximo (ordem natural)")
        void testBetweenWithNaturalOrder() {
            Range<Integer> range = Range.between(10, 20);
            assertEquals(10, range.getMinimum());
            assertEquals(20, range.getMaximum());
            assertTrue(range.isNaturalOrdering());
        }

        @Test
        @DisplayName("Criação com valores mínimo e máximo (ordem inversa)")
        void testBetweenWithInvertedOrder() {
            Range<Integer> range = Range.between(20, 10);
            assertEquals(10, range.getMinimum());
            assertEquals(20, range.getMaximum());
        }

        @Test
        @DisplayName("Criação com comparator customizado")
        void testBetweenWithCustomComparator() {
            Comparator<String> lengthComparator = Comparator.comparingInt(String::length);
            Range<String> range = Range.between("a", "zzzz", lengthComparator);
            
            assertEquals("a", range.getMinimum());
            assertEquals("zzzz", range.getMaximum());
            assertFalse(range.isNaturalOrdering());
            assertSame(lengthComparator, range.getComparator());
        }

        @Test
        @DisplayName("Criação de range com único elemento")
        void testIs() {
            Range<Integer> range = Range.is(15);
            assertEquals(15, range.getMinimum());
            assertEquals(15, range.getMaximum());
        }

        @Test
        @DisplayName("Criação deve lançar exceção para elementos nulos")
        void testCreationWithNullElements() {
            assertThrows(NullPointerException.class, () -> Range.between(null, 10));
            assertThrows(NullPointerException.class, () -> Range.between(10, null));
            assertThrows(NullPointerException.class, () -> Range.is(null));
        }
    }

    @Nested
    @DisplayName("Testes para métodos de verificação")
    class VerificationTests {
        private Range<Integer> range;

        @BeforeEach
        void setUp() {
            range = Range.between(10, 20);
        }

        @Test
        @DisplayName("contains deve retornar true para elementos dentro do range")
        void testContainsWithinRange() {
            assertTrue(range.contains(10));
            assertTrue(range.contains(15));
            assertTrue(range.contains(20));
        }

        @Test
        @DisplayName("contains deve retornar false para elementos fora do range")
        void testContainsOutsideRange() {
            assertFalse(range.contains(9));
            assertFalse(range.contains(21));
            assertFalse(range.contains(null));
        }

        @Test
        @DisplayName("containsRange deve verificar se outro range está contido")
        void testContainsRange() {
            Range<Integer> insideRange = Range.between(12, 18);
            Range<Integer> outsideRange = Range.between(8, 22);
            Range<Integer> overlappingStart = Range.between(8, 15);
            Range<Integer> overlappingEnd = Range.between(15, 25);

            assertTrue(range.containsRange(insideRange));
            assertFalse(range.containsRange(outsideRange));
            assertFalse(range.containsRange(overlappingStart));
            assertFalse(range.containsRange(overlappingEnd));
            assertFalse(range.containsRange(null));
        }

        @Test
        @DisplayName("elementCompareTo deve indicar posição relativa do elemento")
        void testElementCompareTo() {
            assertEquals(0, range.elementCompareTo(15));
            assertEquals(1, range.elementCompareTo(5));
            assertEquals(-1, range.elementCompareTo(25));
            assertThrows(NullPointerException.class, () -> range.elementCompareTo(null));
        }
    }

    @Nested
    @DisplayName("Testes para métodos de posicionamento relativo")
    class PositioningTests {
        private Range<Integer> range;

        @BeforeEach
        void setUp() {
            range = Range.between(10, 20);
        }

        @Test
        @DisplayName("isAfter deve verificar se range está após elemento")
        void testIsAfter() {
            assertTrue(range.isAfter(5));
            assertFalse(range.isAfter(10));
            assertFalse(range.isAfter(15));
            assertFalse(range.isAfter(25));
            assertFalse(range.isAfter(null));
        }

        @Test
        @DisplayName("isAfterRange deve verificar se range está após outro range")
        void testIsAfterRange() {
            Range<Integer> beforeRange = Range.between(1, 5);
            Range<Integer> overlappingRange = Range.between(5, 15);
            Range<Integer> afterRange = Range.between(25, 30);

            assertTrue(range.isAfterRange(beforeRange));
            assertFalse(range.isAfterRange(overlappingRange));
            assertFalse(range.isAfterRange(afterRange));
            assertFalse(range.isAfterRange(null));
        }

        @Test
        @DisplayName("isBefore deve verificar se range está antes do elemento")
        void testIsBefore() {
            assertFalse(range.isBefore(5));
            assertFalse(range.isBefore(10));
            assertFalse(range.isBefore(15));
            assertTrue(range.isBefore(25));
            assertFalse(range.isBefore(null));
        }

        @Test
        @DisplayName("isBeforeRange deve verificar se range está antes de outro range")
        void testIsBeforeRange() {
            Range<Integer> beforeRange = Range.between(1, 5);
            Range<Integer> overlappingRange = Range.between(15, 25);
            Range<Integer> afterRange = Range.between(25, 30);

            assertFalse(range.isBeforeRange(beforeRange));
            assertFalse(range.isBeforeRange(overlappingRange));
            assertTrue(range.isBeforeRange(afterRange));
            assertFalse(range.isBeforeRange(null));
        }

        @Test
        @DisplayName("isStartedBy e isEndedBy devem verificar limites do range")
        void testBoundaryChecks() {
            assertTrue(range.isStartedBy(10));
            assertFalse(range.isStartedBy(15));
            assertFalse(range.isStartedBy(20));
            assertFalse(range.isStartedBy(null));

            assertFalse(range.isEndedBy(10));
            assertFalse(range.isEndedBy(15));
            assertTrue(range.isEndedBy(20));
            assertFalse(range.isEndedBy(null));
        }
    }

    @Nested
    @DisplayName("Testes para métodos de sobreposição e interseção")
    class OverlapTests {
        private Range<Integer> range;

        @BeforeEach
        void setUp() {
            range = Range.between(10, 20);
        }

        @Test
        @DisplayName("isOverlappedBy deve detectar sobreposição com outros ranges")
        void testIsOverlappedBy() {
            Range<Integer> noOverlapBefore = Range.between(1, 5);
            Range<Integer> noOverlapAfter = Range.between(25, 30);
            Range<Integer> overlapStart = Range.between(5, 15);
            Range<Integer> overlapEnd = Range.between(15, 25);
            Range<Integer> contained = Range.between(12, 18);
            Range<Integer> containing = Range.between(5, 25);

            assertFalse(range.isOverlappedBy(noOverlapBefore));
            assertFalse(range.isOverlappedBy(noOverlapAfter));
            assertTrue(range.isOverlappedBy(overlapStart));
            assertTrue(range.isOverlappedBy(overlapEnd));
            assertTrue(range.isOverlappedBy(contained));
            assertTrue(range.isOverlappedBy(containing));
            assertFalse(range.isOverlappedBy(null));
        }

        @Test
        @DisplayName("intersectionWith deve calcular interseção corretamente")
        void testIntersectionWith() {
            Range<Integer> overlapStart = Range.between(5, 15);
            Range<Integer> overlapEnd = Range.between(15, 25);
            Range<Integer> contained = Range.between(12, 18);
            Range<Integer> same = Range.between(10, 20);

            assertEquals(Range.between(10, 15), range.intersectionWith(overlapStart));
            assertEquals(Range.between(15, 20), range.intersectionWith(overlapEnd));
            assertEquals(contained, range.intersectionWith(contained));
            assertEquals(range, range.intersectionWith(same));

            Range<Integer> noOverlap = Range.between(1, 5);
            assertThrows(IllegalArgumentException.class, () -> range.intersectionWith(noOverlap));
        }
    }

    @Nested
    @DisplayName("Testes para métodos utilitários")
    class UtilityTests {
        @Test
        @DisplayName("fit deve ajustar valores aos limites do range")
        void testFit() {
            Range<Integer> range = Range.between(10, 20);
            
            assertEquals(10, range.fit(5));
            assertEquals(10, range.fit(10));
            assertEquals(15, range.fit(15));
            assertEquals(20, range.fit(20));
            assertEquals(20, range.fit(25));
            
            assertThrows(NullPointerException.class, () -> range.fit(null));
        }

        @Test
        @DisplayName("equals e hashCode devem funcionar corretamente")
        void testEqualsAndHashCode() {
            Range<Integer> range1 = Range.between(10, 20);
            Range<Integer> range2 = Range.between(10, 20);
            Range<Integer> range3 = Range.between(15, 25);
            Range<Integer> range4 = Range.between(10, 20, Comparator.reverseOrder());

            assertEquals(range1, range2);
            assertNotEquals(range1, range3);
            assertNotEquals(range1, range4);
            assertEquals(range1.hashCode(), range2.hashCode());
        }

        @Test
        @DisplayName("toString deve retornar representação adequada")
        void testToString() {
            Range<Integer> range = Range.between(10, 20);
            assertEquals("[10..20]", range.toString());
            assertEquals("Range from 10 to 20 with comparator", 
                range.toString("Range from %1$s to %2$s with comparator %3$s"));
        }
    }

    @Nested
    @DisplayName("Testes com tipos customizados e comparators")
    class CustomTypeTests {
        static class Person implements Comparable<Person> {
            final String name;
            final int age;

            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }

            @Override
            public int compareTo(Person other) {
                return Integer.compare(this.age, other.age);
            }
        }

        @Test
        @DisplayName("Teste com tipo customizado e ordenação natural")
        void testWithCustomTypeNaturalOrder() {
            Person young = new Person("Alice", 20);
            Person middle = new Person("Bob", 30);
            Person old = new Person("Charlie", 40);

            Range<Person> range = Range.between(young, old);
            
            assertTrue(range.contains(middle));
            assertFalse(range.contains(new Person("Dave", 15)));
            assertEquals(young, range.getMinimum());
            assertEquals(old, range.getMaximum());
        }

        @Test
        @DisplayName("Teste com tipo customizado e comparator")
        void testWithCustomTypeAndComparator() {
            Comparator<Person> nameComparator = Comparator.comparing(p -> p.name);
            Person a = new Person("Alice", 40);
            Person b = new Person("Bob", 30);
            Person c = new Person("Charlie", 20);

            Range<Person> range = Range.between(a, c, nameComparator);
            
            assertTrue(range.contains(b));
            assertFalse(range.contains(new Person("Dave", 25)));
            assertEquals(a, range.getMinimum());
            assertEquals(c, range.getMaximum());
            assertSame(nameComparator, range.getComparator());
        }
    }
}
