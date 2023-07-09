# Commons-Chain - Reloaded

Is a clone of <https://github.com/apache/commons-chain.git> - Branche `CHAIN_1_X`, aiming to bring Commons-Chain to a current technology.

Full [CHANGELOG](CHANGELOG.md)

For documentation see [https://weblegacy.github.io/commons-chain](https://weblegacy.github.io/commons-chain)

## Building Commons-Chain - Reloaded

### Prerequesits

* Apache Maven 3.5.4\+
* JDK 11\+

### Building-Steps

1. Clean full project  
   `mvn clean`
2. Build and test project
   `mvn verify`
   * to skip tests  
     add `-DskipTests` for example `mvn -DskipTests verify`
4. Generate source- and javadoc-artifacts  
   `mvn -DskipTests package`
5. Generate site-documentation  
   `mvn -DskipTests site`  
   or  
   `mvn -DskipTests clean site site:stage`
6. Publish site-documentation  
   1. `mvn -DskipTests clean site site:stage`
   2. `mvn scm-publish:publish-scm`
7. Generate Assemblies  
   `mvn -DskipTests package`
8. Deploy all artifacts to `Central-Repo`  
   * `mvn clean deploy` for SNAPSHOTs
   * `mvn clean deploy` for releases

### Support runs

* Set version number  
  `mvn versions:set -DnewVersion=...`
* Dependency Report  
  `mvn versions:display-dependency-updates versions:display-plugin-updates versions:display-property-updates`
