package de.bsi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class MainIn21 {

    private static final Logger log = Logger.getGlobal();

    public static void main(String[] args) {
        patternMatchingInSwitch();
        sequencedList();
        recordPatterns();
        virtualThread();
    }

    private static void patternMatchingInSwitch() {
        BaseStream stream = DoubleStream.of(1.1);
        switch (stream) {
            case null -> log.info("null is now a possible case.");
            case IntStream is when is.isParallel() ->
                    log.info("Expression in case.");
            case DoubleStream ds -> log.info("Casted in case.");
            default -> throw new IllegalStateException();
        }
    }

    private static void sequencedList() {
        // Mutable list created.
        List<String> list = new ArrayList<>(List.of("1st", "2nd", "3rd"));

        log.info("Read first & last element in list: %s & %s"
                .formatted(list.getFirst(), list.getLast()));

        var reversedList = list.reversed();
        reversedList.addFirst("4th");
        log.info("Reversed order with new first element: " + reversedList);
        log.info("Changed list: " + list);

        list.removeFirst();
        list.removeLast();
        list.addFirst("begin");
        list.addLast("end");
        log.info("Changed list: " + list);
    }

    record Pair(String key, Integer value){}

    record Single(Double element){}

    private static void recordPatterns() {
        var mixedList = List.of(
                new Single(1.1), 
                new Pair("2nd", 2));

        for (Object entry : mixedList) {
            if (entry instanceof Pair(String k, Integer v))
                log.info("Record has: %s %d".formatted(k, v));
            switch (entry) {
                case Single s -> log.info("Record: " + s);
                case Pair(String k, Integer v) -> log.finest("...");
                default -> throw new IllegalStateException();
            }
        }
    }

    private static void virtualThread() {
        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            var future = executorService.submit(() -> {
                var thread = Thread.currentThread();
                log.info(thread.threadId() + " : "
                        + thread.isVirtual());
            });
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
