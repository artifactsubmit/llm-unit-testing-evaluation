package experimento.deepseek.range;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

class DeepSeek2RangeTest {

    @Nested
    @DisplayName("Testes para criação de Range")
    class CreationTests {
        @Test
        @DisplayName("Criação com elementos comparáveis - ordem normal")
        void createRangeWithComparable_NormalOrder() {
            Range<Integer> range = Range.between(1, 10);
            assertEquals(1, range.getMinimum());
            assertEquals(10, range.getMaximum());
            assertTrue(range.isNaturalOrdering());
        }

        @Test
        @DisplayName("Criação com elementos comparáveis - ordem inversa")
        void createRangeWithComparable_ReverseOrder() {
            Range<Integer> range = Range.between(10, 1);
            assertEquals(1, range.getMinimum());
            assertEquals(10, range.getMaximum());
        }

        @Test
        @DisplayName("Criação com comparator personalizado")
        void createRangeWithCustomComparator() {
            Comparator<String> lengthComparator = Comparator.comparingInt(String::length);
            Range<String> range = Range.between("a", "zzzzz", lengthComparator);
            
            assertEquals("a", range.getMinimum());
            assertEquals("zzzzz", range.getMaximum());
            assertFalse(range.isNaturalOrdering());
            assertSame(lengthComparator, range.getComparator());
        }

        @Test
        @DisplayName("Criação de range com mesmo elemento")
        void createRangeWithSameElement() {
            Range<Integer> range = Range.is(5);
            assertEquals(5, range.getMinimum());
            assertEquals(5, range.getMaximum());
        }

        @Test
        @DisplayName("Criação com elementos nulos deve lançar exceção")
        void createRangeWithNullElements_ThrowsException() {
            assertThrows(NullPointerException.class, () -> Range.between(null, 5));
            assertThrows(NullPointerException.class, () -> Range.between(5, null));
            assertThrows(NullPointerException.class, () -> Range.is(null));
        }
    }

    @Nested
    @DisplayName("Testes para método contains()")
    class ContainsTests {
        private Range<Integer> range;

        @BeforeEach
        void setUp() {
            range = Range.between(5, 10);
        }

        @Test
        @DisplayName("Elemento dentro do range")
        void elementInsideRange() {
            assertTrue(range.contains(7));
        }

        @Test
        @DisplayName("Elemento no limite inferior")
        void elementAtLowerBound() {
            assertTrue(range.contains(5));
        }

        @Test
        @DisplayName("Elemento no limite superior")
        void elementAtUpperBound() {
            assertTrue(range.contains(10));
        }

        @Test
        @DisplayName("Elemento abaixo do range")
        void elementBelowRange() {
            assertFalse(range.contains(4));
        }

        @Test
        @DisplayName("Elemento acima do range")
        void elementAboveRange() {
            assertFalse(range.contains(11));
        }

        @Test
        @DisplayName("Elemento nulo")
        void nullElement() {
            assertFalse(range.contains(null));
        }
    }

    @Nested
    @DisplayName("Testes para método containsRange()")
    class ContainsRangeTests {
        private Range<Integer> range;

        @BeforeEach
        void setUp() {
            range = Range.between(5, 10);
        }

        @Test
        @DisplayName("Range completamente contido")
        void rangeCompletelyContained() {
            Range<Integer> other = Range.between(6, 9);
            assertTrue(range.containsRange(other));
        }

        @Test
        @DisplayName("Range igual")
        void rangeEqual() {
            assertTrue(range.containsRange(range));
        }

        @Test
        @DisplayName("Range com mesmo mínimo mas máximo menor")
        void rangeSameMinimumContainedMaximum() {
            Range<Integer> other = Range.between(5, 8);
            assertTrue(range.containsRange(other));
        }

        @Test
        @DisplayName("Range com mesmo máximo mas mínimo maior")
        void rangeSameMaximumContainedMinimum() {
            Range<Integer> other = Range.between(7, 10);
            assertTrue(range.containsRange(other));
        }

