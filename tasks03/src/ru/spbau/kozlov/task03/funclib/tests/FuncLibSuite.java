package ru.spbau.kozlov.task03.funclib.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The {@link FuncLibSuite} class tests all the FuncLib functionality.
 *
 * @author adkozlov
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestCompositions.class,
        TestPredicates.class,
        TestComparator.class,
        TestSecondOrderFunctions.class
})
public class FuncLibSuite {
}
