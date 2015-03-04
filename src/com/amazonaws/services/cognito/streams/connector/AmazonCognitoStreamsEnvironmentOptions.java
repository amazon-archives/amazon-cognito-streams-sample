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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.services.kinesis.connectors.KinesisConnectorConfiguration;
import com.google.common.base.MoreObjects;

/**
 * Helper class to load environment variables into properties and ensure
 * table is available in Redshift.
 *
 */
public class AmazonCognitoStreamsEnvironmentOptions {
    private static final Log LOG = LogFactory.getLog(AmazonCognitoStreamsEnvironmentOptions.class);
    
    static String getJDBCConnection() {
        String variable = System.getProperty("JDBC_CONNECTION_STRING");
        if (variable == null) {
            throw new RuntimeException("JDBC_CONNECTION_STRING not set");
        }
        return variable;
    }
    
    static String getKinesisInputStream() {
        
        String variable = MoreObjects.firstNonNull(System.getProperty("KINESIS_INPUT_STREAM"), 
                System.getProperty("PARAM1"));
        
        return variable;
    }
    
    static String getRedshiftUserName() {
        String variable = MoreObjects.firstNonNull(System.getProperty("REDSHIFT_USER_NAME"), 
                System.getProperty("PARAM2"));
        
        return variable;
    }
    
    static String getRedshiftPassword() {
        String variable = MoreObjects.firstNonNull(System.getProperty("REDSHIFT_PASSWORD"), 
                System.getProperty("PARAM3"));
        
        return variable;
    }
    
    static String getS3BucketName() {
        String variable = MoreObjects.firstNonNull(System.getProperty("S3_BUCKET_NAME"), 
                System.getProperty("PARAM4"));
        
        return variable;
    }
    
    static String getRegion() {
        String variable = MoreObjects.firstNonNull(System.getProperty("REGION"), 
                System.getProperty("PARAM5"));
        
        return variable;
    }
    
    static void createRedshiftTable(Properties properties) {
        // Ensure our data table exists
        Properties loginProperties = new Properties();
        loginProperties.setProperty("user", getRedshiftUserName());
        loginProperties.setProperty("password", getRedshiftPassword());
        
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ")
        .append(properties.getProperty(KinesisConnectorConfiguration.PROP_REDSHIFT_DATA_TABLE))
        .append(" (")
        .append("identityPoolId varchar(128),")
        .append("identityId varchar(128),")
        .append("datasetName varchar(128),")
        .append("operation varchar(64),")
        .append("key varchar(1024),")
        .append("value varchar(4096),")
        .append("op varchar(64),")
        .append("syncCount int,")
        .append("deviceLastModifiedDate timestamp,")
        .append("lastModifiedDate timestamp")
        .append(")");

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(getJDBCConnection(), loginProperties);
            
            Statement stmt = conn.createStatement();
            stmt.execute(builder.toString());
            stmt.close();
        } catch (SQLException e) {
            LOG.error("Failed to create table.", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                LOG.error("Failed close connection.", e);
            }
        }
    }
    
    public static void bootstrapEnv(Properties properties) {
        properties.setProperty(KinesisConnectorConfiguration.PROP_REDSHIFT_URL, getJDBCConnection());
        properties.setProperty(KinesisConnectorConfiguration.PROP_S3_BUCKET, getS3BucketName());
        properties.setProperty(KinesisConnectorConfiguration.PROP_REDSHIFT_USERNAME, getRedshiftUserName());
        properties.setProperty(KinesisConnectorConfiguration.PROP_REDSHIFT_PASSWORD, getRedshiftPassword());
        properties.setProperty(KinesisConnectorConfiguration.PROP_KINESIS_INPUT_STREAM, getKinesisInputStream());
        properties.setProperty(KinesisConnectorConfiguration.PROP_REGION_NAME, getRegion());
        
        createRedshiftTable(properties);
    }
}
