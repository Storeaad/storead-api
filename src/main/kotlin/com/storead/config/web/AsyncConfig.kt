package com.storead.config.web

import org.slf4j.MDC
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskDecorator
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.Executor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor


@Configuration
@EnableAsync(proxyTargetClass = true)
class AsyncConfig() : AsyncConfigurer {

    override fun getAsyncExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.setTaskDecorator(MDCCopyTaskDecorator())
        executor.initialize()
        return executor
    }
}


class MDCCopyTaskDecorator : TaskDecorator {
    override fun decorate(runnable: Runnable): Runnable {
        val context = MDC.getCopyOfContextMap()
        return Runnable {
            try {
                if (context != null) {
                    MDC.setContextMap(context)
                }
                runnable.run()
            } finally {
                MDC.clear()
            }
        }
    }
}