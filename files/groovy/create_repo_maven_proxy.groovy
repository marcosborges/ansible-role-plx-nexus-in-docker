import groovy.json.JsonSlurper

def parsed_args = new JsonSlurper().parseText(args)
if(!repository.getRepositoryManager().get(parsed_args.name)) {
    repository.createMavenProxy(
        "${parsed_args.name}",
        "${parsed_args.remote_url}"
    );
}