        @Test
        @DisplayName("Range parcialmente contido - mínimo fora")
        void rangePartiallyContained_MinOutside() {
            Range<Integer> other = Range.between(4, 8);
            assertFalse(range.containsRange(other));
        }

        @Test
        @DisplayName("Range parcialmente contido - máximo fora")
        void rangePartiallyContained_MaxOutside() {
            Range<Integer> other = Range.between(7, 11);
            assertFalse(range.containsRange(other));
        }

        @Test
        @DisplayName("Range completamente fora - abaixo")
        void rangeCompletelyOutside_Below() {
            Range<Integer> other = Range.between(1, 4);
            assertFalse(range.containsRange(other));
        }

        @Test
        @DisplayName("Range completamente fora - acima")
        void rangeCompletelyOutside_Above() {
            Range<Integer> other = Range.between(11, 15);
            assertFalse(range.containsRange(other));
        }

        @Test
        @DisplayName("Range nulo")
        void nullRange() {
            assertFalse(range.containsRange(null));
        }
    }

    @Nested
    @DisplayName("Testes para método elementCompareTo()")
    class ElementCompareToTests {
        private Range<Integer> range;

        @BeforeEach
        void setUp() {
            range = Range.between(5, 10);
        }

        @Test
        @DisplayName("Elemento abaixo do range")
        void elementBelowRange() {
            assertEquals(1, range.elementCompareTo(3));
        }

        @Test
        @DisplayName("Elemento no limite inferior")
        void elementAtLowerBound() {
            assertEquals(0, range.elementCompareTo(5));
        }

        @Test
        @DisplayName("Elemento dentro do range")
        void elementInsideRange() {
            assertEquals(0, range.elementCompareTo(7));
        }

        @Test
        @DisplayName("Elemento no limite superior")
        void elementAtUpperBound() {
            assertEquals(0, range.elementCompareTo(10));
        }

        @Test
        @DisplayName("Elemento acima do range")
        void elementAboveRange() {
            assertEquals(-1, range.elementCompareTo(12));
        }

        @Test
        @DisplayName("Elemento nulo deve lançar exceção")
        void nullElement_ThrowsException() {
            assertThrows(NullPointerException.class, () -> range.elementCompareTo(null));
        }
    }

    @Nested
    @DisplayName("Testes para método fit()")
    class FitTests {
        private Range<Integer> range;

        @BeforeEach
        void setUp() {
            range = Range.between(5, 10);
        }

        @Test
        @DisplayName("Elemento abaixo do range")
        void elementBelowRange() {
            assertEquals(5, range.fit(3));
        }

        @Test
        @DisplayName("Elemento no limite inferior")
        void elementAtLowerBound() {
            assertEquals(5, range.fit(5));
        }

        @Test
        @DisplayName("Elemento dentro do range")
        void elementInsideRange() {
            assertEquals(7, range.fit(7));
        }

        @Test
        @DisplayName("Elemento no limite superior")
        void elementAtUpperBound() {
            assertEquals(10, range.fit(10));
        }

        @Test
        @DisplayName("Elemento acima do range")
        void elementAboveRange() {
            assertEquals(10, range.fit(12));
        }

        @Test
        @DisplayName("Elemento nulo deve lançar exceção")
        void nullElement_ThrowsException() {
            assertThrows(NullPointerException.class, () -> range.fit(null));
        }
    }

    @Nested
    @DisplayName("Testes para métodos isBefore/isAfter")
    class BeforeAfterTests {
        private Range<Integer> range;

        @BeforeEach
        void setUp() {
            range = Range.between(5, 10);
        }

        @Test
        @DisplayName("isBefore - elemento abaixo")
        void isBefore_ElementBelow() {
            assertTrue(range.isBefore(4));
        }

        @Test
        @DisplayName("isBefore - elemento no mínimo")
        void isBefore_ElementAtMinimum() {
            assertFalse(range.isBefore(5));
        }

        @Test
        @DisplayName("isBefore - elemento dentro")
        void isBefore_ElementInside() {
            assertFalse(range.isBefore(7));
        }

