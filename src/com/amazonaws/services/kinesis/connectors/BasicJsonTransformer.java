/*
// Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0.
 */
package com.amazonaws.services.kinesis.connectors;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.services.kinesis.connectors.interfaces.ITransformer;
import com.amazonaws.services.kinesis.model.Record;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class implements the ITransformer interface and provides an implementation of the toClass()
 * method for deserializing and serializing JSON strings. The constructor takes the class to
 * transform to/from JSON. The Record parameter of the toClass() method is expected to contain a
 * byte representation of a JSON string.
 * 
 * @param <T>
 */
public abstract class BasicJsonTransformer<T, U> implements ITransformer<T, U> {
    private static final Log LOG = LogFactory.getLog(BasicJsonTransformer.class);
    protected Class<T> inputClass;

    public BasicJsonTransformer(Class<T> inputClass) {
        this.inputClass = inputClass;
    }

    @Override
    public T toClass(Record record) throws IOException {
        try {
            return new ObjectMapper().readValue(record.getData().array(), this.inputClass);
        } catch (IOException e) {
            String message = "Error parsing record from JSON: " + new String(record.getData().array());
            LOG.error(message, e);
            throw new IOException(message, e);
        }
    }

}
