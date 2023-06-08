title: Cookbook
author: Ted Husted <husted@apache.org>
author: Martin Cooper <martinc@apache.org>

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

## Introduction

The essence of computing might be that for any expected input (A),
we return the expected output (B). The challenge is getting from (A) to
(B). For a simple program, (A) to (B) might be a single transformation.
Say, shifting a character code 32 digits so that "a" becomes "A". In a
complex application, A to B can be a long and winding road.

We might need to confirm that the user is authorized to create (B)
from (A). We might need to find that (A) is valid input for (B). We might
need to convert (A) from another character set. We may need to insert a
preamble before writing (B). We may need to merge another resource with
(A) before creating (B). Meanwhile, if anything goes wrong during
processing, the error must be handled, and even logged. Some tasks might
be able to continue after a non-fatal error, or, if the error is fatal,
all processing might need to halt.

There are many different ways programmers organize processing logic
within an application. Often, the difference between an elegant
architecture and a muddled ball-of mud is how control flows from one
process to another. To realize and retain elegance, we must organize
complex, multi-step processes so that they are easy to discover and
change.

<!-- p><remark>[TODO:]</remark></p -->

## Separate "business" logic from "presentation" logic

_Problem:_ You want to cleanly separate the
execution and presentation layers without complicating the design of your
application.

_Solution:_ Use the
Chain of Responsibility and
Command patterns so that the presentation layer can
execute a command, or chain of commands, without needing to know how the
command is implemented.

_Discussion_: To be useful, most applications
need to run a process and then tell the client what happened. In practice,
we find mixing "running" and "telling" together creates code that can be
hard to test and maintain. If we can have one component run (or execute)
the process, and another component report (or present) the result, then we
can test, create, and maintain each component separately. But, how can we
cleanly separate the execution and presentation layers without
complicating the design of an application?

Most application frameworks, especially web application frameworks,
rely on the Command pattern. An incoming HTTP request is mapped to some
type of "command" object. The command object takes whatever action is
required, using information passed in the HTTP request.

<!-- p><remark><remark>[Glossary: "Chain of Responsibility pattern",
"Command pattern" entry]</remark></remark></p -->

<!-- figure>
  <title>Using information passed in the request, the Command object takes
  whatever action is required.</title>

  <olink><remark>[TODO: line art.]</remark></olink>
</figure -->

In practice, there are usually commands within commands. A Command
object in a web application often looks like a sandwich. First, it does
some things for the benefit of the presentation layer, then it executes
the business logic, and then it does some more presentation layer things.
The problem many developers face is how to cleanly separate the
business logic in the middle of a web command from
other necessary tasks that are part of the request/response
transaction.