        @Test
        @DisplayName("isBefore - elemento no máximo")
        void isBefore_ElementAtMaximum() {
            assertFalse(range.isBefore(10));
        }

        @Test
        @DisplayName("isBefore - elemento acima")
        void isBefore_ElementAbove() {
            assertFalse(range.isBefore(11));
        }

        @Test
        @DisplayName("isAfter - elemento abaixo")
        void isAfter_ElementBelow() {
            assertFalse(range.isAfter(4));
        }

        @Test
        @DisplayName("isAfter - elemento no mínimo")
        void isAfter_ElementAtMinimum() {
            assertFalse(range.isAfter(5));
        }

        @Test
        @DisplayName("isAfter - elemento dentro")
        void isAfter_ElementInside() {
            assertFalse(range.isAfter(7));
        }

        @Test
        @DisplayName("isAfter - elemento no máximo")
        void isAfter_ElementAtMaximum() {
            assertFalse(range.isAfter(10));
        }

        @Test
        @DisplayName("isAfter - elemento acima")
        void isAfter_ElementAbove() {
            assertTrue(range.isAfter(11));
        }

        @Test
        @DisplayName("isBefore/isAfter com elemento nulo")
        void beforeAfter_NullElement() {
            assertFalse(range.isBefore(null));
            assertFalse(range.isAfter(null));
        }
    }

    @Nested
    @DisplayName("Testes para métodos isBeforeRange/isAfterRange")
    class BeforeAfterRangeTests {
        private Range<Integer> range;

        @BeforeEach
        void setUp() {
            range = Range.between(5, 10);
        }

        @Test
        @DisplayName("isBeforeRange - range completamente acima")
        void isBeforeRange_RangeCompletelyAbove() {
            Range<Integer> other = Range.between(11, 15);
            assertTrue(range.isBeforeRange(other));
        }

        @Test
        @DisplayName("isBeforeRange - range parcialmente acima")
        void isBeforeRange_RangePartiallyAbove() {
            Range<Integer> other = Range.between(8, 12);
            assertFalse(range.isBeforeRange(other));
        }

        @Test
        @DisplayName("isBeforeRange - range sobreposto")
        void isBeforeRange_RangeOverlapping() {
            Range<Integer> other = Range.between(7, 9);
            assertFalse(range.isBeforeRange(other));
        }

        @Test
        @DisplayName("isBeforeRange - range completamente abaixo")
        void isBeforeRange_RangeCompletelyBelow() {
            Range<Integer> other = Range.between(1, 4);
            assertFalse(range.isBeforeRange(other));
        }

        @Test
        @DisplayName("isAfterRange - range completamente abaixo")
        void isAfterRange_RangeCompletelyBelow() {
            Range<Integer> other = Range.between(1, 4);
            assertTrue(range.isAfterRange(other));
        }

        @Test
        @DisplayName("isAfterRange - range parcialmente abaixo")
        void isAfterRange_RangePartiallyBelow() {
            Range<Integer> other = Range.between(3, 7);
            assertFalse(range.isAfterRange(other));
        }

        @Test
        @DisplayName("isAfterRange - range sobreposto")
        void isAfterRange_RangeOverlapping() {
            Range<Integer> other = Range.between(7, 9);
            assertFalse(range.isAfterRange(other));
        }

        @Test
        @DisplayName("isAfterRange - range completamente acima")
        void isAfterRange_RangeCompletelyAbove() {
            Range<Integer> other = Range.between(11, 15);
            assertFalse(range.isAfterRange(other));
        }

        @Test
        @DisplayName("isBeforeRange/isAfterRange com range nulo")
        void beforeAfterRange_NullRange() {
            assertFalse(range.isBeforeRange(null));
            assertFalse(range.isAfterRange(null));
        }
    }

    @Nested
    @DisplayName("Testes para métodos isStartedBy/isEndedBy")
    class StartedEndedByTests {
        private Range<Integer> range;

        @BeforeEach
        void setUp() {
            range = Range.between(5, 10);
        }

