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

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.kinesis.connectors.KinesisConnectorConfiguration;
import com.amazonaws.services.kinesis.connectors.redshift.RedshiftBasicEmitter;

/**
 * Extension of RedshiftBasicEmitter to support session based credentials.
 */
public class AmazonCognitoStreamsRedshiftEmitter extends RedshiftBasicEmitter {
    private final AWSCredentialsProvider credentialsProvider;
    private final String s3bucket;
    private final String redshiftTable;
    private final char redshiftDelimiter;
    
    public AmazonCognitoStreamsRedshiftEmitter(KinesisConnectorConfiguration configuration) {
        super(configuration);
        s3bucket = configuration.S3_BUCKET;
        redshiftTable = configuration.REDSHIFT_DATA_TABLE;
        redshiftDelimiter = configuration.REDSHIFT_DATA_DELIMITER;
        credentialsProvider = configuration.AWS_CREDENTIALS_PROVIDER;
    }
    
    @Override
    protected String generateCopyStatement(String s3File) {
        BasicSessionCredentials creds = (BasicSessionCredentials) credentialsProvider.getCredentials();
        
        StringBuilder exec = new StringBuilder();
        exec.append("COPY " + redshiftTable + " ");
        exec.append("FROM 's3://" + s3bucket + "/" + s3File + "' ");
        exec.append("CREDENTIALS 'aws_access_key_id=" + creds.getAWSAccessKeyId());
        exec.append(";aws_secret_access_key=" + creds.getAWSSecretKey());
        exec.append(";token=" +  creds.getSessionToken() + "' ");
        exec.append("DELIMITER '" + redshiftDelimiter + "'");
        exec.append(";");
        return exec.toString();
    }

}
