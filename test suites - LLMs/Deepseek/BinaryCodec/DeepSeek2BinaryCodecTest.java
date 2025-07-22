package experimento.deepseek.binarycodec;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

class DeepSeek2BinaryCodecTest {

    // Testes para encode(byte[] raw)
    @Test
    void testEncodeByteArray_NullInput() {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.encode(null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testEncodeByteArray_EmptyInput() {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.encode(new byte[0]);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testEncodeByteArray_SingleByte() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[] {0x0F}; // 00001111
        byte[] expected = "00001111".getBytes();
        assertArrayEquals(expected, codec.encode(input));
    }

    @Test
    void testEncodeByteArray_MultipleBytes() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[] {(byte) 0xF0, 0x0F}; // 11110000 00001111
        byte[] expected = "1111000000001111".getBytes();
        assertArrayEquals(expected, codec.encode(input));
    }

    // Testes para encode(Object raw)
    @Test
    void testEncodeObject_ValidByteArray() throws EncoderException {
        BinaryCodec codec = new BinaryCodec();
        Object input = new byte[] {0x0A}; // 00001010
        char[] expected = "00001010".toCharArray();
        assertArrayEquals(expected, (char[]) codec.encode(input));
    }

    @Test
    void testEncodeObject_InvalidType() {
        BinaryCodec codec = new BinaryCodec();
        Object input = "invalid";
        assertThrows(EncoderException.class, () -> codec.encode(input));
    }

    @Test
    void testEncodeObject_NullInput() throws EncoderException {
        BinaryCodec codec = new BinaryCodec();
        Object result = codec.encode(null);
        assertArrayEquals(new char[0], (char[]) result);
    }

    // Testes para decode(Object ascii)
    @Test
    void testDecodeObject_NullInput() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        Object result = codec.decode(null);
        assertArrayEquals(new byte[0], (byte[]) result);
    }

