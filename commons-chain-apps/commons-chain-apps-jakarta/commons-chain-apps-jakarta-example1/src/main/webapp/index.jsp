<!--

 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to you under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

-->
<%@ page language="java"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
  <head>
    <title>Chain Example Index Page</title>
  </head>
  <body>

    <h1>Chain Example Index Page</h1>

    <ul>
      <li>Path Info Example</li>
      <li>
        <ul>
          <li><a href="exec/foo">exec/foo</a></li>
          <li><a href="exec/bar">exec/bar</a></li>
        </ul>
      </li>
      <li>Request Parameter Example</li>
      <li>
        <ul>
          <li><a href="execute?command=foo">execute?command=foo</a></li>
          <li><a href="execute?command=bar">execute?command=bar</a></li>
        </ul>
      </li>
      <li>Servlet Path</li>
      <li>
        <ul>
          <li><a href="foo.execute">foo.execute</a></li>
          <li><a href="bar.execute">bar.execute</a></li>
        </ul>
      </li>
    </ul>

    <table border="1">
       <tr><th>Example</th><th>Command</th><th>Count</th></tr>
       <tr><td rowspan="2" valign="top">Path Info</td><td>/foo</td><td><c:out value="${pathinfoFooCount}" default="0"/></td></tr>
       <tr><td>/bar</td><td><c:out value="${pathinfoBarCount}" default="0"/></td></tr>
       <tr><td rowspan="2" valign="top">Request Parameter</td><td>foo</td><td><c:out value="${reqparamFooCount}" default="0"/></td></tr>
       <tr><td>bar</td><td><c:out value="${reqparamBarCount}" default="0"/></td></tr>
       <tr><td rowspan="2" valign="top">Servlet path</td><td>/foo.execute</td><td><c:out value="${servletpathFooCount}" default="0"/></td></tr>
       <tr><td>/bar.execute</td><td><c:out value="${servletpathBarCount}" default="0"/></td></tr>
    </table>

  </body>
</html>