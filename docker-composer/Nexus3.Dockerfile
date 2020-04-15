ARG NEXUS_VERSION=3.22.0

FROM sonatype/nexus3:$NEXUS_VERSION
ARG NEXUS_VERSION=3.22.0
ARG NEXUS_BUILD=02
ARG COMPOSER_VERSION=0.0.4
ARG TARGET_DIR=/opt/sonatype/nexus/system/org/sonatype/nexus/plugins/nexus-repository-composer/${COMPOSER_VERSION}/
USER root
RUN mkdir -p ${TARGET_DIR}; \
    sed -i 's@wrap</feature>@wrap</feature>\n        <feature prerequisite="false" dependency="false">nexus-repository-composer</feature>@g' /opt/sonatype/nexus/system/com/sonatype/nexus/assemblies/nexus-oss-feature/${NEXUS_VERSION}-${NEXUS_BUILD}/nexus-oss-feature-${NEXUS_VERSION}-${NEXUS_BUILD}-features.xml; \
    sed -i 's@</features>@<feature name="nexus-repository-composer" description="org.sonatype.nexus.plugins:nexus-repository-composer" version="0.0.4">\n        <details>org.sonatype.nexus.plugins:nexus-repository-composer2</details>\n        <bundle>mvn:org.sonatype.nexus.plugins/nexus-repository-composer/0.0.4</bundle>\n    </feature>\n    </features>@g' /opt/sonatype/nexus/system/com/sonatype/nexus/assemblies/nexus-oss-feature/${NEXUS_VERSION}-${NEXUS_BUILD}/nexus-oss-feature-${NEXUS_VERSION}-${NEXUS_BUILD}-features.xml;
COPY nexus-repository-composer-0.0.4.jar ${TARGET_DIR}
COPY nexus-repository-composer-0.0.4.jar /opt/sonatype/nexus/

USER nexus
