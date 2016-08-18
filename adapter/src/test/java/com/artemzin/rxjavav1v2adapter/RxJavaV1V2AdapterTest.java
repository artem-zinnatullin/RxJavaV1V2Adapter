package com.artemzin.rxjavav1v2adapter;

import io.reactivex.Flowable;
import io.reactivex.observers.TestObserver;
import io.reactivex.processors.PublishProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import rx.Observable;
import rx.Subscription;
import rx.observers.TestSubscriber;
import rx.subjects.PublishSubject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class RxJavaV1V2AdapterTest {

    public static class O2ToO1 {

        io.reactivex.subjects.PublishSubject<String> o2 = io.reactivex.subjects.PublishSubject.create();
        Observable<String> o1 = RxJavaV1V2Adapter.o2ToO1((io.reactivex.Observable<String>) o2);
        TestSubscriber<String> ts1 = new TestSubscriber<String>();
        Subscription s1;

        @Before
        public void beforeEachTest() {
            s1 = o1.subscribe(ts1);
        }

        @Test
        public void initial() {
            ts1.assertNoValues();
        }

        @Test
        public void next() {
            o2.onNext("a");
            o2.onNext("b");
            o2.onNext("c");

            ts1.assertValues("a", "b", "c");

            ts1.assertNoErrors();
            ts1.assertNotCompleted();
        }

        @Test
        public void error() {
            Throwable e1 = new RuntimeException();
            Throwable e2 = new RuntimeException();

            o2.onError(e1);
            o2.onError(e2);

            ts1.assertError(e1);
            assertThat(ts1.getOnErrorEvents()).hasSize(1);

            ts1.assertNoValues();
            ts1.assertNotCompleted();
        }

        @Test
        public void complete() {
            o2.onComplete();
            o2.onComplete();

            ts1.assertCompleted();
            assertThat(ts1.getCompletions()).isEqualTo(1);

            ts1.assertNoValues();
            ts1.assertNoErrors();
        }

        @Test
        public void nextUnsubscribe() {
            s1.unsubscribe();
            o2.onNext("b");

            ts1.assertNoValues();
            ts1.assertNoErrors();
            ts1.assertNotCompleted();
        }

        @Test
        public void errorUnsubscribe() {
            s1.unsubscribe();
            o2.onError(new RuntimeException());

            ts1.assertNoValues();
            ts1.assertNoErrors();
            ts1.assertNotCompleted();
        }

        @Test
        public void completeUnsubscribe() {
            s1.unsubscribe();
            o2.onComplete();

            ts1.assertNoValues();
            ts1.assertNoErrors();
            ts1.assertNotCompleted();
        }
    }

    public static class F2ToO1 {

        PublishProcessor<String> f2 = PublishProcessor.create();
        Observable<String> o1 = RxJavaV1V2Adapter.f2ToO1((Flowable<String>) f2);
        TestSubscriber<String> ts1 = new TestSubscriber<String>();
        Subscription s1;

        @Before
        public void beforeEachTest() {
            s1 = o1.subscribe(ts1);
        }

        @Test
        public void initial() {
            ts1.assertNoValues();
        }

        @Test
        public void next() {
            f2.onNext("a");
            f2.onNext("b");
            f2.onNext("c");

            ts1.assertValues("a", "b", "c");

            ts1.assertNoErrors();
            ts1.assertNotCompleted();
        }

        @Test
        public void error() {
            Throwable e1 = new RuntimeException();
            Throwable e2 = new RuntimeException();

            f2.onError(e1);
            f2.onError(e2);

            ts1.assertError(e1);
            assertThat(ts1.getOnErrorEvents()).hasSize(1);

            ts1.assertNoValues();
            ts1.assertNotCompleted();
        }

        @Test
        public void complete() {
            f2.onComplete();
            f2.onComplete();

            ts1.assertCompleted();
            assertThat(ts1.getCompletions()).isEqualTo(1);

            ts1.assertNoValues();
            ts1.assertNoErrors();
        }

        @Test
        public void nextUnsubscribe() {
            s1.unsubscribe();
            f2.onNext("b");

            ts1.assertNoValues();
            ts1.assertNoErrors();
            ts1.assertNotCompleted();
        }

        @Test
        public void errorUnsubscribe() {
            s1.unsubscribe();
            f2.onError(new RuntimeException());

            ts1.assertNoValues();
            ts1.assertNoErrors();
            ts1.assertNotCompleted();
        }

        @Test
        public void completeUnsubscribe() {
            s1.unsubscribe();
            f2.onComplete();

            ts1.assertNoValues();
            ts1.assertNoErrors();
            ts1.assertNotCompleted();
        }
    }

    public static class O1ToO2 {

        PublishSubject<String> o1 = PublishSubject.create();
        io.reactivex.Observable<String> o2 = RxJavaV1V2Adapter.o1ToO2((Observable<String>) o1);
        TestObserver<String> to2 = new TestObserver<String>();

        @Before
        public void beforeEachTest() {
            o2.subscribe(to2);
        }

        @Test
        public void initial() {
            to2.assertNoValues();
        }

        @Test
        public void next() {
            o1.onNext("a");
            o1.onNext("b");
            o1.onNext("c");

            to2.assertValues("a", "b", "c");

            to2.assertNoErrors();
            to2.assertNotComplete();
        }

        @Test
        public void error() {
            Throwable e1 = new RuntimeException();
            Throwable e2 = new RuntimeException();

            o1.onError(e1);
            o1.onError(e2);

            to2.assertError(e1);
            assertThat(to2.errorCount()).isEqualTo(1);

            to2.assertNoValues();
            to2.assertNotComplete();
        }

        @Test
        public void complete() {
            o1.onCompleted();
            o1.onCompleted();

            to2.assertComplete();
            assertThat(to2.completions()).isEqualTo(1);

            to2.assertNoValues();
            to2.assertNoErrors();
        }

        @Test
        public void nextUnsubscribe() {
            to2.dispose();
            o1.onNext("a");
            
            to2.assertNoValues();
            to2.assertNoErrors();
            to2.assertNotComplete();
        }
        
        @Test
        public void errorUnsubscribe() {
            to2.dispose();
            o1.onError(new RuntimeException());

            to2.assertNoValues();
            to2.assertNoErrors();
            to2.assertNotComplete();
        }

        @Test
        public void completeUnsubscribe() {
            to2.dispose();
            o1.onCompleted();

            to2.assertNoValues();
            to2.assertNoErrors();
            to2.assertNotComplete();
        }
    }
    
    public static class O1ToF2 {

        PublishSubject<String> o1 = PublishSubject.create();
        io.reactivex.Flowable<String> f2 = RxJavaV1V2Adapter.o1ToF2((Observable<String>) o1);
        io.reactivex.subscribers.TestSubscriber<String> ts2 = new io.reactivex.subscribers.TestSubscriber<String>();
        
        @Before
        public void beforeEachTest() {
            f2.subscribe(ts2);
        }

        @Test
        public void initial() {
            ts2.assertNoValues();
        }

        @Test
        public void next() {
            o1.onNext("a");
            o1.onNext("b");
            o1.onNext("c");

            ts2.assertValues("a", "b", "c");

            ts2.assertNoErrors();
            ts2.assertNotComplete();
        }

        @Test
        public void error() {
            Throwable e1 = new RuntimeException();
            Throwable e2 = new RuntimeException();

            o1.onError(e1);
            o1.onError(e2);

            ts2.assertError(e1);
            assertThat(ts2.errorCount()).isEqualTo(1);

            ts2.assertNoValues();
            ts2.assertNotComplete();
        }

        @Test
        public void complete() {
            o1.onCompleted();
            o1.onCompleted();

            ts2.assertComplete();
            assertThat(ts2.completions()).isEqualTo(1);

            ts2.assertNoValues();
            ts2.assertNoErrors();
        }

        @Test
        public void nextUnsubscribe() {
            ts2.dispose();
            o1.onNext("a");

            ts2.assertNoValues();
            ts2.assertNoErrors();
            ts2.assertNotComplete();
        }

        @Test
        public void errorUnsubscribe() {
            ts2.dispose();
            o1.onError(new RuntimeException());

            ts2.assertNoValues();
            ts2.assertNoErrors();
            ts2.assertNotComplete();
        }

        @Test
        public void completeUnsubscribe() {
            ts2.dispose();
            o1.onCompleted();

            ts2.assertNoValues();
            ts2.assertNoErrors();
            ts2.assertNotComplete();
        }
    }
}