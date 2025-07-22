package experimento.deepseek.binarycodec;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import java.util.stream.Stream;

class DeepSeek4BinaryCodecTest {

    // Test data providers
    static Stream<byte[]> binaryDataProvider() {
        return Stream.of(
                new byte[]{0},
                new byte[]{1},
                new byte[]{-1}, // 0xFF
                new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                new byte[]{Byte.MIN_VALUE, Byte.MAX_VALUE}
        );
    }

    static Stream<String> asciiDataProvider() {
        return Stream.of(
                "00000000",
                "11111111",
                "10101010",
                "01010101",
                "0000000011111111",
                "0000000000000000",
                "1111111111111111"
        );
    }

    // Test cases for encode(byte[] raw)
    @ParameterizedTest
    @NullAndEmptySource
    void testEncodeByteArrayWithNullOrEmptyInput(byte[] input) {
        BinaryCodec codec = new BinaryCodec();
        assertArrayEquals(BinaryCodec.EMPTY_BYTE_ARRAY, codec.encode(input));
    }

    @ParameterizedTest
    @MethodSource("binaryDataProvider")
    void testEncodeByteArrayWithValidInput(byte[] input) {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.encode(input);

        // Verify the length is correct (8 bits per byte)
        assertEquals(input.length * 8, result.length);

        // Verify all bytes are either '0' or '1' ASCII
        for (byte b : result) {
            assertTrue(b == '0' || b == '1');
        }
    }

    // Test cases for encode(Object raw)
    @Test
    void testEncodeObjectWithInvalidType() {
        BinaryCodec codec = new BinaryCodec();
        assertThrows(EncoderException.class, () -> codec.encode("invalid type"));
    }

    @ParameterizedTest
    @MethodSource("binaryDataProvider")
    void testEncodeObjectWithValidByteArray(byte[] input) {
        BinaryCodec codec = new BinaryCodec();
        try {
            char[] result = (char[]) codec.encode(input);
            assertEquals(input.length * 8, result.length);
            for (char c : result) {
                assertTrue(c == '0' || c == '1');
            }
        } catch (EncoderException e) {
            fail("Should not throw exception for valid input");
        }
    }

