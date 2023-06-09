import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kmongo_version: String by project
val koin_version: String by project
val commons_codec_version: String by project

plugins {
    kotlin("jvm") version "1.8.21"
    id("io.ktor.plugin") version "2.3.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "5.2.0"

}

group = "com.pixel_Alireza"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    project.setProperty("mainClassName", mainClass.get())

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

val sshAntTask = configurations.create("sshAntTask")

dependencies {
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-websockets:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")


    //Kmongo (Kotlin MongoBD)
    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")

    implementation("commons-codec:commons-codec:$commons_codec_version")


    // Koin core features
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")


    sshAntTask("org.apache.ant:ant-jsch:1.9.2")



    tasks.withType<ShadowJar> {
        manifest {
            attributes(
                "Main-Class" to application.mainClass.get()
            )
        }
    }

    ant.withGroovyBuilder {
        "taskdef"(
            "name" to "scp",
            "classname" to "org.apache.tools.ant.taskdefs.optional.ssh.Scp",
            "classpath" to configurations.get("sshAntTask").asPath
        )
        "taskdef"(
            "name" to "ssh",
            "classname" to "org.apache.tools.ant.taskdefs.optional.ssh.SSHExec",
            "classpath" to configurations.get("sshAntTask").asPath
        )
    }


    task("deploy") {
        dependsOn("clean", "shadowJar")
        ant.withGroovyBuilder {
            doLast {
                val knownHosts = File.createTempFile("knownhosts", "txt")
                val user = "root"
                val host ="82.115.21.81"
                val pk = file("keys/id_rsa")
                try {
                    "scp"(
                        "file" to file("build/libs/pixel_Alireza.gameland-all.jar"),
                        "todir" to "$user@$host:/root/androidProjects/gameland",
                        "keyfile" to pk,
                        "trust" to true,
                        "knownhosts" to knownHosts
                    )
                    "ssh"(
                        "host" to host,
                        "username" to user,
                        "keyfile" to pk,
                        "trust" to true,
                        "knownhosts" to knownHosts,
                        "command" to "mv /root/androidProjects/gameland/pixel_Alireza.gameland-all.jar /root/androidProjects/gameland/gameland-server.jar"
                    )
                    "ssh"(
                        "host" to host,
                        "username" to user,
                        "keyfile" to pk,
                        "trust" to true,
                        "knownhosts" to knownHosts,
                        "command" to "systemctl stop gameland"
                    )
                    "ssh"(
                        "host" to host,
                        "username" to user,
                        "keyfile" to pk,
                        "trust" to true,
                        "knownhosts" to knownHosts,
                        "command" to "systemctl start gameland"
                    )
                } finally {
                    knownHosts.delete()
                }
            }
        }
    }
}