package experimento.deepseek.range;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class DeepSeek4RangeTest {

    @Nested
    @DisplayName("Testes de construção da Range")
    class ConstructionTests {
        
        @Test
        @DisplayName("Criação com valores mínimo e máximo (ordem natural)")
        void testBetweenWithNaturalOrdering() {
            Range<Integer> range = Range.between(10, 20);
            assertEquals(10, range.getMinimum());
            assertEquals(20, range.getMaximum());
            assertTrue(range.isNaturalOrdering());
        }
        
        @Test
        @DisplayName("Criação com valores invertidos (ordem natural)")
        void testBetweenWithInvertedValuesNaturalOrdering() {
            Range<Integer> range = Range.between(20, 10);
            assertEquals(10, range.getMinimum());
            assertEquals(20, range.getMaximum());
        }
        
        @Test
        @DisplayName("Criação com comparador customizado")
        void testBetweenWithCustomComparator() {
            Comparator<String> lengthComparator = Comparator.comparingInt(String::length);
            Range<String> range = Range.between("a", "zz", lengthComparator);
            
            assertEquals("a", range.getMinimum());
            assertEquals("zz", range.getMaximum());
            assertFalse(range.isNaturalOrdering());
            assertSame(lengthComparator, range.getComparator());
        }
        
        @Test
        @DisplayName("Criação com valores invertidos e comparador customizado")
        void testBetweenWithInvertedValuesCustomComparator() {
            Comparator<String> reverseComparator = Comparator.reverseOrder();
            Range<String> range = Range.between("z", "a", reverseComparator);
            
            assertEquals("z", range.getMinimum());
            assertEquals("a", range.getMaximum());
        }
        
        @Test
        @DisplayName("Criação de range com único elemento (ordem natural)")
        void testIsWithNaturalOrdering() {
            Range<Integer> range = Range.is(15);
            assertEquals(15, range.getMinimum());
            assertEquals(15, range.getMaximum());
            assertTrue(range.isNaturalOrdering());
        }
        
        @Test
        @DisplayName("Criação de range com único elemento e comparador customizado")
        void testIsWithCustomComparator() {
            Comparator<String> lengthComparator = Comparator.comparingInt(String::length);
            Range<String> range = Range.is("test", lengthComparator);
            
            assertEquals("test", range.getMinimum());
            assertEquals("test", range.getMaximum());
            assertSame(lengthComparator, range.getComparator());
        }
        
        @Test
        @DisplayName("Lançamento de NullPointerException para elementos nulos")
        void testNullElementsThrowsException() {
            assertThrows(NullPointerException.class, () -> Range.between(null, 10));
            assertThrows(NullPointerException.class, () -> Range.between(10, null));
            assertThrows(NullPointerException.class, () -> Range.between(null, null));
            assertThrows(NullPointerException.class, () -> Range.is(null));
        }
    }

    @Nested
    @DisplayName("Testes de métodos de verificação")
    class VerificationTests {
        private final Range<Integer> range = Range.between(10, 20);
        
        @Test
        @DisplayName("Teste de contains para valores dentro do range")
        void testContainsWithinRange() {
            assertTrue(range.contains(10));
            assertTrue(range.contains(15));
            assertTrue(range.contains(20));
        }
        
        @Test
        @DisplayName("Teste de contains para valores fora do range")
        void testContainsOutsideRange() {
            assertFalse(range.contains(9));
            assertFalse(range.contains(21));
            assertFalse(range.contains(null));
        }
        
        @Test
        @DisplayName("Teste de isAfter para vários valores")
        void testIsAfter() {
            assertFalse(range.isAfter(10));
            assertFalse(range.isAfter(15));
            assertFalse(range.isAfter(20));
            assertTrue(range.isAfter(9));
            assertFalse(range.isAfter(21));
            assertFalse(range.isAfter(null));
        }
        
        @Test
        @DisplayName("Teste de isBefore para vários valores")
        void testIsBefore() {
            assertFalse(range.isBefore(10));
            assertFalse(range.isBefore(15));
            assertFalse(range.isBefore(20));
            assertFalse(range.isBefore(9));
            assertTrue(range.isBefore(21));
            assertFalse(range.isBefore(null));
        }
        
        @Test
        @DisplayName("Teste de isStartedBy e isEndedBy")
        void testIsStartedByAndIsEndedBy() {
            assertTrue(range.isStartedBy(10));
            assertFalse(range.isStartedBy(11));
            assertFalse(range.isStartedBy(null));
            
            assertTrue(range.isEndedBy(20));
            assertFalse(range.isEndedBy(19));
            assertFalse(range.isEndedBy(null));
        }
        
        @Test
        @DisplayName("Teste de elementCompareTo")
        void testElementCompareTo() {
            assertEquals(0, range.elementCompareTo(15));
            assertEquals(-1, range.elementCompareTo(5));
            assertEquals(1, range.elementCompareTo(25));
            assertThrows(NullPointerException.class, () -> range.elementCompareTo(null));
        }
    }

    @Nested
    @DisplayName("Testes de operações entre ranges")
    class RangeOperationsTests {
        private final Range<Integer> range = Range.between(10, 20);
        
        @Test
        @DisplayName("Teste de containsRange")
        void testContainsRange() {
            Range<Integer> insideRange = Range.between(12, 18);
            Range<Integer> overlappingRange = Range.between(15, 25);
            Range<Integer> outsideRange = Range.between(1, 5);
            Range<Integer> equalRange = Range.between(10, 20);
            
            assertTrue(range.containsRange(insideRange));
            assertFalse(range.containsRange(overlappingRange));
            assertFalse(range.containsRange(outsideRange));
            assertTrue(range.containsRange(equalRange));
            assertFalse(range.containsRange(null));
        }
        
        @Test
        @DisplayName("Teste de isAfterRange")
        void testIsAfterRange() {
            Range<Integer> beforeRange = Range.between(1, 5);
            Range<Integer> touchingRange = Range.between(1, 10);
            Range<Integer> overlappingRange = Range.between(5, 15);
            
            assertTrue(range.isAfterRange(beforeRange));
            assertFalse(range.isAfterRange(touchingRange));
            assertFalse(range.isAfterRange(overlappingRange));
            assertFalse(range.isAfterRange(null));
        }
        
        @Test
        @DisplayName("Teste de isBeforeRange")
        void testIsBeforeRange() {
            Range<Integer> afterRange = Range.between(25, 30);
            Range<Integer> touchingRange = Range.between(20, 30);
            Range<Integer> overlappingRange = Range.between(15, 25);
            
            assertTrue(range.isBeforeRange(afterRange));
            assertFalse(range.isBeforeRange(touchingRange));
            assertFalse(range.isBeforeRange(overlappingRange));
            assertFalse(range.isBeforeRange(null));
        }
        
        @Test
        @DisplayName("Teste de isOverlappedBy")
        void testIsOverlappedBy() {
            Range<Integer> overlappingStart = Range.between(5, 15);
            Range<Integer> overlappingEnd = Range.between(15, 25);
            Range<Integer> inside = Range.between(12, 18);
            Range<Integer> outside = Range.between(1, 5);
            Range<Integer> same = Range.between(10, 20);
            
            assertTrue(range.isOverlappedBy(overlappingStart));
            assertTrue(range.isOverlappedBy(overlappingEnd));
            assertTrue(range.isOverlappedBy(inside));
            assertFalse(range.isOverlappedBy(outside));
            assertTrue(range.isOverlappedBy(same));
            assertFalse(range.isOverlappedBy(null));
        }
        
        @Test
        @DisplayName("Teste de intersectionWith")
        void testIntersectionWith() {
            Range<Integer> overlappingStart = Range.between(5, 15);
            Range<Integer> overlappingEnd = Range.between(15, 25);
            Range<Integer> inside = Range.between(12, 18);
            
            assertEquals(Range.between(10, 15), range.intersectionWith(overlappingStart));
            assertEquals(Range.between(15, 20), range.intersectionWith(overlappingEnd));
            assertEquals(Range.between(12, 18), range.intersectionWith(inside));
            assertEquals(range, range.intersectionWith(range));
            
            Range<Integer> nonOverlapping = Range.between(1, 5);
            assertThrows(IllegalArgumentException.class, () -> range.intersectionWith(nonOverlapping));
        }
    }

    @Nested
    @DisplayName("Testes de métodos utilitários")
    class UtilityTests {
        private final Range<Integer> range = Range.between(10, 20);
        
        @Test
        @DisplayName("Teste de fit")
        void testFit() {
            assertEquals(10, range.fit(5));
            assertEquals(10, range.fit(10));
            assertEquals(15, range.fit(15));
            assertEquals(20, range.fit(20));
            assertEquals(20, range.fit(25));
            assertThrows(NullPointerException.class, () -> range.fit(null));
        }
        
        @Test
        @DisplayName("Teste de equals e hashCode")
        void testEqualsAndHashCode() {
            Range<Integer> sameRange = Range.between(10, 20);
            Range<Integer> differentRange = Range.between(5, 15);
            Range<Integer> sameValuesDifferentOrder = Range.between(20, 10);
            
            assertEquals(range, sameRange);
            assertEquals(range.hashCode(), sameRange.hashCode());
            assertNotEquals(range, differentRange);
            assertEquals(range, sameValuesDifferentOrder);
            assertNotEquals(range, null);
            assertNotEquals(range, "not a range");
        }
        
        @Test
        @DisplayName("Teste de toString")
        void testToString() {
            assertEquals("[10..20]", range.toString());
            assertEquals("Range from 10 to 20", range.toString("Range from %1$s to %2$s"));
        }
    }

    @Nested
    @DisplayName("Testes com tipos complexos e comparadores customizados")
    class ComplexTypeTests {
        static class Person {
            final String name;
            final int age;
            
            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
            
            @Override
            public String toString() {
                return name;
            }
        }
        
        private final Comparator<Person> ageComparator = Comparator.comparingInt(p -> p.age);
        private final Person young = new Person("Young", 20);
        private final Person middle = new Person("Middle", 40);
        private final Person old = new Person("Old", 60);
        private final Range<Person> ageRange = Range.between(young, old, ageComparator);
        
        @Test
        @DisplayName("Teste de range com objetos complexos")
        void testComplexObjectRange() {
            assertTrue(ageRange.contains(middle));
            assertFalse(ageRange.contains(new Person("TooYoung", 10)));
            assertFalse(ageRange.contains(new Person("TooOld", 70)));
        }
        
        @Test
        @DisplayName("Teste de fit com objetos complexos")
        void testFitWithComplexObjects() {
            Person tooYoung = new Person("TooYoung", 10);
            Person tooOld = new Person("TooOld", 70);
            
            assertSame(young, ageRange.fit(tooYoung));
            assertSame(old, ageRange.fit(tooOld));
            assertSame(middle, ageRange.fit(middle));
        }
        
        @Test
        @DisplayName("Teste de operações entre ranges com objetos complexos")
        void testRangeOperationsWithComplexObjects() {
            Person middleAged1 = new Person("Middle1", 30);
            Person middleAged2 = new Person("Middle2", 50);
            Range<Person> middleRange = Range.between(middleAged1, middleAged2, ageComparator);
            
            assertTrue(ageRange.containsRange(middleRange));
            assertTrue(ageRange.isOverlappedBy(middleRange));
            assertEquals(middleRange, ageRange.intersectionWith(middleRange));
        }
    }

    @Nested
    @DisplayName("Testes de mutação (PIT)")
    class MutationTests {
        @Test
        @DisplayName("Teste de hashCode com cache")
        void testHashCodeCache() {
            Range<Integer> range = Range.between(1, 10);
            int firstHash = range.hashCode();
            int secondHash = range.hashCode();
            assertEquals(firstHash, secondHash);
        }
        
        @Test
        @DisplayName("Teste de toString com cache")
        void testToStringCache() {
            Range<Integer> range = Range.between(1, 10);
            String firstString = range.toString();
            String secondString = range.toString();
            assertSame(firstString, secondString);
        }
        
        @Test
        @DisplayName("Teste de equals com diferentes tipos")
        void testEqualsWithDifferentTypes() {
            Range<Integer> range = Range.between(1, 10);
            assertNotEquals(range, new Object());
        }
        
        @ParameterizedTest
        @ValueSource(ints = {5, 10, 15, 20})
        @DisplayName("Teste paramétrico de contains")
        void parametricContainsTest(int value) {
            Range<Integer> range = Range.between(10, 20);
            boolean expected = value >= 10 && value <= 20;
            assertEquals(expected, range.contains(value));
        }
        
        @ParameterizedTest
        @CsvSource({
            "5, 15, true",
            "15, 25, true",
            "1, 5, false",
            "25, 30, false",
            "10, 20, true"
        })
        @DisplayName("Teste paramétrico de isOverlappedBy")
        void parametricIsOverlappedByTest(int from, int to, boolean expected) {
            Range<Integer> range = Range.between(10, 20);
            Range<Integer> other = Range.between(from, to);
            assertEquals(expected, range.isOverlappedBy(other));
        }
    }
}
