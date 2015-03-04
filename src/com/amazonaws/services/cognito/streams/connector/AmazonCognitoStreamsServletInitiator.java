/*
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Amazon Software License (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/asl/
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.services.cognito.streams.connector;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Wrapper class to make tomcat run our worker even though we're not running real servlets.
 */
public class AmazonCognitoStreamsServletInitiator implements ServletContextListener {
    private static final Log LOG = LogFactory.getLog(AmazonCognitoStreamsServletInitiator.class);

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // Load the JDBC Driver
        try {
            LOG.info("Loading driver...");
            Class.forName("org.postgresql.Driver");
            LOG.info("Driver loaded!");
        } catch (ClassNotFoundException e) {
            LOG.error(e);
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
        
        AmazonCognitoStreamsConnectorExecutor worker = new AmazonCognitoStreamsConnectorExecutor();
        new Thread(worker).start();
    }
}
