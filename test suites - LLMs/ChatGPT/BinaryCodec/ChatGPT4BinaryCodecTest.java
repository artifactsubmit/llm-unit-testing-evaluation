package experimento.chatgpt.binarycodec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT4BinaryCodecTest {

    private final BinaryCodec codec = new BinaryCodec();

    // ------------------------------
    // ENCODE
    // ------------------------------

    @Test
    void testEncodeByteArrayToAsciiBytes() {
        byte[] input = {0x0F}; // 00001111
        byte[] expected = "11110000".getBytes();
        assertArrayEquals(expected, codec.encode(input));
    }

    @Test
    void testEncodeObjectValidByteArray() throws EncoderException {
        byte[] input = {0x01}; // 00000001
        char[] expected = {'1','0','0','0','0','0','0','0'}; // ordem invertida
        assertArrayEquals(expected, (char[]) codec.encode(input));
    }

    @Test
    void testEncodeObjectInvalidTypeThrowsException() {
        assertThrows(EncoderException.class, () -> codec.encode("not a byte[]"));
    }

    // ------------------------------
    // DECODE
    // ------------------------------

    @Test
    void testDecodeByteArray() {
        byte[] input = "11110000".getBytes();
        byte[] expected = {0x0F}; // 00001111
        assertArrayEquals(expected, codec.decode(input));
    }

    @Test
    void testDecodeCharArray() throws DecoderException {
        char[] input = {'1','1','1','1','0','0','0','0'};
        byte[] expected = {0x0F};
        assertArrayEquals(expected, (byte[]) codec.decode(input));
    }

    @Test
    void testDecodeString() throws DecoderException {
        String input = "11110000";
        byte[] expected = {0x0F};
        assertArrayEquals(expected, (byte[]) codec.decode(input));
    }

    @Test
    void testDecodeObjectNullReturnsEmptyArray() throws DecoderException {
        byte[] result = (byte[]) codec.decode(null);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testDecodeObjectInvalidTypeThrowsException() {
        assertThrows(DecoderException.class, () -> codec.decode(123));
    }

    // ------------------------------
    // toByteArray
    // ------------------------------

    @Test
    void testToByteArrayFromValidBinaryString() {
        String binary = "11110000";
        byte[] expected = {0x0F};
        assertArrayEquals(expected, codec.toByteArray(binary));
    }

    @Test
    void testToByteArrayNullInputReturnsEmpty() {
        assertEquals(0, codec.toByteArray(null).length);
    }

    // ------------------------------
    // STATIC fromAscii / toAscii
    // ------------------------------

    @Test
    void testFromAsciiCharArray() {
        char[] input = {'1','1','1','1','0','0','0','0'};
        byte[] expected = {0x0F};
        assertArrayEquals(expected, BinaryCodec.fromAscii(input));
    }

    @Test
    void testFromAsciiByteArray() {
        byte[] input = "11110000".getBytes();
        byte[] expected = {0x0F};
        assertArrayEquals(expected, BinaryCodec.fromAscii(input));
    }

    @Test
    void testFromAsciiNullAndEmpty() {
        assertEquals(0, BinaryCodec.fromAscii((byte[]) null).length);
        assertEquals(0, BinaryCodec.fromAscii(new byte[0]).length);
        assertEquals(0, BinaryCodec.fromAscii((char[]) null).length);
        assertEquals(0, BinaryCodec.fromAscii(new char[0]).length);
    }

    @Test
    void testToAsciiBytes() {
        byte[] input = {0x0F}; // 00001111
        byte[] expected = "11110000".getBytes();
        assertArrayEquals(expected, BinaryCodec.toAsciiBytes(input));
    }

    @Test
    void testToAsciiChars() {
        byte[] input = {0x0F}; // 00001111
        char[] expected = {'1','1','1','1','0','0','0','0'};
        assertArrayEquals(expected, BinaryCodec.toAsciiChars(input));
    }

    @Test
    void testToAsciiString() {
        byte[] input = {0x0F};
        assertEquals("11110000", BinaryCodec.toAsciiString(input));
    }

    // ------------------------------
    // Reversibilidade
    // ------------------------------

    @Test
    void testEncodeThenDecodeReversibility() {
        byte[] original = {0x55, (byte) 0xAA}; // 01010101 10101010
        byte[] encoded = codec.encode(original);
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(original, decoded);
    }
}

