package experimento.chatgpt.binarycodec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChatGPT3BinaryCodecTest {

    private final BinaryCodec codec = new BinaryCodec();

    // -----------------------------
    // TESTES PARA encode(byte[])
    // -----------------------------

    @Test
    void encodeByteArray_shouldReturnCorrectAsciiBytes() {
        byte[] input = new byte[]{(byte) 0b10101010};
        byte[] expected = "01010101".getBytes(); // bits são invertidos na codificação (big-endian)
        assertArrayEquals(expected, codec.encode(input));
    }

    @Test
    void encodeByteArray_shouldReturnEmptyArrayForEmptyInput() {
        assertArrayEquals(new byte[0], codec.encode(new byte[0]));
    }

    // -----------------------------
    // TESTES PARA encode(Object)
    // -----------------------------

    @Test
    void encodeObject_shouldReturnAsciiCharsForValidByteArray() throws EncoderException {
        byte[] input = {(byte) 0b11110000};
        char[] expected = "00001111".toCharArray();
        assertArrayEquals(expected, (char[]) codec.encode(input));
    }

    @Test
    void encodeObject_shouldThrowEncoderExceptionForInvalidType() {
        assertThrows(EncoderException.class, () -> codec.encode("invalid"));
    }

    // -----------------------------
    // TESTES PARA decode(Object)
    // -----------------------------

    @Test
    void decodeObject_shouldReturnByteArrayFromByteInput() throws DecoderException {
        byte[] input = "00001111".getBytes();
        byte[] expected = {(byte) 0b11110000};
        assertArrayEquals(expected, (byte[]) codec.decode(input));
    }

    @Test
    void decodeObject_shouldReturnByteArrayFromCharInput() throws DecoderException {
        char[] input = "00001111".toCharArray();
        byte[] expected = {(byte) 0b11110000};
        assertArrayEquals(expected, (byte[]) codec.decode(input));
    }

    @Test
    void decodeObject_shouldReturnByteArrayFromStringInput() throws DecoderException {
        String input = "00001111";
        byte[] expected = {(byte) 0b11110000};
        assertArrayEquals(expected, (byte[]) codec.decode(input));
    }

    @Test
    void decodeObject_shouldReturnEmptyArrayForNullInput() throws DecoderException {
        assertArrayEquals(new byte[0], (byte[]) codec.decode(null));
    }

    @Test
    void decodeObject_shouldThrowDecoderExceptionForInvalidType() {
        assertThrows(DecoderException.class, () -> codec.decode(123));
    }

    // -----------------------------
    // TESTES PARA decode(byte[])
    // -----------------------------

    @Test
    void decodeByteArray_shouldDecodeCorrectly() {
        byte[] input = "00001111".getBytes();
        byte[] expected = {(byte) 0b11110000};
        assertArrayEquals(expected, codec.decode(input));
    }

    // -----------------------------
    // TESTES PARA toByteArray(String)
    // -----------------------------

    @Test
    void toByteArray_shouldReturnDecodedBytesFromString() {
        String input = "00001111";
        byte[] expected = {(byte) 0b11110000};
        assertArrayEquals(expected, codec.toByteArray(input));
    }

    @Test
    void toByteArray_shouldReturnEmptyArrayForNull() {
        assertArrayEquals(new byte[0], codec.toByteArray(null));
    }

    // -----------------------------
    // TESTES PARA toAsciiBytes(byte[])
    // -----------------------------

    @Test
    void toAsciiBytes_shouldConvertByteArrayToAsciiBinary() {
        byte[] input = {(byte) 0b00001111};
        byte[] expected = "11110000".getBytes();
        assertArrayEquals(expected, BinaryCodec.toAsciiBytes(input));
    }

    @Test
    void toAsciiBytes_shouldReturnEmptyForNullOrEmptyInput() {
        assertArrayEquals(new byte[0], BinaryCodec.toAsciiBytes(null));
        assertArrayEquals(new byte[0], BinaryCodec.toAsciiBytes(new byte[0]));
    }

    // -----------------------------
    // TESTES PARA toAsciiChars(byte[])
    // -----------------------------

    @Test
    void toAsciiChars_shouldReturnAsciiCharsFromByteArray() {
        byte[] input = {(byte) 0b00001111};
        char[] expected = "11110000".toCharArray();
        assertArrayEquals(expected, BinaryCodec.toAsciiChars(input));
    }

    @Test
    void toAsciiChars_shouldReturnEmptyForEmptyOrNullInput() {
        assertArrayEquals(new char[0], BinaryCodec.toAsciiChars(null));
        assertArrayEquals(new char[0], BinaryCodec.toAsciiChars(new byte[0]));
    }

    // -----------------------------
    // TESTES PARA fromAscii(byte[]) e fromAscii(char[])
    // -----------------------------

    @Test
    void fromAsciiByte_shouldDecodeBinaryStringToByteArray() {
        byte[] input = "11110000".getBytes();
        byte[] expected = {(byte) 0b00001111};
        assertArrayEquals(expected, BinaryCodec.fromAscii(input));
    }

    @Test
    void fromAsciiChar_shouldDecodeBinaryCharsToByteArray() {
        char[] input = "11110000".toCharArray();
        byte[] expected = {(byte) 0b00001111};
        assertArrayEquals(expected, BinaryCodec.fromAscii(input));
    }

    @Test
    void fromAscii_shouldReturnEmptyArrayForEmptyInput() {
        assertArrayEquals(new byte[0], BinaryCodec.fromAscii(new byte[0]));
        assertArrayEquals(new byte[0], BinaryCodec.fromAscii(new char[0]));
        assertArrayEquals(new byte[0], BinaryCodec.fromAscii((char[]) null));
    }
}

