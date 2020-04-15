import groovy.json.JsonSlurper
parsed_args = new JsonSlurper().parseText(args)

def repoManager = repository.getRepositoryManager()

if (!repoManager.get("${parsed_args.name}.hosted")) {
    repository.createMavenHosted(
        "${parsed_args.name}.hosted"/*,
        String blobStoreName,
        boolean strictContentTypeValidation,
        VersionPolicy versionPolicy,
        WritePolicy writePolicy,
        LayoutPolicy layoutPolicy*/
    );
}

if (!repoManager.get("${parsed_args.name}.proxy")) {
    repository.createMavenProxy(
        "${parsed_args.name}.proxy",
        "https://repo1.maven.org/maven2/"/*,
        String blobStoreName,
        boolean strictContentTypeValidation,
        VersionPolicy versionPolicy,
        LayoutPolicy layoutPolicy*/
    );
}


/*createMavenGroup(
    String name,
    List<String> members,
    String blobStoreName
);*/