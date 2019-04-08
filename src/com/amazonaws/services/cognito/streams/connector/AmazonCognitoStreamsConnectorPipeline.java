/*
// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0.
 */
package com.amazonaws.services.cognito.streams.connector;

import com.amazonaws.services.kinesis.connectors.KinesisConnectorConfiguration;
import com.amazonaws.services.kinesis.connectors.impl.AllPassFilter;
import com.amazonaws.services.kinesis.connectors.impl.BasicMemoryBuffer;
import com.amazonaws.services.kinesis.connectors.interfaces.IBuffer;
import com.amazonaws.services.kinesis.connectors.interfaces.IEmitter;
import com.amazonaws.services.kinesis.connectors.interfaces.IFilter;
import com.amazonaws.services.kinesis.connectors.interfaces.IKinesisConnectorPipeline;
import com.amazonaws.services.kinesis.connectors.interfaces.ITransformer;

/**
 * Connector pipeline for Amazon Cognito streams.
 * 
 * Consumes all events from stream and uses custom Redshift emitter.
 *
 */
public class AmazonCognitoStreamsConnectorPipeline implements
        IKinesisConnectorPipeline<AmazonCognitoStreamsEventBean, byte[]> {

    @Override
    public IEmitter<byte[]> getEmitter(KinesisConnectorConfiguration configuration) {
        return new AmazonCognitoStreamsRedshiftEmitter(configuration);
    }

    @Override
    public IBuffer<AmazonCognitoStreamsEventBean> getBuffer(
            KinesisConnectorConfiguration configuration) {
        return new BasicMemoryBuffer<AmazonCognitoStreamsEventBean>(configuration);
    }

    @Override
    public ITransformer<AmazonCognitoStreamsEventBean, byte[]> getTransformer(
            KinesisConnectorConfiguration configuration) {
        return new AmazonCognitoStreamsEventBeanTransformer(configuration);
    }

    @Override
    public IFilter<AmazonCognitoStreamsEventBean> getFilter(
            KinesisConnectorConfiguration configuration) {
        return new AllPassFilter<AmazonCognitoStreamsEventBean>();
    }

}
