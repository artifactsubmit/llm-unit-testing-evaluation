package experimento.chatgpt.hex;
//import experimento.Hex;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChatGPT4HexTest {

    // --------------------
    // Métodos estáticos
    // --------------------

    @Test
    @DisplayName("decodeHex - deve decodificar corretamente valores hexadecimais válidos")
    void testDecodeHex_validInput() throws DecoderException {
        char[] input = "48656c6c6f".toCharArray(); // "Hello"
        byte[] expected = "Hello".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expected, Hex.decodeHex(input));
    }

    @Test
    @DisplayName("decodeHex - deve lançar exceção para número ímpar de caracteres")
    void testDecodeHex_oddLength_throwsException() {
        char[] input = "ABC".toCharArray();
        DecoderException ex = assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
        assertEquals("Odd number of characters.", ex.getMessage());
    }

    @Test
    @DisplayName("decodeHex - deve lançar exceção para caractere inválido")
    void testDecodeHex_illegalCharacter_throwsException() {
        char[] input = "GG".toCharArray();
        assertThrows(DecoderException.class, () -> Hex.decodeHex(input));
    }

    @Test
    @DisplayName("encodeHex - deve codificar corretamente em minúsculo")
    void testEncodeHex_lowercase() {
        byte[] input = {0x0F, 0x10};
        char[] expected = {'0', 'f', '1', '0'};
        assertArrayEquals(expected, Hex.encodeHex(input));
    }

    @Test
    @DisplayName("encodeHex - deve codificar corretamente em maiúsculo")
    void testEncodeHex_uppercase() {
        byte[] input = {0x0F, 0x10};
        char[] expected = {'0', 'F', '1', '0'};
        assertArrayEquals(expected, Hex.encodeHex(input, false));
    }

    @Test
    @DisplayName("encodeHexString - deve retornar string hexadecimal correta")
    void testEncodeHexString() {
        byte[] input = "Hi".getBytes(StandardCharsets.UTF_8);
        assertEquals("4869", Hex.encodeHexString(input));
    }

    @Test
    @DisplayName("toDigit - deve converter corretamente caractere hexadecimal")
    void testToDigit_valid() throws DecoderException {
        assertEquals(10, Hex.toDigit('a', 0));
        assertEquals(15, Hex.toDigit('F', 1));
    }

    @Test
    @DisplayName("toDigit - deve lançar exceção para caractere inválido")
    void testToDigit_invalid_throwsException() {
        assertThrows(DecoderException.class, () -> Hex.toDigit('Z', 2));
    }

    // --------------------
    // Métodos de instância
    // --------------------

    @Test
    @DisplayName("getCharsetName - deve retornar UTF-8 por padrão")
    void testGetCharsetName_defaultConstructor() {
        Hex hex = new Hex();
        assertEquals("UTF-8", hex.getCharsetName());
    }

    @Test
    @DisplayName("encode - deve codificar byte[] e retornar byte[] de caracteres hex")
    void testEncode_bytes() {
        Hex hex = new Hex();
        byte[] input = "Oi".getBytes(StandardCharsets.UTF_8);
        byte[] result = hex.encode(input);
        assertEquals("4f69", new String(result, StandardCharsets.UTF_8).toLowerCase());
    }

    @Test
    @DisplayName("encode - deve codificar String e retornar char[]")
    void testEncode_string() throws EncoderException {
        Hex hex = new Hex();
        char[] result = (char[]) hex.encode("Hi");
        assertArrayEquals("4869".toCharArray(), result);
    }

    @Test
    @DisplayName("encode - deve lançar exceção para tipo inválido")
    void testEncode_invalidType_throwsException() {
        Hex hex = new Hex();
        assertThrows(EncoderException.class, () -> hex.encode(123));
    }

    @Test
    @DisplayName("decode - deve decodificar byte[] para byte[]")
    void testDecode_bytes() throws DecoderException {
        Hex hex = new Hex();
        byte[] encoded = "4869".getBytes(StandardCharsets.UTF_8);
        byte[] expected = "Hi".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expected, hex.decode(encoded));
    }

    @Test
    @DisplayName("decode - deve decodificar String para byte[]")
    void testDecode_string() throws DecoderException {
        Hex hex = new Hex();
        byte[] expected = "Hi".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expected, (byte[]) hex.decode("4869"));
    }

    @Test
    @DisplayName("decode - deve lançar exceção para tipo inválido")
    void testDecode_invalidType_throwsException() {
        Hex hex = new Hex();
        assertThrows(DecoderException.class, () -> hex.decode(123));
    }

    @Test
    @DisplayName("toString - deve conter o nome do charset")
    void testToString_containsCharset() {
        Hex hex = new Hex("UTF-8");
        assertTrue(hex.toString().contains("UTF-8"));
    }
}

