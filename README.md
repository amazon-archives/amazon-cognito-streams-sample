# Amazon Cognito Streams connector for Amazon Redshift

This example application is meant to demonstrate how to consume [Amazon Cognito streams](http://mobile.awsblog.com/post/Tx35782XTJZROY4) and model data in Amazon Redshift.

## Building the sample

This sample is designed to be built with Maven. All requisite dependencies are captured in the `pom.xml`. Simply use `mvn package` to build the war and deploy to Elastic Beanstalk or directly to EC2.

## Configuring the sample

The sample requires the following environment variables be set:

* `JDBC_CONNECTION_STRING` - This JDBC connection string for your Amazon Redshift cluster.
* `KINESIS_INPUT_STREAM` (or `PARAM1`) - The name of the Kinesis stream to look for Amazon Cognito updates.
* `REDSHIFT_USER_NAME` (or `PARAM2`) - The master user name for your Amazon Redshift cluster.
* `REDSHIFT_PASSWORD` (or `PARAM3`) - The master user password for your Amazon Redshift cluster.
* `S3_BUCKET_NAME` (or `PARAM4`) - The name of the S3 bucket to use for intermediate storage of data. This bucket should be configured to delete old data via S3 lifecycle configuration.
* `REGION` (or `PARAM5`) - The region of all your resources. 

## Deploying the sample

We've included a [CloudFormation template](CognitoStreamsSample.json) for deploying a binary version of this sample. In addition to configuring the above environment variables, it will create the Amazon Redshift cluster and other associated resources. Consider using this to get started and for deploying future updates to this sample.
