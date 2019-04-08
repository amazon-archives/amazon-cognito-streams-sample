/*
// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0.
 */
package com.amazonaws.services.cognito.streams.connector;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.services.kinesis.connectors.KinesisConnectorConfiguration;
import com.amazonaws.services.kinesis.connectors.redshift.RedshiftTransformer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

/**
 * Transformer for event bean. Each event bean can become multiple rows in Redshift.
 * 
 * Transformer is also responsible for fetching the S3 URL (if supplied)
 */
public class AmazonCognitoStreamsEventBeanTransformer extends RedshiftTransformer<AmazonCognitoStreamsEventBean> {
    
    private static final Log LOG = LogFactory.getLog(AmazonCognitoStreamsEventBeanTransformer.class);
    
    private static final ObjectMapper om = new ObjectMapper();

    private final char delim;
    
    public AmazonCognitoStreamsEventBeanTransformer(KinesisConnectorConfiguration config) {
        super(AmazonCognitoStreamsEventBean.class);
        delim = config.REDSHIFT_DATA_DELIMITER;
    }

    @Override
    public String toDelimitedString(AmazonCognitoStreamsEventBean dataObject) {
        StringBuilder builder = new StringBuilder();
        StringBuilder dataBuilder = new StringBuilder();
        
        dataBuilder.append(dataObject.getIdentityPoolId()).append(delim)
        .append(dataObject.getIdentityId()).append(delim)
        .append(truncate(sanitize(dataObject.getDatasetName()),128)).append(delim)
        .append(dataObject.getOperation());
        
        String repeatingPart = dataBuilder.toString();
        
        // If the data object has a URL, parse the records from the S3 file
        if (dataObject.getKinesisSyncRecordsURL() != null) {
            LOG.info("fetching records from " + dataObject.getKinesisSyncRecordsURL());
            try {
                URL url = new URL(dataObject.getKinesisSyncRecordsURL());
                List<AmazonCognitoStreamsRecordBean> parsed = om.readValue(url.openStream(), 
                        om.getTypeFactory().constructCollectionType(List.class, AmazonCognitoStreamsRecordBean.class));
                dataObject.setKinesisSyncRecords(parsed);
            }
            catch (Exception e) {
                LOG.error("Unable to parse S3 payload",e);
                throw new RuntimeException("Unable to parse S3 payload",e);
            }
            LOG.info("fetched " + dataObject.getKinesisSyncRecords().size() + " records from S3");
        }
        
        // For some operations, neither records nor URL will be populated
        if (dataObject.getKinesisSyncRecords() == null) {
            AmazonCognitoStreamsRecordBean tempBean = new AmazonCognitoStreamsRecordBean();
            tempBean.setDeviceLastModifiedDate(new Date());
            tempBean.setLastModifiedDate(new Date());
            dataObject.setKinesisSyncRecords(ImmutableList.of(tempBean));
        }
        
        for (AmazonCognitoStreamsRecordBean recordObject: dataObject.getKinesisSyncRecords()) {
        
            builder.append(repeatingPart).append(delim)
            .append(truncate(sanitize(recordObject.getKey()),1024)).append(delim)
            .append(truncate(sanitize(recordObject.getValue()),4096)).append(delim)
            .append(recordObject.getOp()).append(delim)
            .append(recordObject.getSyncCount()).append(delim)
            .append(new Timestamp(recordObject.getDeviceLastModifiedDate().getTime())).append(delim)
            .append(new Timestamp(recordObject.getLastModifiedDate().getTime()))
            .append("\n");
        }
       
        LOG.info("processed " + dataObject.getKinesisSyncRecords().size() + " records from Kinesis");

        return builder.toString();
    }

    
    /**
     * Remove characters known to cause issues in Redshift import
     * @param string
     * @return
     */
    private String sanitize(String string) {
        if (string == null) {
            return null;
        }
        string = string.replace("\n", " ");
        string = string.replace(Character.toString(delim), " ");
        string = string.replaceAll("\\x00", "?");
        return string;
    }

    /**
     * Truncate values to length.
     * @param string
     * @param maxLength
     * @return
     */
    private String truncate(String string, int maxLength) {
        if (string == null) {
            return null;
        }
        if (string.length() > maxLength) {
            string = string.substring(0, maxLength);
        }
        return string;
    }
}
