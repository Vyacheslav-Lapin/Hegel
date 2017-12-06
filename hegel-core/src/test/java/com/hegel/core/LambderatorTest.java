package com.hegel.core;

import com.hegel.core.test.Tests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class LambderatorTest {


    //    private BufferedReader reader = new BufferedReader(
//            new FileReader(
//                    new File()
//            )
//    );
    private Lambderator<String> lambderator; // TODO: 18/02/2017

    @BeforeEach
    void setUp() {
        lambderator = new Lambderator<String>() {
            int i = 5;

            @Override
            public boolean tryAdvance(Consumer<? super String> action) {
                action.accept(String.valueOf(i--));
                return i != 0;
            }
        };
    }

    @Test
    void trySplit() {
        String s = Tests.fromSystemOutPrint(() -> lambderator.stream().forEach(System.out::print));
        assertThat(s, is("54321"));
    }
}