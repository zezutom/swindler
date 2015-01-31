# swindler
A minimalistic Java mocking library based on reflection API and javassist.

## Features

### Field Manipulation

[Swindler](./src/main/java/org/zezutom/swindler/Swindler.java) allows to modify any instance field, even in nested classes.

Just a few quick cheats and a call of Math.random() becomes totally predictable:
```java
Swindler.with("java.lang.Math$RandomNumberGeneratorHolder")
  .get("randomNumberGenerator")
  .set(new Random() {
    @Override
    public double nextDouble() {
      return 1;
    }
  });
  org.junit.Assert.assertEquals(Math.random(), 1.0, 0);
```

All right, field manipulation isn't always the best approach, since it requires a fairly good knowledge of intrinsic details. 
Tweaking of [LocalDateTime.now()](http://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html#now--) would be significantly more challenging. Typically, we don't necessarily need the original
method be invoked. The only thing which matters is the returned result. That's when mocking comes in handy.

### Method Mocking

[Mock](./src/main/java/org/zezutom/swindler/Mock.java) intercepts method calls and, when appropriate, returns a fake value instead of passing control to the original method.

Suppose the following model:
```java
class Foo {
    public String foo() {
        return "foo";
    }
}

class Bar {
    public String bar() {
        return "bar";
    }
    public String bar(String bah) {
        return bah + " in the bar!";
    }
}
```

Ideally, we don't need to care about anything but a method name:
```java
  String fakeValue = "mocked foo";
  Foo mock = Mock.mock(Foo.class);
  Mock.when(mock, "foo()").thenReturn(fakeValue);
  assertEquals(fakeValue, mock.foo());
```

Overloaded methods aren't a problem either. One just needs to be explicit with argument types:
```java
  String fakeValue = "what a mockery";
  Bar mock = Mock.mock(Bar.class);
  Mock.when(mock, "bar(java.lang.String)").thenReturn(fakeValue);
  assertEquals(fakeValue, mock.bar("bloke"));
  assertEquals(new Bar().bar(), mock.bar());
```

The matching can be made even more specific by providing argument values:
```java
   String fakeValue = "hi bloke!";
  Bar mock = Mock.mock(Bar.class);
  Mock.when(mock, "bar(java.lang.String)", "bloke").thenReturn(fakeValue);
  assertEquals(fakeValue, mock.bar("bloke"));

  String nonMatchingValue = "old sport";
  assertEquals(new Bar().bar(nonMatchingValue), mock.bar(nonMatchingValue));
```
## Limitations
The framework wouldn't work for classes marked as final. Method matching is based on proxying, so it only applies on an instance level. 
