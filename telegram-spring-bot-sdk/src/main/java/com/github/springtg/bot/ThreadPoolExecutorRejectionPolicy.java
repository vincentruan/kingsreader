/*
 * Copyright [2018] [vincentruan]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.springtg.bot;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * The RejectedExecutionHandler type. When a bounded queue cannot accept any
 * additional tasks, this determines the behavior. While the default is ABORT,
 * consider using CALLER_RUNS to throttle inbound tasks. In other words, by forcing
 * the caller to run the task itself, it will not be able to provide another task
 * until after it completes the task at hand. In the meantime, one or more tasks
 * may be removed from the queue. Alternatively, if it is not critical to run every
 * task, consider using DISCARD to drop the current task or DISCARD_OLDEST to drop
 * the task at the head of the queue.
 *
 * @author vincentruan
 * @version 1.0.0
 */
public enum ThreadPoolExecutorRejectionPolicy {

    /**
     * A handler for rejected tasks that throws a
     * {@code RejectedExecutionException}.
     */
    ABORT(ThreadPoolExecutor.AbortPolicy.class),

    /**
     * A handler for rejected tasks that runs the rejected task
     * directly in the calling thread of the {@code execute} method,
     * unless the executor has been shut down, in which case the task
     * is discarded.
     */
    CALLER_RUNS(ThreadPoolExecutor.CallerRunsPolicy.class),

    /**
     * A handler for rejected tasks that silently discards the
     * rejected task.
     */
    DISCARD(ThreadPoolExecutor.DiscardPolicy.class),

    /**
     * A handler for rejected tasks that discards the oldest unhandled
     * request and then retries {@code execute}, unless the executor
     * is shut down, in which case the task is discarded.
     */
    DISCARD_OLDEST(ThreadPoolExecutor.DiscardOldestPolicy.class);

    private Class<? extends RejectedExecutionHandler> policyClass;

    ThreadPoolExecutorRejectionPolicy(Class<? extends RejectedExecutionHandler> policyClass) {
        this.policyClass = policyClass;
    }

    public Class<? extends RejectedExecutionHandler> getPolicyClass() {
        return policyClass;
    }

    public static ThreadPoolExecutorRejectionPolicy getRejectionPolicyByName(String rejectionPolicy) {
        if(StringUtils.isBlank(rejectionPolicy)) {
            return null;
        }

        for (ThreadPoolExecutorRejectionPolicy threadPoolExecutorRejectionPolicy : values()) {
            if(threadPoolExecutorRejectionPolicy.name().equalsIgnoreCase(rejectionPolicy)) {
                return threadPoolExecutorRejectionPolicy;
            }
        }

        return null;
    }

}
