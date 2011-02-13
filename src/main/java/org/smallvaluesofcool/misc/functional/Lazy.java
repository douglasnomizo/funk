package org.smallvaluesofcool.misc.functional;

import org.smallvaluesofcool.misc.datastructures.TwoTuple;
import org.smallvaluesofcool.misc.functional.functors.DoFunction;
import org.smallvaluesofcool.misc.functional.functors.MapFunction;
import org.smallvaluesofcool.misc.functional.functors.NotPredicateFunction;
import org.smallvaluesofcool.misc.functional.functors.PredicateFunction;
import org.smallvaluesofcool.misc.functional.iterators.MappedIterator;
import org.smallvaluesofcool.misc.functional.iterators.PredicatedIterator;

import java.util.Iterator;

import static org.smallvaluesofcool.misc.IteratorUtils.toIterable;
import static org.smallvaluesofcool.misc.Literals.twoTuple;

public class Lazy {
    public static <S, T> Iterable<T> map(Iterable<? extends S> iterable, final MapFunction<S, T> function) {
        return toIterable(new MappedIterator<S, T>(iterable.iterator(), function));
    }

    public static <T> Iterable<T> filter(Iterable<? extends T> iterable, PredicateFunction<T> predicate) {
        return toIterable(new PredicatedIterator<T>(iterable.iterator(), predicate));
    }

    public static <T> Iterable<T> reject(Iterable<T> iterable, PredicateFunction<T> predicate) {
        return toIterable(new PredicatedIterator<T>(iterable.iterator(), new NotPredicateFunction<T>(predicate)));
    }

    public static <T> Iterable<TwoTuple<Integer, T>> enumerate(Iterable<? extends T> iterable) {
        return toIterable(new MappedIterator<T, TwoTuple<Integer, T>>(iterable.iterator(),
                new MapFunction<T, TwoTuple<Integer, T>>() {
                    private int index = 0;

                    public TwoTuple<Integer, T> map(T input) {
                        return twoTuple(index++, input);
                    }
                }));
    }

    public static <S, T> Iterable<TwoTuple<S, T>> zip(Iterable<? extends S> iterable1, Iterable<? extends T> iterable2) {
        final Iterator<? extends S> iterator1 = iterable1.iterator();
        final Iterator<? extends T> iterator2 = iterable2.iterator();
        return toIterable(
                new Iterator<TwoTuple<S, T>>() {
                    @Override
                    public boolean hasNext() {
                        return iterator1.hasNext() && iterator2.hasNext();
                    }

                    @Override
                    public TwoTuple<S, T> next() {
                        return twoTuple(iterator1.next(), iterator2.next());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                });
    }

    public static <T> Iterable<T> each(Iterable<? extends T> iterable, final DoFunction<T> doFunction) {
        final Iterator<? extends T> iterator = iterable.iterator();
        return toIterable(
                new Iterator<T>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public T next() {
                        T next = iterator.next();
                        doFunction.actOn(next);
                        return next;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                });
    }
}
