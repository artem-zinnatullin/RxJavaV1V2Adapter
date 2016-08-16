package com.artemzin.rxjavav1v2adapter;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableSource;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.observable.ObservableFromSource;
import rx.AsyncEmitter;
import rx.Observable;
import rx.functions.Action1;

public final class RxJavaV1ToV2Adapter {

    private RxJavaV1ToV2Adapter() {
        throw new IllegalStateException("No instances please!");
    }

    public static <T> Observable<T> o2ToO1(final io.reactivex.Observable<T> o2) {
        return Observable.fromAsync(new Action1<AsyncEmitter<T>>() {
            @Override
            public void call(final AsyncEmitter<T> asyncEmitter) {
                o2.subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T value) throws Exception {
                        asyncEmitter.onNext(value);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable error) throws Exception {
                        asyncEmitter.onError(error);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        asyncEmitter.onCompleted();
                    }
                });
            }
        }, AsyncEmitter.BackpressureMode.NONE);
    }

    public static <T> Observable<T> f2ToO1(final Flowable<T> f2) {
        return Observable.fromAsync(new Action1<AsyncEmitter<T>>() {
            @Override
            public void call(final AsyncEmitter<T> asyncEmitter) {
                f2.subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T value) throws Exception {
                        asyncEmitter.onNext(value);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable error) throws Exception {
                        asyncEmitter.onError(error);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        asyncEmitter.onCompleted();
                    }
                });
            }
        }, AsyncEmitter.BackpressureMode.NONE);
    }

    public static <T> io.reactivex.Observable o1ToO2(final Observable<T> o1) {
        return io.reactivex.Observable.create(new ObservableSource<T>() {
            @Override
            public void subscribe(final Observer<? super T> observer) {
                o1.subscribe(new rx.Observer<T>() {
                    @Override
                    public void onNext(T value) {
                        observer.onNext(value);
                    }

                    @Override
                    public void onError(Throwable error) {
                        observer.onError(error);
                    }

                    @Override
                    public void onCompleted() {
                        observer.onComplete();
                    }
                });
            }
        });
    }

    public static <T> Flowable<T> o1ToF2(final Observable<T> o1) {
        return Flowable.create(new FlowableSource<T>() {
            @Override
            public void subscribe(final FlowableEmitter<T> flowableEmitter) {
                o1.subscribe(new rx.Observer<T>() {
                    @Override
                    public void onNext(T value) {
                        flowableEmitter.onNext(value);
                    }

                    @Override
                    public void onError(Throwable error) {
                        flowableEmitter.onError(error);
                    }

                    @Override
                    public void onCompleted() {
                        flowableEmitter.onComplete();
                    }
                });
            }
        }, FlowableEmitter.BackpressureMode.NONE);
    }
}
