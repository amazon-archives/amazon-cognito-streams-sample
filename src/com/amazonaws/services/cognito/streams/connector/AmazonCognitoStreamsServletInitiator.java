/*
// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0.
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
