package com.github.springtg.bot.platform.handler.resolver;

import com.github.springtg.bot.platform.handler.MessageHandler;
import com.github.springtg.bot.platform.handler.RegexpMessageTextHandler;
import com.github.springtg.bot.platform.model.UpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.github.springtg.bot.platform.handler.UpdateFunctions.MESSAGE_TEXT_FROM_UPDATE_EVENT;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Sergey Kuptsov
 * @since 04/07/2016
 */
@Component
public class RegexpMessageTextHandlerResolver extends AbstractMessageHandlerResolver {
    private final Map<Integer, Pattern> regexpMessagePatterns = new HashMap<>();
    private final Map<Integer, RegexpMessageTextHandler> regexpMessageTextHandlers = new HashMap<>();
    private Integer hashCounter = 0;

    @Autowired(required = false)
    public RegexpMessageTextHandlerResolver(@NotNull List<RegexpMessageTextHandler> regexpMessageTextHandlerList) {
        checkNotNull(regexpMessageTextHandlerList, "Handlers cannot be null");
        regexpMessageTextHandlerList.forEach(this::add);
    }

    public RegexpMessageTextHandlerResolver() {
    }

    public void add(@NotNull RegexpMessageTextHandler regexpMessageTextHandler) {
        checkNotNull(regexpMessageTextHandler, "Handler cannot be null");
        int i = hashCounter++;
        regexpMessagePatterns.put(i, Pattern.compile(regexpMessageTextHandler.getMessageRegexp()));
        regexpMessageTextHandlers.put(i, regexpMessageTextHandler);
    }

    @Override
    protected MessageHandler resolveProcessor(UpdateEvent updateEvent) {
        for (Map.Entry<Integer, Pattern> patternEntry : regexpMessagePatterns.entrySet()) {
            if (patternEntry.getValue().matcher(MESSAGE_TEXT_FROM_UPDATE_EVENT.apply(updateEvent)).matches()) {
                return regexpMessageTextHandlers.get(patternEntry.getKey());
            }
        }

        return MessageHandler.EMPTY;
    }
}
