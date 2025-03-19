pluginManagement {
    repositories {
        google() // Repositório do Google para dependências do Android
        mavenCentral() // Repositório Maven Central, onde muitas dependências estão hospedadas
        gradlePluginPortal() // Repositório para plugins do Gradle
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GerenciamentoDeViagem"
include(":app")
