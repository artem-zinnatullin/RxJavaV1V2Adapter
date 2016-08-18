# RxJava v1 <-> v2 Adapter


Adapter between RxJava [v1](https://github.com/ReactiveX/RxJava/tree/1.x) and [v2](https://github.com/ReactiveX/RxJava/tree/2.x).

###With this library you will be able to convert:

* v1 `Observable` to v2 `Observable` and vice versa.
* v1 `Observable` to v2 `Flowable` and vice versa.
* v1 `Single` to v2 `Single` and vice versa.
* v1 `Consumable` to v2 `Consumable` and vice versa.

### TODO

* Publish to Maven Central.
* Kotlin part: extension functions `val omg = Observable.just("wow").toO2()`.
* Connect v1 `Observable` Backpressure with v2 `Flowable` Backpressure, currently it's unbounded.
