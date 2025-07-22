package experimento.chatgpt.binarycodec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT5BinaryCodecTest {

    private final BinaryCodec codec = new BinaryCodec();

    @Test
    void testEncodeByteArrayToAsciiBytes() {
        byte[] input = {0x0F}; // 00001111
        byte[] expected = "11110000".getBytes();
        byte[] result = codec.encode(input);
        assertArrayEquals(expected, result);
    }

    @Test
    void testEncodeByteArrayToAsciiChars() throws EncoderException {
        byte[] input = {0x55}; // 01010101
        char[] expected = "10101010".toCharArray();
        Object result = codec.encode(input);
        assertArrayEquals(expected, (char[]) result);
    }

    @Test
    void testEncodeInvalidObjectThrowsEncoderException() {
        Object invalidInput = "invalid";
        assertThrows(EncoderException.class, () -> codec.encode(invalidInput));
    }

    @Test
    void testDecodeByteArrayAsciiToBinary() {
        byte[] ascii = "11000000".getBytes(); // Should return 0x03
        byte[] result = codec.decode(ascii);
        assertEquals(1, result.length);
        assertEquals((byte) 0x03, result[0]);
    }

    @Test
    void testDecodeCharArrayAsciiToBinary() throws DecoderException {
        char[] ascii = "10000000".toCharArray(); // 00000001
        byte[] result = (byte[]) codec.decode(ascii);
        assertEquals(1, result.length);
        assertEquals((byte) 0x01, result[0]);
    }

    @Test
    void testDecodeStringAsciiToBinary() throws DecoderException {
        String ascii = "00000001";
        byte[] result = (byte[]) codec.decode(ascii);
        assertEquals(1, result.length);
        assertEquals((byte) 0x80, result[0]);
    }

    @Test
    void testDecodeInvalidObjectThrowsDecoderException() {
        Object invalidInput = 123;
        assertThrows(DecoderException.class, () -> codec.decode(invalidInput));
    }

    @Test
    void testToByteArrayFromBinaryString() {
        String ascii = "00000001";
        byte[] result = codec.toByteArray(ascii);
        assertEquals(1, result.length);
        assertEquals((byte) 0x80, result[0]);
    }

    @Test
    void testToByteArrayFromNullStringReturnsEmpty() {
        byte[] result = codec.toByteArray(null);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testFromAsciiWithEmptyCharArrayReturnsEmpty() {
        char[] input = {};
        byte[] result = BinaryCodec.fromAscii(input);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testFromAsciiWithNullByteArrayReturnsEmpty() {
        byte[] result = BinaryCodec.fromAscii((byte[]) null);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testToAsciiBytesFromBinary() {
        byte[] raw = {(byte) 0xA0}; // 10100000
        byte[] expected = "00000101".getBytes(); // LSB first
        byte[] result = BinaryCodec.toAsciiBytes(raw);
        assertArrayEquals(expected, result);
    }

    @Test
    void testToAsciiCharsFromBinary() {
        byte[] raw = {(byte) 0x0A}; // 00001010
        char[] expected = "01010000".toCharArray(); // LSB first
        char[] result = BinaryCodec.toAsciiChars(raw);
        assertArrayEquals(expected, result);
    }

    @Test
    void testToAsciiStringFromBinary() {
        byte[] raw = {(byte) 0x01}; // 00000001
        String expected = "10000000"; // LSB first
        String result = BinaryCodec.toAsciiString(raw);
        assertEquals(expected, result);
    }

    @Test
    void testRoundTripEncodingDecoding() {
        byte[] original = {(byte) 0x5A}; // 01011010
        byte[] ascii = codec.encode(original);
        byte[] decoded = codec.decode(ascii);
        assertArrayEquals(original, decoded);
    }

    @Test
    void testEmptyInputReturnsEmptyForAllConversions() throws EncoderException, DecoderException {
        byte[] empty = new byte[0];

        assertArrayEquals(new byte[0], codec.encode(empty));
        assertArrayEquals(new byte[0], codec.decode(empty));
        assertArrayEquals(new char[0], (char[]) codec.encode(empty));
        assertArrayEquals(new byte[0], BinaryCodec.fromAscii(new char[0]));
        assertArrayEquals(new byte[0], BinaryCodec.fromAscii(new byte[0]));
        assertEquals("", BinaryCodec.toAsciiString(empty));
        assertArrayEquals(new byte[0], BinaryCodec.toAsciiBytes(empty));
        assertArrayEquals(new char[0], BinaryCodec.toAsciiChars(empty));
    }
}

