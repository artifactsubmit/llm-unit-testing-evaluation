package experimento.deepseek.hex;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

class DeepSeek2HexTest {

    // Testes para decodeHex(char[] data)
    @Test
    void decodeHex_WithValidInput_ShouldReturnCorrectByteArray() throws DecoderException {
        char[] input = {'0', '1', 'a', 'f', 'F', 'F', '0', '0'};
        byte[] expected = {0x01, (byte) 0xaf, (byte) 0xFF, 0x00};
        assertArrayEquals(expected, Hex.decodeHex(input));
    }

    @Test
    void decodeHex_WithOddNumberOfChars_ShouldThrowDecoderException() {
        char[] input = {'0', '1', 'a'};
        assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
    }

    @ParameterizedTest
    @ValueSource(chars = {'g', 'h', 'z', '!', '@', 'G', 'H', 'Z'})
    void decodeHex_WithInvalidChars_ShouldThrowDecoderException(char invalidChar) {
        char[] input = {'0', '1', invalidChar, 'f'};
        assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
    }

    @Test
    void decodeHex_WithEmptyInput_ShouldReturnEmptyByteArray() throws DecoderException {
        char[] input = {};
        byte[] expected = {};
        assertArrayEquals(expected, Hex.decodeHex(input));
    }

