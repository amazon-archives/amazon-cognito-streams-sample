/*
// Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0.
 */
package com.amazonaws.services.cognito.streams.connector;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * An instance of an event record written to the stream by Amazon Cognito.
 * 
 * Contains either a list of Records or a URL that will contain the list of records.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmazonCognitoStreamsEventBean {
    
    private String identityPoolId;
    private String identityId;
    private String datasetName;
    private String operation;
    private List<AmazonCognitoStreamsRecordBean> kinesisSyncRecords;
    private String kinesisSyncRecordsURL;
    private Date lastModifiedDate;
    private Integer syncCount;
    
    public String getIdentityPoolId() {
        return identityPoolId;
    }
    
    public void setIdentityPoolId(String identityPoolId) {
        this.identityPoolId = identityPoolId;
    }
    
    public String getIdentityId() {
        return identityId;
    }
    
    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }
    
    public String getDatasetName() {
        return datasetName;
    }
    
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    public List<AmazonCognitoStreamsRecordBean> getKinesisSyncRecords() {
        return kinesisSyncRecords;
    }
    
    public void setKinesisSyncRecords(List<AmazonCognitoStreamsRecordBean> kinesisSyncRecords) {
        this.kinesisSyncRecords = kinesisSyncRecords;
    }
    
    public String getKinesisSyncRecordsURL() {
        return kinesisSyncRecordsURL;
    }
    
    public void setKinesisSyncRecordsURL(String kinesisSyncRecordsURL) {
        this.kinesisSyncRecordsURL = kinesisSyncRecordsURL;
    }
    
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }
    
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    public Integer getSyncCount() {
        return syncCount;
    }
    
    public void setSyncCount(Integer syncCount) {
        this.syncCount = syncCount;
    }
}