    // Test cases for decode(Object ascii)
    @Test
    void testDecodeObjectWithNullInput() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        assertArrayEquals(BinaryCodec.EMPTY_BYTE_ARRAY, (byte[]) codec.decode(null));
    }

    @Test
    void testDecodeObjectWithInvalidType() {
        BinaryCodec codec = new BinaryCodec();
        assertThrows(DecoderException.class, () -> codec.decode(123));
    }

    @ParameterizedTest
    @MethodSource("asciiDataProvider")
    void testDecodeObjectWithValidStringInput(String input) throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = (byte[]) codec.decode(input);
        assertEquals(input.length() / 8, result.length);
    }

    // Test cases for decode(byte[] ascii)
    @ParameterizedTest
    @NullAndEmptySource
    void testDecodeByteArrayWithNullOrEmptyInput(byte[] input) {
        BinaryCodec codec = new BinaryCodec();
        assertArrayEquals(BinaryCodec.EMPTY_BYTE_ARRAY, codec.decode(input));
    }

    @Test
    void testDecodeByteArrayWithInvalidCharacters() {
        BinaryCodec codec = new BinaryCodec();
        byte[] invalidInput = {'0', '1', '2', '0'}; // '2' is invalid
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> codec.decode(invalidInput));
    }

    // Test cases for toByteArray(String ascii)
    @ParameterizedTest
    @NullAndEmptySource
    void testToByteArrayWithNullOrEmptyInput(String input) {
        BinaryCodec codec = new BinaryCodec();
        assertArrayEquals(BinaryCodec.EMPTY_BYTE_ARRAY, codec.toByteArray(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"00000000", "11111111", "10101010", "01010101"})
    void testToByteArrayWithValidInput(String input) {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.toByteArray(input);
        assertEquals(input.length() / 8, result.length);
    }

    // Test cases for fromAscii(byte[] ascii)
    @ParameterizedTest
    @NullAndEmptySource
    void testFromAsciiByteArrayWithNullOrEmptyInput(byte[] input) {
        assertArrayEquals(BinaryCodec.EMPTY_BYTE_ARRAY, BinaryCodec.fromAscii(input));
    }

    @Test
    void testFromAsciiByteArrayWithValidInput() {
        byte[] input = {'1', '0', '1', '0', '1', '0', '1', '0'}; // Represents 0xAA
        byte[] result = BinaryCodec.fromAscii(input);
        assertEquals(1, result.length);
        assertEquals((byte) 0xAA, result[0]);
    }

    // Test cases for fromAscii(char[] ascii)
    @ParameterizedTest
    @NullAndEmptySource
    void testFromAsciiCharArrayWithNullOrEmptyInput(char[] input) {
        assertArrayEquals(BinaryCodec.EMPTY_BYTE_ARRAY, BinaryCodec.fromAscii(input));
    }

    @Test
    void testFromAsciiCharArrayWithValidInput() {
        char[] input = {'0', '1', '0', '1', '0', '1', '0', '1'}; // Represents 0x55
        byte[] result = BinaryCodec.fromAscii(input);
        assertEquals(1, result.length);
        assertEquals((byte) 0x55, result[0]);
    }

    // Test cases for toAsciiBytes(byte[] raw)
    @ParameterizedTest
    @NullAndEmptySource
    void testToAsciiBytesWithNullOrEmptyInput(byte[] input) {
        assertArrayEquals(BinaryCodec.EMPTY_BYTE_ARRAY, BinaryCodec.toAsciiBytes(input));
    }

    @Test
    void testToAsciiBytesWithSingleByte() {
        byte[] input = {(byte) 0xAA}; // 10101010
        byte[] expected = {'1', '0', '1', '0', '1', '0', '1', '0'};
        assertArrayEquals(expected, BinaryCodec.toAsciiBytes(input));
    }

    // Test cases for toAsciiChars(byte[] raw)
    @ParameterizedTest
    @NullAndEmptySource
    void testToAsciiCharsWithNullOrEmptyInput(byte[] input) {
        assertArrayEquals(BinaryCodec.EMPTY_CHAR_ARRAY, BinaryCodec.toAsciiChars(input));
    }

    @Test
    void testToAsciiCharsWithSingleByte() {
        byte[] input = {(byte) 0x55}; // 01010101
        char[] expected = {'0', '1', '0', '1', '0', '1', '0', '1'};
        assertArrayEquals(expected, BinaryCodec.toAsciiChars(input));
    }

    // Test cases for toAsciiString(byte[] raw)
    @ParameterizedTest
    @NullAndEmptySource
    void testToAsciiStringWithNullOrEmptyInput(byte[] input) {
        assertEquals("", BinaryCodec.toAsciiString(input));
    }

    @Test
    void testToAsciiStringWithSingleByte() {
        byte[] input = {(byte) 0xF0}; // 11110000
        String expected = "11110000";
        assertEquals(expected, BinaryCodec.toAsciiString(input));
    }

    // Round-trip tests
    @ParameterizedTest
    @MethodSource("binaryDataProvider")
    void testEncodeDecodeRoundTrip(byte[] input) {
        BinaryCodec codec = new BinaryCodec();
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    @ParameterizedTest
    @MethodSource("asciiDataProvider")
    void testDecodeEncodeRoundTrip(String input) throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        byte[] decoded = (byte[]) codec.decode(input);
        String encoded = BinaryCodec.toAsciiString(decoded);
        assertEquals(input, encoded);
    }

    // Edge case tests
    @Test
    void testFromAsciiWithIncompleteByte() {
        // 7 bits (should be padded with leading zeros)
        char[] input = {'1', '0', '1', '0', '1', '0', '1'};
        byte[] result = BinaryCodec.fromAscii(input);
        assertEquals(1, result.length);
        assertEquals((byte) 0xAA, result[0]); // Same as 10101010 (last 0 is implied)
    }

    @Test
    void testToAsciiBytesWithAllBitsSet() {
        byte[] input = {(byte) 0xFF}; // 11111111
        byte[] expected = {'1', '1', '1', '1', '1', '1', '1', '1'};
        assertArrayEquals(expected, BinaryCodec.toAsciiBytes(input));
    }

    @Test
    void testToAsciiBytesWithZeroByte() {
        byte[] input = {0x00}; // 00000000
        byte[] expected = {'0', '0', '0', '0', '0', '0', '0', '0'};
        assertArrayEquals(expected, BinaryCodec.toAsciiBytes(input));
    }
}
