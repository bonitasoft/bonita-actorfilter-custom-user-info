# Custom user info actor filter

[![Build](https://github.com/bonitasoft/bonita-actorfilter-custom-user-info/workflows/Build/badge.svg)](https://github.com/bonitasoft/bonita-actorfilter-custom-user-info/actions?query=workflow%3ABuild)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=bonitasoft_bonita-actorfilter-custom-user-info&metric=alert_status)](https://sonarcloud.io/dashboard?id=bonitasoft_bonita-actorfilter-custom-user-info)
[![GitHub release](https://img.shields.io/github/v/release/bonitasoft/bonita-actorfilter-custom-user-info?color=blue&label=Release)](https://github.com/bonitasoft/bonita-actorfilter-custom-user-info/releases)
[![Maven Central](https://img.shields.io/maven-central/v/org.bonitasoft.actorfilter/bonita-actorfilter-custom-user-info?color=orange&label=Maven%20Central)](https://search.maven.org/artifact/org.bonitasoft.actorfilter/bonita-actorfilter-custom-user-info)
[![License: GPL v2](https://img.shields.io/badge/License-GPL%20v2-yellow.svg)](https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html)

Filter users of an actor based on their custom user information

## Build

__Clone__ or __fork__ this repository, then at the root of the project run:

`./mvnw`

## Release

In order to create a new release:
- On the release branch, make sure to update the pom version (remove the -SNAPSHOT)
- Run the action 'Create release', set the version to release as parameter
- When the action is completed, do not forget to manage the release on the nexus (close and release)
- Update the `master` with the next SNAPSHOT version.

## Contributing

We would love you to contribute, pull requests are welcome! Please see the [CONTRIBUTING.md](CONTRIBUTING.md) for more information.

## License

The sources and documentation in this project are released under the [GPLv2 License](LICENSE)


