package(default_visibility = ["//visibility:public"])

java_library(
    name = "server",
    srcs = glob(["tech/brownbear/server/*.java"]),
    deps = [
        "@maven//:io_undertow_undertow_core",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_template_soy",
        "@maven//:com_google_code_gson_gson",
        "@tech_brownbear_resources//src:resources",
        "@tech_brownbear_soy//src:soy",
    ],
)