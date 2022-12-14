plugins {
    id(Plugins.SPRING_BOOT.name)
    kotlin("jvm")
    kotlin("plugin.spring")
}

tasks.named("jar") {
    enabled = false
}

dependencies {
    implementation(project(":account-role"))
    implementation(Libs.SPRING_BOOT_STARTER_SECURITY)
    implementation(Libs.SPRING_BOOT_STARTER_OAUTH2_CLIENT)
    implementation(Libs.SPRING_BOOT_STARTER_WEB)
    implementation(Libs.JACKSON_MODULE_KOTLIN)
    implementation(Libs.KOTLIN_REFLECT)
    implementation(Libs.TRINO_JDBC)
    annotationProcessor(Libs.SPRING_BOOT_CONFIGURATION_PROCESSOR)
    testImplementation(Libs.SPRING_BOOT_STARTER_TEST)
}
