package com.artemzin.rxjavav1v2adapter;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableSource;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import rx.AsyncEmitter;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

import java.util.concurrent.atomic.AtomicReference;

public final class RxJavaV1V2Adapter {

    private RxJavaV1V2Adapter() {
        throw new IllegalStateException("No instances please!");
    }

    public static <T> Observable<T> o2ToO1(final io.reactivex.Observable<T> o2) {
        return Observable.fromAsync(new Action1<AsyncEmitter<T>>() {
            @Override
            public void call(final AsyncEmitter<T> asyncEmitter) {
                o2.subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(final Disposable disposable) {
                        asyncEmitter.setCancellation(new AsyncEmitter.Cancellable() {
                            @Override
                            public void cancel() throws Exception {
                                disposable.dispose();
                            }
                        });
                    }

                    @Override
                    public void onNext(T value) {
                        asyncEmitter.onNext(value);
                    }

                    @Override
                    public void onError(Throwable error) {
                        asyncEmitter.onError(error);
                    }

                    @Override
                    public void onComplete() {
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
                f2.subscribe(new org.reactivestreams.Subscriber<T>() {
                    @Override
                    public void onSubscribe(final org.reactivestreams.Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                        
                        asyncEmitter.setCancellation(new AsyncEmitter.Cancellable() {
                            @Override
                            public void cancel() throws Exception {
                                subscription.cancel();
                            }
                        });
                    }

                    @Override
                    public void onNext(T value) {
                        asyncEmitter.onNext(value);
                    }

                    @Override
                    public void onError(Throwable error) {
                        asyncEmitter.onError(error);
                    }

                    @Override
                    public void onComplete() {
                        asyncEmitter.onCompleted();
                    }
                });
            }
        }, AsyncEmitter.BackpressureMode.NONE);
    }

    public static <T> io.reactivex.Observable<T> o1ToO2(final Observable<T> o1) {
        return io.reactivex.Observable.create(new ObservableSource<T>() {
            @Override
            public void subscribe(final Observer<? super T> observer) {
                final AtomicReference<Subscription> subscription = new AtomicReference<Subscription>();
                observer.onSubscribe(new Disposable() {
                    @Override
                    public void dispose() {
                        final Subscription s = subscription.get();

                        if (s != null) {
                            s.unsubscribe();
                        }
                    }

                    @Override
                    public boolean isDisposed() {
                        final Subscription s = subscription.get();

                        if (s == null) {
                            return false;
                        } else {
                            return s.isUnsubscribed();
                        }
                    }
                });

                final Subscription s = o1.subscribe(new rx.Observer<T>() {
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
                
                subscription.set(s);
            }
        });
    }

    public static <T> Flowable<T> o1ToF2(final Observable<T> o1) {
        return Flowable.create(new FlowableSource<T>() {
            @Override
            public void subscribe(final FlowableEmitter<T> flowableEmitter) {
                final Subscription subscription = o1.subscribe(new rx.Observer<T>() {
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

                flowableEmitter.setCancellation(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        subscription.unsubscribe();
                    }
                });
            }
        }, FlowableEmitter.BackpressureMode.NONE);
    }
}
