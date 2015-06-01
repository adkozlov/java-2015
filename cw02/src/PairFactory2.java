import ru.spbau.kozlov.cw02.ConcreteFactory;
import ru.spbau.kozlov.cw02.Factory;

/**
 * @author adkozlov
 */
public class PairFactory2 implements ConcreteFactory {

    static {
        Factory.getInstance().registerConcreteFactory(new PairFactory2(), "Pair");
    }

    public Object createInstance() {
        throw new RuntimeException();
    }
}
