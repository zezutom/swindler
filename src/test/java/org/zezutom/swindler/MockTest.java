package org.zezutom.swindler;

import org.junit.Test;
import org.zezutom.swindler.model.Bar;
import org.zezutom.swindler.model.Foo;

import static org.junit.Assert.assertEquals;
/**
 * Created by tom on 30/01/2015.
 */
public class MockTest {

    @Test
    public void method_can_be_overridden() {
        String fakeValue = "mocked foo";
        Foo mock = Mock.mock(Foo.class);
        Mock.when(mock, "foo()").thenReturn(fakeValue);
        assertEquals(fakeValue, mock.foo());
    }

    @Test
    public void selective_matching_works() {
        String fakeValue = "what a mockery";
        Bar mock = Mock.mock(Bar.class);
        Mock.when(mock, "bar(java.lang.String)").thenReturn(fakeValue);
        assertEquals(fakeValue, mock.bar("bloke"));
        assertEquals(new Bar().bar(), mock.bar());
    }

    @Test
    public void value_based_matching_works() {
        String fakeValue = "hi bloke!";
        Bar mock = Mock.mock(Bar.class);
        Mock.when(mock, "bar(java.lang.String)", "bloke").thenReturn(fakeValue);
        assertEquals(fakeValue, mock.bar("bloke"));

        String nonMatchingValue = "old sport";
        assertEquals(new Bar().bar(nonMatchingValue), mock.bar(nonMatchingValue));
    }
}
