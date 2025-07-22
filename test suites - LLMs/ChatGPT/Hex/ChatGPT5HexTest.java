package experimento.chatgpt.hex;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT5HexTest {

    @Test
    void testEncodeHexLowerCase() {
        byte[] input = {0x0A, 0x1B, 0x2C};
        char[] expected = {'0','a','1','b','2','c'};
        assertArrayEquals(expected, Hex.encodeHex(input));
    }

    @Test
    void testEncodeHexUpperCase() {
        byte[] input = {0x0A, 0x1B, 0x2C};
        char[] expected = {'0','A','1','B','2','C'};
        assertArrayEquals(expected, Hex.encodeHex(input, false));
    }

    @Test
    void testEncodeHexString() {
        byte[] input = {0x0F, 0x1A};
        assertEquals("0f1a", Hex.encodeHexString(input));
    }

    @Test
    void testDecodeHexValid() throws DecoderException {
        char[] hexChars = {'0', 'f', '1', 'a'};
        byte[] expected = {0x0F, 0x1A};
        assertArrayEquals(expected, Hex.decodeHex(hexChars));
    }

    @Test
    void testDecodeHexOddLengthThrowsException() {
        char[] hexChars = {'0', 'f', '1'};
        DecoderException ex = assertThrows(DecoderException.class, () -> Hex.decodeHex(hexChars));
        assertEquals("Odd number of characters.", ex.getMessage());
    }

    @Test
    void testDecodeHexIllegalCharacterThrowsException() {
        char[] hexChars = {'0', 'g'};
        DecoderException ex = assertThrows(DecoderException.class, () -> Hex.decodeHex(hexChars));
        assertTrue(ex.getMessage().contains("Illegal hexadecimal charcter"));
    }

    @Test
    void testToDigitValidChars() throws DecoderException {
        assertEquals(10, Hex.toDigit('a', 0));
        assertEquals(15, Hex.toDigit('F', 0));
        assertEquals(0, Hex.toDigit('0', 0));
    }

    @Test
    void testToDigitInvalidCharThrowsException() {
        DecoderException ex = assertThrows(DecoderException.class, () -> Hex.toDigit('z', 1));
        assertTrue(ex.getMessage().contains("Illegal hexadecimal charcter"));
    }

    @Test
    void testDecodeByteArrayUtf8() throws DecoderException {
        Hex hex = new Hex("UTF-8");
        byte[] input = "0f1a".getBytes();
        byte[] expected = {0x0F, 0x1A};
        assertArrayEquals(expected, hex.decode(input));
    }

    @Test
    void testDecodeObjectWithString() throws DecoderException {
        Hex hex = new Hex();
        Object result = hex.decode("0f1a");
        assertArrayEquals(new byte[]{0x0F, 0x1A}, (byte[]) result);
    }

    @Test
    void testDecodeObjectWithCharArray() throws DecoderException {
        Hex hex = new Hex();
        Object result = hex.decode(new char[]{'0', 'f', '1', 'a'});
        assertArrayEquals(new byte[]{0x0F, 0x1A}, (byte[]) result);
    }

    @Test
    void testDecodeObjectWithInvalidTypeThrowsException() {
        Hex hex = new Hex();
        assertThrows(DecoderException.class, () -> hex.decode(123));
    }

    @Test
    void testEncodeByteArrayToByteArray() {
        Hex hex = new Hex("UTF-8");
        byte[] input = {0x0F, 0x1A};
        byte[] result = hex.encode(input);
        assertEquals("0f1a", new String(result));
    }

    @Test
    void testEncodeObjectWithString() throws EncoderException {
        Hex hex = new Hex();
        Object result = hex.encode("test");
        assertArrayEquals(Hex.encodeHex("test".getBytes()), (char[]) result);
    }

    @Test
    void testEncodeObjectWithByteArray() throws EncoderException {
        Hex hex = new Hex();
        byte[] input = {0x0F, 0x1A};
        Object result = hex.encode(input);
        assertArrayEquals(Hex.encodeHex(input), (char[]) result);
    }

    @Test
    void testEncodeObjectWithInvalidTypeThrowsException() {
        Hex hex = new Hex();
        assertThrows(EncoderException.class, () -> hex.encode(123));
    }

    @Test
    void testGetCharsetName() {
        Hex hex = new Hex("UTF-16");
        assertEquals("UTF-16", hex.getCharsetName());
    }

    @Test
    void testToStringIncludesCharset() {
        Hex hex = new Hex("ISO-8859-1");
        String representation = hex.toString();
        assertTrue(representation.contains("charsetName=ISO-8859-1"));
    }

    @Test
    void testDefaultCharsetIsUtf8() {
        Hex hex = new Hex();
        assertEquals("UTF-8", hex.getCharsetName());
    }

}

