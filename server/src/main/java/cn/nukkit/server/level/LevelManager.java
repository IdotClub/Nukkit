package cn.nukkit.server.level;

import cn.nukkit.api.level.Level;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.lang.management.ManagementFactory;
import java.util.*;

public class LevelManager {
    private final List<Level> levels = new ArrayList<>();
    private final Map<Level, LevelTicker> levelTasks = new HashMap<>();

    public synchronized void register(Level level) {
        Preconditions.checkNotNull(level, "level");
        Preconditions.checkArgument(!levels.contains(level), "level already registered");
        levels.add(level);
    }

    public synchronized boolean deregister(Level level) {
        Preconditions.checkNotNull(level, "level");
        if (levelTasks.containsKey(level)) {
            throw new IllegalArgumentException("Level is still being ticked");
        }
        return levels.remove(level);
    }

    public synchronized void start(NukkitLevel level) {
        Preconditions.checkNotNull(level, "level");
        Preconditions.checkState(!levelTasks.containsKey(level), "level already being ticked");
        LevelTicker ticker = new LevelTicker(level);
        levelTasks.put(level, ticker);
    }

    public synchronized void stop(Level level) {
        Preconditions.checkNotNull(level, "level");
        LevelTicker ticker = levelTasks.remove(level);
        Preconditions.checkState(ticker != null, "level is not being ticked");
        ticker.stop();
    }

    public synchronized List<Level> getLevels() {
        return ImmutableList.copyOf(levels);
    }

    public static String createUniqueLevelId(int random) {
        long bootTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        long currentTime = System.currentTimeMillis();
        long id = currentTime ^ (random >> 31);
        id *= bootTime ^ random;

        byte[] idBytes = new byte[8];
        for (int i = 7; i >= 0; i--) {
            idBytes[i] = (byte) (id & 0xFF);
            id >>= 8;
        }
        return Base64.getUrlEncoder().encodeToString(idBytes);
    }

    public static int parseSeed(@Nonnull Object seed) {
        Preconditions.checkNotNull(seed, "seed");
        int value = 0;
        if (seed instanceof Number) {
            value = ((Number) seed).intValue();
        } else if (seed instanceof String) {
            try {
                value = (int) Long.parseLong((String) seed);
            } catch (Exception e) {
                // Ignore
            }
        }

        if (value < 50 && value > -50) {
            value = seed.hashCode();
        }
        return value;
    }

    private class LevelTicker extends TimerTask {
        private final Timer timer;
        private final NukkitLevel level;

        private LevelTicker(NukkitLevel level) {
            this.level = level;
            this.timer = new Timer("Level ticker - " + level.getName(), true);
            this.timer.scheduleAtFixedRate(this, 50, 50); // 20 times per second
        }

        public void stop() {
            timer.cancel();
        }

        @Override
        public void run() {
            level.onTick();
        }
    }
}
