package com.assigment.ip.infrastructure.log;

import com.assigment.ip.application.port.out.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggerImpl implements Logger {

    @Override
    public void info(String infoText) {
        log.info(infoText);
    }

    @Override
    public void error(String errorText) {
        log.error(errorText);
    }

    @Override
    public void warning(String warnText) {
        log.warn(warnText);
    }
}