        @Test
        @DisplayName("isStartedBy - elemento no mínimo")
        void isStartedBy_ElementAtMinimum() {
            assertTrue(range.isStartedBy(5));
        }

        @Test
        @DisplayName("isStartedBy - elemento não no mínimo")
        void isStartedBy_ElementNotAtMinimum() {
            assertFalse(range.isStartedBy(6));
        }

        @Test
        @DisplayName("isEndedBy - elemento no máximo")
        void isEndedBy_ElementAtMaximum() {
            assertTrue(range.isEndedBy(10));
        }

        @Test
        @DisplayName("isEndedBy - elemento não no máximo")
        void isEndedBy_ElementNotAtMaximum() {
            assertFalse(range.isEndedBy(9));
        }

        @Test
        @DisplayName("isStartedBy/isEndedBy com elemento nulo")
        void startedEndedBy_NullElement() {
            assertFalse(range.isStartedBy(null));
            assertFalse(range.isEndedBy(null));
        }
    }

    @Nested
    @DisplayName("Testes para método isOverlappedBy()")
    class IsOverlappedByTests {
        private Range<Integer> range;

        @BeforeEach
        void setUp() {
            range = Range.between(5, 10);
        }

        @Test
        @DisplayName("Range completamente contido")
        void rangeCompletelyContained() {
            Range<Integer> other = Range.between(6, 9);
            assertTrue(range.isOverlappedBy(other));
        }

        @Test
        @DisplayName("Range sobreposto no início")
        void rangeOverlappingAtStart() {
            Range<Integer> other = Range.between(3, 7);
            assertTrue(range.isOverlappedBy(other));
        }

        @Test
        @DisplayName("Range sobreposto no final")
        void rangeOverlappingAtEnd() {
            Range<Integer> other = Range.between(8, 12);
            assertTrue(range.isOverlappedBy(other));
        }

        @Test
        @DisplayName("Range igual")
        void rangeEqual() {
            assertTrue(range.isOverlappedBy(range));
        }

        @Test
        @DisplayName("Range adjacente no início")
        void rangeAdjacentAtStart() {
            Range<Integer> other = Range.between(3, 5);
            assertTrue(range.isOverlappedBy(other));
        }

        @Test
        @DisplayName("Range adjacente no final")
        void rangeAdjacentAtEnd() {
            Range<Integer> other = Range.between(10, 12);
            assertTrue(range.isOverlappedBy(other));
        }

        @Test
        @DisplayName("Range completamente abaixo")
        void rangeCompletelyBelow() {
            Range<Integer> other = Range.between(1, 4);
            assertFalse(range.isOverlappedBy(other));
        }

        @Test
        @DisplayName("Range completamente acima")
        void rangeCompletelyAbove() {
            Range<Integer> other = Range.between(11, 15);
            assertFalse(range.isOverlappedBy(other));
        }

        @Test
        @DisplayName("Range nulo")
        void nullRange() {
            assertFalse(range.isOverlappedBy(null));
        }
    }

    @Nested
    @DisplayName("Testes para método intersectionWith()")
    class IntersectionWithTests {
        private Range<Integer> range;

        @BeforeEach
        void setUp() {
            range = Range.between(5, 10);
        }

        @Test
        @DisplayName("Interseção com range completamente contido")
        void intersectionWithCompletelyContainedRange() {
            Range<Integer> other = Range.between(6, 9);
            Range<Integer> result = range.intersectionWith(other);
            assertEquals(6, result.getMinimum());
            assertEquals(9, result.getMaximum());
        }

        @Test
        @DisplayName("Interseção com range sobreposto no início")
        void intersectionWithRangeOverlappingAtStart() {
            Range<Integer> other = Range.between(3, 7);
            Range<Integer> result = range.intersectionWith(other);
            assertEquals(5, result.getMinimum());
            assertEquals(7, result.getMaximum());
        }

        @Test
        @DisplayName("Interseção com range sobreposto no final")
        void intersectionWithRangeOverlappingAtEnd() {
            Range<Integer> other = Range.between(8, 12);
            Range<Integer> result = range.intersectionWith(other);
            assertEquals(8, result.getMinimum());
            assertEquals(10, result.getMaximum());
        }

