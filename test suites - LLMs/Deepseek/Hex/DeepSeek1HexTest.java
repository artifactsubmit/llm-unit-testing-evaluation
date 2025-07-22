package experimento.deepseek.hex;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

class DeepSeek1HexTest {

    // Testes para decodeHex(char[] data)
    @Test
    void decodeHex_ValidInput_ReturnsCorrectByteArray() throws DecoderException {
        char[] input = {'0', '1', 'a', 'f', 'F', '0'};
        byte[] expected = {0x01, (byte) 0xaf, (byte) 0xf0};
        assertArrayEquals(expected, Hex.decodeHex(input));
    }

    @Test
    void decodeHex_OddNumberOfChars_ThrowsDecoderException() {
        char[] input = {'0', '1', 'a'};
        assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
    }

    @ParameterizedTest
    @ValueSource(chars = {'g', 'h', '!', '@', ' ', 'G', 'H'})
    void decodeHex_InvalidHexChars_ThrowsDecoderException(char invalidChar) {
        char[] input = {'0', '1', invalidChar, 'f'};
        assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
    }

    @Test
    void decodeHex_EmptyArray_ReturnsEmptyByteArray() throws DecoderException {
        char[] input = {};
        byte[] expected = {};
        assertArrayEquals(expected, Hex.decodeHex(input));
    }