<!-- p><remark><remark>[Glossary: "business logic" , "request/response
transaction" entry]</remark></remark></p -->

<!-- figure>
  <title>A Command object often looks like a business logic
  sandwich.</title>

  <olink><remark>[TODO: line art.]</remark></olink>
</figure -->

The _Chain of Responsibility_ package combines the
Command pattern with the classic Chain of Responsibility
pattern to make it easy to call a business command as part of
a larger application command. (For more about the patterns, see
_Design Patterns: Elements of Reusable Object Orientated
Software_ [ISBN 0-201-63361-2]).

<!-- p><remark><remark>[Glossary: "Chain of Responsibility pattern"
entry]</remark></remark></p -->

<!-- p><figure>
    <title>The Chain package makes it easy to call a business operation
    from within a larger transaction.</title>

    <olink>[TODO: line art]</olink>
  </figure></p -->

To implement the patterns, the `Chain` package
defines five key interfaces:

* Context
* Command
* Chain
* Filter
* Catalog

_Context._ A `Context`
represents the state of an application. In the Chain package,
`Context` is a marker interface for a
`java.util.Map`. The Context is an envelope
containing the attributes needed to complete a transaction. In other
words, a Context is a stateful object with member values.

_Command._ A `Command`
represents a unit of work. A Command has a single
entry method: `public boolean execute(Context
context)`. A Command acts upon the state passed to it through
a context object, but retains no state of its own. Commands may be
assembled into a Chain, so that a complex transaction can be created from
discrete units of work. If a Command returns `true`, then
other Commands in a Chain should _not_ be executed. If
a Command returns `false`, then other Commands in the Chain
(if any) may execute.

<!-- p><remark><remark>[Glossary: "unit of work"
entry]</remark></remark></p -->

_Chain._ `Chain` implements
the `Command` interface, so a
`Chain` can be used interchangeably with a
`Command`. An application doesn't need to know if
it's calling a Chain or a Command, so you can refactor from one to the
other. A Chain can nest other Chains as desired. This property is known as
the _Liskov substitution principle_.

<!-- p>_<remark>[Glossary: "Liskov substitution
principle" entry]</remark>_</p -->

_Filter._ Ideally, every command would be an
island. In real life, we sometimes need to allocate resources and be
assured the resources will be released no matter what happens. A
`Filter` is a specialized
`Command` that adds a
`postProcess` method. A
`Chain` is expected to call the
`postProcess` method of any filters in the chain
before returning. A Command that implements Filter can safely release any
resources it allocated through the `postProcess`
method, even if those resources are shared with other Commands.

_Catalog._ Many applications use "facades" and
"factories" and other techniques to avoid binding layers too closely
together. Layers need to interact, but often we don't want them to
interact at the classname level. A `Catalog` is a
collection of logically named Commands (or Chains) that a client can
execute, without knowing the Command's classname.

<!-- p><figure>
    <title>The five core interfaces: Context, Command, Chain, Filter, and
    Catalog.</title>

    <olink><remark>[TODO: UML for Context, Command, Chain, Filter, and
    Catalog.]</remark></olink>
 </figure></p -->

The rest of the chapter features recipes that will help you put the
Chain of Responsibility package to work in your own applications.

## Test a Command

_Problem:_ You want to start using
`Command` objects in your application.

_Solution:_ Use Test Driven
Development to create a test for a Command, and let the test
tell you how to write the Command. When the test passes, you will have a
working Command to integrate into your application.

<!-- p>_<remark>[Glossary: "Test Driven
Development" entry]</remark>_</p -->

_Discussion:_ Let's say we're working on an
application that maintains a "`Profile`" object for
each client. We need to change the state of the Profile during the
client's "session" with the application, which may span several requests.
Different application environments may preserve a Profile in different
ways. A web application may store a Profile as an attribute of the
HttpSession or as a client-side "cookie". An EJB application may maintain
a Profile as an attribute of the client's environment. Regardless, you
would like a single Command that can check to see if a client has a
Profile object, and, if not, create one. The Command does not know how the
application stores a Profile, or even if it is stored.

One reason we use Commands is because they are easy to test. In this
recipe, let's write a test for our Command. In another recipe, we will
create the corresponding Command. This approach is known as Test Driven
Development.

To test our Command, we can simply

1. Create a Context with a known state

2. Create a Command instance to test

3. Execute the Command, passing our Context

4. Confirm that our Context now contains the expected state

For the `Context`, we can use the
`ContextBase` class provided as part of the Chain
package. The `ProfileCheck` Command and Profile
object are shown in the next recipe. The remaining code for our
`TestProfileCheck` TestCase is shown as Example 1.

```java
package org.apache.commons.mailreader;

import junit.framework.TestCase;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.mailreader.commands.ProfileCheck;
import org.apache.commons.chain.mailreader.commands.Profile;
import org.apache.commons.chain.impl.ContextBase;

public class ProfileCheckTest extends TestCase {

   public void testProfileCheckNeed() {

        Context context = new ContextBase();
        Command command = new ProfileCheck();
        try {
            command.execute(context);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        Profile profile = (Profile) context.get(Profile.PROFILE_KEY);
        assertNotNull("Missing Profile", profile);

    }
```

Since we're using a test-first approach, we can't run or even
compile this class (yet). But we can use the test class to tell us which
other classes we need to write. The next recipe shows how to create a
Command.

## Create a Command

_Problem:_ You need to create a
`Command` for your application, so that a test of the
Command will succeed.

_Solution:_ Use the test to tell you what code
will realize the Command's _API
contract_.

<!-- p><remark>[Glossary: _"API contract_"
entry.]</remark></p -->

_Discussion:_ A key reason for using Commands,
and chains of Commands, is testability. Since Commands are designed to act
on whatever `Context` they receive, we can create a
Context with a known state to test our Command. In the preceding recipe,
we created a test for a `ProfileCheck` command. Let's
implement that Command so that it passes our test.

To pass the ProfileCheck test, we need to

1. Retrieve the Profile from the Context, using Profile.PROFILE_KEY
   as the attribute name.

2. If Profile is NULL, create a Profile and store it in the
   Context.

3. Return `false` or `true` to the
   caller.

Whether to return `false` or `true` at
step 3 is optional. You could choose to return `true`,
since this Command did check the profile. Or, you could decide to return
`false`, so that the Command can be used as part of a
Chain. The return value controls whether a chain terminates or continues.
True forces a chain to end. False allows a chain to continue. For now,
we'll just return `false`, so that our Command could be
used as part of a larger chain of Commands.

The code implementing our ProfileCheck Command is shown as Example
2.

```java
package org.apache.commons.chain.mailreader.commands;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

public class ProfileCheck implements Command {

    public Profile newProfile(Context context) { return new Profile(); }

    public boolean execute(Context context) throws Exception {
        Object profile = context.get(Profile.PROFILE_KEY);
        if (null == profile) {
            profile = newProfile(context);
            context.put(Profile.PROFILE_KEY, profile);
        }
        return false;
    }
}
```

To compile our Command and run our test, we also need a
`Profile` class. Example 3 shows the simplest
implementation of Profile that will pass our test.

```java
package org.apache.commons.chain.mailreader.commands; 
public class Profile { 
    public static String PROFILE_KEY = "profile"; 
}
```

Note that we used a separate method to create the Profile object. If
we buried a call to "new Profile()" in the Execute method, we could not
reuse our CheckProfile Command to create specialized Profiles. Using
helper methods to create objects is known as the
Factory pattern.

<!-- p><remark><remark>[Glossary: "Factory pattern"
entry]</remark></remark></p -->

We should now be able to compile all three classes and run our
test.

<!-- p><figure>
    <title>Green bar for org.apache.commons.mailreader.ProfileCheckTest
    [TODO: Screen capture]</title>

    <mediaobject>
      <imageobject>
        <imagedata fileref="???" />
      </imageobject>
    </mediaobject>
  </figure></p -->

## Create a Context

_Problem:_ You want a
`Context` that is
_type-safe_,
_encapsulated_, or interoperable
with components that expect JavaBean
properties.

<!-- p><remark>[Glossary entry: _type-safety_,
_encapsulate, JavaBean]_</remark></p -->

_Solution:_ Extend your Context class from
`ContextBase`, and add whatever JavaBean properties
you need.

_Discussion_: Many components already use a
"context". Each of the various Java Servlet "scopes" have a context
object. The Apache Velocity product relies
on a context object. Most operating systems have a list of simple
"environment" settings that is a "context". These examples all use a "map"
or "dictionary" style context. These contexts are a simple list of
entries, where each entry is a key and a value.

Other components also use what amounts to a context but predefine
the entries as object properties. The Apache Struts framework is one
example. Developers can define a JavaBean (or
"`ActionForm`") to act as the context for a request.
Some components mix both approaches. The Servlet request and session
objects expose a Map-style context along with several predefined
properties. Struts supports a variant of the ActionForm that utilizes a
Map.

Architects will often choose a Map-style context because they are
easy to implement and _very_ easy to extend. Usually,
developers can add their own entries to a Map-style context at will. Of
course, <!-- as illustrated by figure 6, --> every engineering decision is a
trade-off. Maps trade type-safety and encapsulation for flexibility and
extensibility. Other times, architects will decide to trade flexibility
for type-safety. Or, we may decide to trade extensibility for
encapsulation. Often, these decisions are driven by the need to
interoperate with other components that may expect either a Map or a
JavaBean.

<!-- figure>
  <title>Every engineering decision is a trade-off.</title>

  <olink>[:TODO: line art - (Do it right; Do it soon; Do it cheap: Choose
  any two.)]</olink>
</figure -->

The Apache Commons Chain of Command architects have chosen a
Map-style context as the default. The Chain Context is nothing but a
"marker interface" for the standard Java `Map`
interface.

```java
public interface Context extends Map {
}
```

However, to provide developers with type-safety,
encapsulation, and interoperability, Chain provides a sophisticated
`ContextBase` class that also supports JavaBean
properties.

If a developer declares a JavaBean property on a subclass of
ContextBase, this property is automatically used by the Map methods. The
Map `get` and `put` methods
of ContextBase introspect the subclass. If they find a JavaBean property
named after the key argument, the getter or setter method is called
instead.

This bit of wizardry enforces type-safety for any declared
properties, but developers can still use the context as if it were an
ordinary Map. If all needed attributes are defined as properties, then a
ContextBase can interoperate with components that expect a Map and also
with components that expect a JavaBean -- all at the same time. Everything
is transparent, and there are no special requirements for the
caller.

Let's create a test for a ContextBase subclass to prove the JavaBean
properties and Map methods are interoperable and type-safe.

To test the context for interoperability, we'll need to do four
things:

1. Assign a value to a typed property using a JavaBean
   setter

2. Retrieve the same value using the Map get method

3. Assign another value using the Map set method

4. Retrieve the update value using a JavaBean setter

To test the context for type-safety, we will also need to

1. Assign a `String` to a typed property
   using the Map get method

2. Confirm that the assignation throws a "type mismatch"
   exception

To write these tests, let's create a context with a
_Locale_ property for an application named
"MailReader". The code for our `LocaleValueTest` is
shown <!-- in Example 5 --> below.

```java
package org.apache.commons.mailreader;
import junit.framework.TestCase;
import junit.framework.Assert;
import org.apache.commons.chain.mailreader.MailReader;
import java.util.Locale;

public class LocaleValueTest extends TestCase {

    MailReader context;

    public void setUp() {
        context = new MailReader();
    }

    public void testLocaleSetPropertyGetMap() {
        Locale expected = Locale.CANADA_FRENCH;
        context.setLocale(expected);
        Locale locale = (Locale) context.get(MailReader.LOCALE_KEY);
        Assert.assertNotNull(locale);
        Assert.assertEquals(expected, locale);
    }

    public void testLocalePutMapGetProperty() {
        Locale expected = Locale.ITALIAN;
        context.put(MailReader.LOCALE_KEY, expected);
        Locale locale = context.getLocale();
        Assert.assertNotNull(locale);
        Assert.assertEquals(expected, locale);
    }

    public void testLocaleSetTypedWithStringException() {
        String localeString = Locale.US.toString();
        try {
            context.put(MailReader.LOCALE_KEY, localeString);
            fail("Expected 'argument type mismatch' error");
        } catch (UnsupportedOperationException expected) {
            ;
        }
    }
}
```

A `MailReader` Context object that passes
the LocaleValueTest is shown <!-- as Example 6 --> below.

```java
package org.apache.commons.chain.mailreader;
import org.apache.commons.chain.impl.ContextBase;
import java.util.Locale;

public class MailReader extends ContextBase {Prop
    public static String LOCALE_KEY = "locale";
    private Locale locale;
    public Locale getLocale() {
        return locale;
    }
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
```

The MailReader object <!-- in Example 6 --> above shows how much utility is
built into ContextBase class. All we had to do was define the property.
The base class took care of the rest. Of course, there is no free lunch.
ContextBase has to go through the bother of introspection to tell if an
attribute has a property or not. The ContextBase code is written to be
efficient, but if your application can just use a Map-style context, you
could use the leaner version of a MailReader context shown <!-- in Example
7 --> below.

```java
package org.apache.commons.chain.mailreader;
import org.apache.commons.chain.Context;
import java.util.Hashmap;

public class MailReader extends Hashmap implements Context {
    public static String LOCALE_KEY = "locale";
}
```

By extending the stock ContextBase subclass, or rolling your
own class with a HashMap, you can use whatever type of context is best for
your own artichtecture.

## Create a Catalog

_Problem:_ You want to layer your application
without creating dependencies on `Command` objects
that exist in different layers.

_Solution:_ Assign each command a logical name so
that it can be called from a "catalog". A catalog moves dependency on to
the logical name and away from the Java classname or classnames. The
caller has a dependency on the catalog but not on the actual Command
classes.

_Discussion:_ Context and Command objects are
usually used to join layers of an application together. How can one layer
call Commands in another layer without creating new dependencies between
the two layers?

Interlayer dependencies are a common dilemma in enterprise
applications. We want to layer our application so that it becomes robust
and cohesive, but we also need a way for the different layers to interact
with each other. The Commons Chain package offers a
`Catalog` object to help solve problems with
dependencies between layers, as well as between components on the same
layer.

A Catalog can be configured through metadata
(an XML document) and instantiated at application startup. Clients can
retrieve whatever `Commands` they need from the
Catalog at runtime. If Commands need to be refactored, new classnames can
be referenced in the metadata, with no changes to the application
code.

<!-- p><remark>[Glossary: Add "metadata"_
entry.]_</remark></p -->

Let's take a look at some code that uses a Catalog. <!-- Example 8 shows --> Shown below is
a method that executes a Command from a Catalog stored in a web
application's servlet context.

```java
   boolean executeCatalogCommand(Context context,
            String name, HttpServletRequest request) 
        throws Exception {

        ServletContext servletContext =
                request.getSession().getServletContext();  
        Catalog catalog =
                (Catalog) servletContext.getAttribute("catalog");
        Command command = catalog.getCommand(name);
        boolean stop = command.execute(context);
        return stop;
        
    }
```

<!-- p><remark>[Glossary: Add _"type-safety"_,
"_encapsulate", "JavaBean" entry.]_</remark></p -->

Notice that we only pass the name of a Command into the method. Also
note that we retrieve the Command and pass it the Context without knowing
the precise type of either object. All references are to the standard
interfaces.

<!-- Example 9 shows -->
Shown below is an XML document that can be used to create a
Catalog, like the one called in <!-- Example 8 --> the example above.

```xml
<?xml version="1.0" ?>
<catalog>
  <command 
    name="LocaleChange" 
    className="org.apache.commons.chain.mailreader.commands.LocaleChange"/>
  <command 
    name="LogonUser" 
    className="org.apache.commons.chain.mailreader.commands.LogonUser"/>
</catalog>
```

The application needs to know the name given to a Command we
want to execute, but it does not need to know the classname of the
Command. The Command could also be a `Chain` of
Commands. We can refactor Commands within the Catalog and make
zero-changes to the application. For example, we might decide to check for
a user profile before changing a user's locale setting. If we wanted to
make running a `CheckProfile` Command part of
"LocaleChange", we could change the Catalog to make "LocaleChange" a
Chain. <!-- Example 10 --> The following example shows Catalog metadata where "LocaleChange" is a Chain.

```xml
<catalog>
  <chain name="LocaleChange">
    <command 
      className="org.apache.commons.chain.mailreader.commands.ProfileCheck"/>
    <command 
      className="org.apache.commons.chain.mailreader.commands.LocaleChange"/>
  </chain>
  <command 
    name="LogonUser" 
    className="org.apache.commons.chain.mailreader.commands.LogonUser"/>
</catalog>
```

In the "Create a Command" recipe, we use a factory method to create
a "Profile" object. If we subclass that Command to create a specialized
Profile, we can cite the new classname in the Catalog, with zero changes
to the rest of the application.

Being able to make quick and easy changes to an application can have
a big effect on the bottom line. The recurring, annual maintenance cost of
applications can range between 25% to 50% of the initial development cost
(Gartner Group, May 2002).

## Load a Catalog From a Web Application

_Problem:_ You'd like to load a catalog
automatically when a web application starts.

_Solution:_ Utilize the
`ChainListener` bundled with the Commons Chain of
Responsibility Package.

_Discussion:_ A Catalog can be created
progmatically, using conventional Java statements, or by specifying the
catalog members as metadata (an XML document). For testing, it can be
easiest to create a catalog programatically. For deployment, catalogs are
much easier to maintain as metadata. The downside of using metadata is
that it needs to be parsed so that the specified objects can be created.
Happily, the Commons Chain of Responsibility package comes bundled with a
Listener that can read a Catalog metadata file and create the
corresponding object graph.

To use ChainListener in a web application, just add a reference to
your application's web.xml (yet another metadata document). One such
reference is shown <!-- as Example 11 --> below.

```xml
<!-- Commons Chain listener to load catalogs  -->
<context-param>
  <param-name>org.apache.commons.chain.CONFIG_CLASS_RESOURCE</param-name>
  <param-value>resources/catalog.xml</param-value>
</context-param>
<listener>
  <listener-class>org.apache.commons.chain.web.ChainListener</listener-class>
</listener>
```

The elements in <!-- Example 11 --> this example expect that there is a
"catalog.xml" file stored on the application's classpath under a directory
named "resources". Usually, this would mean that there is a "resources"
directory under "WEB-INF/classes". If you are using Maven to build your
application, Maven can copy metadata files from your source tree to the
web infrastructure tree automatically. Many teams do the same with custom
Ant build files. <!-- Example 12 shows --> Shown below is a fragment of a Maven properties file
that copies `catalog.xml` from a directory under
"`src/resources/chain`" to
"`/WEB-INF/classpath/resources`" under the web
deployment directory.

```xml
<!-- ... -->

<build>
  <sourceDirectory>src/java</sourceDirectory>
  <resources>
    <resource>
      <directory>${basedir}/src/resources/chain</directory>
      <targetPath>resources</targetPath>
      <includes>
        <include>catalog.xml</include>
      </includes>
    </resource>
  </resources>
</build>

<!-- ... -->
```

You can also configure ChainListener to read files from a system path
or from a JAR. See the JavaDoc for all the configuration details. There
is also a `ChainServlet` if you are using the Servlet 2.2
platform.

Using the default attribute, and given an
`HttpServletRequest` instance, you can access the
catalog by coding:

```java
Catalog catalog = (Catalog) request.getSession()
        .getServletContext().getAttribute("catalog");
```

Given the catalog, you can execute a command and pass it a context,
like so:

```java
Command command = catalog.getCommand(commandName);  
    boolean stop = command.execute(context);
```

Of course, the hard part is populating the context and determining
which command we need to run for a given request. That work is often left
to a Front Controller, like the one implemented by Apache Struts.
Accordingly, we include a "Call a Command from Struts" recipe in this
chapter. If you like Controllers, but don't like Struts, there are also
"Create a Controller" and "Call a Command from a Servlet" recipes.

## Call a Command From Struts

_Problem:_ You'd like to call Commands from
within a Struts application.

_Solution:_ Use a CommandAction to call a Command
named for your ActionForm.

_Discussion:_ As a Front Controller, the Apache
Struts web application framework has three primary responsibilities.

1. Validate a user request

2. Process a user request

3. Create a response to the request

The third item is usually delegated to a server page.
Struts provides framework-aware components, like JSP tag libraries, to
encourage developers to use another resource to create the response. In
this way, Struts needs only to select the resource. The actual response
creation is handled elsewhere.

Struts also bundles a component to help validate the user request.
The Struts Validator utilizes metadata to vet request values and create
user prompts should validation fail.

To discharge its responsibility to "Process a user request", Struts
provides an extension point called the "`Action`"
class. The Struts Action is a blank slate where developers can do whatever
is necessary to process the request. Some developers even make JDBC calls
from Actions, but such practices are discouraged. The Struts best practice
is for Actions to delegate business and system logic calls to another
component, such as a business facade. The Struts
Action passes appropriate values to one or methods on the facade. The
outcome is used to determine an appropriate response. Often, the outcome
of an Action is described as either "success " or "failure".

<!-- p><remark>[Glossary: "Business facade" entry]</remark></p -->

Aside from the blank Action, Struts distributes several "standard"
Actions, such as the `DispatchAction`. The standard
Actions are designed to be used several times in different ways within an
application. To allow reuse of Actions, Struts provides a
Decorator class called an
`ActionMapping`. Runtime details can be specified
through the `ActionMappings`, so that each usage of a
standard Action can be slightly different.

To solve the problem of calling a Command from Struts, we can use a
standard Action to retrieve the Catalog and call the Command. We can
specify runtime details in the ActionMapping. Our details include which
set of validations to pass and which Command to
run.

In practice, the set of validations we need to pass and the command
we need to run are closely coupled. In fact, it can be a good practice to
create a distinct set of validations for each Command. If a Command
changes, then its validations can change with it, without affecting other
Commands.

In Struts, the set of validations is coupled to the ActionForm name.
The ActionForm name is a logical identifier, separate from the ActionForm
classname. When you use the Struts Validator, the "form" name for the
Validations is the same string as the ActionForm "name" specified by the
ActionMapping. A database guru would call this a 1:1 relation; the
Validator form name and the ActionForm name are shared keys. If we want
each Command to have its own set of validations, and it's own
ActionMapping, it follows that we should use the same "key" throughout.
The Command name can be the ActionForm name as well as the Validator form
name.

<!-- Example 13 -->
The following example shows how the names line up in the three metadata files,
the catalog.xml, the validation.xml, and the struts-config.xml. The token,
or "key", that links the three files together is "LocaleChange"

```xml
<!-- catalog.xml -->
<?xml version="1.0" ?>
<catalog>
    <command
        name="<em>LocaleChange</em>"
        className="org.apache.commons.chain.mailreader.commands.LocaleChange" />
</catalog>

<!-- validation.xml -->
<?xml version="1.0" ?>
<!DOCTYPE form-validation PUBLIC
    "-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.1//EN"
    "http://jakarta.apache.org/commons/dtds/validator_1_1.dtd">
<form-validation>
    <formset>
        <form name="<em>LocaleChange</em>">
            <field property="language" depends="required">
                <arg0 key="prompt.language"/>
            </field>
        </form>
    </formset>
</form-validation>

<!-- struts-config.xml -->
<?xml version="1.0" ?>
<!DOCTYPE struts-config PUBLIC
          "-//Apache Software Foundation//DTD Struts Configuration 1.2//EN"
          "http://jakarta.apache.org/struts/dtds/struts-config_1_2.dtd">
<struts-config>
    <form-beans>
        <form-bean
            name="<em>LocaleChange</em>"
            type="org.apache.struts.validator.DynaValidatorForm">
           <form-property name="language" type="java.lang.String"/>
           <form-property name="country" type="java.lang.String"/>
         </form-bean>
     </form-beans>
    <action-mappings>
        <action path="/LocaleChange"
            name="<em>LocaleChange</em>"
            type="org.apache.commons.chain.mailreader.struts.CommandAction">
        <forward name="success" path="/Welcome.do" />
        </action>
    </action-mappings>
<struts-config>
```

In <!-- Example 13 --> the above example, we used "LocaleChange" for the Command name,
the validation Form name, and the Action form-bean name. To trigger the
thread, all we need to do is define a generic Action that will use the
form-bean name as the Command name. <!-- Example 14 --> The example below shows our
`CommandAction`.

```java
package org.apache.commons.chain.mailreader.struts;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.web.servlet.ServletWebContext;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;as the ActionForm name.
import org.apache.struts.action.ActionMapping;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommandAction extends Action {

    protected Command getCommand(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        Catalog catalog = (Catalog) request.getSession()
                .getServletContext().getAttribute("catalog");
        String name = mapping.getName();
        Command command = catalog.getCommand(name);
        return command;
    }

    protected Context getContext(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        ServletContext application = request.getSession()
                .getServletContext();
        Context context = new ServletWebContext(
                application, request, response);
        return context;
    }

    protected static String SUCCESS = "success";

    protected ActionForward findLocation(ActionMapping mapping,
            boolean stop) {
        if (stop) return mapping.getInputForward(); // Something failed
        return mapping.findForward(SUCCESS);
    }

    public ActionForward execute(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {
        Command command = getCommand(mapping, form, request, response);
        Context context = getContext(mapping, form, request, response);
        boolean stop = command.execute(context);
        ActionForward location = findLocation(mapping, stop);
        return location;
    }
```

The entry point to an Action class is the
`execute` method. Our execute method calls
`getCommand` and
`getContext` methods that we have defined to obtain
the Command from the catalog and to build a Context based on the web
request. Keeping life simple, we use the
`ServletWebContext` bundled with Commons Chain.
Depending on your needs, you might want to define your own specialized
Context. (See "Create a Context" Recipe.) Our execute method then calls the
command's `execute` method. We pass the return
value of command.execute to our `findLocation`
method, which determines "success" or "failure".

Another way to write CommandAction would be to use the ActionMapping
"`parameter`" property to indicate the Command name .
To do that, we'd patch `getCommand` to call
`mapping.getParameter()` instead of
`getName()`, like this:

```patch
  -     String name = mapping.getName();
  +     String name = mapping.getParameter();
```

(The minus sign means remove, or subtract, the line. The plus sign
means insert, or add, the line. The Unix patch program follows this
format.)

<!-- p><remark>[Glossary: "patch" entry]</remark></p -->

The "parameter" approach in the preceding example lets us name the
form-beans independently of the Command name. But, a consequence is that
we have to specify the Command name for each ActionMapping.
(_Bor-ring!_) You could also merge the two approaches
and return the parameter property only when it is used, like
this:

```patch
        String name = mapping.getParameter();
  +     if ((null==name) || (name=="")) name = mapping.getName();
```

Or you could mix and match the two approches, using CommandAction
when the formbean name and the command name match, and a
CommandParameterAction, when they do not. Struts allows you to use as many
Actions, and standard Actions, as you like.

Note that our Command is expected to do the "custom" work usually
delegated to the Action. Consequently, we do not need to create an Action
subclass for each task. We can use one or two standard Actions and have
them call the appropriate Command class. A set of related tasks (or
"story") might share an ActionForm class and a Command class, but, most
often, the Actions can be standard, reusable Actions.

Something else to note about <!-- Example 14 --> the above example is that we use the
"LocaleChange" token as the `path` attribute. This
means that the story would be trigged by opening (for example) the
"`/LocaleChange.do`" page. Even so, the path is
_not_ part of our semantic
chain. The path is not a fully logical name that we control.
The path token is shared with the container, and the container may have
its own constraints on the path. (JAAS pattern matching, for example.) The
path can't be part of our chain of keys, since it is shared with the
container's "business logic".

<!-- p><remark>[Glossary: _Add "Semantic chain pattern"_
entry.]</remark></p -->

Having used "LocaleChange" for everything else, using it for the
path token seems natural. Most of us would do the same. But, the path can
vary as needed, without upsetting the rest of the semantic chain. If the
"path" needs to change to suit a change in the JAAS configuration, nothing
else needs to change.

Of course, there would be several other ways to call a Command from
a Struts Action. Since the request is passed to the Action, it's easy to
obtain a Catalog stored in application scope. Once you have access to the
Catalog, the rest is easy.

Other frameworks, like WebWorks and Maverick, have components
similar to Struts Actions. Any of these components can be used to create a
Context, access the Catalog, and execute a Command.

## Create a Controller

_Problem:_ You want to base your application's
Controller components on the Commons Chain of
Command package.

<!-- p><remark>[Glossary: _Add "Controller pattern"_
entry, along with Application Controller and Front
Controller.]</remark></p -->

_Solution:_ Create a set of interfaces for a
Controller package that can be implemented using base classes from the
Chain of Command package.

_Warning: Since we are creating a base package, this recipe
is longer than most. Each individual component is simple enough, but there
are several components to cover. Since the components are interrelated,
covering them separately would be confusing. So, sit back, loosen your
belt, and enjoy, while we whip up a "seven-course meal"._

_Discussion:_ Many applications use
implementations of the Controller pattern to field user requests.
_Core J2EE Patterns: Best Practices and Design
Strategies_ [ISBN 0-13-142246-4] describes a controller as a component that
"interacts with a client, controlling and managing the handling of each
request." There are several flavors of controllers, including Application
Controllers and Front Controllers. Many web application frameworks, like
Apache Struts, utilize a Front Controller.

Often, an implementation of the Controller pattern will in turn use
the Command pattern or Chain of Command pattern. How can we use the
Commons Chain of Command package to implement a Controller?

Following the general description from Core J2EE Patterns, let's
start by defining a test that passes a request to a controller and
confirms that an appropriate response is returned.

To write our test, we need to:

1. Create a Controller.

2. Add a Handler for our Request to the Controller.

3. Create a Request and pass it to the Controller.

4. Confirm that the Request returns the expected Response.

To simplify writing the test, lets make a few executive
decisions:

1. The Request and Response object have "name" properties.

2. The name of a Response matches the name of its Request (a shared
   key).

3. The test will be based on interfaces; implemented classes will
   extend Commons Chain members.

4. The Controller extends Catalog.

5. The Request and Response extend Context.

6. The Request Handler extends Command.

7. For no particular reason, we'll call our controller package
   "Agility".

<!-- Example 15 -->
The following example shows a `ProcessingTest` class with
our `testRequestResponseNames` method.

```java
package org.apache.commons.agility;

import junit.framework.TestCase; 
import org.apache.commons.agility.impl.ControllerCatalog;
import org.apache.commons.agility.impl.HandlerCommand;
import org.apache.commons.agility.impl.RequestContext;

public class ProcessingTest extends TestCase {

    public void testRequestResponseName() {
        
        String NAME = "TestProcessing"; 

        Controller controller = new ControllerCatalog();

        RequestHandler handler = new HandlerCommand(NAME);
        controller.addHandler(handler);
        Request request = new RequestContext(NAME);
        controller.process(request);
        Response response = request.getResponse();

        assertNotNull(response);
        assertEquals(NAME, response.getName());
    }
}
```

To compile the ProcessingTest class, we will need interface
members for `Controller`,
`RequestHandler`, `Request`, and
`Response`, and class members for
`ControllerCatalog`,
`HandlerCommand`, and
`RequestContext`. <!-- Figure 7 shows the UML for the four
interfaces.-->

<!-- figure>
    <title>The four interfaces needed to realize ProcessingTest</title>

    <olink><remark>[TODO: UML for Controller, RequestHandler, Request, and
    Response ]</remark></olink>
</figure -->

<!-- Referring to the UML in Figure 7, we can write the code for our
interface members, as shown in Example 16. -->

```java
// Controller.java
package org.apache.commons.agility;
public interface Controller {
    void addHandler(RequestHandler handler);
    RequestHandler getHandler(String name) throws ProcessException;
    void process(Request request) throws ProcessException;
}

// Request.java
package org.apache.commons.agility;
public interface Request {
    String getName();
    Response getResponse();
    void setResponse(Response response);
}

// Response.java
package org.apache.commons.agility;
public interface Response {
    String getName();
}

// RequestHandler.java
package org.apache.commons.agility;
public interface RequestHandler {
    String getName();
    void handle(Request request) throws ProcessException;
}

// ProcessException.java
package org.apache.commons.agility;
public class ProcessException extends Exception {
    public ProcessException(Throwable cause) {
        super(cause);
    }
}
```

With the interfaces out of the way, we can turn to the classes we
need to implement.<!-- The UML for the class members we need is shown as
Figure 8. -->

<!-- figure>
    <title>The thee classes needed to realize ProcessingTest.</title>

    <olink><remark>[TODO: UML for ControllerCatalog, RequestContext, and
    HandlerCommand.]</remark></olink>
</figure -->

If we create the classes <!-- indicated by Figure 8 -->, and stub-out the
methods, we can get the code to compile. The test will run, but skeleton
classes won't pass muster. Let's implement each class, starting with
HandlerCommand, which is shown <!-- as Example 17 --> below.

```java
package org.apache.commons.agility.impl;

import org.apache.commons.agility.ProcessException;
import org.apache.commons.agility.Request;
import org.apache.commons.agility.RequestHandler;
import org.apache.commons.agility.Response;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

public class HandlerCommand implements Command, RequestHandler {
    String name =  null;

    public HandlerCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean execute(Context context) throws Exception {
        handle((Request) context);
        return true;
    }

    public void handle(Request request) throws ProcessException {
        try {
            String name = request.getName();
            Response response = new ResponseContext(name);
            request.setResponse(response);
        } catch (Exception e) {
            throw new ProcessException(e);
        }
    }
}
```

The `handle(Request)` method of
HandlerCommand realizes the prime responsibility for this class: create a
Response for the Request. The execute(Context) method is an
adapter that delegates to the handle method. Now we
can call execute or handle and achieve the same result. The constructor
assigns each instance of HandlerCommand a name so that it can be matched
with a Request.

<!-- p><remark><remark>[Glossary: "apapter pattern"
entry]</remark></remark></p -->

The handle(Request) method shown here is not very useful. However,
it will pass our test and prove the infrastructure is working. Subclasses
can override handle(Request) to create the appropriate Response for a
given Request. Since HandlerCommands are still Commands, we can itemize
our HandlerCommand subclasses as metadata (an XML document). This will
make it easy to handle new Requests as our application grows.

The HandlerCommand class creates a ResponseContext instance and sets
it as the Response. The ResponseContext class is shown <!-- as Example
18 --> below.

```java
package org.apache.commons.agility.impl;

import org.apache.commons.agility.Response;
import org.apache.commons.chain.impl.ContextBase;

public class ResponseContext extends ContextBase implements Response {

    private String name;

    public ResponseContext(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

Since we're just testing the infrastructure, our
ResponseContext is rudimentary. A Front Controller for a web application
framework might define several attributes for a Response, such as the
location of a server page. The RequestHandler can create any kind of
Response object that might be needed.

Whatever RequestHandlers we need are added to the Catalog, either as
metadata or programatically. Our tests add the handler programatically, so
we need to implement the AddHandler method. <!-- Example 19 --> The following shows our
implementation of CatalogController.

```java
package org.apache.commons.agility.impl;

import org.apache.commons.agility.Controller;
import org.apache.commons.agility.ProcessException;
import org.apache.commons.agility.Request;
import org.apache.commons.agility.RequestHandler;
import org.apache.commons.chain.impl.CatalogBase;
import org.apache.commons.chain.Command;

public class ControllerCatalog extends CatalogBase implements Controller {
    public RequestHandler getHandler(String name) {
        return (RequestHandler) getCommand(request.getName());
    }

    public void addHandler(RequestHandler handler) {
        this.addCommand(handler.getName(), (Command) handler);
    }

    public void process(Request request) throws ProcessException {
        Handler handler = getHandler(request.getName());
        if (handler != null) handler.handle(request);
    }
}
```

The main entry point to our Controller is the
`process(Request)` method. This method could host a
great deal of functionality. We could even implement the process method as
a series of Commands or Chains of Commands. An application could then
fine-tune the request processing by specifying different Commands in a
metadata catalog. The Struts web application framework uses this approach
for its request processor.

But for now, we just want to pass our test. All the process method
needs to do is find the RequestHandler and call its handle(Request)
method. We can do that just by looking up the name of the Request in our
catalog and retrieving the matching RequestHandler (or Command).

The `addHandler(RequestHandler)` method is
another adapter that delegates to an inherited method. In this case,
addHandler calls `addCommand(String,Command)`.
Since our RequestHandlers are Commands, they can be passed to the
superclass method. The `getHandler(String)` method
is yet another adapter/delegate.

Last but not least is the RequestContext class, shown <!-- as Example
20 --> below.

```java
package org.apache.commons.agility.impl;

import org.apache.commons.agility.Request;
import org.apache.commons.agility.Response;
import org.apache.commons.chain.impl.ContextBase;

public class RequestContext extends ContextBase implements Request {

    private String name;
    private Response response;

    public RequestContext(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
```

Like the ResponseContext, an application could add several
properties to its Request class. A web application might wrap or transfer
attributes from the HttpServletRequest. But so long as the class
implements Request and Context, it will plug into our Controller
implementation.

Using the interfaces and base classes shown here, you can create
whatever Controllers you need.

## Call a Command From a Servlet

_Problem:_ You would like to call commands during
your application using a servlet.

_Solution:_ Use the Listener from the "Load a
Catalog from a Web Application" recipe to setup a list of Commands, and
the Controller from the "Create a Controller" recipe to process the
request.

_Discussion: [TODO:]_
