import groovy.json.JsonSlurper

parsed_args = new JsonSlurper().parseText(args)

existingBlobStore = blobStore.getBlobStoreManager().get(parsed_args.name)
if (existingBlobStore == null) {
    blobStore.createFileBlobStore(parsed_args.name, parsed_args.path)
}

/*
if (parsed_args.type == "S3") {
      //config['bucket'] = '{{.BucketName}}'
      //config['accessKeyId'] = '{{.AwsAccessKey}}'
      //config['secretAccessKey'] = '{{.AwsSecret}}'
      //config['assumeRole'] = '{{.AwsIamRole}}'
      //config['region'] = '{{.AwsRegion}}'
      blobStore.createS3BlobStore(parsed_args.name, parsed_args.config)
      msg = "S3 blobstore {} created"
  } else {
      blobStore.createFileBlobStore(parsed_args.name, parsed_args.path)
      msg = "Created blobstore {} created"
  }
 */
