package experimento.deepseek.binarycodec;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

class DeepSeek3BinaryCodecTest {

    // Testes para encode(byte[] raw)
    @Test
    void testEncodeByteArray_WithEmptyInput_ShouldReturnEmptyArray() {
        BinaryCodec codec = new BinaryCodec();
        byte[] empty = new byte[0];
        byte[] result = codec.encode(empty);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testEncodeByteArray_WithSingleByte_ShouldReturnCorrectBinaryRepresentation() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[] {0x0F}; // 00001111
        byte[] expected = "00001111".getBytes();
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testEncodeByteArray_WithMultipleBytes_ShouldReturnCorrectBinaryRepresentation() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[] {(byte) 0xF0, 0x0F}; // 11110000 00001111
        byte[] expected = "1111000000001111".getBytes();
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);
    }

    // Testes para encode(Object raw)
    @Test
    void testEncodeObject_WithByteArray_ShouldReturnCharArray() throws EncoderException {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[] {0x0F}; // 00001111
        char[] expected = "00001111".toCharArray();
        char[] result = (char[]) codec.encode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testEncodeObject_WithNonByteArray_ShouldThrowEncoderException() {
        BinaryCodec codec = new BinaryCodec();
        String input = "invalid";
        assertThrows(EncoderException.class, () -> codec.encode(input));
    }

    @ParameterizedTest
    @NullSource
    void testEncodeObject_WithNullInput_ShouldThrowEncoderException(Object input) {
        BinaryCodec codec = new BinaryCodec();
        assertThrows(EncoderException.class, () -> codec.encode(input));
    }

    // Testes para decode(Object ascii)
    @Test
    void testDecodeObject_WithNullInput_ShouldReturnEmptyArray() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = (byte[]) codec.decode(null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testDecodeObject_WithByteArray_ShouldReturnDecodedBytes() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = "00001111".getBytes();
        byte[] expected = new byte[] {0x0F};
        byte[] result = (byte[]) codec.decode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testDecodeObject_WithCharArray_ShouldReturnDecodedBytes() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        char[] input = "00001111".toCharArray();
        byte[] expected = new byte[] {0x0F};
        byte[] result = (byte[]) codec.decode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testDecodeObject_WithString_ShouldReturnDecodedBytes() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        String input = "00001111";
        byte[] expected = new byte[] {0x0F};
        byte[] result = (byte[]) codec.decode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testDecodeObject_WithInvalidType_ShouldThrowDecoderException() {
        BinaryCodec codec = new BinaryCodec();
        assertThrows(DecoderException.class, () -> codec.decode(123));
    }

    // Testes para decode(byte[] ascii)
    @Test
    void testDecodeByteArray_WithEmptyInput_ShouldReturnEmptyArray() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[0];
        byte[] result = codec.decode(input);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testDecodeByteArray_WithValidInput_ShouldReturnDecodedBytes() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = "10101010".getBytes();
        byte[] expected = new byte[] {(byte) 0xAA};
        byte[] result = codec.decode(input);
        assertArrayEquals(expected, result);
    }

    // Testes para toByteArray(String ascii)
    @Test
    void testToByteArray_WithNullInput_ShouldReturnEmptyArray() {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.toByteArray(null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testToByteArray_WithEmptyString_ShouldReturnEmptyArray() {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.toByteArray("");
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testToByteArray_WithValidString_ShouldReturnDecodedBytes() {
        BinaryCodec codec = new BinaryCodec();
        String input = "11110000";
        byte[] expected = new byte[] {(byte) 0xF0};
        byte[] result = codec.toByteArray(input);
        assertArrayEquals(expected, result);
    }

    // Testes para fromAscii(byte[] ascii)
    @Test
    void testFromAsciiByteArray_WithNullInput_ShouldReturnEmptyArray() {
        byte[] result = BinaryCodec.fromAscii((byte[]) null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testFromAsciiByteArray_WithEmptyInput_ShouldReturnEmptyArray() {
        byte[] result = BinaryCodec.fromAscii(new byte[0]);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testFromAsciiByteArray_WithValidInput_ShouldReturnDecodedBytes() {
        byte[] input = "11001100".getBytes();
        byte[] expected = new byte[] {(byte) 0xCC};
        byte[] result = BinaryCodec.fromAscii(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testFromAsciiByteArray_WithInvalidCharacters_ShouldIgnoreNonBinaryChars() {
        byte[] input = "11x0y1z0".getBytes(); // Invalid chars should be treated as '0'
        byte[] expected = new byte[] {0xC0}; // 11000000
        byte[] result = BinaryCodec.fromAscii(input);
        assertArrayEquals(expected, result);
    }

    // Testes para fromAscii(char[] ascii)
    @Test
    void testFromAsciiCharArray_WithNullInput_ShouldReturnEmptyArray() {
        byte[] result = BinaryCodec.fromAscii((char[]) null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testFromAsciiCharArray_WithEmptyInput_ShouldReturnEmptyArray() {
        byte[] result = BinaryCodec.fromAscii(new char[0]);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testFromAsciiCharArray_WithValidInput_ShouldReturnDecodedBytes() {
        char[] input = "00110011".toCharArray();
        byte[] expected = new byte[] {0x33};
        byte[] result = BinaryCodec.fromAscii(input);
        assertArrayEquals(expected, result);
    }

    // Testes para toAsciiBytes(byte[] raw)
    @Test
    void testToAsciiBytes_WithNullInput_ShouldReturnEmptyArray() {
        byte[] result = BinaryCodec.toAsciiBytes(null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testToAsciiBytes_WithEmptyInput_ShouldReturnEmptyArray() {
        byte[] result = BinaryCodec.toAsciiBytes(new byte[0]);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testToAsciiBytes_WithSingleByte_ShouldReturnCorrectBinaryString() {
        byte[] input = new byte[] {(byte) 0xAA}; // 10101010
        byte[] expected = "10101010".getBytes();
        byte[] result = BinaryCodec.toAsciiBytes(input);
        assertArrayEquals(expected, result);
    }

    // Testes para toAsciiChars(byte[] raw)
    @Test
    void testToAsciiChars_WithNullInput_ShouldReturnEmptyArray() {
        char[] result = BinaryCodec.toAsciiChars(null);
        assertArrayEquals(new char[0], result);
    }

    @Test
    void testToAsciiChars_WithEmptyInput_ShouldReturnEmptyArray() {
        char[] result = BinaryCodec.toAsciiChars(new byte[0]);
        assertArrayEquals(new char[0], result);
    }

    @Test
    void testToAsciiChars_WithSingleByte_ShouldReturnCorrectBinaryString() {
        byte[] input = new byte[] {0x55}; // 01010101
        char[] expected = "01010101".toCharArray();
        char[] result = BinaryCodec.toAsciiChars(input);
        assertArrayEquals(expected, result);
    }

    // Testes para toAsciiString(byte[] raw)
    @Test
    void testToAsciiString_WithNullInput_ShouldReturnEmptyString() {
        String result = BinaryCodec.toAsciiString(null);
        assertEquals("", result);
    }

    @Test
    void testToAsciiString_WithEmptyInput_ShouldReturnEmptyString() {
        String result = BinaryCodec.toAsciiString(new byte[0]);
        assertEquals("", result);
    }

    @Test
    void testToAsciiString_WithSingleByte_ShouldReturnCorrectBinaryString() {
        byte[] input = new byte[] {(byte) 0xFF}; // 11111111
        String expected = "11111111";
        String result = BinaryCodec.toAsciiString(input);
        assertEquals(expected, result);
    }

    // Testes para comportamento de borda e casos especiais
    @Test
    void testDecode_WithIncompleteByte_ShouldPadWithZeros() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        String input = "101"; // Only 3 bits, should be treated as 00000101
        byte[] expected = new byte[] {0x05};
        byte[] result = (byte[]) codec.decode(input);
        assertArrayEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1", "01", "10", "00000000", "11111111", "01010101"})
    void testEncodeDecodeRoundTrip_ShouldReturnOriginalValue(String binaryString) throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        byte[] decoded = (byte[]) codec.decode(binaryString);
        String encoded = BinaryCodec.toAsciiString(decoded);
        assertEquals(binaryString, encoded);
    }

    @Test
    void testMultipleByteEncodingDecoding_ShouldMaintainConsistency() {
        BinaryCodec codec = new BinaryCodec();
        byte[] original = new byte[] {0x12, 0x34, 0x56, 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xFF};
        byte[] encoded = codec.encode(original);
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(original, decoded);
    }
}
