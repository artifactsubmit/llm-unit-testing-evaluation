package experimento;

import experimento.Hex;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HexBaseTest {

    @Test
    void testEncodeDecodeByteArray() {
        Hex hex = new Hex();

        byte[] original = new byte[]{0x0A, 0x1B, 0x2C};
        byte[] encoded = hex.encode(original);
        assertNotNull(encoded);

        try {
            byte[] decoded = hex.decode(encoded);
            assertArrayEquals(original, decoded);
        } catch (DecoderException e) {
            fail("DecoderException not expected");
        }
    }
    @Test
    void testStaticEncodeDecodeHex() {
        byte[] input = new byte[]{1, 2, 3};
        char[] encoded = Hex.encodeHex(input);
        assertNotNull(encoded);

        try {
            byte[] decoded = Hex.decodeHex(encoded);
            assertNotNull(decoded);
        } catch (DecoderException e) {
            fail("DecoderException not expected");
        }
    }

    @Test
    void testCharsetAndToString() {
        Hex hex = new Hex("UTF-8");
        assertEquals("UTF-8", hex.getCharsetName());
        assertTrue(hex.toString().contains("Hex"));
    }

    @Test
    void testToDigit() {
        try {
            int digit = Hex.toDigit('F', 0);
            assertEquals(15, digit);
        } catch (DecoderException e) {
            fail("DecoderException not expected in toDigit");
        }
    }
}
