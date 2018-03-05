package classact.com.clever_little_monkey.utils;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by hcdjeong on 2017/09/17.
 * Courtesy: https://gist.github.com/benjchristensen/04eef9ca0851f3a5d7bf
 * Courtesy: https://blog.kaush.co/2014/12/24/implementing-an-event-bus-with-rxjava-rxbus/
 */

@Singleton
public class Bus {

    private final Relay<Object> bus = PublishRelay.create().toSerialized();

    @Inject
    public Bus() {
        // Empty Constructor
    }

    public void send(Object o) {
        bus.accept(o);
    }

    public Flowable<Object> asFlowable() {
        return bus.toFlowable(BackpressureStrategy.LATEST);
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}