package net.joaopeixoto.geode.shell;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Logger;

import com.google.common.collect.ImmutableList;
import org.apache.commons.io.IOUtils;



public class DataFeeder implements Runnable {

    protected final Logger LOG = Logger.getLogger(getClass().getName());

    private volatile long freq = 1000L;

    private volatile boolean active = false;

    private Random rn = new Random();

    private LinkedList<Float> queue = new LinkedList<>();

    @Override
    public void run() {
        LOG.info("Starting data feeder...");
        active = true;
        while (active) {
            try {
                Thread.sleep(freq);
                float nextPoint = getNextPoint();
                PoorManRestClient.sendPointToUi(
                        new DataPoint(System.currentTimeMillis(), nextPoint));
                PoorManRestClient.sendPointToGeode(
                        new DataPoint(System.currentTimeMillis(), nextPoint));
            }
            catch (Exception e) {
                LOG.warning(e.getMessage());
            }
        }
        LOG.info("Terminating data feeder...");
    }

    public boolean isActive() {
        return active;
    }

    public void stop() {
        active = false;
    }

    public long getFreq() {
        return freq;
    }

    public void setFreq(long freq) {
        this.freq = freq;
    }

    public void playPattern(String p) {
        InputStream is = getClass().getResourceAsStream("/pattern-" + p + ".txt");
        try {
            for (String s : ImmutableList.copyOf(IOUtils.toString(is).split("\n"))) {
                queue.add(Float.parseFloat(s));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float getNextPoint() {
        if (queue.isEmpty()) {
            return getNoisyPoint();
        }

        return queue.pop();
    }

    private float getNoisyPoint() {
        int maximum = 4;
        int minimum = 0;
        int n = maximum - minimum + 1;
        int i = rn.nextInt() % n;
        return minimum + i;
    }

}
