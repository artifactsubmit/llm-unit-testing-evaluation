package experimento.deepseek.hex;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

class DeepSeek5HexTest {

    // Testes para decodeHex(char[] data)

    @Test
    void testDecodeHex_ValidInput_ShouldReturnCorrectBytes() throws DecoderException {
        char[] input = {'0', '1', 'a', 'f', 'F', '0'};
        byte[] expected = {0x01, (byte) 0xaf, (byte) 0xf0};
        assertArrayEquals(expected, Hex.decodeHex(input));
    }

    @Test
    void testDecodeHex_OddNumberOfChars_ShouldThrowDecoderException() {
        char[] input = {'0', '1', 'a'};
        assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
    }

    @Test
    void testDecodeHex_EmptyArray_ShouldReturnEmptyByteArray() throws DecoderException {
        char[] input = {};
        byte[] expected = {};
        assertArrayEquals(expected, Hex.decodeHex(input));
    }

    @Test
    void testDecodeHex_InvalidHexChars_ShouldThrowDecoderException() {
        char[] input = {'0', '1', 'x', 'y'};
        assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
    }

    // Testes para encodeHex(byte[] data)

    @Test
    void testEncodeHex_ValidInput_ShouldReturnLowercaseHexChars() {
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xf0};
        char[] expected = {'0', '1', 'a', 'f', 'f', '0'};
        assertArrayEquals(expected, Hex.encodeHex(input));
    }

    @Test
    void testEncodeHex_EmptyArray_ShouldReturnEmptyCharArray() {
        byte[] input = {};
        char[] expected = {};
        assertArrayEquals(expected, Hex.encodeHex(input));
    }

    // Testes para encodeHex(byte[] data, boolean toLowerCase)

    @Test
    void testEncodeHex_WithLowercaseTrue_ShouldReturnLowercaseHexChars() {
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xf0};
        char[] expected = {'0', '1', 'a', 'f', 'f', '0'};
        assertArrayEquals(expected, Hex.encodeHex(input, true));
    }

    @Test
    void testEncodeHex_WithLowercaseFalse_ShouldReturnUppercaseHexChars() {
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xf0};
        char[] expected = {'0', '1', 'A', 'F', 'F', '0'};
        assertArrayEquals(expected, Hex.encodeHex(input, false));
    }

    // Testes para encodeHex(byte[] data, char[] toDigits)

    @Test
    void testEncodeHex_WithCustomDigits_ShouldReturnCorrectHexChars() {
        byte[] input = {0x01, (byte) 0xaf};
        char[] customDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        char[] expected = {'0', '1', 'A', 'F'};
        assertArrayEquals(expected, Hex.encodeHex(input, customDigits));
    }

    @Test
    void testEncodeHex_WithNullDigits_ShouldThrowNullPointerException() {
        byte[] input = {0x01};
        assertThrows(NullPointerException.class, () -> Hex.encodeHex(input, null));
    }

    // Testes para encodeHexString(byte[] data)

    @Test
    void testEncodeHexString_ValidInput_ShouldReturnCorrectHexString() {
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xf0};
        String expected = "01aff0";
        assertEquals(expected, Hex.encodeHexString(input));
    }

    @Test
    void testEncodeHexString_EmptyArray_ShouldReturnEmptyString() {
        byte[] input = {};
        String expected = "";
        assertEquals(expected, Hex.encodeHexString(input));
    }

    // Testes para toDigit(char ch, int index)

    @ParameterizedTest
    @CsvSource({
            "'0', 0, 0",
            "'9', 1, 9",
            "'a', 2, 10",
            "'f', 3, 15",
            "'A', 4, 10",
            "'F', 5, 15"
    })
    void testToDigit_ValidHexChars_ShouldReturnCorrectValue(char ch, int index, int expected) throws DecoderException {
        assertEquals(expected, Hex.toDigit(ch, index));
    }

    @ParameterizedTest
    @ValueSource(chars = {'x', 'G', '!', ' '})
    void testToDigit_InvalidHexChars_ShouldThrowDecoderException(char invalidChar) {
        assertThrows(DecoderException.class, () -> Hex.toDigit(invalidChar, 0));
    }

    // Testes para decode(byte[] array)

    @Test
    void testDecode_ValidHexBytes_ShouldReturnCorrectBytes() throws DecoderException {
        byte[] input = "01aff0".getBytes(StandardCharsets.UTF_8);
        byte[] expected = {0x01, (byte) 0xaf, (byte) 0xf0};
        assertArrayEquals(expected, new Hex().decode(input));
    }

    @Test
    void testDecode_WithCustomCharset_ShouldHandleCharsetCorrectly() throws DecoderException {
        byte[] input = "01aff0".getBytes(StandardCharsets.ISO_8859_1);
        byte[] expected = {0x01, (byte) 0xaf, (byte) 0xf0};
        assertArrayEquals(expected, new Hex("ISO-8859-1").decode(input));
    }

    @Test
    void testDecode_InvalidCharset_ShouldThrowDecoderException() {
        byte[] input = "01aff0".getBytes();
        Hex hex = new Hex("INVALID_CHARSET");
        assertThrows(DecoderException.class, () -> hex.decode(input));
    }

    // Testes para decode(Object object)

    @Test
    void testDecodeObject_WithString_ShouldReturnCorrectBytes() throws DecoderException {
        String input = "01aff0";
        byte[] expected = {0x01, (byte) 0xaf, (byte) 0xf0};
        assertArrayEquals(expected, (byte[]) new Hex().decode(input));
    }

    @Test
    void testDecodeObject_WithCharArray_ShouldReturnCorrectBytes() throws DecoderException {
        char[] input = {'0', '1', 'a', 'f', 'f', '0'};
        byte[] expected = {0x01, (byte) 0xaf, (byte) 0xf0};
        assertArrayEquals(expected, (byte[]) new Hex().decode(input));
    }

    @Test
    void testDecodeObject_WithInvalidType_ShouldThrowDecoderException() {
        Object input = 12345;
        assertThrows(DecoderException.class, () -> new Hex().decode(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testDecodeObject_WithNullOrEmpty_ShouldHandleCorrectly(Object input) {
        if (input == null) {
            assertThrows(NullPointerException.class, () -> new Hex().decode(input));
        } else {
            assertDoesNotThrow(() -> new Hex().decode(input));
        }
    }

    // Testes para encode(byte[] array)

    @Test
    void testEncode_ValidBytes_ShouldReturnHexBytes() {
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xf0};
        byte[] expected = "01aff0".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expected, new Hex().encode(input));
    }

    @Test
    void testEncode_WithCustomCharset_ShouldHandleCharsetCorrectly() {
        byte[] input = {0x01, (byte) 0xaf};
        byte[] expected = "01af".getBytes(StandardCharsets.ISO_8859_1);
        assertArrayEquals(expected, new Hex("ISO-8859-1").encode(input));
    }

    // Testes para encode(Object object)

    @Test
    void testEncodeObject_WithString_ShouldReturnHexChars() throws EncoderException {
        String input = "test";
        char[] expected = Hex.encodeHex(input.getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(expected, (char[]) new Hex().encode(input));
    }

    @Test
    void testEncodeObject_WithByteArray_ShouldReturnHexChars() throws EncoderException {
        byte[] input = {0x01, (byte) 0xaf};
        char[] expected = {'0', '1', 'a', 'f'};
        assertArrayEquals(expected, (char[]) new Hex().encode(input));
    }

    @Test
    void testEncodeObject_WithInvalidType_ShouldThrowEncoderException() {
        Object input = 12345;
        assertThrows(EncoderException.class, () -> new Hex().encode(input));
    }

    @Test
    void testEncodeObject_WithInvalidCharset_ShouldThrowEncoderException() {
        Hex hex = new Hex("INVALID_CHARSET");
        assertThrows(EncoderException.class, () -> hex.encode("test"));
    }

    // Testes para getCharsetName()

    @Test
    void testGetCharsetName_DefaultConstructor_ShouldReturnDefaultCharset() {
        assertEquals(Hex.DEFAULT_CHARSET_NAME, new Hex().getCharsetName());
    }

    @Test
    void testGetCharsetName_CustomConstructor_ShouldReturnCustomCharset() {
        String customCharset = "ISO-8859-1";
        assertEquals(customCharset, new Hex(customCharset).getCharsetName());
    }

    // Testes para toString()

    @Test
    void testToString_ShouldContainClassNameAndCharset() {
        String result = new Hex().toString();
        assertTrue(result.contains("Hex"));
        assertTrue(result.contains(Hex.DEFAULT_CHARSET_NAME));
    }

    @Test
    void testToString_WithCustomCharset_ShouldContainCustomCharset() {
        String customCharset = "ISO-8859-1";
        String result = new Hex(customCharset).toString();
        assertTrue(result.contains(customCharset));
    }

    // Testes adicionais para mutação

    @Test
    void testEncodeDecodeRoundTrip_ShouldReturnOriginalData() throws DecoderException {
        byte[] original = {0x00, 0x01, 0x7f, (byte) 0x80, (byte) 0xff};
        char[] encoded = Hex.encodeHex(original);
        byte[] decoded = Hex.decodeHex(encoded);
        assertArrayEquals(original, decoded);
    }

    @Test
    void testEncodeHexStringDecodeHexRoundTrip_ShouldReturnOriginalData() throws DecoderException {
        byte[] original = {0x00, 0x01, 0x7f, (byte) 0x80, (byte) 0xff};
        String encoded = Hex.encodeHexString(original);
        byte[] decoded = Hex.decodeHex(encoded.toCharArray());
        assertArrayEquals(original, decoded);
    }

    @Test
    void testEncodeHexWithDifferentCases_ShouldProduceDifferentOutput() {
        byte[] input = {0x0a, 0x0b, 0x0c};
        char[] lower = Hex.encodeHex(input, true);
        char[] upper = Hex.encodeHex(input, false);
        assertFalse(Arrays.equals(lower, upper));
        assertEquals(new String(lower).toUpperCase(), new String(upper));
    }
}
