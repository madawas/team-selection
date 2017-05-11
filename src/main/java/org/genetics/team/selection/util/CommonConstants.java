package org.genetics.team.selection.util;

import java.io.File;

public class CommonConstants {
    /**
     * Default configuration path
     */
    public static final String DEFAULT_CONFIG_PATH = "conf" + File.separator + "config.yaml";

    /**
     * Sample input path
     */
    public static final String DEFAULT_INPUT_PATH = "samples" + File.separator + "sample_input.csv";

    public static final String DEFAULT_LOG_PROPERTY_PATH = "conf" + File.separator + "log4j.properties";

    /**
     * Header mappers - id
     */
    public static final String HEADER_ID = "id";

    /**
     * Header mappers - type
     */
    public static final String HEADER_TYPE = "type";

    /**
     * Header mappers - name
     */
    public static final String HEADER_NAME = "name";

    /**
     * Header mappers - prefix for attribute
     */
    public static final String ATTRIBUTE_PREFIX = "attribute";
}
