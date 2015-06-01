/**
 * @author adkozlov
 */
public class Pair {

    private final Object first, second;

    public Pair(Object first, Object second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
