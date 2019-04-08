/*
// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0.
 */
package com.amazonaws.services.cognito.streams.connector;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.services.kinesis.connectors.KinesisConnectorConfiguration;
import com.amazonaws.services.kinesis.connectors.KinesisConnectorExecutorBase;
import com.amazonaws.services.kinesis.connectors.KinesisConnectorRecordProcessorFactory;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;

/**
 * Implementation of a KinesisConnectorExecutor.
 * 
 * Responsible for starting loading necessary configuration and starting up Worker instance.
 *
 */
public class AmazonCognitoStreamsConnectorExecutor extends KinesisConnectorExecutorBase<AmazonCognitoStreamsEventBean, byte[]> {
    
    private static final Log LOG = LogFactory.getLog(AmazonCognitoStreamsConnectorExecutor.class);
    
    protected final KinesisConnectorConfiguration config;
    
    public AmazonCognitoStreamsConnectorExecutor() {
        
        // Load properties configured in the bundle
        InputStream configStream = getClass().getClassLoader().getResourceAsStream("connector.properties");
        
        Properties properties = new Properties();
        try {
            properties.load(configStream);
            configStream.close();
        } catch (IOException e) {
            String msg = "Could not load properties file from classpath";
            LOG.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
        
        // Overlay properties set in environment
        AmazonCognitoStreamsEnvironmentOptions.bootstrapEnv(properties);
        
        // Always use Instance Profile credentials
        InstanceProfileCredentialsProvider credentialsProvider = new InstanceProfileCredentialsProvider();
        
        this.config = new KinesisConnectorConfiguration(properties, credentialsProvider);
        
        super.initialize(this.config);
    }

    @Override
    public KinesisConnectorRecordProcessorFactory<AmazonCognitoStreamsEventBean, byte[]> getKinesisConnectorRecordProcessorFactory() {
        return new KinesisConnectorRecordProcessorFactory<>(new AmazonCognitoStreamsConnectorPipeline(), config);
    }

}
