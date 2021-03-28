package dev.junomc.antibotplus.filters;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class NewEngine implements EngineInterface {
    private int msgHidden;
    
    public NewEngine() {
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
        ((Logger)LogManager.getRootLogger()).addFilter((Filter)new LogFilter());
    }
}