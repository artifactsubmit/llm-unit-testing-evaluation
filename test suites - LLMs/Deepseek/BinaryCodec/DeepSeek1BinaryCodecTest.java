package experimento.deepseek.binarycodec;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.DecoderException;
import java.util.stream.Stream;

class DeepSeek1BinaryCodecTest {

    // Testes para encode(byte[] raw)
    @ParameterizedTest
    @NullAndEmptySource
    void testEncodeByteArrayWithNullOrEmptyInput(byte[] input) {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.encode(input);
        assertArrayEquals(BinaryCodec.EMPTY_BYTE_ARRAY, result);
    }

    @Test
    void testEncodeByteArrayWithSingleByte() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[] {0x0F}; // 00001111 in binary
        byte[] expected = "00001111".getBytes();
        assertArrayEquals(expected, codec.encode(input));
    }

    @Test
    void testEncodeByteArrayWithMultipleBytes() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[] {(byte) 0xF0, 0x0F}; // 11110000 00001111
        byte[] expected = "1111000000001111".getBytes();
        assertArrayEquals(expected, codec.encode(input));
    }

    // Testes para encode(Object raw)
    @Test
    void testEncodeObjectWithByteArray() throws EncoderException {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = new byte[] {0x0F}; // 00001111
        char[] expected = "00001111".toCharArray();
        assertArrayEquals(expected, (char[]) codec.encode(input));
    }

    @Test
    void testEncodeObjectWithInvalidType() {
        BinaryCodec codec = new BinaryCodec();
        String invalidInput = "not a byte array";
        assertThrows(EncoderException.class, () -> codec.encode(invalidInput));
    }

    @Test
    void testEncodeObjectWithNullInput() throws EncoderException {
        BinaryCodec codec = new BinaryCodec();
        char[] result = (char[]) codec.encode(null);
        assertArrayEquals(BinaryCodec.EMPTY_CHAR_ARRAY, result);
    }

    // Testes para decode(Object ascii)
    @ParameterizedTest
    @MethodSource("provideValidDecodeObjects")
    void testDecodeObjectWithValidInputTypes(Object input, byte[] expected) throws DecoderException {
        BinaryCodec codec = new BinaryCodec();
        assertArrayEquals(expected, (byte[]) codec.decode(input));
    }

    private static Stream<Object[]> provideValidDecodeObjects() {
        return Stream.of(
                new Object[] {new byte[] {'1','0','1','0','1','0','1','0'}, new byte[] {(byte) 0xAA}},
                new Object[] {new char[] {'1','0','1','0','1','0','1','0'}, new byte[] {(byte) 0xAA}},
                new Object[] {"10101010", new byte[] {(byte) 0xAA}},
                new Object[] {null, BinaryCodec.EMPTY_BYTE_ARRAY}
        );
    }

    @Test
    void testDecodeObjectWithInvalidType() {
        BinaryCodec codec = new BinaryCodec();
        assertThrows(DecoderException.class, () -> codec.decode(123));
    }

    // Testes para decode(byte[] ascii)
    @ParameterizedTest
    @NullAndEmptySource
    void testDecodeByteArrayWithNullOrEmptyInput(byte[] input) {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.decode(input);
        assertArrayEquals(BinaryCodec.EMPTY_BYTE_ARRAY, result);
    }

    @Test
    void testDecodeByteArrayWithSingleByte() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = "10101010".getBytes();
        byte[] expected = new byte[] {(byte) 0xAA};
        assertArrayEquals(expected, codec.decode(input));
    }

    @Test
    void testDecodeByteArrayWithMultipleBytes() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = "1010101001010101".getBytes();
        byte[] expected = new byte[] {(byte) 0xAA, (byte) 0x55};
        assertArrayEquals(expected, codec.decode(input));
    }

    @Test
    void testDecodeByteArrayWithInvalidCharacters() {
        BinaryCodec codec = new BinaryCodec();
        byte[] input = "10102A10".getBytes(); // Contains invalid character '2' and 'A'
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> codec.decode(input));
    }

    // Testes para toByteArray(String ascii)
    @ParameterizedTest
    @NullAndEmptySource
    void testToByteArrayWithNullOrEmptyInput(String input) {
        BinaryCodec codec = new BinaryCodec();
        byte[] result = codec.toByteArray(input);
        assertArrayEquals(BinaryCodec.EMPTY_BYTE_ARRAY, result);
    }

    @Test
    void testToByteArrayWithValidString() {
        BinaryCodec codec = new BinaryCodec();
        String input = "11110000";
        byte[] expected = new byte[] {(byte) 0xF0};
        assertArrayEquals(expected, codec.toByteArray(input));
    }

    // Testes para fromAscii(byte[] ascii)
    @Test
    void testFromAsciiByteArrayWithPartialByte() {
        byte[] input = "101".getBytes(); // Not multiple of 8
        byte[] result = BinaryCodec.fromAscii(input);
        // Should ignore the partial byte (last 5 bits would be 0)
        byte[] expected = new byte[] { (byte) 0xA0 }; // 10100000
        assertArrayEquals(expected, result);
    }

    // Testes para fromAscii(char[] ascii)
    @Test
    void testFromAsciiCharArrayWithPartialByte() {
        char[] input = "0101".toCharArray(); // Not multiple of 8
        byte[] result = BinaryCodec.fromAscii(input);
        // Should ignore the partial byte (last 4 bits would be 0)
        byte[] expected = new byte[] { (byte) 0x50 }; // 01010000
        assertArrayEquals(expected, result);
    }

    // Testes para toAsciiBytes(byte[] raw)
    @Test
    void testToAsciiBytesWithSingleByte() {
        byte[] input = new byte[] {(byte) 0xFF}; // 11111111
        byte[] expected = "11111111".getBytes();
        assertArrayEquals(expected, BinaryCodec.toAsciiBytes(input));
    }

    // Testes para toAsciiChars(byte[] raw)
    @Test
    void testToAsciiCharsWithMultipleBytes() {
        byte[] input = new byte[] {(byte) 0xAA, (byte) 0x55}; // 10101010 01010101
        char[] expected = "1010101001010101".toCharArray();
        assertArrayEquals(expected, BinaryCodec.toAsciiChars(input));
    }

    // Testes para toAsciiString(byte[] raw)
    @Test
    void testToAsciiStringWithByteArray() {
        byte[] input = new byte[] {(byte) 0x0F, (byte) 0xF0}; // 00001111 11110000
        String expected = "0000111111110000";
        assertEquals(expected, BinaryCodec.toAsciiString(input));
    }

    // Testes de roundtrip (encode -> decode)
    @ParameterizedTest
    @MethodSource("provideRoundtripTestData")
    void testRoundtripEncodingDecoding(byte[] original) {
        BinaryCodec codec = new BinaryCodec();
        byte[] encoded = codec.encode(original);
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(original, decoded);
    }

    private static Stream<byte[]> provideRoundtripTestData() {
        return Stream.of(
                new byte[0],
                new byte[] {0x00},
                new byte[] {(byte) 0xFF},
                new byte[] {0x12, 0x34, 0x56, 0x78},
                new byte[] {(byte) 0xAA, (byte) 0x55, (byte) 0xAA, (byte) 0x55}
        );
    }

    // Testes para comportamento com caracteres inválidos
    @ParameterizedTest
    @ValueSource(chars = {'2', ' ', 'a', 'Z', '@'})
    void testFromAsciiWithInvalidCharacters(char invalidChar) {
        char[] input = new char[] {'1', '0', invalidChar, '1'};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> BinaryCodec.fromAscii(input));
    }

    // Testes para limites/bordas
    @Test
    void testLargeInputEncoding() {
        byte[] input = new byte[1024];
        for (int i = 0; i < input.length; i++) {
            input[i] = (byte) (i % 256);
        }
        BinaryCodec codec = new BinaryCodec();
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    // Testes para verificar a ordem dos bits
    @Test
    void testBitOrderInEncoding() {
        byte[] input = new byte[] {(byte) 0x80}; // 10000000
        byte[] expected = "10000000".getBytes();
        assertArrayEquals(expected, BinaryCodec.toAsciiBytes(input));
    }

    @Test
    void testBitOrderInDecoding() {
        byte[] input = "00000001".getBytes(); // LSB is last in array
        byte[] expected = new byte[] {0x01}; // 00000001
        assertArrayEquals(expected, BinaryCodec.fromAscii(input));
    }
}
