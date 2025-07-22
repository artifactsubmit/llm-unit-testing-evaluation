package experimento.chatgpt.hex;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT1HexTest {

    // ----------- MÉTODOS ESTÁTICOS -----------

    @Test
    void decodeHex_validHexChars_returnsCorrectBytes() throws DecoderException {
        char[] input = {'4', '1', '2', '0'};
        byte[] expected = {0x41, 0x20};
        byte[] actual = Hex.decodeHex(input);
        assertArrayEquals(expected, actual);
    }

    @Test
    void decodeHex_oddLength_throwsDecoderException() {
        char[] input = {'A', 'B', 'C'};
        DecoderException ex = assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
        assertEquals("Odd number of characters.", ex.getMessage());
    }

    @Test
    void decodeHex_invalidCharacter_throwsDecoderException() {
        char[] input = {'0', 'G'};
        DecoderException ex = assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
        assertTrue(ex.getMessage().contains("Illegal hexadecimal charcter G"));
    }

    @Test
    void encodeHex_lowercaseEncoding_returnsExpectedChars() {
        byte[] input = {0x0F, 0x10};
        char[] expected = {'0', 'f', '1', '0'};
        char[] actual = Hex.encodeHex(input, true);
        assertArrayEquals(expected, actual);
    }

    @Test
    void encodeHex_uppercaseEncoding_returnsExpectedChars() {
        byte[] input = {0x0F, 0x10};
        char[] expected = {'0', 'F', '1', '0'};
        char[] actual = Hex.encodeHex(input, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    void encodeHex_usingDigitsLower_returnsCorrectOutput() {
        byte[] input = {0x01, 0x23};
        char[] expected = {'0', '1', '2', '3'};
        char[] actual = Hex.encodeHex(input, new char[]{'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'});
        assertArrayEquals(expected, actual);
    }

    @Test
    void encodeHexString_returnsExpectedString() {
        byte[] input = {0x11, 0x22};
        String expected = "1122";
        assertEquals(expected, Hex.encodeHexString(input));
    }

    @Test
    void toDigit_validHexChar_returnsCorrectDigit() throws DecoderException {
        assertEquals(10, Hex.toDigit('a', 0));
        assertEquals(15, Hex.toDigit('F', 1));
    }

    @Test
    void toDigit_invalidHexChar_throwsDecoderException() {
        DecoderException ex = assertThrows(DecoderException.class, () -> Hex.toDigit('Z', 2));
        assertTrue(ex.getMessage().contains("Illegal hexadecimal charcter Z"));
    }

    // ----------- MÉTODOS DE INSTÂNCIA -----------

    @Test
    void decode_byteArray_validHex_returnsCorrectBytes() throws DecoderException {
        Hex hex = new Hex();
        byte[] input = "6162".getBytes(); // "ab"
        byte[] expected = {'a', 'b'};
        assertArrayEquals(expected, hex.decode(input));
    }

    @Test
    void decode_invalidCharset_throwsDecoderException() {
        Hex hex = new Hex("INVALID_CHARSET");
        byte[] input = "6162".getBytes();
        DecoderException ex = assertThrows(DecoderException.class, () -> hex.decode(input));
        assertTrue(ex.getMessage().contains("INVALID_CHARSET"));
    }

    @Test
    void decode_objectAsString_returnsDecodedBytes() throws DecoderException {
        Hex hex = new Hex();
        Object input = "4142";
        byte[] expected = {'A', 'B'};
        assertArrayEquals(expected, (byte[]) hex.decode(input));
    }

    @Test
    void decode_objectAsCharArray_returnsDecodedBytes() throws DecoderException {
        Hex hex = new Hex();
        Object input = new char[]{'4', '3'};
        byte[] expected = {0x43};
        assertArrayEquals(expected, (byte[]) hex.decode(input));
    }

    @Test
    void decode_objectInvalidType_throwsDecoderException() {
        Hex hex = new Hex();
        Object input = 12345;
        assertThrows(DecoderException.class, () -> hex.decode(input));
    }

    @Test
    void encode_byteArray_returnsExpectedBytes() {
        Hex hex = new Hex();
        byte[] input = {0x61}; // 'a'
        byte[] result = hex.encode(input);
        assertEquals("61", new String(result));
    }

    @Test
    void encode_objectAsByteArray_returnsCharArray() throws EncoderException {
        Hex hex = new Hex();
        Object input = new byte[]{0x62}; // 'b'
        Object result = hex.encode(input);
        assertArrayEquals(new char[]{'6', '2'}, (char[]) result);
    }

    @Test
    void encode_objectAsString_returnsCharArray() throws EncoderException {
        Hex hex = new Hex();
        Object input = "C";
        Object result = hex.encode(input);
        assertArrayEquals(new char[]{'4', '3'}, (char[]) result);
    }

    @Test
    void encode_objectInvalidType_throwsEncoderException() {
        Hex hex = new Hex();
        Object input = 999;
        assertThrows(EncoderException.class, () -> hex.encode(input));
    }

    @Test
    void getCharsetName_returnsConfiguredCharset() {
        Hex hex = new Hex("UTF-16");
        assertEquals("UTF-16", hex.getCharsetName());
    }

    @Test
    void toString_includesCharsetName() {
        Hex hex = new Hex("UTF-8");
        String result = hex.toString();
        assertTrue(result.contains("charsetName=UTF-8"));
    }
}


