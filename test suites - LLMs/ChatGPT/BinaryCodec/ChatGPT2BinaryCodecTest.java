package experimento.chatgpt.binarycodec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT2BinaryCodecTest {

    private final BinaryCodec codec = new BinaryCodec();

    @Test
    void testEncodeByteArrayToAsciiBytes() {
        byte[] input = {(byte) 0b10101010}; // 170
        byte[] expected = "01010101".getBytes();
        assertArrayEquals(expected, codec.encode(input));
    }

    @Test
        void testEncodeByteArrayToAsciiChars() throws EncoderException {
            byte[] input = {(byte) 0b11110000}; // 240
            char[] expected = "00001111".toCharArray();
            assertArrayEquals(expected, (char[]) codec.encode(input));
        }

    @Test
    void testEncodeObjectThrowsEncoderException() {
        assertThrows(EncoderException.class, () -> codec.encode("invalid"));
    }

    @Test
    void testDecodeFromByteArrayOfAscii() throws DecoderException {
        byte[] ascii = "00001111".getBytes();
        byte[] expected = {(byte) 0b11110000};
        assertArrayEquals(expected, (byte[]) codec.decode(ascii));
    }

    @Test
    void testDecodeFromCharArrayOfAscii() throws DecoderException {
        char[] ascii = "10101010".toCharArray();
        byte[] expected = {(byte) 0b01010101};
        assertArrayEquals(expected, (byte[]) codec.decode(ascii));
    }

    @Test
    void testDecodeFromStringOfAscii() throws DecoderException {
        String ascii = "11110000";
        byte[] expected = {(byte) 0b00001111};
        assertArrayEquals(expected, (byte[]) codec.decode(ascii));
    }

    @Test
    void testDecodeFromObjectThrowsDecoderException() {
        assertThrows(DecoderException.class, () -> codec.decode(12345));
    }

    @Test
    void testDecodeNullReturnsEmptyArray() throws DecoderException {
        assertArrayEquals(new byte[0], (byte[]) codec.decode(null));
    }

    @Test
    void testDecodeByteArrayReturnsExpected() {
        byte[] input = "00000001".getBytes(); // Should return byte[] with 1 at the last bit
        byte[] expected = {(byte) 0b10000000};
        assertArrayEquals(expected, codec.decode(input));
    }

    @Test
    void testToByteArrayFromAsciiString() {
        String ascii = "00000001";
        byte[] expected = {(byte) 0b10000000};
        assertArrayEquals(expected, codec.toByteArray(ascii));
    }

    @Test
    void testToByteArrayFromNullStringReturnsEmpty() {
        assertArrayEquals(new byte[0], codec.toByteArray(null));
    }

    @Test
    void testToAsciiBytesFromByteArray() {
        byte[] input = {(byte) 0b00001111};
        byte[] expected = "11110000".getBytes();
        assertArrayEquals(expected, BinaryCodec.toAsciiBytes(input));
    }

    @Test
    void testToAsciiBytesFromEmptyInput() {
        assertArrayEquals(new byte[0], BinaryCodec.toAsciiBytes(new byte[0]));
    }

    @Test
    void testToAsciiCharsFromByteArray() {
        byte[] input = {(byte) 0b11111111};
        char[] expected = "11111111".toCharArray();
        assertArrayEquals(expected, BinaryCodec.toAsciiChars(input));
    }

    @Test
    void testFromAsciiCharArray() {
        char[] input = "00000001".toCharArray(); // should yield 0b10000000
        byte[] expected = {(byte) 0b10000000};
        assertArrayEquals(expected, BinaryCodec.fromAscii(input));
    }

    @Test
    void testFromAsciiByteArray() {
        byte[] input = "11111111".getBytes(); // should yield 0b11111111
        byte[] expected = {(byte) 0b11111111};
        assertArrayEquals(expected, BinaryCodec.fromAscii(input));
    }

    @Test
    void testToAsciiString() {
        byte[] input = {(byte) 0b10101010};
        String expected = "01010101";
        assertEquals(expected, BinaryCodec.toAsciiString(input));
    }

    @Test
    void testFromAsciiEmptyCharArray() {
        assertArrayEquals(new byte[0], BinaryCodec.fromAscii(new char[0]));
    }

    @Test
    void testFromAsciiEmptyByteArray() {
        assertArrayEquals(new byte[0], BinaryCodec.fromAscii(new byte[0]));
    }
}

