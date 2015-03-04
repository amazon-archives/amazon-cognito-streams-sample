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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Bean that represents a single change in a dataset. 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmazonCognitoStreamsRecordBean {
    private String key;

    private String value;

    private Long syncCount;

    private Date lastModifiedDate;

    private Date deviceLastModifiedDate;

    private String op;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getSyncCount() {
        return syncCount;
    }

    public void setSyncCount(Long syncCount) {
        this.syncCount = syncCount;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Date getDeviceLastModifiedDate() {
        return deviceLastModifiedDate;
    }

    public void setDeviceLastModifiedDate(Date deviceLastModifiedDate) {
        this.deviceLastModifiedDate = deviceLastModifiedDate;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }
}
