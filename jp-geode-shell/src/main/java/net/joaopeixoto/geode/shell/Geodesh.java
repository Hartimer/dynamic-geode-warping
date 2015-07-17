package net.joaopeixoto.geode.shell;

import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class Geodesh implements CommandMarker {

    private DataFeeder dataFeeder = new DataFeeder();

    @CliCommand(value = "feeder start", help = "Start the data feeder")
    public void feederStart() {
        if (!dataFeeder.isActive()) {
            new Thread(dataFeeder).start();
        }
    }

    @CliCommand(value = "feeder stop", help = "Start the data feeder")
    public void feederStop() {
        dataFeeder.stop();
    }

    @CliCommand(value = "feeder freq", help = "Set the data feeder frequency")
    public String feederFreq(
        @CliOption(key = { "", "freq" }, mandatory = true, help = "The frequency in milliseconds")
        long freq)
    {
        dataFeeder.setFreq(freq);
        return "New data frequency set to " + freq + " milliseconds";
    }

    @CliCommand(value = "feeder play", help = "Set the data feeder frequency")
    public String feederPlay(
        @CliOption(key = { "", "pattern" }, mandatory = true, help = "The pattern to play")
        String pattern)
    {
        dataFeeder.playPattern(pattern.toLowerCase());
        return "Playing patter " + pattern.toLowerCase();
    }

    @CliAvailabilityIndicator({ "feeder start" })
    public boolean isFeederStartAvailable() {
        return !dataFeeder.isActive();
    }

    @CliAvailabilityIndicator({ "feeder stop" })
    public boolean isFeederStopAvailable() {
        return !isFeederStartAvailable();
    }

}
