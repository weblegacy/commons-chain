# Commons-Chain - Reloaded

Is a clone of [Commons-Chain - Branche `CHAIN_1_X`](https://github.com/apache/commons-chain/tree/CHAIN_1_X), aiming to bring Commons-Chain to a current technology.

Full [CHANGELOG](CHANGELOG.md)

For documentation see [https://weblegacy.github.io/commons-chain](https://weblegacy.github.io/commons-chain)

## Building Commons-Chain - Reloaded

### Prerequesits

* Apache Maven 3.8.1\+
* JDK 9\+

### MAVEN-Profiles

* **apps** - Includes the example-apps into build
  * Adds the module `commons-chain-apps`

### Building-Steps

1. Clean full project  
   `mvn -Papps clean`
2. Build and test project
   * with example-apps  
     `mvn -Papps verify`
   * without example-apps  
     `mvn verify`
   * to skip tests  
     add `-DskipTests` for example `mvn -Papps -DskipTests verify`
4. Generate source- and javadoc-artifacts  
   `mvn -Papps -DskipTests package`
5. Generate site-documentation  
   `mvn -Papps site site:stage`
6. Publish site-documentation  
   1. `mvn clean verify`
   2. `mvn site site:stage`
   3. `mvn scm-publish:publish-scm`
7. Generate Assemblies  
   `mvn -Papps -DskipTests package`
8. Deploy all artifacts to `Central-Repo`  
   * `mvn clean deploy` for SNAPSHOTs
   * `mvn clean deploy` for releases

### Support runs

* Set version number  
  `mvn -Papps versions:set -DnewVersion=...`
* Dependency Report  
  `mvn -Papps versions:display-dependency-updates versions:display-plugin-updates versions:display-property-updates`
