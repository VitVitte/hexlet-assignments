package exercise;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


class ApplicationTest {


    @Test
    public void testInspect() {
        Application.main(null);

        final var expected1 = "Method getCity returns a value of type String";
        final var expected2 = "Method getPostalCode returns a value of type int";
        final var notExpected = "getFullAddress";

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Application.main(null);

        final var actual = out.toString().trim();

        assertThat(actual)
            .contains(expected1, expected2)
            .doesNotContain(notExpected);
    }
}
