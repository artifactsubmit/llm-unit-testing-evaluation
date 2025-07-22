package experimento.chatgpt.hex;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HexTest {

    @Test
    void testEncodeHexLowerCase() {
        byte[] data = {0x0F, 0x10, (byte) 0xFF};
        char[] expected = {'0', 'f', '1', '0', 'f', 'f'};
        assertArrayEquals(expected, Hex.encodeHex(data));
    }

    @Test
    void testEncodeHexUpperCase() {
        byte[] data = {0x0F, 0x10, (byte) 0xFF};
        char[] expected = {'0', 'F', '1', '0', 'F', 'F'};
        assertArrayEquals(expected, Hex.encodeHex(data, false));
    }

    @Test
    void testEncodeHexCustomDigits() {
        byte[] data = {0x0A};
        char[] customDigits = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] result = Hex.encodeHex(data, customDigits);
        assertEquals(customDigits[0], result[0]); // high nibble
        assertEquals(customDigits[10], result[1]); // low nibble (0xA = 10)
    }

    @Test
    void testEncodeHexString() {
        byte[] data = {0x01, 0x23, 0x45};
        assertEquals("012345", Hex.encodeHexString(data));
    }

    @Test
    void testDecodeHexValid() throws DecoderException {
        char[] hexChars = {'0', '1', '2', '3', '4', '5'};
        byte[] expected = {0x01, 0x23, 0x45};
        assertArrayEquals(expected, Hex.decodeHex(hexChars));
    }

    @Test
    void testDecodeHexOddLengthThrowsException() {
        char[] hexChars = {'0', '1', '2'};
        DecoderException ex = assertThrows(DecoderException.class, () -> Hex.decodeHex(hexChars));
        assertTrue(ex.getMessage().contains("Odd number of characters"));
    }

    @Test
    void testDecodeHexInvalidCharThrowsException() {
        char[] hexChars = {'0', 'G'};
        DecoderException ex = assertThrows(DecoderException.class, () -> Hex.decodeHex(hexChars));
        assertTrue(ex.getMessage().contains("Illegal hexadecimal charcter"));
    }

    @Test
    void testToDigitValidChar() throws DecoderException {
        assertEquals(10, Hex.toDigit('a', 0));
        assertEquals(15, Hex.toDigit('F', 1));
    }

    @Test
    void testToDigitInvalidCharThrowsException() {
        DecoderException ex = assertThrows(DecoderException.class, () -> Hex.toDigit('Z', 2));
        assertTrue(ex.getMessage().contains("Illegal hexadecimal charcter"));
    }

    @Test
    void testDecodeByteArray() throws DecoderException {
        Hex hex = new Hex(StandardCharsets.UTF_8.name());
        byte[] encoded = "4869".getBytes(StandardCharsets.UTF_8);
        byte[] decoded = hex.decode(encoded);
        assertArrayEquals(new byte[]{0x48, 0x69}, decoded);
    }

    @Test
    void testDecodeObjectWithString() throws DecoderException {
        Hex hex = new Hex();
        Object decoded = hex.decode("4869");
        assertArrayEquals(new byte[]{0x48, 0x69}, (byte[]) decoded);
    }

    @Test
    void testDecodeObjectWithCharArray() throws DecoderException {
        Hex hex = new Hex();
        Object decoded = hex.decode(new char[]{'4', '8', '6', '9'});
        assertArrayEquals(new byte[]{0x48, 0x69}, (byte[]) decoded);
    }

    @Test
    void testDecodeObjectWithInvalidType() {
        Hex hex = new Hex();
        assertThrows(DecoderException.class, () -> hex.decode(123));
    }

    @Test
    void testEncodeByteArrayToBytes() {
        Hex hex = new Hex(StandardCharsets.UTF_8.name());
        byte[] encoded = hex.encode("Hi".getBytes(StandardCharsets.UTF_8));
        assertEquals("4869", new String(encoded, StandardCharsets.UTF_8));
    }

    @Test
    void testEncodeObjectWithString() throws EncoderException {
        Hex hex = new Hex(StandardCharsets.UTF_8.name());
        Object encoded = hex.encode("Hi");
        assertArrayEquals(new char[]{'4', '8', '6', '9'}, (char[]) encoded);
    }

    @Test
    void testEncodeObjectWithByteArray() throws EncoderException {
        Hex hex = new Hex();
        Object encoded = hex.encode(new byte[]{0x48, 0x69});
        assertArrayEquals(new char[]{'4', '8', '6', '9'}, (char[]) encoded);
    }

    @Test
    void testEncodeObjectInvalidType() {
        Hex hex = new Hex();
        assertThrows(EncoderException.class, () -> hex.encode(123.45));
    }

    @Test
    void testGetCharsetName() {
        Hex hex = new Hex("ISO-8859-1");
        assertEquals("ISO-8859-1", hex.getCharsetName());
    }

    @Test
    void testToStringMethodIncludesCharset() {
        Hex hex = new Hex("UTF-8");
        String result = hex.toString();
        assertTrue(result.contains("charsetName=UTF-8"));
    }
}
