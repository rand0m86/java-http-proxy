package com.github.rand0m86.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EnumerationUtil {

    public static <T> Stream<T> enumerationToStream(Enumeration<T> enumeration) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<T>() {
                            @Override
                            public boolean hasNext() {
                                return enumeration.hasMoreElements();
                            }

                            @Override
                            public T next() {
                                return enumeration.nextElement();
                            }
                        }, Spliterator.ORDERED),
                false
        );
    }

    public static <T> List<T> enumerationToList(Enumeration<T> enumeration) {
        return enumerationToStream(enumeration).collect(Collectors.toList());
    }
}
