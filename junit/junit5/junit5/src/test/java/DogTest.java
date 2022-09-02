import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DogTest {

    @BeforeAll
    public static void init() {
        System.out.println("Doing stuff");
    }

    @BeforeEach
    public void doEach() {
        System.out.println("Hey Doggo");
    }

    @AfterAll
    public static void finish() {
        System.out.println("Finishing stuff");
    }

    @AfterEach
    public void doAfterEach() {
        System.out.println("Bye Doggo");
    }

    @Test
    public void barkFailure() {
        String expectedString = "Meow";
        assertEquals(expectedString, "Woof");
    }

    @Disabled("Dog 1 please don't woof")
    @Test
    public void testBark1() {
        String expectedString = "woof1";
        assertEquals(expectedString, "woof1");
        System.out.println("WOOF => 1");
    }

    @Test
    public void testBark2() {
        String expectedString = "woof2";
        assertEquals(expectedString, "woof2");
        System.out.println("WOOF => 2");
    }

    @Test
    public void testNotBark() {
        String unexpectedString = "";
        assertNotEquals(unexpectedString, "woof");
        System.out.println("Didn't woof!!");
    }

    @Test
    public void nullCheck() {
        Object dog = null;
        assertNull(dog);
        System.out.println("Null dog :(");
    }

    @Test
    public void nonNullCheck() {
        String dog = "Max";
        assertNotNull(dog);
        System.out.println("Hey I am " + dog);
    }

    @Test
    public void trueCheck() {
        int dogAge = 2;
        assertTrue(dogAge < 5);
        System.out.println("I am young :)");
    }

    @Test
    public void falseCheck() {
        int dogAge = 7;
        assertFalse(dogAge < 5);
        System.out.println("I am old :(");
    }
}
