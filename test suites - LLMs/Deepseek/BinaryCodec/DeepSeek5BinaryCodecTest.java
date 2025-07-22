package experimento.deepseek.binarycodec;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

class DeepSeek5BinaryCodecTest {

    // Testes para encode(byte[] raw)
    @Test
    void testEncodeByteArray_WithEmptyArray_ShouldReturnEmptyArray() {
        BinaryCodec codec = new BinaryCodec();
        byte[] empty = new byte[0];
        byte[] result = codec.encode(empty);
        assertArrayEquals(empty, result);
    }

    @Test
    void testEncodeByteArray_WithSingleByte_ShouldReturnCorrectBinaryRepresentation() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[]{(byte) 0x0F}; // 00001111
        byte[] expected = "00001111".getBytes();
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testEncodeByteArray_WithMultipleBytes_ShouldReturnCorrectBinaryRepresentation() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[]{(byte) 0x0F, (byte) 0xF0}; // 00001111 11110000
        byte[] expected = "0000111111110000".getBytes();
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);
    }

    // Testes para encode(Object raw)
    @Test
    void testEncodeObject_WithByteArray_ShouldReturnCharArray() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[]{(byte) 0x0A}; // 00001010
        char[] expected = "00001010".toCharArray();
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
    void testEncodeObject_WithNull_ShouldThrowEncoderException(Object input) {
        BinaryCodec codec = new BinaryCodec();
        assertThrows(EncoderException.class, () -> codec.encode(input));
    }

    // Testes para decode(Object ascii)
    @Test
    void testDecodeObject_WithNull_ShouldReturnEmptyByteArray() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = (byte[]) codec.decode(null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testDecodeObject_WithByteArray_ShouldReturnCorrectBinary() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = "00001111".getBytes();
        byte[] expected = new byte[]{(byte) 0x0F};
        byte[] result = (byte[]) codec.decode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testDecodeObject_WithCharArray_ShouldReturnCorrectBinary() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        char[] input = "11110000".toCharArray();
        byte[] expected = new byte[]{(byte) 0xF0};
        byte[] result = (byte[]) codec.decode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testDecodeObject_WithString_ShouldReturnCorrectBinary() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        String input = "10101010";
        byte[] expected = new byte[]{(byte) 0xAA};
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
    void testDecodeByteArray_WithEmptyArray_ShouldReturnEmptyArray() {
        BinaryCodec codec = new BinaryCodec();
        byte[] empty = new byte[0];
        byte[] result = codec.decode(empty);
        assertArrayEquals(empty, result);
    }

    @Test
    void testDecodeByteArray_WithValidInput_ShouldReturnCorrectBinary() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = "11001100".getBytes();
        byte[] expected = new byte[]{(byte) 0xCC};
        byte[] result = codec.decode(input);
        assertArrayEquals(expected, result);
    }

    // Testes para toByteArray(String ascii)
    @Test
    void testToByteArray_WithNull_ShouldReturnEmptyArray() {
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
    void testToByteArray_WithValidString_ShouldReturnCorrectBinary() {
        BinaryCodec codec = new BinaryCodec();
        String input = "01010101";
        byte[] expected = new byte[]{(byte) 0x55};
        byte[] result = codec.toByteArray(input);
        assertArrayEquals(expected, result);
    }

    // Testes para fromAscii(byte[])
    @Test
    void testFromAsciiByteArray_WithEmptyArray_ShouldReturnEmptyArray() {
        byte[] result = BinaryCodec.fromAscii(new byte[0]);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testFromAsciiByteArray_WithNull_ShouldReturnEmptyArray() {
        byte[] result = BinaryCodec.fromAscii(null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testFromAsciiByteArray_WithSingleByte_ShouldReturnCorrectBinary() {
        byte[] input = "10000000".getBytes();
        byte[] expected = new byte[]{(byte) 0x01};
        byte[] result = BinaryCodec.fromAscii(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testFromAsciiByteArray_WithMultipleBytes_ShouldReturnCorrectBinary() {
        byte[] input = "1000000011111111".getBytes();
        byte[] expected = new byte[]{(byte) 0x01, (byte) 0xFF};
        byte[] result = BinaryCodec.fromAscii(input);
        assertArrayEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(bytes = {'2', 'a', ' ', '\n'})
    void testFromAsciiByteArray_WithInvalidChars_ShouldIgnoreNonBinaryDigits(byte invalidChar) {
        byte[] input = new byte[]{'1', '0', invalidChar, '1', '0'};
        byte[] expected = new byte[]{(byte) 0x05}; // Only '101' at the end is considered
        byte[] result = BinaryCodec.fromAscii(input);
        assertArrayEquals(expected, result);
    }

    // Testes para fromAscii(char[])
    @Test
    void testFromAsciiCharArray_WithEmptyArray_ShouldReturnEmptyArray() {
        char[] empty = new char[0];
        byte[] result = BinaryCodec.fromAscii(empty);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testFromAsciiCharArray_WithNull_ShouldReturnEmptyArray() {
        byte[] result = BinaryCodec.fromAscii((char[]) null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testFromAsciiCharArray_WithSingleChar_ShouldReturnCorrectBinary() {
        char[] input = "00000001".toCharArray();
        byte[] expected = new byte[]{(byte) 0x80};
        byte[] result = BinaryCodec.fromAscii(input);
        assertArrayEquals(expected, result);
    }

    // Testes para toAsciiBytes(byte[])
    @Test
    void testToAsciiBytes_WithEmptyArray_ShouldReturnEmptyArray() {
        byte[] result = BinaryCodec.toAsciiBytes(new byte[0]);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testToAsciiBytes_WithNull_ShouldReturnEmptyArray() {
        byte[] result = BinaryCodec.toAsciiBytes(null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testToAsciiBytes_WithSingleByte_ShouldReturnCorrectAscii() {
        byte[] input = new byte[]{(byte) 0xFF};
        byte[] expected = "11111111".getBytes();
        byte[] result = BinaryCodec.toAsciiBytes(input);
        assertArrayEquals(expected, result);
    }

    // Testes para toAsciiChars(byte[])
    @Test
    void testToAsciiChars_WithEmptyArray_ShouldReturnEmptyArray() {
        char[] result = BinaryCodec.toAsciiChars(new byte[0]);
        assertArrayEquals(new char[0], result);
    }

    @Test
    void testToAsciiChars_WithNull_ShouldReturnEmptyArray() {
        char[] result = BinaryCodec.toAsciiChars(null);
        assertArrayEquals(new char[0], result);
    }

    @Test
    void testToAsciiChars_WithSingleByte_ShouldReturnCorrectAscii() {
        byte[] input = new byte[]{(byte) 0xAA};
        char[] expected = "10101010".toCharArray();
        char[] result = BinaryCodec.toAsciiChars(input);
        assertArrayEquals(expected, result);
    }

    // Testes para toAsciiString(byte[])
    @Test
    void testToAsciiString_WithEmptyArray_ShouldReturnEmptyString() {
        String result = BinaryCodec.toAsciiString(new byte[0]);
        assertEquals("", result);
    }

    @Test
    void testToAsciiString_WithNull_ShouldReturnEmptyString() {
        String result = BinaryCodec.toAsciiString(null);
        assertEquals("", result);
    }

    @Test
    void testToAsciiString_WithSingleByte_ShouldReturnCorrectString() {
        byte[] input = new byte[]{(byte) 0x55};
        String expected = "01010101";
        String result = BinaryCodec.toAsciiString(input);
        assertEquals(expected, result);
    }

    // Testes para comportamentos de borda e casos especiais
    @Test
    void testPartialByteDecoding_ShouldHandleCorrectly() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = "101".getBytes(); // Only 3 bits
        byte[] expected = new byte[]{(byte) 0x05}; // 00000101 (but actually stored as 10100000 due to implementation)
        byte[] result = codec.decode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testRoundTripEncodingDecoding_ShouldReturnOriginalData() {
        BinaryCodec codec = new BinaryCodec();
        byte[] original = new byte[]{(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78};

        // Encode to ASCII
        byte[] ascii = codec.encode(original);

        // Decode back to binary
        byte[] decoded = codec.decode(ascii);

        assertArrayEquals(original, decoded);
    }

    @Test
    void testMixedCaseBinaryString_ShouldDecodeCorrectly() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        String input = "01aBcD10"; // Only '0' and '1' should be considered
        byte[] expected = new byte[]{(byte) 0x42}; // 01000010 (only the 01000010 part is considered)
        byte[] result = (byte[]) codec.decode(input);
        assertArrayEquals(expected, result);
    }
}
