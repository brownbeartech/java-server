load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

# Maven
RULES_JVM_EXTERNAL_TAG = "2.3"
RULES_JVM_EXTERNAL_SHA = "375b1592e3f4e0a46e6236e19fc30c8020c438803d4d49b13b40aaacd2703c30"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "io.undertow:undertow-core:2.0.13.Final",
        "org.slf4j:slf4j-api:1.7.22",
        "commons-logging:commons-logging:1.2",
        "com.google.guava:guava:28.0-jre",
        "com.google.template:soy:2017-04-23",
        "com.google.code.gson:gson:2.8.5",
    ],
    repositories = [
        "https://jcenter.bintray.com/",
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
)
# End Maven

# Brownbear
git_repository(
    name = "tech_brownbear_resources",
    remote = "https://github.com/brownbeartech/java-resources.git",
    commit = "826afee55c361b2a6b022c6c0458f486e20ce613",
    shallow_since = "1562798190 -0400",
)

git_repository(
    name = "tech_brownbear_soy",
    remote = "https://github.com/brownbeartech/java-soy.git",
    commit = "eafbcaea61801fe81d62bcd0cd238a26c0bfce54",
    shallow_since = "1562798105 -0400",
)

# Brownbear local
local_repository(
    name = "tech_brownbear_resources_local",
    path = "../java-resources",
)

local_repository(
    name = "tech_brownbear_soy_local",
    path = "../java-soy",
)