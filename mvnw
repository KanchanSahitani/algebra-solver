#!/bin/sh
# Maven Wrapper script for Linux/macOS (needed for cloud builds)

MAVEN_PROJECTBASEDIR="$(cd "$(dirname "$0")" && pwd)"
WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"

if [ ! -f "$WRAPPER_JAR" ]; then
    echo "Downloading Maven Wrapper..."
    mkdir -p "$MAVEN_PROJECTBASEDIR/.mvn/wrapper"
    curl -o "$WRAPPER_JAR" \
      "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
fi

exec java \
  -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" \
  -classpath "$WRAPPER_JAR" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"
