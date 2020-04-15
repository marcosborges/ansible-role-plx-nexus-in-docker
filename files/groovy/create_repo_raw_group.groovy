import groovy.json.JsonSlurper
import org.sonatype.nexus.repository.config.Configuration

parsed_args = new JsonSlurper().parseText(args)

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
        recipeName: 'raw-group',
        online: true,
        attributes: [
                group  : [
                        memberNames: parsed_args.member_repos
                ],
                storage: [
                        blobStoreName: parsed_args.blob_store,
                        strictContentTypeValidation: Boolean.valueOf(parsed_args.strict_content_validation)
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
