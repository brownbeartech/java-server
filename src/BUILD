package(default_visibility = ["//visibility:public"])

java_library(
    name = "server",
    srcs = glob(["tech/brownbear/server/*.java"]),
    deps = [
        "@maven//:io_undertow_undertow_core",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:commons_logging_commons_logging",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_template_soy",
        "@maven//:com_google_code_gson_gson",
        "@tech_brownbear_resources_local//src:resources",
        "@tech_brownbear_soy_local//src:soy",
    ],
)