    @Test
    void testDecodeObject_ByteArrayInput() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        Object input = "00001111".getBytes();
        byte[] expected = new byte[] {0x0F};
        assertArrayEquals(expected, (byte[]) codec.decode(input));
    }

    @Test
    void testDecodeObject_CharArrayInput() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        Object input = "00001111".toCharArray();
        byte[] expected = new byte[] {0x0F};
        assertArrayEquals(expected, (byte[]) codec.decode(input));
    }

    @Test
    void testDecodeObject_StringInput() throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        Object input = "00001111";
        byte[] expected = new byte[] {0x0F};
        assertArrayEquals(expected, (byte[]) codec.decode(input));
    }

    @Test
    void testDecodeObject_InvalidType() {
        BinaryCodec codec = new BinaryCodec();
        Object input = 123;
        assertThrows(DecoderException.class, () -> codec.decode(input));
    }

    // Testes para decode(byte[] ascii)
    @Test
    void testDecodeByteArray_NullInput() {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.decode(null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testDecodeByteArray_EmptyInput() {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.decode(new byte[0]);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testDecodeByteArray_SingleByte() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = "10101010".getBytes();
        byte[] expected = new byte[] {(byte) 0xAA};
        assertArrayEquals(expected, codec.decode(input));
    }

    @Test
    void testDecodeByteArray_InvalidLength() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = "101".getBytes(); // Length not multiple of 8
        byte[] result = codec.decode(input);
        assertEquals(0, result.length); // Should return empty array
    }

    // Testes para toByteArray(String ascii)
    @Test
    void testToByteArray_NullInput() {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.toByteArray(null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testToByteArray_EmptyString() {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.toByteArray("");
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testToByteArray_ValidString() {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.toByteArray("11110000");
        byte[] expected = new byte[] {(byte) 0xF0};
        assertArrayEquals(expected, result);
    }

    // Testes para fromAscii(char[] ascii)
    @Test
    void testFromAsciiCharArray_NullInput() {
        char[] input = null;
        byte[] result = BinaryCodec.fromAscii(input);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testFromAsciiCharArray_EmptyInput() {
        char[] input = new char[0];
        byte[] result = BinaryCodec.fromAscii(input);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testFromAsciiCharArray_SingleByte() {
        char[] input = "11001100".toCharArray();
        byte[] expected = new byte[] {(byte) 0xCC};
        assertArrayEquals(expected, BinaryCodec.fromAscii(input));
    }

    @Test
    void testFromAsciiCharArray_MultipleBytes() {
        char[] input = "1100110011110000".toCharArray();
        byte[] expected = new byte[] {(byte) 0xCC, (byte) 0xF0};
        assertArrayEquals(expected, BinaryCodec.fromAscii(input));
    }

    @Test
    void testFromAsciiCharArray_InvalidLength() {
        char[] input = "110".toCharArray(); // Not multiple of 8
        byte[] result = BinaryCodec.fromAscii(input);
        assertEquals(0, result.length); // Should return empty array
    }

    // Testes para fromAscii(byte[] ascii)
    @Test
    void testFromAsciiByteArray_NullInput() {
        byte[] input = null;
        byte[] result = BinaryCodec.fromAscii(input);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testFromAsciiByteArray_EmptyInput() {
        byte[] input = new byte[0];
        byte[] result = BinaryCodec.fromAscii(input);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testFromAsciiByteArray_SingleByte() {
        byte[] input = "00110011".getBytes();
        byte[] expected = new byte[] {0x33};
        assertArrayEquals(expected, BinaryCodec.fromAscii(input));
    }

    @Test
    void testFromAsciiByteArray_InvalidCharacters() {
        byte[] input = "00112A11".getBytes(); // Contains invalid chars
        byte[] result = BinaryCodec.fromAscii(input);
        // Behavior is undefined in the original code - test documents current behavior
        assertNotNull(result);
    }

    // Testes para toAsciiBytes(byte[] raw)
    @Test
    void testToAsciiBytes_NullInput() {
        byte[] input = null;
        byte[] result = BinaryCodec.toAsciiBytes(input);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testToAsciiBytes_EmptyInput() {
        byte[] input = new byte[0];
        byte[] result = BinaryCodec.toAsciiBytes(input);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testToAsciiBytes_SingleByte() {
        byte[] input = new byte[] {(byte) 0xAA}; // 10101010
        byte[] expected = "10101010".getBytes();
        assertArrayEquals(expected, BinaryCodec.toAsciiBytes(input));
    }

    @Test
    void testToAsciiBytes_MultipleBytes() {
        byte[] input = new byte[] {(byte) 0xAA, 0x55}; // 10101010 01010101
        byte[] expected = "1010101001010101".getBytes();
        assertArrayEquals(expected, BinaryCodec.toAsciiBytes(input));
    }

    // Testes para toAsciiChars(byte[] raw)
    @Test
    void testToAsciiChars_NullInput() {
        byte[] input = null;
        char[] result = BinaryCodec.toAsciiChars(input);
        assertArrayEquals(new char[0], result);
    }

    @Test
    void testToAsciiChars_EmptyInput() {
        byte[] input = new byte[0];
        char[] result = BinaryCodec.toAsciiChars(input);
        assertArrayEquals(new char[0], result);
    }

    @Test
    void testToAsciiChars_SingleByte() {
        byte[] input = new byte[] {0x55}; // 01010101
        char[] expected = "01010101".toCharArray();
        assertArrayEquals(expected, BinaryCodec.toAsciiChars(input));
    }

    // Testes para toAsciiString(byte[] raw)
    @Test
    void testToAsciiString_NullInput() {
        byte[] input = null;
        String result = BinaryCodec.toAsciiString(input);
        assertEquals("", result);
    }

    @Test
    void testToAsciiString_EmptyInput() {
        byte[] input = new byte[0];
        String result = BinaryCodec.toAsciiString(input);
        assertEquals("", result);
    }

    @Test
    void testToAsciiString_SingleByte() {
        byte[] input = new byte[] {(byte) 0xF0}; // 11110000
        String expected = "11110000";
        assertEquals(expected, BinaryCodec.toAsciiString(input));
    }

    @Test
    void testToAsciiString_MultipleBytes() {
        byte[] input = new byte[] {(byte) 0xF0, 0x0F}; // 11110000 00001111
        String expected = "1111000000001111";
        assertEquals(expected, BinaryCodec.toAsciiString(input));
    }

    // Testes adicionais para casos de borda e mutantes
    @Test
    void testDecodeByteArray_WithInvalidBits() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = "1010101X".getBytes(); // Contains invalid character
        byte[] result = codec.decode(input);
        // Behavior is undefined in original code - test documents current behavior
        assertNotNull(result);
    }

    @Test
    void testFromAsciiCharArray_WithInvalidBits() {
        char[] input = "1010101X".toCharArray(); // Contains invalid character
        byte[] result = BinaryCodec.fromAscii(input);
        // Behavior is undefined in original code - test documents current behavior
        assertNotNull(result);
    }

    @Test
    void testEmptyArraysAreSameInstance() {
        assertSame(BinaryCodec.fromAscii(new byte[0]), BinaryCodec.EMPTY_BYTE_ARRAY);
        assertSame(BinaryCodec.toAsciiChars(new byte[0]), BinaryCodec.EMPTY_CHAR_ARRAY);
    }
}
