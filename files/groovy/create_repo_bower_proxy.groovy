import groovy.json.JsonSlurper
import org.sonatype.nexus.repository.config.Configuration

parsed_args = new JsonSlurper().parseText(args)

authentication = parsed_args.remote_username == null ? null : [
        type: 'username',
        username: parsed_args.remote_username,
        password: parsed_args.remote_password
]

private Configuration newConfiguration(Map map) {
    Configuration config
    def repositoryManager = repository.repositoryManager
    try {
        config = repositoryManager.newConfiguration()
    } catch (MissingMethodException) {
        // Compatibility with nexus versions older than 3.21
        config = Configuration.newInstance()
    }
    config.with {
        repositoryName = map.repositoryName
        recipeName = map.recipeName
        online = map.online
        attributes = map.attributes as Map
    }
    return config
}

configuration = newConfiguration(
        repositoryName: parsed_args.name,
        recipeName: 'bower-proxy',
        online: true,
        attributes: [
                bower: [
                        rewritePackageUrls: true
                ],
                proxy: [
                        remoteUrl: parsed_args.remote_url,
                        contentMaxAge: 1440.0,
                        metadataMaxAge: 1440.0
                ],
                httpclient: [
                        blocked: false,
                        autoBlock: true
                ],
                storage: [
                        blobStoreName: parsed_args.blob_store,
                        strictContentTypeValidation: Boolean.valueOf(parsed_args.strict_content_validation)
                ],
                negativeCache: [
                        enabled: true,
                        timeToLive: 1440.0
                ]
        ]
)

def existingRepository = repository.getRepositoryManager().get(parsed_args.name)

if (existingRepository != null) {
    existingRepository.stop()
    configuration.attributes['storage']['blobStoreName'] = existingRepository.configuration.attributes['storage']['blobStoreName']
    existingRepository.update(configuration)
    existingRepository.start()
} else {
    repository.getRepositoryManager().create(configuration)
}
