# Changes

## 1.3 / YYYY-MM-DD

* Adapt JoCoCo-Report because the refactored commons-chain test-classes
* Refactoring commons-chain test-classes to reuse it in commons-chain-web
* Correct config-parameters of `japicmp-maven-plugin`
* Add missing default-constructor (JDK20-warning)
* Replace `maven-bundle-plugin` with `bnd-maven-plugin`
* Rename pom-property `encoding` to `sourceEncoding`
* SureFire-Report added
* Rename sub-modules to maven-standard
* Some doc-updates
* Remove unused dependency `commons-beanutils`
* Delete unused `Utilities`-class
* Split module `core` into `core` and `web`
* Adaptions for mulit-module-project
* Move `src` to new submodule `core`
* Update checkstyle from 10.12.1 to 10.12.2
* Update junit-jupiter-api from 5.10.0-RC1 to 5.10.0
* Update `README` and correct link in `CHANGELOG`
* Add `maven-scm-publish-plugin` and update `README`
* Downgrade `maven-fluido-skin` from 2.0.0-M6 to 1.11.2
* Downgrade `maven-site-plugin` from 4.0.0-M7 to 3.12.1 (Change-Report not correct)
* Add `Distribution-Management` in `POM`-File
* Delete `Issue-Link-Template`-Parameter in `POM`-File
* Add `LICENSE.txt` and `NOTICE.txt` to jars
* Corr: Add OSGi-Infos
* Update junit-jupiter-api from 5.10.0-M1 to 5.10.0-RC1
* Update checkstyle from 10.12.0 to 10.12.1
* Correct Clone-Link in README
* Add OSGi-Infos
* Update junit-jupiter-api from 5.9.3 to 5.10.0-M1
* Update maven-fluido-skin from 1.11.2 to 2.0.0-M6
* Update maven-site-plugin from 3.12.1 to 4.0.0-M7
* Update checkstyle from 10.10.0 to 10.12.0
* Correct site-url
* Correction in `cookbook.md`
* Change Javadoc
* Resolve `Checkstyle`-Messages with irrelevant API-break
* Optimize get-/set-locale-command with lambda expressions
* Resolve some `SpotBugs`
* Add final keyword
* Optimize portlet- and servlet-classes with lambda expressions
* tipfix in Javadoc
* Rename class `Unsafe` to `Utilities`
* Resolve some pmd messages
* Resolve some SpotBugs-messages
* Change back to more back-version-compatibility
* Config jar-plugin to generate a more detail `MANIFEST.MF`
* Add license and organization to POM
* Doc: Update links to GitHub
* Doc: Building only with `Maven`
* Move `proposal`- and `whiteboard`-doc to site
* Convert site-doc-files to md
* Delete old jdk-1.3-profile
* Delete old building-files
* Remove redundant calls to super()
* Remove useless null-check
* Resolve pmd messages
* Resolve all checkstyle-errors
* Update `commons-beanutils` from 1.8.3 to 1.9.4
* Use lazily instantiated Map of faces-cookies
* Add portlet-cookie-implementation
* Update `myfaces-api` 1.1.0 to `javax.faces-api` 2.3
* Update `commons-logging` from 1.1.1 to 1.2
* Update `servlet-api` from 2.3 to 4.0.1
* Update `portlet-api` from 1.0 to 3.0.1
* Update `commons-digester` from 1.8 to 2.1
* Correct removed surefire-parameter `forkMode`
* Update `junit-jupiter-api` from 5.9.2 to 5.9.3
* Update `maven-surefire-plugin` from 3.0.0-M7 to 3.0.0
* Update `maven-resources-plugin` from 3.2.0 to 3.3.1
* Update `maven-pmd-plugin` from 3.17.0 to 3.20.0
* Update `maven-jxr-plugin` from 3.2.0 to 3.3.0
* Update `maven-jar-plugin` from 3.2.2 to 3.3.0
* Update `maven-install-plugin` from 3.0.0-M1 to 3.1.1
* Update `maven-enforcer-plugin` from 3.1.0 to 3.3.0
* Update `maven-deploy-plugin` from 3.0.0-M2 to 3.1.1
* Update `maven-dependency-plugin` from 3.3.0 to 3.5.0
* Update `maven-compiler-plugin` from 3.10.1 to 3.11.0
* Update `maven-checkstyle-plugin` from 3.1.2 to 3.2.2
* Update `maven-assembly-plugin` from 3.4.1 to 3.5.0
* Update `spotbugs-maven-plugin` from 4.7.2.1 to 4.7.3.4
* Change `maven-default-skin` to `maven-fluido-skin`
* Make better config for `maven-javadoc-plugin`
* Replace `clirr-maven-plugin` with `japicmp-maven-plugin`
* Add `maven-pmd-plugin`
* Add `maven-jxr-plugin`
* Update dependencies because dependencies-analysis
* Add `maven-dependency-plugin`
* Add missing `maven-project-info-reports-plugin`
* Upgrade `checkstyle.xml` for current checkstyle-version 10.10.0
* Replace `findbugs-maven-plugin` with `spotbugs-maven-plugin`
* Replace `cobertura-maven-plugin` with `jacoco-maven-plugin`
* Resolve compiler-warnings
* Upgrade `site.xml`
* Correct JavaDoc-Errors
* Eclipse-CleanUp: Add @Override, remove unnecessary casts, optimize ifs, use extended for-loop, organize imports, trim lines,
* Correct clear and containsValue
* Rework and improve generics
* Use contains instead indexOf
* Remove unnecessary brackets
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
* Clone of <https://github.com/apache/commons-chain.git> - CHAIN_1_X
