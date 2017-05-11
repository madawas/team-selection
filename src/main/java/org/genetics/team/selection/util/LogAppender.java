/*
 * Copyright 2017 Madawa Soysa
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

package org.genetics.team.selection.util;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Extended appender for application logs
 */
public class LogAppender extends AppenderSkeleton {
    private final JTextArea console;

    public LogAppender(JTextArea console) {
        this.console = console;
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        console.append(generateLogMessage(loggingEvent));
    }

    /**
     * Generates formatted log message
     * @param event {@link LoggingEvent}
     * @return formatted log message
     */
    private String generateLogMessage(LoggingEvent event) {
        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        if (event.locationInformationExists()) {
            StringBuilder message = new StringBuilder();
            message.append("\n");
            message.append(dFormat.format(new Date(event.getTimeStamp())));
            message.append(":  ");
            message.append(event.getLocationInformation().getClassName());
            message.append(" - ");
            message.append(event.getMessage().toString());
            return message.toString();
        }
        return event.getMessage().toString();
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
