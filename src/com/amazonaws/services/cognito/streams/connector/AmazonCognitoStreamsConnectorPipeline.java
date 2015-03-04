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
