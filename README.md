## CyclopsCore

[![Build Status](https://travis-ci.org/CyclopsMC/CyclopsCore.svg?branch=master-1.11)](https://travis-ci.org/CyclopsMC/CyclopsCore)
[![Coverage Status](https://coveralls.io/repos/github/CyclopsMC/CyclopsCore/badge.svg)](https://coveralls.io/github/CyclopsMC/CyclopsCore)
[![Download](https://img.shields.io/maven-metadata/v/https/oss.jfrog.org/artifactory/simple/libs-release/org/cyclops/cyclopscore/CyclopsCore/maven-metadata.xml.svg) ](https://oss.jfrog.org/artifactory/simple/libs-release/org/cyclops/cyclopscore/CyclopsCore/)
[![CurseForge](http://cf.way2muchnoise.eu/full_232758_downloads.svg)](http://minecraft.curseforge.com/projects/232758)
[![Discord](https://img.shields.io/discord/386052815128100865.svg?colorB=7289DA&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHYAAABWAgMAAABnZYq0AAAACVBMVEUAAB38%2FPz%2F%2F%2F%2Bm8P%2F9AAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhBxwQJhxy2iqrAAABoElEQVRIx7WWzdGEIAyGgcMeKMESrMJ6rILZCiiBg4eYKr%2Fd1ZAfgXFm98sJfAyGNwno3G9sLucgYGpQ4OGVRxQTREMDZjF7ILSWjoiHo1n%2BE03Aw8p7CNY5IhkYd%2F%2F6MtO3f8BNhR1QWnarCH4tr6myl0cWgUVNcfMcXACP1hKrGMt8wcAyxide7Ymcgqale7hN6846uJCkQxw6GG7h2MH4Czz3cLqD1zHu0VOXMfZjHLoYvsdd0Q7ZvsOkafJ1P4QXxrWFd14wMc60h8JKCbyQvImzlFjyGoZTKzohwWR2UzSONHhYXBQOaKKsySsahwGGDnb%2FiYPJw22sCqzirSULYy1qtHhXGbtgrM0oagBV4XiTJok3GoLoDNH8ooTmBm7ZMsbpFzi2bgPGoXWXME6XT%2BRJ4GLddxJ4PpQy7tmfoU2HPN6cKg%2BledKHBKlF8oNSt5w5g5o8eXhu1IOlpl5kGerDxIVT%2BztzKepulD8utXqpChamkzzuo7xYGk%2FkpSYuviLXun5bzdRf0Krejzqyz7Z3p0I1v2d6HmA07dofmS48njAiuMgAAAAASUVORK5CYII%3D)](https://discord.gg/9yDxubB)

Library mod for several other [mods](https://github.com/CyclopsMC).

All stable releases (including deobfuscated builds) can be found on [CurseForge](https://minecraft.curseforge.com/projects/cyclops-core/files).

[Development builds](https://oss.jfrog.org/artifactory/simple/libs-release/org/cyclops/cyclopscore/CyclopsCore/) are hosted by [JFrog Artifactory](https://www.jfrog.com/artifactory/).

### Using CyclopsCore

There are two ways to use this library inside your mod.

#### 1. Using a stable release

You can add CyclopsCore to your development environment by adding the following code to your Gradle build file:

    repositories {
        maven {
            name "Cyclops Repo"
            url "https://dl.bintray.com/cyclopsmc/dev/"
        }
    }
    ...
    dependencies {
        compile "org.cyclops.cyclopscore:CyclopsCore:${config.minecraft_version}-${config.cyclopscore_version}:deobf"
    }

Obviously, you should fill in the required Minecraft and CyclopsCore version yourself.


#### 2. Using a locally built release

If you do local changes to your CyclopsCore instance and want to test this locally for your mod, you can place your modified build to your local Maven repository by executing the following command:

    ./gradlew uploadArchives

Then you need to add the following to your Gradle build file to be able to use this custom build:

    repositories {
        mavenLocal()
    }
    ...
    dependencies {
        compile "org.cyclops.cyclopscore:CyclopsCore:${config.minecraft_version}-${project.cyclopscore_version_local}:deobf"
    }

While again making sure that you fill in the required Minecraft and CyclopsCore version.

### Contributing
* Before submitting a pull request containing a new feature, please discuss this first with one of the lead developers.
* When fixing an accepted bug, make sure to declare this in the issue so that no duplicate fixes exist.
* All code must comply to our coding conventions, be clean and must be well documented.

### Issues
* All bug reports and other issues are appreciated. If the issue is a crash, please include the FULL Forge log.
* Before submission, first check for duplicates, including already closed issues since those can then be re-opened.

### Branching Strategy

For every major Minecraft version, two branches exist:

* `master-{mc_version}`: Latest (potentially unstable) development.
* `release-{mc_version}`: Latest stable release for that Minecraft version. This is also tagged with all mod releases.

### Building and setting up a development environment

This mod uses [Project Lombok](http://projectlombok.org/) -- an annotation processor that allows us you to generate constructors, getters and setters using annotations -- to speed up recurring tasks and keep part of our codebase clean at the same time. Because of this it is advised that you install a plugin for your IDE that supports Project Lombok. Should you encounter any weird errors concerning missing getter or setter methods, it's probably because your code has not been processed by Project Lombok's processor. A list of Project Lombok plugins can be found [here](http://projectlombok.org/download.html).

### License
All code and images are licenced under the [MIT License](https://github.com/CyclopsMC/CyclopsCore/blob/master-1.8/LICENSE.txt)
