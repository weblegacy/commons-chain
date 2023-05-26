title: Building
author: Commons Documentation Team <dev@commons.apache.org>

<!--
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.
-->

## Overview

Commons Chain uses [Maven 2](https://maven.apache.org) or
[Ant](https://ant.apache.org) as a build system.

Chain 1.2 requires a minimum of JDK 1.3 to build, although the Maven 2 build
requires JDK 1.4+

## Maven 2 Goals

The following **_Maven 2_** commands can be used to build Chain:

* `mvn clean` - clean up
* `mvn test` - compile and run the unit tests
* `mvn site` - create the documentation
* `mvn package` - build the jar
* `mvn install` - build the jar and install in local maven repository
* `mvn site assembly:assembly` - Create the source and binary distributions

## Ant Goals

The following **_Ant_** commands can be used to build Chain:

* `ant clean` - clean up
* `ant test` - compile and run the unit tests
* `ant javadoc` - create javadocs
* `ant jar` - build the jar
* `ant dist` - Create the source and binary distributions
