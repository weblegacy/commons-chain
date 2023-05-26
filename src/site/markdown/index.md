title: Overview
author: Commons Documentation Team <dev@commons.apache.org>
author: Martin Cooper <martinc@apache.org>
author: Craig McClanahan <craigmcc@apache.org>

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

## Commons Chain

A popular technique for organizing the execution of complex
processing flows is the "Chain of Responsibility" pattern, as
described (among many other places) in the classic "Gang of Four"
design patterns book. Although the fundamental API contracts
required to implement this design patten are extremely simple, it
is useful to have a base API that facilitates using the pattern,
and (more importantly) encouraging composition of command
implementations from multiple diverse sources.

Towards that end, the Chain API models a computation as a
series of "commands" that can be combined into a "chain". The API
for a command consists of a single method
(`execute()`), which is passed a "context" parameter
containing the dynamic state of the computation, and whose return
value is a boolean that determines whether or not processing for
the current chain has been completed (true), or whether
processing should be delegated to the next command in the chain
(false).

The "context" abstraction is designed to isolate command
implementations from the environment in which they are run (such
as a command that can be used in either a Servlet or Portlet,
without being tied directly to the API contracts of either of
these environments). For commands that need to allocate resources
prior to delegation, and then release them upon return (even if a
delegated-to command throws an exception), the "filter" extension
to "command" provides a `postprocess()` method for
this cleanup. Finally, commands can be stored and looked up in a
"catalog" to allow deferral of the decision on which command (or
chain) is actually executed.

To maximize the usefulness of the Chain of Responsibility
pattern APIs, the fundamental interface contracts are defined in
a manner with zero dependencies other than an appropriate JDK.
Convenience base class implementations of these APIs are
provided, as well as more specialized (but optional)
implementations for the web environment (i.e. servlets and
portlets).

Given that command implementations are designed to conform
with these recommendations, it should be feasible to utilize the
Chain of Responsibility APIs in the "front controller" of a web
application framework (such as Struts), but also be able to use
it in the business logic and persistence tiers to model complex
computational requirements via composition. In addition,
separation of a computation into discrete commands that operate
on a general purpose context allows easier creation of commands
that are unit testable, because the impact of executing a command
can be directly measured by observing the corresponding state
changes in the context that is supplied.

## Documentation

* The [Javadoc](./api-release/index.html) of the latest Release.
* The [Cookbook](./cookbook.html) containing "recipes" for using the chain.
* The [SVN repository](./source-repository.html) can be browsed.

## Downloading Chain

See the [Downloads](./downloads.html) page for current/previous
releases.

## Support

The [commons mailing lists](./mail-lists.html) act as the main support forum.
The user list is suitable for most library usage queries.
The dev list is intended for the development discussion.
Please remember that the lists are shared between all commons components,
so prefix your email by [chain].

Issues may be reported via [ASF JIRA](./issue-tracking.html).
