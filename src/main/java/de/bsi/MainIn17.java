package de.bsi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class MainIn17 {

    private static final Logger log = Logger.getGlobal();

    public static void main(String[] args) {
        log.info(patternMatchingInSwitch(DoubleStream.of(1.1)));
        sequencedList();
        recordPatterns();
        virtualThread();
    }

    private static String patternMatchingInSwitch(BaseStream stream) {
        // TODO Convert to 1 switch statement.
        if (stream == null)
            return "null is now a possible case.";
        else if (stream instanceof IntStream is && is.isParallel())
            return "Expression in case.";
        else if (stream instanceof DoubleStream ds)
            return "Casted in case.";
        else
            throw new IllegalStateException();
    }

    private static void sequencedList() {
        // Mutable list created.
        List<String> list = new ArrayList<>(List.of("1st", "2nd", "3rd"));

        // TODO Read with index parameter.
        log.info("Read first & last element in list: %s & %s"
                .formatted(list.get(0), list.get(list.size() - 1)));

        // TODO Reverse list
        var reversedList = new ArrayList<String>();
        reversedList.add(0, "4th");
        log.info("Reversed order with new first element: " + reversedList);
        log.info("Changed list: " + list);

        // TODO removeFirst, removeLast, addFirst, addLast
        log.info("Changed list: " + list);
    }

    record Pair(String key, Integer value){}

    record Single(Double element){}

    private static void recordPatterns() {
        var mixedList = List.of(
                new Single(1.1), 
                new Pair("2nd", 2));

        for (Object entry : mixedList) {
            // TODO direct access to key and value without getter.
            if (entry instanceof Pair p)
                log.info("Record has: %s %d".formatted(p.key(), p.value()));

            // TODO Use switch with records
            if (entry instanceof Single s)
                log.info("Record: " + s);
            else if (entry instanceof Pair)
                log.finest("...");
            else
                throw new IllegalStateException();
        }
    }

    private static void virtualThread() {
        try (var executorService = Executors.newCachedThreadPool()) {
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
