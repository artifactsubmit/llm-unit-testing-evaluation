package experimento.chatgpt.hex;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT2HexTest {

    @Test
    void testEncodeHexLowerCase() {
        byte[] input = {0x1, 0xA, 0xF, 0x10, 0x2F};
        char[] expected = {'0', '1', '0', 'a', '0', 'f', '1', '0', '2', 'f'};
        char[] result = Hex.encodeHex(input, true);
        assertArrayEquals(expected, result);
    }

    @Test
    void testEncodeHexUpperCase() {
        byte[] input = {0x1, 0xA, 0xF, 0x10, 0x2F};
        char[] expected = {'0', '1', '0', 'A', '0', 'F', '1', '0', '2', 'F'};
        char[] result = Hex.encodeHex(input, false);
        assertArrayEquals(expected, result);
    }

    @Test
    void testEncodeHexString() {
        byte[] input = {0x12, 0x34, 0x56};
        String expected = "123456";
        String result = Hex.encodeHexString(input);
        assertEquals(expected, result);
    }

    @Test
    void testDecodeHexValidInput() throws DecoderException {
        char[] input = {'0', '1', '0', 'a', 'f', 'f'};
        byte[] expected = {0x01, 0x0A, (byte) 0xFF};
        byte[] result = Hex.decodeHex(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testDecodeHexOddLengthThrowsException() {
        char[] input = {'0', '1', '0'};
        DecoderException exception = assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
        assertTrue(exception.getMessage().contains("Odd number of characters"));
    }

    @Test
    void testDecodeHexIllegalCharThrowsException() {
        char[] input = {'0', 'Z'};
        DecoderException exception = assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
        assertTrue(exception.getMessage().contains("Illegal hexadecimal charcter"));
    }

    @Test
    void testToDigitValid() throws DecoderException {
        assertEquals(10, Hex.toDigit('a', 0));
        assertEquals(15, Hex.toDigit('f', 0));
        assertEquals(5, Hex.toDigit('5', 0));
    }

    @Test
    void testToDigitInvalidThrowsException() {
        DecoderException exception = assertThrows(DecoderException.class, () -> Hex.toDigit('z', 1));
        assertTrue(exception.getMessage().contains("Illegal hexadecimal charcter"));
    }

    @Test
    void testEncodeByteArrayToByteArray() {
        Hex hex = new Hex();
        byte[] input = {0x0F, 0x1A};
        byte[] result = hex.encode(input);
        String strResult = new String(result);
        assertEquals("0f1a", strResult);
    }

    @Test
    void testDecodeByteArray() throws DecoderException {
        Hex hex = new Hex();
        byte[] input = "0f1a".getBytes();
        byte[] expected = {0x0F, 0x1A};
        assertArrayEquals(expected, hex.decode(input));
    }

    @Test
    void testDecodeByteArrayWithInvalidCharset() {
        Hex hex = new Hex("INVALID_CHARSET");
        byte[] input = "0f1a".getBytes();
        assertThrows(IllegalStateException.class, () -> hex.encode(input));
    }

    @Test
    void testEncodeWithObjectString() throws EncoderException {
        Hex hex = new Hex();
        char[] result = (char[]) hex.encode("hello");
        assertEquals("68656c6c6f", new String(result));
    }

    @Test
    void testEncodeWithObjectByteArray() throws EncoderException {
        Hex hex = new Hex();
        char[] result = (char[]) hex.encode("hi".getBytes());
        assertEquals("6869", new String(result));
    }

    @Test
    void testEncodeWithInvalidObjectThrowsException() {
        Hex hex = new Hex();
        assertThrows(EncoderException.class, () -> hex.encode(123));
    }

    @Test
    void testDecodeWithObjectString() throws DecoderException {
        Hex hex = new Hex();
        byte[] result = (byte[]) hex.decode("6869");
        assertArrayEquals("hi".getBytes(), result);
    }

    @Test
    void testDecodeWithObjectCharArray() throws DecoderException {
        Hex hex = new Hex();
        byte[] result = (byte[]) hex.decode(new char[]{'6', '8', '6', '9'});
        assertArrayEquals("hi".getBytes(), result);
    }

    @Test
    void testDecodeWithInvalidObjectThrowsException() {
        Hex hex = new Hex();
        assertThrows(DecoderException.class, () -> hex.decode(42.0));
    }

    @Test
    void testGetCharsetNameDefault() {
        Hex hex = new Hex();
        assertEquals("UTF-8", hex.getCharsetName());
    }

    @Test
    void testGetCharsetNameCustom() {
        Hex hex = new Hex("ISO-8859-1");
        assertEquals("ISO-8859-1", hex.getCharsetName());
    }

    @Test
    void testToStringIncludesCharset() {
        Hex hex = new Hex("UTF-16");
        String result = hex.toString();
        assertTrue(result.contains("UTF-16"));
    }
}


