package samples.ignite.partitions;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CachePeekMode;

public class Loader {
    public static void main(String[] args) throws InterruptedException {
        Ignition.setClientMode(true);

        Ignite ignite = Ignition.start("ignite-server.xml");

        IgniteCache<Integer, String> cache1 = ignite.cache("cache1");
        IgniteCache<Integer, String> cache2 = ignite.cache("cache2");

        int cache1Size = cache1.size(CachePeekMode.PRIMARY);
        int cache2Size = cache2.size(CachePeekMode.PRIMARY);

        for (int t = cache1Size, c = cache2Size; t < 1_000_000; t++, c++) {
            if (t % (20 * 80) == 0)
                System.out.format("%s-%s\n", t, c);
            else if (t % 20 == 0)
                System.out.print(".");

            cache1.put(t, "v" + t);
            cache2.put(c, "v" + c);

            Thread.sleep(10);
        }
    }
}