        @Test
        @DisplayName("Interseção com range igual")
        void intersectionWithEqualRange() {
            Range<Integer> result = range.intersectionWith(range);
            assertEquals(5, result.getMinimum());
            assertEquals(10, result.getMaximum());
        }

        @Test
        @DisplayName("Interseção com range não sobreposto deve lançar exceção")
        void intersectionWithNonOverlappingRange_ThrowsException() {
            Range<Integer> other = Range.between(1, 4);
            assertThrows(IllegalArgumentException.class, () -> range.intersectionWith(other));
        }

        @Test
        @DisplayName("Interseção com range nulo deve lançar exceção")
        void intersectionWithNullRange_ThrowsException() {
            assertThrows(NullPointerException.class, () -> range.intersectionWith(null));
        }
    }

    @Nested
    @DisplayName("Testes para equals() e hashCode()")
    class EqualsAndHashCodeTests {
        @Test
        @DisplayName("Igualdade com mesmo range")
        void equalityWithSameRange() {
            Range<Integer> range1 = Range.between(5, 10);
            Range<Integer> range2 = Range.between(5, 10);
            assertEquals(range1, range2);
            assertEquals(range1.hashCode(), range2.hashCode());
        }

        @Test
        @DisplayName("Igualdade com range criado em ordem inversa")
        void equalityWithReverseOrderRange() {
            Range<Integer> range1 = Range.between(5, 10);
            Range<Integer> range2 = Range.between(10, 5);
            assertEquals(range1, range2);
            assertEquals(range1.hashCode(), range2.hashCode());
        }

        @Test
        @DisplayName("Desigualdade com range diferente")
        void inequalityWithDifferentRange() {
            Range<Integer> range1 = Range.between(5, 10);
            Range<Integer> range2 = Range.between(6, 10);
            assertNotEquals(range1, range2);
        }

        @Test
        @DisplayName("Desigualdade com tipo diferente")
        void inequalityWithDifferentType() {
            Range<Integer> range = Range.between(5, 10);
            assertNotEquals(range, "not a range");
        }

        @Test
        @DisplayName("Desigualdade com nulo")
        void inequalityWithNull() {
            Range<Integer> range = Range.between(5, 10);
            assertNotEquals(null, range);
        }
    }

    @Nested
    @DisplayName("Testes para toString()")
    class ToStringTests {
        @Test
        @DisplayName("toString() padrão")
        void defaultToString() {
            Range<Integer> range = Range.between(5, 10);
            assertEquals("[5..10]", range.toString());
        }

        @Test
        @DisplayName("toString() com formato personalizado")
        void toStringWithCustomFormat() {
            Range<Integer> range = Range.between(5, 10);
            String result = range.toString("Range from %1$s to %2$s");
            assertEquals("Range from 5 to 10", result);
        }
    }

    @Nested
    @DisplayName("Testes com tipos complexos e comparadores personalizados")
    class ComplexTypeTests {
        static class Person {
            String name;
            int age;

            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }

            @Override
            public String toString() {
                return name + " (" + age + ")";
            }
        }

        @Test
        @DisplayName("Range com objetos complexos e comparator personalizado")
        void rangeWithComplexObjectsAndCustomComparator() {
            Comparator<Person> ageComparator = Comparator.comparingInt(p -> p.age);
            Person young = new Person("Alice", 20);
            Person old = new Person("Bob", 60);
            
            Range<Person> range = Range.between(young, old, ageComparator);
            
            Person middleAged = new Person("Charlie", 40);
            Person tooYoung = new Person("Dave", 15);
            Person tooOld = new Person("Eve", 65);
            
            assertTrue(range.contains(middleAged));
            assertFalse(range.contains(tooYoung));
            assertFalse(range.contains(tooOld));
            assertEquals(young, range.fit(tooYoung));
            assertEquals(old, range.fit(tooOld));
        }
    }
}
