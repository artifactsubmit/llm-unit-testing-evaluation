package experimento.deepseek.hex;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import java.util.stream.Stream;
import java.nio.charset.StandardCharsets;

class DeepSeek4HexTest {

    // Testes para decodeHex(char[] data)
    @Test
    void decodeHex_ValidEvenLengthInput_ReturnsCorrectByteArray() throws DecoderException {
        char[] input = {'0', '1', 'A', 'F', 'f', 'e'};
        byte[] expected = {0x01, (byte) 0xAF, (byte) 0xfe};
        assertArrayEquals(expected, Hex.decodeHex(input));
    }

    @Test
    void decodeHex_OddLengthInput_ThrowsDecoderException() {
        char[] input = {'0', '1', 'A'};
        assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
    }

    @Test
    void decodeHex_InvalidHexCharacters_ThrowsDecoderException() {
        char[] input = {'G', '1', 'X', 'Y'};
        assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void decodeHex_NullOrEmptyInput_ReturnsEmptyArray(char[] input) throws DecoderException {
        byte[] result = Hex.decodeHex(input);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // Testes para encodeHex(byte[] data)
    @Test
    void encodeHex_ValidInput_ReturnsLowercaseHexChars() {
        byte[] input = {0x01, 0x2F, (byte) 0xFE};
        char[] expected = {'0', '1', '2', 'f', 'f', 'e'};
        assertArrayEquals(expected, Hex.encodeHex(input));
    }

    @Test
    void encodeHex_EmptyInput_ReturnsEmptyArray() {
        byte[] input = {};
        char[] result = Hex.encodeHex(input);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void encodeHex_NullInput_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Hex.encodeHex(null));
    }

    // Testes para encodeHex(byte[] data, boolean toLowerCase)
    @Test
    void encodeHex_WithToLowerCaseTrue_ReturnsLowercaseHexChars() {
        byte[] input = {0x0A, 0x1B, (byte) 0xCD};
        char[] expected = {'0', 'a', '1', 'b', 'c', 'd'};
        assertArrayEquals(expected, Hex.encodeHex(input, true));
    }

    @Test
    void encodeHex_WithToLowerCaseFalse_ReturnsUppercaseHexChars() {
        byte[] input = {0x0A, 0x1B, (byte) 0xCD};
        char[] expected = {'0', 'A', '1', 'B', 'C', 'D'};
        assertArrayEquals(expected, Hex.encodeHex(input, false));
    }

    // Testes para encodeHex(byte[] data, char[] toDigits)
    @Test
    void encodeHex_WithCustomDigits_ReturnsCorrectHexChars() {
        byte[] input = {0x01, 0x0F};
        char[] customDigits = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        char[] expected = {'0', '1', '0', 'f'};
        assertArrayEquals(expected, Hex.encodeHex(input, customDigits));
    }

    @Test
    void encodeHex_WithNullDigits_ThrowsNullPointerException() {
        byte[] input = {0x01};
        assertThrows(NullPointerException.class, () -> Hex.encodeHex(input, null));
    }

    // Testes para encodeHexString(byte[] data)
    @Test
    void encodeHexString_ValidInput_ReturnsLowercaseHexString() {
        byte[] input = {0x01, 0x2F, (byte) 0xFE};
        String expected = "012ffe";
        assertEquals(expected, Hex.encodeHexString(input));
    }

    @Test
    void encodeHexString_EmptyInput_ReturnsEmptyString() {
        byte[] input = {};
        String result = Hex.encodeHexString(input);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Testes para toDigit(char ch, int index)
    @ParameterizedTest
    @ValueSource(chars = {'0', '1', '9', 'a', 'f', 'A', 'F'})
    void toDigit_ValidHexChars_ReturnsCorrectValue(char ch) throws DecoderException {
        int result = Hex.toDigit(ch, 0);
        assertTrue(result >= 0 && result <= 15);
    }

    @ParameterizedTest
    @ValueSource(chars = {'g', 'G', 'x', 'X', ' ', '-', '$'})
    void toDigit_InvalidHexChars_ThrowsDecoderException(char ch) {
        assertThrows(DecoderException.class, () -> Hex.toDigit(ch, 0));
    }

    // Testes para decode(byte[] array)
    @Test
    void decode_ValidHexBytes_ReturnsCorrectByteArray() throws DecoderException {
        byte[] input = "012fFe".getBytes(StandardCharsets.UTF_8);
        byte[] expected = {0x01, 0x2F, (byte) 0xFE};
        assertArrayEquals(expected, new Hex().decode(input));
    }

    @Test
    void decode_InvalidCharset_ThrowsDecoderException() {
        Hex hex = new Hex("INVALID_CHARSET");
        byte[] input = "0123".getBytes(StandardCharsets.UTF_8);
        assertThrows(DecoderException.class, () -> hex.decode(input));
    }

    // Testes para decode(Object object)
    @Test
    void decode_WithStringObject_ReturnsCorrectByteArray() throws DecoderException {
        String input = "012fFe";
        byte[] expected = {0x01, 0x2F, (byte) 0xFE};
        assertArrayEquals(expected, (byte[]) new Hex().decode(input));
    }

    @Test
    void decode_WithCharArrayObject_ReturnsCorrectByteArray() throws DecoderException {
        char[] input = {'0', '1', '2', 'f', 'F', 'e'};
        byte[] expected = {0x01, 0x2F, (byte) 0xFE};
        assertArrayEquals(expected, (byte[]) new Hex().decode(input));
    }

    @Test
    void decode_WithInvalidObjectType_ThrowsDecoderException() {
        Object input = 12345;
        assertThrows(DecoderException.class, () -> new Hex().decode(input));
    }

    // Testes para encode(byte[] array)
    @Test
    void encode_ValidInput_ReturnsHexBytes() {
        byte[] input = {0x01, 0x2F, (byte) 0xFE};
        byte[] expected = "012ffe".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expected, new Hex().encode(input));
    }

    @Test
    void encode_WithInvalidCharset_ThrowsIllegalStateException() {
        Hex hex = new Hex("INVALID_CHARSET");
        byte[] input = {0x01, 0x02};
        assertThrows(IllegalStateException.class, () -> hex.encode(input));
    }

    // Testes para encode(Object object)
    @Test
    void encode_WithStringObject_ReturnsHexChars() throws EncoderException {
        String input = "test";
        char[] result = (char[]) new Hex().encode(input);
        assertNotNull(result);
        assertEquals(8, result.length); // "test" in hex is 74657374 (8 chars)
    }

    @Test
    void encode_WithByteArrayObject_ReturnsHexChars() throws EncoderException {
        byte[] input = {0x01, 0x02};
        char[] expected = {'0', '1', '0', '2'};
        assertArrayEquals(expected, (char[]) new Hex().encode(input));
    }

    @Test
    void encode_WithInvalidObjectType_ThrowsEncoderException() {
        Object input = 12345;
        assertThrows(EncoderException.class, () -> new Hex().encode(input));
    }

    // Testes para getCharsetName()
    @Test
    void getCharsetName_DefaultConstructor_ReturnsDefaultCharset() {
        Hex hex = new Hex();
        assertEquals(Hex.DEFAULT_CHARSET_NAME, hex.getCharsetName());
    }

    @Test
    void getCharsetName_CustomConstructor_ReturnsCustomCharset() {
        String customCharset = StandardCharsets.ISO_8859_1.name();
        Hex hex = new Hex(customCharset);
        assertEquals(customCharset, hex.getCharsetName());
    }

    // Testes para toString()
    @Test
    void toString_ContainsClassAndCharsetInfo() {
        Hex hex = new Hex();
        String result = hex.toString();
        assertTrue(result.contains("Hex"));
        assertTrue(result.contains(hex.getCharsetName()));
    }

    // Testes parametrizados para cobrir mais casos
    @ParameterizedTest
    @MethodSource("provideHexPairs")
    void encodeHexThenDecodeHex_ShouldReturnOriginalBytes(byte[] input) throws DecoderException {
        char[] hexChars = Hex.encodeHex(input);
        byte[] result = Hex.decodeHex(hexChars);
        assertArrayEquals(input, result);
    }

    private static Stream<Arguments> provideHexPairs() {
        return Stream.of(
                Arguments.of(new byte[]{0x00}),
                Arguments.of(new byte[]{(byte) 0xFF}),
                Arguments.of(new byte[]{0x12, 0x34, 0x56, 0x78}),
                Arguments.of(new byte[]{(byte) 0xAB, (byte) 0xCD, (byte) 0xEF}),
                Arguments.of(new byte[0]) // edge case: empty array
        );
    }

    @ParameterizedTest
    @MethodSource("provideHexStrings")
    void encodeHexStringThenDecode_ShouldReturnOriginalBytes(String hexString, byte[] expected) throws DecoderException {
        byte[] result = Hex.decodeHex(hexString.toCharArray());
        assertArrayEquals(expected, result);
    }

    private static Stream<Arguments> provideHexStrings() {
        return Stream.of(
                Arguments.of("000102", new byte[]{0x00, 0x01, 0x02}),
                Arguments.of("ffffff", new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF}),
                Arguments.of("a1b2c3", new byte[]{(byte) 0xA1, (byte) 0xB2, (byte) 0xC3}),
                Arguments.of("", new byte[0]) // edge case: empty string
        );
    }
}
