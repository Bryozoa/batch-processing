import com.polixis.batchprocessing.PersonDataSet;
import com.polixis.batchprocessing.PersonItemProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Locale;

public class PersonItemProcessorTest {
    @Test
    public void personIdentityTest() {
        PersonItemProcessor processor = new PersonItemProcessor();

        PersonDataSet person = new PersonDataSet("Delilah", "Gardner", "10/01/21");

        PersonDataSet person2 = processor.process(person);

        Assertions.assertEquals(person.getFirstName().toUpperCase(Locale.ROOT), person2.getFirstName());
        Assertions.assertEquals(person.getLastName().toUpperCase(), person2.getLastName());
        Assertions.assertEquals(person.getDate().toUpperCase(Locale.ROOT), person2.getDate());

    }
}