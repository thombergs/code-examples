
public class GoldFish {
    private String name;
    private int age;

    public GoldFish(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public int calculateSpeed() {
        if (age == 0){
            throw new RuntimeException("This will fail :((");
        }
        return 10 / age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
