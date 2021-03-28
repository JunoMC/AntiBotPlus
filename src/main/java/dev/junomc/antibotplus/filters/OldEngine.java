package dev.junomc.antibotplus.filters;

import dev.junomc.antibotplus.eoyaml.YamlSequence;
import dev.junomc.antibotplus.utils.AntiBotUtils;
import dev.junomc.antibotplus.utils.FileDataUtils;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OldEngine implements EngineInterface {
    private int msgHidden;
    
    public OldEngine() {
        this.msgHidden = 0;
    }
    
    @Override
    public int getHiddenMessagesCount() {
        return this.msgHidden;
    }
    
    @Override
    public void addHiddenMsg() {
        ++this.msgHidden;
    }
    
    @Override
    public void hideConsoleMessages() {
        ((Logger) LogManager.getRootLogger()).addFilter((Filter)new Filter() {
            @Override
            public State getState() {
                return null;
            }

            @Override
            public void initialize() {

            }

            @Override
            public void start() {

            }

            @Override
            public void stop() {

            }

            @Override
            public boolean isStarted() {
                return false;
            }

            @Override
            public boolean isStopped() {
                return false;
            }

            @Override
            public Result getOnMismatch() {
                return null;
            }

            @Override
            public Result getOnMatch() {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0) {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1) {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
                return null;
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
                return null;
            }

            public Result filter(final LogEvent event) {
                List<String> hidden = Arrays.asList("com.mojang.authlib.GameProfile", "Disconnecting com.mojang.authlib.GameProfile", "lost connection");

                for (final String s : hidden) {
                    if (event.getMessage().toString().contains(s)) {
                        final OldEngine old = OldEngine.this;
                        access(old, msgHidden + 1);
                        return Filter.Result.DENY;
                    }
                }
                return null;
            }
        });
    }
    
    private void access(final OldEngine oldEngine, final int msgHidden) {
        oldEngine.msgHidden = msgHidden;
    }
}