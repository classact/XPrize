package classact.com.clever_little_monkey.utils;


import java.util.Random;

public class FisherYates {

    /**
     * Fisher Yates Shuffle
     * Returns a shuffled array of indices 0 .. N-1
     * based on the Fisher Yates algorithm
     * @param N
     * @return
     */
    public static int[] shuffle(int N) {

        // Initialize base based on N
        int[] base = new int[N];

        // Initialized rnd variable used in the shuffle
        Random rnd = new Random();

        // Declare variables to be used;
        int j, a, b;

        // Populate base
        for (int i = 0; i < N; i++) {
            base[i] = i;
        }

        // Start the shuffle
        for (int i = 0; i < N - 1; i++) {

            // Get random value j, where i <= j < N
            j = i + rnd.nextInt(N - i);

            // Store current values
            a = base[i];
            b = base[j];

            // Swap them
            base[i] = b;
            base[j] = a;
        }

        return base;
    }
}
