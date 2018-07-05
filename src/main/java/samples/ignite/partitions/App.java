package samples.ignite.partitions;

import java.util.Collection;
import java.util.Map;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.internal.processors.cache.IgniteCacheProxy;
import org.apache.ignite.internal.processors.cache.distributed.dht.GridDhtPartitionState;
import org.apache.ignite.internal.processors.cache.distributed.dht.preloader.GridDhtPartitionMap;

/** Console app. */
public class App {
    /** Entry point. */
    public static void main(String[] args) {
        if (args.length != 1)
            System.out.format("Usage: java -jar %s IGNITE_SPRING_CONFIG_XML\n", App.class.getName());
        else {
            Ignition.setClientMode(true);

            try (Ignite ignite = Ignition.start(args[0])) {
                for (String cacheName : ignite.cacheNames()) {
                    IgniteCache cache = ignite.cache(cacheName);

                    System.out.format("\n%s\n--------------------------------------\n\n", cacheName);

                    Collection<GridDhtPartitionMap> partMaps =
                        ((IgniteCacheProxy)cache).context().topology().partitionMap(false).values();

                    for (GridDhtPartitionMap partMap : partMaps) {
                        System.out.format("    NODE: %s\n\n", partMap.nodeId().toString());

                        for (Map.Entry<Integer, GridDhtPartitionState> partState : partMap.map().entrySet())
                            System.out.format("        PARTITION %s: %s\n", partState.getKey(), partState.getValue());
                    }
                }
            }
        }
    }
}
