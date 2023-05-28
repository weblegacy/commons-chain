# Proposal for _Chain of Responsibility_ Package

## (0) Rationale

A popular technique for organizing the execution of complex
processing flows is the "Chain of Responsibility" pattern, as
described (among many other places) in the classic "Gang of Four"
design patterns book. Although the fundamental API contracts
required to implement this design patten are extremely simple, it
is useful to have a base API that facilitates using the pattern,
and (more importantly) encouraging composition of command
implementations from multiple diverse sources.

Towards that end, the proposed API models a computation as a
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
portlets). However, conditional compilation in the build script
allows graceful creation of the underlying API JAR file even in
the absence of the optional dependencies.

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

## (1) Scope of the Package

The fundamental API contracts of the Chain of Responsibility
pattern are encapsulated in the following interfaces in package
`org.apache.commons.chain`:

* **Command** - An individual unit of work whose
  `execute()` method is called to perform that
  work.
* **Chain** - A set of commands to which work
  will be delegated, in a well-defined order, until one of the
  commands indicates that work on a request has been completed.
  Note that a `Chain` is itself a
  `Command`, so arbitrarily complex hierarchies of
  execution may be composed.
* **Filter** - A specialized
  `Command` that requires any `Chain` that
  executes it to promise a later call to the
  `postprocess()` method if its `execute()`
  method was ever called, even in the face of exceptions being
  thrown by subsequently called commands.
* **Context** - A container for attributes and
  properties that represent the dynamic state of the computation
  being performed by a `Command` or
  `Chain`. A `Context` instance is passed
  to the `execute()` method of each
  `Command`, which allows `Command`
  instances to be easily shared in a multithread
  environment.
* **Catalog** - A collection of named
  `Command`s (or `Chain`s) that can be used
  to symbolically identify a computation to be performed.

In addition to the fundamental API contracts described above,
additional packages are provided (some of them optional based on
the availability of the corresponding APIs at compile time):

* **org.apache.commons.chain.impl** -
  Convenience base class implementations of the fundamental API
  contracts.
* **org.apache.commons.chain.generic** -
  Implementations of `Command` that are completely
  generic across any execution environment.
* **org.apache.commons.chain.config** -
  Implementation of XML parsing rules (via the use of Commons
  Digester) so that a `Catalog` instance can be
  populated with `Command` and `Chain`
  instances configured from an XML document. Optional, compiled
  only if commons-digester.jar is available.
* **org.apache.commons.chain.web** - Abstract
  implementation of `Context` that represents the
  fundamental characteristics of request, session, and
  application scope objects in a web application environment,
  without being tied specificaly to the Servlet or Portlet APIs.
  These characteristics are exposed under property names that are
  identical to the "implicit variables" of the expression
  language that is defined by JSTL 1.0 and JSP 2.0.
* **org.apache.commons.chain.web.faces** - Concrete
  implementation of `WebContext` for the JavaServer
  Faces API. Optional, compiled only if the JavaServer Faces API
  classes are available.
* **org.apache.commons.chain.web.portlet** - Concrete
  implementation of `WebContext` for the Portlet API.
  Optional, compiled only if the Portlet API classes are
  available.
* **org.apache.commons.chain.web.servlet** - Concrete
  implementation of `WebContext` for the Servlet API.
  Optional, compiled only if the Servlet API classes are
  available.

Over time, it is expected that additional generic commands and
specialized contexts will be developed for specific requirements.
However, conditional compilation capabilities in the build script
should be maintained so that a user of commons-chain need only
provide the APIs that he or she cares about. Likewise, for
maximum reuse, command implementations should be based on the
`org.apache.commons.chain.Context` API, rather than a
more specialized implementation class, if at all possible.

### (1.5) Interaction With Other Packages

_Chain_ relies on:

* Java Development Kit (Version 1.2 or later)
* Commons BeanUtils (version 1.6 or later). OPTIONAL,
  required only to use the
  `org.apache.commons.chain.config` package.
* Commons Collections (version 1.0 or later). OPTIONAL,
  required only to use the
  `org.apache.commons.chain.config` package.
* Commons Digester (version 1.3 or later). OPTIONAL, required
  only to use the `org.apache.commons.chain.config`
  package.
* Commons Logging (version 1.0.3 or later). OPTIONAL,
  required only to use the
  `org.apache.commons.chain.config` package and
  to build and use the
  `org.apache.commons.chain.web.servlet.config`
  package.
* JavaServer Faces API (version 1.0 or later). OPTIONAL,
  required only to use the
  `org.apache.commons.web.faces` package.
* Portlet API (version 1.0 or later). OPTIONAL, required only
  to use the `org.apache.commons.web.portlet`
  package.
* Servlet API (version 2.2 or later). OPTIONAL, required only
  to use the `org.apache.commons.web.servlet`
  package.

## (2) Initial Source of the Package

This package represents a new approach to the Chain of
Responsibility pattern, and the initial source is provided by
Craig R. McClanahan. It was inspired by ideas from many sources
-- in particular, the notion of a Chain being a Command was
copied from the way that handlers are described in Axis.

## (3) Required Jakarta-Commons Resources

* CVS Repository - New directory `chain` in the
  `jakarta-commons` CVS repository.
* Mailing List - Discussions will take place on the general
  _dev@commons.apache.org_ mailing list. To help
  list subscribers identify messages of interest, it is suggested
  that the message subject of messages about this component be
  prefixed with [Chain].
* Bugzilla - New component "Chain" under the "Commons"
  product category, with appropriate version identifiers as
  needed.

## (4) Initial Committers

The initial committers on the Chain component shall
be:

* Craig R. McClanahan
* Ted Husted
* TBD