    // Testes para encodeHex(byte[] data)
    @Test
    void encodeHex_ValidInput_ReturnsLowercaseHexChars() {
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xf0};
        char[] expected = {'0', '1', 'a', 'f', 'f', '0'};
        assertArrayEquals(expected, Hex.encodeHex(input));
    }

    @Test
    void encodeHex_EmptyArray_ReturnsEmptyCharArray() {
        byte[] input = {};
        char[] expected = {};
        assertArrayEquals(expected, Hex.encodeHex(input));
    }

    // Testes para encodeHex(byte[] data, boolean toLowerCase)
    @Test
    void encodeHex_WithToLowerCaseTrue_ReturnsLowercaseHexChars() {
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xf0};
        char[] expected = {'0', '1', 'a', 'f', 'f', '0'};
        assertArrayEquals(expected, Hex.encodeHex(input, true));
    }

    @Test
    void encodeHex_WithToLowerCaseFalse_ReturnsUppercaseHexChars() {
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xf0};
        char[] expected = {'0', '1', 'A', 'F', 'F', '0'};
        assertArrayEquals(expected, Hex.encodeHex(input, false));
    }

    // Testes para encodeHexString(byte[] data)
    @Test
    void encodeHexString_ValidInput_ReturnsLowercaseHexString() {
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xf0};
        String expected = "01aff0";
        assertEquals(expected, Hex.encodeHexString(input));
    }

    @Test
    void encodeHexString_EmptyArray_ReturnsEmptyString() {
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
    void toDigit_ValidHexChars_ReturnsCorrectValue(char ch, int index, int expected) throws DecoderException {
        assertEquals(expected, Hex.toDigit(ch, index));
    }

    @ParameterizedTest
    @ValueSource(chars = {'g', 'h', '!', '@', ' ', 'G', 'H'})
    void toDigit_InvalidHexChars_ThrowsDecoderException(char invalidChar) {
        assertThrows(DecoderException.class, () -> Hex.toDigit(invalidChar, 0));
    }

    // Testes para o construtor e métodos de instância
    @Test
    void constructor_Default_UsesDefaultCharset() {
        Hex hex = new Hex();
        assertEquals(Hex.DEFAULT_CHARSET_NAME, hex.getCharsetName());
    }

    @Test
    void constructor_WithCharset_UsesSpecifiedCharset() {
        String charset = StandardCharsets.ISO_8859_1.name();
        Hex hex = new Hex(charset);
        assertEquals(charset, hex.getCharsetName());
    }

    @Test
    void decode_ValidByteArray_ReturnsDecodedBytes() throws DecoderException {
        Hex hex = new Hex();
        byte[] input = "01aff0".getBytes(StandardCharsets.UTF_8);
        byte[] expected = {0x01, (byte) 0xaf, (byte) 0xf0};
        assertArrayEquals(expected, hex.decode(input));
    }

    @Test
    void decode_InvalidCharset_ThrowsDecoderException() {
        Hex hex = new Hex("INVALID_CHARSET");
        byte[] input = "01aff0".getBytes(StandardCharsets.UTF_8);
        assertThrows(DecoderException.class, () -> hex.decode(input));
    }

    @Test
    void decode_ObjectWithString_ReturnsDecodedBytes() throws DecoderException {
        Hex hex = new Hex();
        String input = "01aff0";
        byte[] expected = {0x01, (byte) 0xaf, (byte) 0xf0};
        assertArrayEquals(expected, (byte[]) hex.decode(input));
    }

    @Test
    void decode_ObjectWithCharArray_ReturnsDecodedBytes() throws DecoderException {
        Hex hex = new Hex();
        char[] input = {'0', '1', 'a', 'f', 'f', '0'};
        byte[] expected = {0x01, (byte) 0xaf, (byte) 0xf0};
        assertArrayEquals(expected, (byte[]) hex.decode(input));
    }

    @Test
    void decode_ObjectWithInvalidType_ThrowsDecoderException() {
        Hex hex = new Hex();
        assertThrows(DecoderException.class, () -> hex.decode(123));
    }

    @Test
    void encode_ByteArray_ReturnsEncodedBytes() {
        Hex hex = new Hex();
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xf0};
        byte[] expected = "01aff0".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expected, hex.encode(input));
    }

    @Test
    void encode_String_ReturnsEncodedChars() throws EncoderException {
        Hex hex = new Hex();
        String input = "test";
        char[] expected = Hex.encodeHex(input.getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(expected, (char[]) hex.encode(input));
    }

    @Test
    void encode_ByteArrayObject_ReturnsEncodedChars() throws EncoderException {
        Hex hex = new Hex();
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xf0};
        char[] expected = {'0', '1', 'a', 'f', 'f', '0'};
        assertArrayEquals(expected, (char[]) hex.encode(input));
    }

    @Test
    void encode_ObjectWithInvalidType_ThrowsEncoderException() {
        Hex hex = new Hex();
        assertThrows(EncoderException.class, () -> hex.encode(123));
    }

    @Test
    void toString_ReturnsExpectedFormat() {
        Hex hex = new Hex();
        String expected = "Hex[charsetName=" + Hex.DEFAULT_CHARSET_NAME + "]";
        assertEquals(expected, hex.toString());
    }

    // Testes para casos limite e especiais
    @Test
    void encodeHex_AllPossibleByteValues_ReturnsCorrectHexChars() {
        byte[] input = new byte[256];
        for (int i = 0; i < 256; i++) {
            input[i] = (byte) i;
        }

        char[] result = Hex.encodeHex(input);
        assertEquals(512, result.length); // 2 chars per byte

        // Verify first and last values
        assertEquals('0', result[0]);
        assertEquals('0', result[1]);
        assertEquals('f', result[510]);
        assertEquals('f', result[511]);
    }

    @Test
    void decodeHex_AllPossibleHexPairs_ReturnsCorrectBytes() throws DecoderException {
        char[] input = new char[512];
        char[] digits = Hex.DIGITS_LOWER;

        for (int i = 0; i < 256; i++) {
            input[2*i] = digits[(i >> 4) & 0x0F];
            input[2*i + 1] = digits[i & 0x0F];
        }

        byte[] result = Hex.decodeHex(input);
        assertEquals(256, result.length);

        for (int i = 0; i < 256; i++) {
            assertEquals((byte) i, result[i]);
        }
    }

    // Testes para valores nulos e vazios
    @ParameterizedTest
    @NullAndEmptySource
    void encodeHex_NullOrEmptyInput_ReturnsEmptyArray(byte[] input) {
        assertArrayEquals(new char[0], Hex.encodeHex(input));
    }

    @Test
    void decodeHex_NullInput_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Hex.decodeHex(null));
    }

    @Test
    void toDigit_NullChar_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Hex.toDigit('\0', 0));
    }

    // Testes para mutantes específicos
    @Test
    void encodeHexString_VerifyNoStringBuilderMutation() {
        byte[] input = {0x01, 0x23, 0x45};
        String firstCall = Hex.encodeHexString(input);
        String secondCall = Hex.encodeHexString(input);
        assertSame(firstCall, secondCall); // Verifica se não há mutação interna
    }

    @Test
    void encodeHex_WithCustomDigits_ReturnsCorrectChars() {
        byte[] input = {0x01, (byte) 0xaf};
        char[] customDigits = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P'};
        char[] expected = {'A', 'B', 'G', 'P'};
        assertArrayEquals(expected, Hex.encodeHex(input, customDigits));
    }

    // Testes de performance para grandes volumes de dados
    @Test
    void encodeHex_LargeInput_ReturnsCorrectResult() {
        byte[] input = new byte[10000];
        new java.util.Random().nextBytes(input);

        char[] result = Hex.encodeHex(input);
        assertEquals(input.length * 2, result.length);

        // Verify first and last bytes were encoded correctly
        byte firstByte = input[0];
        char firstUpper = Hex.DIGITS_LOWER[(firstByte >> 4) & 0x0F];
        char firstLower = Hex.DIGITS_LOWER[firstByte & 0x0F];
        assertEquals(firstUpper, result[0]);
        assertEquals(firstLower, result[1]);

        byte lastByte = input[input.length - 1];
        char lastUpper = Hex.DIGITS_LOWER[(lastByte >> 4) & 0x0F];
        char lastLower = Hex.DIGITS_LOWER[lastByte & 0x0F];
        assertEquals(lastUpper, result[result.length - 2]);
        assertEquals(lastLower, result[result.length - 1]);
    }
}
