# Changes

## 1.3 / YYYY-MM-DD

* Add missing Override-Annotation
* Optimize clear, containsValue, isEmpty, key and sessionExists
* Correction: Return null value if there are no JavaBeans properties
* Adaption to use `ConcurrentHashMap` because it dosen't accept null values
* Extend `ContextBase` from `ConcurrentHashMap` instead `HashMap`
* Clear all catalogs to reset it for JUnit-tests
* Move `overview.html` to maven-standard-path
* Rewrite all `package.html` to `package-info.java`
* Suppress warnings
* Optimize clear-methode
* Add generics to JUnit-Tests
* Correct JUnit-Test `ServletSetLocaleCommandTestCase`
* Upgrade to JUnit5
* Reformats JavaDocs from Test-Classes
* Reformating: JavaDoc-Code, remove HTML-P-Tag, delete empty lines, add deprecated- and
  override-annotations, change JavaDoc exception to throws, typos, remove unnecessary parentheses
* Move TestChain-Class from config to impl
* Set Java-Version to 8
* move test-resources to maven-standard-directory
* remove unnecessary test-resources-entry in POM
* maven-resources-plugin: Define encoding
* Centralize MAVAN-Plugins-Version with PluginManagement and add missing maven-plugins
* Add maven-enforcer-plugin: Enforces required MAVEN-Version
* Remove parent and add mailing-lists
* Remove MAVEN-Default-Plugin-Group (org.apache.maven.plugins)

## 1.3-SNAPSHOT / 2022-11-14

* Update `.gitignore`
* Add `README.md`
* Add `CHANGELOG.md`
* Clone of <https://github.com/weblegacy/commons-chain.git> - CHAIN_1_X
