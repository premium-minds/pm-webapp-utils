# Changelog

## [Unreleased]

### Changed

 - [removed unused dependency com.google.code.gson](https://github.com/premium-minds/pm-webapp-utils/commit/29105e8e793aa74024b55e36065c6190c7e8851a)
 - [Bump maven-scm-publish-plugin from 1.0-beta-2 to 3.0.0](https://github.com/premium-minds/pm-webapp-utils/pull/12)
 - [Bump maven-javadoc-plugin from 2.9.1 to 3.1.1](https://github.com/premium-minds/pm-webapp-utils/pull/11)
 - [Bump maven-scm-provider-gitexe from 1.8.1 to 1.11.2](https://github.com/premium-minds/pm-webapp-utils/pull/9)
 - [Bump maven-compiler-plugin from 2.3.2 to 3.8.1](https://github.com/premium-minds/pm-webapp-utils/pull/8)
 - [Bump javax.servlet-api from 4.0.0 to 4.0.1](https://github.com/premium-minds/pm-webapp-utils/pull/14)
 - [Bump maven-gpg-plugin from 1.5 to 1.6 ](https://github.com/premium-minds/pm-webapp-utils/pull/16)
 - [Bump maven-release-plugin from 2.4.2 to 2.5.3](https://github.com/premium-minds/pm-webapp-utils/pull/15)
 - [Bump nexus-staging-maven-plugin from 1.6.2 to 1.6.8](https://github.com/premium-minds/pm-webapp-utils/pull/17)
 - [Bump log4j-core from 2.11.1 to 2.12.1](https://github.com/premium-minds/pm-webapp-utils/pull/13)
 - [Bump maven-source-plugin from 2.2.1 to 3.2.0](https://github.com/premium-minds/pm-webapp-utils/pull/18)
 - [Bump log4j-core from 2.12.1 to 2.13.0](https://github.com/premium-minds/pm-webapp-utils/pull/19)
 - [Bump junit from 4.12 to 4.13](https://github.com/premium-minds/pm-webapp-utils/pull/21)
 - [Bump maven-source-plugin from 3.2.0 to 3.2.1](https://github.com/premium-minds/pm-webapp-utils/pull/20)
 - [Bump log4j-core from 2.13.0 to 2.13.2](https://github.com/premium-minds/pm-webapp-utils/pull/24)
 - [Bump maven-javadoc-plugin from 3.1.1 to 3.2.0](https://github.com/premium-minds/pm-webapp-utils/pull/23)
 - [Bump log4j-core from 2.13.2 to 2.13.3](https://github.com/premium-minds/pm-webapp-utils/pull/25)
 - [[Security] Bump junit from 4.13 to 4.13.1](https://github.com/premium-minds/pm-webapp-utils/pull/26)
 - [Bump to junit 5](https://github.com/premium-minds/pm-webapp-utils/pull/27)

## [2.0]

### Changed 

 - Upgrade javax.servlet-api to 4.0.0
 - Upgrade log4j-core to 2.11.1
 - Upgrade logback-classic to 1.2.3
 - Upgrade javax.mail-api to 1.6.2
 - Upgrade gson to 2.8.5
 - Upgrade test-jetty-servlet (test scope) to 8.2.0.v20160908
 - Upgrade Java to 8
 
## [1.5]

### Fixed

 - [For some systems where attachment types cannot be automatically determined, added method to set each attachment's content type manually.](https://github.com/premium-minds/pm-webapp-utils/pull/6)
 - [Fix: Send attachments with correct filename, filetype and as attachment (not inline)](https://github.com/premium-minds/pm-webapp-utils/pull/5)

### Added

 - [send emails from SimpleMailer as text (default) or html](https://github.com/premium-minds/pm-webapp-utils/pull/4)
 
## [1.4]

### Fixed

 - [bcc missing in one constructor of simplerMailer](https://github.com/premium-minds/pm-webapp-utils/pull/3)
 - [bump to dependencies versions](https://github.com/premium-minds/pm-webapp-utils/commit/69d6b7a0c3f5cd53269fd6d12b5c4c59738df4d4)
   - javax.servlet:servlet-api:2.5 → javax.servlet:javax.servlet-api:3.1.0
   - log4j:log4j:1.2.16 → org.apache.logging.log4j:log4j-core:2.3
   - ch.qos.logback:logback-classic:1.0.13 → ch.qos.logback:logback-classic:1.1.3
   - javax.mail:mail:1.4.4 → javax.mail:javax.mail-api:1.5.4 
   - junit:junit:3.8.1 → junit:junit:4.12
   - org.eclipse.jetty:test-jetty-servlet:7.6.1.v20120215 → org.eclipse.jetty:test-jetty-servlet:8.1.17.v20150415
   
 
[unreleased]: https://github.com/premium-minds/pm-webapp-utils/compare/v2.0...HEAD
[2.0]: https://github.com/premium-minds/pm-webapp-utils/compare/v1.5...v2.0
[1.5]: https://github.com/premium-minds/pm-webapp-utils/compare/v1.4...v1.5
[1.4]: https://github.com/premium-minds/pm-webapp-utils/compare/v1.3...v1.4
