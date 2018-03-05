package classact.com.clever_little_monkey.utils;

/**
 * Created by hyunchanjeong on 2017/01/31.
 * NOTE THAT THIS IS ACTUALLY WEIGHTED QUICK UNION FIND (not just Union Find)
 */

public class UnionFind {
    private int[] id;
    private int[] sz; // size array

    public UnionFind(int N) {
        id = new int[N];
        sz = new int[N];

        for (int i = 0; i < N; i++) { // N array accesses
            id[i] = i;
            sz[i] = 1;

        }
    }

    public int root(int i) {
        while (id[i] != i) { // depth of i array accesses;
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);

        System.out.println("Roots: " + i + " " + j);

        if (i == j) {
            return;
        }

        int szi = sz[i];
        int szj = sz[j];

        if (szi < szj) {
            id[i] = j;
            sz[j] += sz[i];
        } else {
            id[j] = i;
            sz[i] += sz[j];
        }
    }

    public boolean connected(int p, int q) {
        return root(p) == root(q); // depth of root(p) + depth of root(q) accesses
    }

    public void printAll() {
        int length = id.length;

        for (int i = 0; i < length; i++) { // N array accesses
            if (i < length - 1) {
                System.out.print(id[i] + ", ");
            } else {
                System.out.println(id[i] + ".");
            }

        }
    }
}
