package me.lazyskulptor.demo.repo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class UnitTest {

    @Test
    void testCycle() {
        Mono<String> m1 = Mono.fromSupplier(() -> {
            System.out.println("A");
            return "A";
        })
                .doOnTerminate(() -> System.out.println("1 Terminating"))
                .doAfterTerminate(() -> System.out.println("1 after Terminating"))
                .doOnSubscribe(s -> System.out.println("1 :: " + s))
                .doOnSuccess(s -> System.out.println("1 :: " + s))
                .doFinally(s -> System.out.println("1 :: " + s));
        Mono<String> m2 = Mono.defer(() -> {
            System.out.println("B");
            return Mono.just("B");
        })
                .doOnTerminate(() -> System.out.println("2 Terminating"))
                .doAfterTerminate(() -> System.out.println("2 after Terminating"))
                .doOnSubscribe(s -> System.out.println("2 :: " + s))
                .doOnSuccess(s -> System.out.println("2 :: " + s))
                .doFinally(s -> System.out.println("2 :: " + s));
        Mono<String> m3 = Mono.fromSupplier(() -> {
            System.out.println("C");
            return "C";
        })
                .doOnTerminate(() -> System.out.println("3 Terminating"))
                .doAfterTerminate(() -> System.out.println("3 after Terminating"))
                .doOnSubscribe(s -> System.out.println("3 :: " + s))
                .doOnSuccess(s -> System.out.println("3 :: " + s))
                .doFinally(s -> System.out.println("3 :: " + s));

        var str = m1.flatMap(_s -> m2)
                .flatMap(_s -> m3)
                        .block();
//        System.out.println(m1.then(m2).then(m3).block());
        System.out.println(str);
    }
}
