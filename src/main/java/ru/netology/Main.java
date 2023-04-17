package ru.netology;

import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] texts = new String[25];
        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        int maxValue = 0;
        ExecutorService threadPool = Executors.newFixedThreadPool(texts.length);
        long startTs = System.currentTimeMillis(); // start time

        for (String text : texts) {
            futures.add(threadPool.submit(getMyCallable(text)));
        }

        for (Future<Integer> future : futures) {
            maxValue = Math.max(future.get(), maxValue);
        }

        threadPool.shutdown();
        long endTs = System.currentTimeMillis(); // end time
        System.out.println("Time: " + (endTs - startTs) + "ms");
        System.out.println("Max value: " + maxValue);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static Callable<Integer> getMyCallable(String text) {
        return () -> {
            int maxSize = 0;
            for (int i = 0; i < text.length(); i++) {
                for (int j = 0; j < text.length(); j++) {
                    if (i >= j) {
                        continue;
                    }
                    boolean bFound = false;
                    for (int k = i; k < j; k++) {
                        if (text.charAt(k) == 'b') {
                            bFound = true;
                            break;
                        }
                    }
                    if (!bFound && maxSize < j - i) {
                        maxSize = j - i;
                    }
                }
            }
            System.out.println(text.substring(0, 100) + " -> " + maxSize);
            return maxSize;
        };
    }
}
