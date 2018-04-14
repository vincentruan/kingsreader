package com.github.telegram.bot.platform.worker.saver;

import com.github.telegram.bot.platform.model.UpdateEvent;
import com.github.telegram.bot.platform.model.UpdateEvents;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @author Sergey Kuptsov
 * @since 30/05/2016
 */
public interface UpdatesSaver {
    void save(@NotNull UpdateEvents updateEvents);

    Optional<UpdateEvent> next();
}
