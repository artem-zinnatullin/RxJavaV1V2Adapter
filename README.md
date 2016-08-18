# RxJava v1 <-> v2 Adapter

Adapter between RxJava [v1][RxJavaV1] and [v2][RxJavaV2].

##With this library you will be able to convert:

* v1 `Observable` to v2 `Observable` and vice versa.
* v1 `Observable` to v2 `Flowable` and vice versa.
* v1 `Single` to v2 `Single` and vice versa.
* v1 `Consumable` to v2 `Consumable` and vice versa.

Please consult [RxJava v2 README][RxJavaV2] to see how to add it to your project.

Usage example:

```java
Observable o1 = RxJavaV1ToV2Adapter.o2ToO1(o2);
```

### TODO

* Add  v1 `Single` <-> v2 `Single`.
* Add  v1 `Consumable` <-> v2 `Consumable`.
* Publish to Maven Central.
* Kotlin part: extension functions `val omg = Observable.just("wow").toO2()`.
* Connect v1 `Observable` Backpressure with v2 `Flowable` Backpressure, currently it's unbounded.

###Warning

This library is unstable simply because it depends on SNAPSHOT version of [RxJava v2][RxJavaV2]. Once [RxJava v2][RxJavaV2] will be released this library will be realeased as stable too.

[RxJavaV1]: https://github.com/ReactiveX/RxJava/tree/1.x
[RxJavaV2]: https://github.com/ReactiveX/RxJava/tree/2.x
