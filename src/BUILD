package(default_visibility = ["//visibility:public"])

java_import(
    name = "jars",
    jars = glob(["jars/**/*.jar"]),
)

java_library(
    name = "guava",
    exports = [
        "@com_google_guava//jar",
        "@org_json//jar",
    ],
)

java_library(
    name = "gson",
    exports = [
      "@com_google_code_gson//jar",
    ]
)

java_library(
    name = "logging",
    exports = [
        "@org_slf4j_api//jar",
    ],
)

java_library(
    name = "soy",
    exports = [
        "@com_google_template_soy//jar",
        "@com_google_inject_guice_multibindings//jar",
        "@com_google_inject_guice//jar",
        "@com_google_inject_assistedinject//jar",
        "@com_google_protobuf_3//jar",
        "@com_google_common_html_types//jar",
        ":gson",
        ":jars",
    ]
)

java_library(
    name = "undertow",
    exports = [
        "@io_undertow_core//jar",
        "@io_undertow_websockets_jsr//jar",
        "@org_jboss_logging//jar",
        "@org_jboss_xnio_api//jar",
        "@org_jboss_xnio_nio//jar",
    ]
)

java_library(
    name = "server",
    srcs = glob(["tech/brownbear/server/*.java"]),
    deps = [
        ":gson",
        ":guava",
        ":logging",
        ":soy",
        ":undertow",
        "@tech_brownbear_resources//src:resources",
        "@tech_brownbear_soy//src:soy",
        "@org_slf4j_api//jar",
    ],
)