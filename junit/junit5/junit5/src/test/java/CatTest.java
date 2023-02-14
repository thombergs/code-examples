import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CatTest {
    @Test
    public void testMeow() {
        String catName = "Stilla";
        int catAge = 3;
        boolean isNice = false;

        assertThat(catName, equalTo("Stilla"));
        assertThat(catAge, lessThan(5));
        assertThat(isNice, is(false));
    }

    @Test
    public void testCatInstance() {
        Cat cat = new Cat();

        assertThat(cat, instanceOf(Cat.class));
    }

    @Test
    public void testSameCatInstance() {
        Cat cat = new Cat();

        assertThat(cat, sameInstance(cat));
    }

    @Test
    public void testCollectionContaining() {
        List<String> catNames = asList("Phibi", "Monica", "Stilla");

        assertThat(catNames, hasItems("Monica", "Phibi"));
        assertThat(catNames, not(hasItems("Melih")));
    }

    @Test
    public void testCollectionSize() {
        List<String> catNames = asList("Phibi", "Monica");

        assertThat(catNames, hasSize(2));
    }

    @Test
    public void testBean() {
        Cat cat = new Cat("Mimi");

        assertThat(cat, hasProperty("name", equalTo("Mimi")));
    }

    @Test
    public void testStringEquality() {
        String catNameInCaps = "RACHEL";

        assertThat(catNameInCaps, equalToIgnoringCase("rachel"));
    }

    @Test
    public void testStringContains() {
        String catName = "Joey The Cute";

        assertThat(catName, containsString("Cute"));
    }

}
