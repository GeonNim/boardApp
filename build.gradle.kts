plugins {
    kotlin("jvm") version "1.9.21"
    id("io.ktor.plugin") version "2.3.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    // ✅ Ktor 서버 관련
    implementation("io.ktor:ktor-server-core:2.3.10")  // ✅ 기본 Ktor 기능 포함 (라우팅 포함)
    implementation("io.ktor:ktor-server-netty:2.3.10")
    implementation("io.ktor:ktor-server-auth:2.3.10")
    implementation("io.ktor:ktor-server-auth-jwt:2.3.10")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.10")
    implementation("io.ktor:ktor-serialization-jackson:2.3.10")
    implementation("io.ktor:ktor-server-call-logging:2.3.10")
    implementation("io.ktor:ktor-server-default-headers:2.3.10")
    implementation("io.ktor:ktor-server-status-pages:2.3.10")
    implementation("io.ktor:ktor-server-resources:2.3.10")
    implementation("io.ktor:ktor-server-request-validation:2.3.10")

    // ✅ Exposed (Kotlin ORM) 및 PostgreSQL 드라이버
    implementation("org.jetbrains.exposed:exposed-core:0.40.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.40.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.40.1")
    implementation("org.postgresql:postgresql:42.6.0")

    // ✅ 비밀번호 암호화 (BCrypt)
    implementation("at.favre.lib:bcrypt:0.10.2")

    // ✅ 환경 변수 로드 (.env)
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    // ✅ 코루틴 (비동기 처리)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // ✅ 로깅 (SLF4J + Logback)
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.slf4j:slf4j-api:2.0.9")

    // ✅ 테스트 관련
    testImplementation("io.ktor:ktor-server-tests:2.3.10")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.21")
}

application {
    mainClass.set("com.example.boardapp.MainKt")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

sourceSets {
    main {
        kotlin.srcDirs("src/main/kotlin")
    }
}
