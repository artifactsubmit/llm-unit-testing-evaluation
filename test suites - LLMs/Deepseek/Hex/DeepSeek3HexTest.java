package experimento.deepseek.hex;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DeepSeek3HexTest {

    private Hex hexDefault;
    private Hex hexCustomCharset;

    @BeforeEach
    void setUp() {
        hexDefault = new Hex();
        hexCustomCharset = new Hex(StandardCharsets.ISO_8859_1.name());
    }

    // Testes para decodeHex(char[] data)

    @Test
    void testDecodeHex_ValidInput_ReturnsCorrectBytes() throws DecoderException {
        char[] input = {'0', '1', 'a', 'f', 'F', '0'};
        byte[] expected = {0x01, (byte) 0xaf, (byte) 0xf0};
        assertArrayEquals(expected, Hex.decodeHex(input));
    }

    @Test
    void testDecodeHex_OddNumberOfChars_ThrowsDecoderException() {
        char[] input = {'0', '1', 'a'};
        assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
    }

    @Test
    void testDecodeHex_InvalidHexChars_ThrowsDecoderException() {
        char[] input = {'0', '1', 'x', 'y'};
        assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
    }

    @Test
    void testDecodeHex_EmptyInput_ReturnsEmptyArray() throws DecoderException {
        char[] input = {};
        byte[] expected = {};
        assertArrayEquals(expected, Hex.decodeHex(input));
    }

    // Testes para encodeHex(byte[] data)

    @Test
    void testEncodeHex_DefaultLowerCase() {
        byte[] input = {0x01, 0x2a, (byte) 0xff};
        char[] expected = {'0', '1', '2', 'a', 'f', 'f'};
        assertArrayEquals(expected, Hex.encodeHex(input));
    }

    @Test
    void testEncodeHex_EmptyInput_ReturnsEmptyArray() {
        byte[] input = {};
        char[] expected = {};
        assertArrayEquals(expected, Hex.encodeHex(input));
    }

    // Testes para encodeHex(byte[] data, boolean toLowerCase)

    @Test
    void testEncodeHex_WithCaseOption_LowerCase() {
        byte[] input = {0x0a, 0x1b, (byte) 0xcd};
        char[] expected = {'0', 'a', '1', 'b', 'c', 'd'};
        assertArrayEquals(expected, Hex.encodeHex(input, true));
    }

    @Test
    void testEncodeHex_WithCaseOption_UpperCase() {
        byte[] input = {0x0a, 0x1b, (byte) 0xcd};
        char[] expected = {'0', 'A', '1', 'B', 'C', 'D'};
        assertArrayEquals(expected, Hex.encodeHex(input, false));
    }

    // Testes para encodeHex(byte[] data, char[] toDigits)

    @Test
    void testEncodeHex_WithCustomDigits() {
        byte[] input = {0x0f, 0x10};
        char[] customDigits = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P'};
        char[] expected = {'A', 'P', 'B', 'A'};
        assertArrayEquals(expected, Hex.encodeHex(input, customDigits));
    }

    // Testes para encodeHexString(byte[] data)

    @Test
    void testEncodeHexString_ValidInput() {
        byte[] input = {0x01, 0x23, (byte) 0xab};
        String expected = "0123ab";
        assertEquals(expected, Hex.encodeHexString(input));
    }

    @Test
    void testEncodeHexString_EmptyInput() {
        byte[] input = {};
        String expected = "";
        assertEquals(expected, Hex.encodeHexString(input));
    }

    // Testes para toDigit(char ch, int index)

    @ParameterizedTest
    @ValueSource(chars = {'0', '1', '9', 'a', 'f', 'A', 'F'})
    void testToDigit_ValidChars_ReturnsCorrectValue(char ch) throws DecoderException {
        int digit = Hex.toDigit(ch, 0);
        assertTrue(digit >= 0 && digit <= 15);
    }

    @Test
    void testToDigit_InvalidChar_ThrowsDecoderException() {
        assertThrows(DecoderException.class, () -> Hex.toDigit('x', 0));
    }

    // Testes para decode(byte[] array)

    @Test
    void testDecode_ValidInput_ReturnsCorrectBytes() throws DecoderException {
        byte[] input = "01af".getBytes(StandardCharsets.UTF_8);
        byte[] expected = {0x01, (byte) 0xaf};
        assertArrayEquals(expected, hexDefault.decode(input));
    }

    @Test
    void testDecode_OddNumberOfChars_ThrowsDecoderException() {
        byte[] input = "01a".getBytes(StandardCharsets.UTF_8);
        assertThrows(DecoderException.class, () -> hexDefault.decode(input));
    }

    @Test
    void testDecode_WithCustomCharset() throws DecoderException {
        byte[] input = "01af".getBytes(StandardCharsets.ISO_8859_1);
        byte[] expected = {0x01, (byte) 0xaf};
        assertArrayEquals(expected, hexCustomCharset.decode(input));
    }

    // Testes para decode(Object object)

    @Test
    void testDecodeObject_WithString() throws DecoderException {
        String input = "01af";
        byte[] expected = {0x01, (byte) 0xaf};
        assertArrayEquals(expected, (byte[]) hexDefault.decode(input));
    }

    @Test
    void testDecodeObject_WithCharArray() throws DecoderException {
        char[] input = {'0', '1', 'a', 'f'};
        byte[] expected = {0x01, (byte) 0xaf};
        assertArrayEquals(expected, (byte[]) hexDefault.decode(input));
    }

    @Test
    void testDecodeObject_WithInvalidType_ThrowsDecoderException() {
        assertThrows(DecoderException.class, () -> hexDefault.decode(123));
    }

    // Testes para encode(byte[] array)

    @Test
    void testEncodeByteArray_ValidInput() {
        byte[] input = {0x01, (byte) 0xaf};
        byte[] expected = "01af".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expected, hexDefault.encode(input));
    }

    @Test
    void testEncodeByteArray_WithCustomCharset() {
        byte[] input = {0x01, (byte) 0xaf};
        byte[] expected = "01af".getBytes(StandardCharsets.ISO_8859_1);
        assertArrayEquals(expected, hexCustomCharset.encode(input));
    }

    // Testes para encode(Object object)

    @Test
    void testEncodeObject_WithString() throws EncoderException {
        String input = "test";
        char[] expected = Hex.encodeHex(input.getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(expected, (char[]) hexDefault.encode(input));
    }

    @Test
    void testEncodeObject_WithByteArray() throws EncoderException {
        byte[] input = {0x74, 0x65, 0x73, 0x74}; // "test" in bytes
        char[] expected = Hex.encodeHex(input);
        assertArrayEquals(expected, (char[]) hexDefault.encode(input));
    }

    @Test
    void testEncodeObject_WithInvalidType_ThrowsEncoderException() {
        assertThrows(EncoderException.class, () -> hexDefault.encode(123));
    }

    // Testes para getCharsetName()

    @Test
    void testGetCharsetName_DefaultConstructor() {
        assertEquals(Hex.DEFAULT_CHARSET_NAME, hexDefault.getCharsetName());
    }

    @Test
    void testGetCharsetName_CustomConstructor() {
        assertEquals(StandardCharsets.ISO_8859_1.name(), hexCustomCharset.getCharsetName());
    }

    // Testes para toString()

    @Test
    void testToString_ContainsClassNameAndCharset() {
        String result = hexDefault.toString();
        assertTrue(result.contains("Hex"));
        assertTrue(result.contains(Hex.DEFAULT_CHARSET_NAME));
    }

    // Testes adicionais para mutantes

    @Test
    void testEncodeHex_AllByteValues() {
        byte[] input = new byte[256];
        for (int i = 0; i < 256; i++) {
            input[i] = (byte) i;
        }

        char[] result = Hex.encodeHex(input);
        assertEquals(512, result.length); // 2 chars per byte

        // Verifica alguns valores específicos
        assertEquals('0', result[0]); // 0x00
        assertEquals('0', result[0]);
        assertEquals('f', result[31*2+1]); // 0x1f
        assertEquals('8', result[128*2]); // 0x80
        assertEquals('0', result[128*2]);
        assertEquals('f', result[255*2+1]); // 0xff
    }

    @Test
    void testDecodeHex_AllHexPairs() throws DecoderException {
        char[] input = new char[512];
        char[] digits = Hex.DIGITS_LOWER;

        for (int i = 0; i < 256; i++) {
            input[i*2] = digits[(i >> 4) & 0x0F];
            input[i*2+1] = digits[i & 0x0F];
        }

        byte[] result = Hex.decodeHex(input);
        assertEquals(256, result.length);

        for (int i = 0; i < 256; i++) {
            assertEquals((byte) i, result[i]);
        }
    }
}
