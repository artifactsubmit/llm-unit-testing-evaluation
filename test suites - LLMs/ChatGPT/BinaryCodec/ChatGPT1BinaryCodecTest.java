package experimento.chatgpt.binarycodec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPT1BinaryCodecTest {

    private final BinaryCodec codec = new BinaryCodec();

    @Test
    void encodeByteArray_shouldReturnCorrectAsciiBytes() {
        byte[] input = {(byte) 0b01010101}; // Alterna 01010101
        byte[] expected = "10101010".getBytes(); // Inverso porque os bits são armazenados ao contrário
        byte[] actual = codec.encode(input);
        assertArrayEquals(expected, actual);
    }

    @Test
    void encodeObject_withValidByteArray_shouldReturnAsciiChars() throws EncoderException {
        byte[] input = {(byte) 0b00001111}; // Esperado: 11110000 (invertido)
        char[] result = (char[]) codec.encode(input);
        assertEquals(8, result.length);
        assertEquals('1', result[7]);
        assertEquals('0', result[0]);
    }

    @Test
    void encodeObject_withInvalidType_shouldThrowEncoderException() {
        assertThrows(EncoderException.class, () -> codec.encode("invalid"));
    }

    @Test
    void decodeObject_withByteArray_shouldReturnOriginalBinary() throws DecoderException {
        byte[] ascii = "00001111".getBytes(); // Inverso na reconstrução
        byte[] expected = {(byte) 0b11110000};
        byte[] result = (byte[]) codec.decode(ascii);
        assertArrayEquals(expected, result);
    }

    @Test
    void decodeObject_withCharArray_shouldReturnOriginalBinary() throws DecoderException {
        char[] ascii = "11110000".toCharArray();
        byte[] expected = {(byte) 0b00001111};
        byte[] result = (byte[]) codec.decode(ascii);
        assertArrayEquals(expected, result);
    }

    @Test
    void decodeObject_withString_shouldReturnOriginalBinary() throws DecoderException {
        String ascii = "11110000";
        byte[] expected = {(byte) 0b00001111};
        byte[] result = (byte[]) codec.decode(ascii);
        assertArrayEquals(expected, result);
    }

    @Test
    void decodeObject_withNull_shouldReturnEmptyArray() throws DecoderException {
        byte[] result = (byte[]) codec.decode(null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void decodeObject_withInvalidType_shouldThrowDecoderException() {
        assertThrows(DecoderException.class, () -> codec.decode(123));
    }

    @Test
    void decodeByteArray_shouldDecodeToOriginalBinary() {
        byte[] ascii = "00001111".getBytes();
        byte[] expected = {(byte) 0b11110000};
        byte[] result = codec.decode(ascii);
        assertArrayEquals(expected, result);
    }

    @Test
    void toByteArray_withNull_shouldReturnEmptyArray() {
        assertArrayEquals(new byte[0], codec.toByteArray(null));
    }

    @Test
    void toByteArray_withValidBinaryString_shouldReturnBinaryData() {
        String ascii = "00000001";
        byte[] expected = {(byte) 0b10000000};
        byte[] result = codec.toByteArray(ascii);
        assertArrayEquals(expected, result);
    }

    @Test
    void toAsciiBytes_shouldReturnCorrectAsciiBytes() {
        byte[] input = {(byte) 0b11110000};
        byte[] expected = "00001111".getBytes();
        byte[] actual = BinaryCodec.toAsciiBytes(input);
        assertArrayEquals(expected, actual);
    }

    @Test
    void toAsciiBytes_withEmptyArray_shouldReturnEmptyArray() {
        assertArrayEquals(new byte[0], BinaryCodec.toAsciiBytes(new byte[0]));
    }

    @Test
    void toAsciiChars_shouldReturnCorrectAsciiChars() {
        byte[] input = {(byte) 0b00001111};
        char[] expected = "11110000".toCharArray();
        char[] actual = BinaryCodec.toAsciiChars(input);
        assertArrayEquals(expected, actual);
    }

    @Test
    void toAsciiChars_withEmptyArray_shouldReturnEmptyArray() {
        assertArrayEquals(new char[0], BinaryCodec.toAsciiChars(new byte[0]));
    }

    @Test
    void fromAsciiCharArray_shouldReturnCorrectBinary() {
        char[] ascii = "00000001".toCharArray();
        byte[] expected = {(byte) 0b10000000};
        byte[] result = BinaryCodec.fromAscii(ascii);
        assertArrayEquals(expected, result);
    }

    @Test
    void fromAsciiCharArray_withEmptyInput_shouldReturnEmptyArray() {
        assertArrayEquals(new byte[0], BinaryCodec.fromAscii(new char[0]));
    }

    @Test
    void fromAsciiByteArray_shouldReturnCorrectBinary() {
        byte[] ascii = "11111111".getBytes();
        byte[] expected = {(byte) 0b11111111};
        byte[] result = BinaryCodec.fromAscii(ascii);
        assertArrayEquals(expected, result);
    }

    @Test
    void fromAsciiByteArray_withNull_shouldReturnEmptyArray() {
        assertArrayEquals(new byte[0], BinaryCodec.fromAscii((byte[]) null));
    }

    @Test
    void toAsciiString_shouldReturnCorrectAsciiString() {
        byte[] input = {(byte) 0b00000001};
        String expected = "10000000";
        String result = BinaryCodec.toAsciiString(input);
        assertEquals(expected, result);
    }

    @Test
    void roundTripEncodeDecode_shouldReturnOriginalData() throws EncoderException, DecoderException {
        byte[] original = {(byte) 0b10101010};
        Object encoded = codec.encode(original);
        byte[] ascii = (encoded instanceof char[]) ?
                new String((char[]) encoded).getBytes() : (byte[]) encoded;
        byte[] decoded = codec.decode(ascii);
        assertArrayEquals(original, decoded);
    }
}

