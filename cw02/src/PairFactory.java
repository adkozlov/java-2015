import ru.spbau.kozlov.cw02.ConcreteFactory;
import ru.spbau.kozlov.cw02.Factory;

/**
 * @author adkozlov
 */
public class PairFactory implements ConcreteFactory {

    private final static Object first;

    static {
        Factory.getInstance().registerConcreteFactory(new PairFactory(), "Pair");
        first = "Name";
    }

    public Object createInstance(String string) {
        return new Pair(first, string);
    }
}