    // Testes para encodeHex(byte[] data)
    @Test
    void encodeHex_WithValidInput_ShouldReturnCorrectCharArray() {
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xFF, 0x00};
        char[] expected = {'0', '1', 'a', 'f', 'f', 'f', '0', '0'};
        assertArrayEquals(expected, Hex.encodeHex(input));
    }

    @Test
    void encodeHex_WithEmptyInput_ShouldReturnEmptyCharArray() {
        byte[] input = {};
        char[] expected = {};
        assertArrayEquals(expected, Hex.encodeHex(input));
    }

    // Testes para encodeHex(byte[] data, boolean toLowerCase)
    @Test
    void encodeHex_WithLowerCaseTrue_ShouldReturnLowercaseChars() {
        byte[] input = {0x0a, 0x0B, 0x0C, 0x0D};
        char[] expected = {'0', 'a', '0', 'b', '0', 'c', '0', 'd'};
        assertArrayEquals(expected, Hex.encodeHex(input, true));
    }

    @Test
    void encodeHex_WithLowerCaseFalse_ShouldReturnUppercaseChars() {
        byte[] input = {0x0a, 0x0B, 0x0C, 0x0D};
        char[] expected = {'0', 'A', '0', 'B', '0', 'C', '0', 'D'};
        assertArrayEquals(expected, Hex.encodeHex(input, false));
    }

    // Testes para encodeHexString(byte[] data)
    @Test
    void encodeHexString_WithValidInput_ShouldReturnCorrectString() {
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xFF, 0x00};
        String expected = "01afff00";
        assertEquals(expected, Hex.encodeHexString(input));
    }

    @Test
    void encodeHexString_WithEmptyInput_ShouldReturnEmptyString() {
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
    void toDigit_WithValidChars_ShouldReturnCorrectValue(char ch, int index, int expected) throws DecoderException {
        assertEquals(expected, Hex.toDigit(ch, index));
    }

    @ParameterizedTest
    @ValueSource(chars = {'g', 'h', '!', '@', 'G', 'H'})
    void toDigit_WithInvalidChars_ShouldThrowDecoderException(char invalidChar) {
        assertThrows(DecoderException.class, () -> Hex.toDigit(invalidChar, 0));
    }

    // Testes para decode(byte[] array)
    @Test
    void decode_WithValidByteArray_ShouldReturnCorrectBytes() throws DecoderException {
        byte[] input = "01afff00".getBytes();
        byte[] expected = {0x01, (byte) 0xaf, (byte) 0xFF, 0x00};
        assertArrayEquals(expected, new Hex().decode(input));
    }

    @Test
    void decode_WithOddLengthByteArray_ShouldThrowDecoderException() {
        byte[] input = "01a".getBytes();
        assertThrows(DecoderException.class, () -> new Hex().decode(input));
    }

    // Testes para decode(Object object)
    @Test
    void decode_WithValidString_ShouldReturnCorrectBytes() throws DecoderException {
        String input = "01afff00";
        byte[] expected = {0x01, (byte) 0xaf, (byte) 0xFF, 0x00};
        assertArrayEquals(expected, (byte[]) new Hex().decode(input));
    }

    @Test
    void decode_WithValidCharArray_ShouldReturnCorrectBytes() throws DecoderException {
        char[] input = {'0', '1', 'a', 'f', 'f', 'f', '0', '0'};
        byte[] expected = {0x01, (byte) 0xaf, (byte) 0xFF, 0x00};
        assertArrayEquals(expected, (byte[]) new Hex().decode(input));
    }

    @Test
    void decode_WithInvalidObjectType_ShouldThrowDecoderException() {
        Object input = 12345;
        assertThrows(DecoderException.class, () -> new Hex().decode(input));
    }

    // Testes para encode(byte[] array)
    @Test
    void encode_WithValidByteArray_ShouldReturnCorrectEncodedBytes() {
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xFF, 0x00};
        byte[] expected = "01afff00".getBytes();
        assertArrayEquals(expected, new Hex().encode(input));
    }

    @Test
    void encode_WithEmptyByteArray_ShouldReturnEmptyByteArray() {
        byte[] input = {};
        byte[] expected = {};
        assertArrayEquals(expected, new Hex().encode(input));
    }

    // Testes para encode(Object object)
    @Test
    void encode_WithValidString_ShouldReturnCorrectCharArray() throws EncoderException {
        String input = "test";
        char[] result = (char[]) new Hex().encode(input);
        assertNotNull(result);
        assertEquals(8, result.length);
    }

    @Test
    void encode_WithValidByteArray_ShouldReturnCorrectCharArray() throws EncoderException {
        byte[] input = {0x01, (byte) 0xaf, (byte) 0xFF, 0x00};
        char[] expected = {'0', '1', 'a', 'f', 'f', 'f', '0', '0'};
        assertArrayEquals(expected, (char[]) new Hex().encode(input));
    }

    @Test
    void encode_WithInvalidObjectType_ShouldThrowEncoderException() {
        Object input = 12345;
        assertThrows(EncoderException.class, () -> new Hex().encode(input));
    }

    // Testes para getCharsetName()
    @Test
    void getCharsetName_WithDefaultConstructor_ShouldReturnDefaultCharset() {
        assertEquals(Hex.DEFAULT_CHARSET_NAME, new Hex().getCharsetName());
    }

    @Test
    void getCharsetName_WithCustomCharset_ShouldReturnCustomCharset() {
        String customCharset = StandardCharsets.ISO_8859_1.name();
        assertEquals(customCharset, new Hex(customCharset).getCharsetName());
    }

    // Testes para toString()
    @Test
    void toString_ShouldContainClassNameAndCharsetName() {
        String result = new Hex().toString();
        assertTrue(result.contains("Hex"));
        assertTrue(result.contains("charsetName=" + Hex.DEFAULT_CHARSET_NAME));
    }

    // Testes para construtores
    @Test
    void constructor_WithNullCharset_ShouldUseDefaultCharset() {
        Hex hex = new Hex(null);
        assertEquals(Hex.DEFAULT_CHARSET_NAME, hex.getCharsetName());
    }

    // Testes parametrizados para várias combinações de entrada
    @ParameterizedTest
    @MethodSource("provideHexTestData")
    void encodeThenDecode_ShouldReturnOriginalData(byte[] input) throws DecoderException {
        Hex hex = new Hex();
        char[] encoded = hex.encodeHex(input);
        byte[] decoded = hex.decodeHex(encoded);
        assertArrayEquals(input, decoded);
    }

    private static Stream<Arguments> provideHexTestData() {
        return Stream.of(
                Arguments.of(new byte[]{0}),
                Arguments.of(new byte[]{127}),
                Arguments.of(new byte[]{-128}),
                Arguments.of(new byte[]{0, 1, 2, 3, 4, 5}),
                Arguments.of(new byte[]{-1, -2, -3, -4, -5}),
                Arguments.of("Hello World".getBytes())
        );
    }

    // Testes de borda/canto
    @Test
    void encodeHex_WithAllPossibleByteValues_ShouldProduceCorrectOutput() {
        byte[] allBytes = new byte[256];
        for (int i = 0; i < 256; i++) {
            allBytes[i] = (byte) (i - 128);
        }

        char[] result = Hex.encodeHex(allBytes);
        assertEquals(512, result.length); // 2 chars per byte

        // Verify first and last values
        assertEquals('8', result[0]); // First byte is -128 (0x80)
        assertEquals('0', result[1]);
        assertEquals('7', result[510]); // Last byte is 127 (0x7F)
        assertEquals('f', result[511]);
    }

    // Testes para comportamento com charset inválido
    @Test
    void encode_WithInvalidCharset_ShouldThrowIllegalStateException() {
        Hex hex = new Hex("INVALID_CHARSET");
        byte[] input = {0x01, 0x02};
        assertThrows(IllegalStateException.class, () -> hex.encode(input));
    }

    @Test
    void decode_WithInvalidCharset_ShouldThrowDecoderException() {
        Hex hex = new Hex("INVALID_CHARSET");
        byte[] input = "0102".getBytes();
        assertThrows(DecoderException.class, () -> hex.decode(input));
    }

    // Testes para null e vazio
    @ParameterizedTest
    @NullAndEmptySource
    void encodeHex_WithNullInput_ShouldThrowNullPointerException(byte[] input) {
        assertThrows(NullPointerException.class, () -> Hex.encodeHex(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void decodeHex_WithNullInput_ShouldThrowNullPointerException(char[] input) {
        assertThrows(NullPointerException.class, () -> Hex.decodeHex(input));
    }
}
