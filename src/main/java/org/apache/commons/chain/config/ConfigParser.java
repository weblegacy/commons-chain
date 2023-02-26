/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.chain.config;

import java.net.URL;
import org.apache.commons.chain.Catalog;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;

/**
 * Class to parse the contents of an XML configuration file (using
 * Commons Digester) that defines and configures commands and command chains
 * to be registered in a {@link Catalog}. Advanced users can configure the
 * detailed parsing behavior by configuring the properties of an instance
 * of this class prior to calling the {@code parse()} method. It
 * is legal to call the {@code parse()} method more than once, in order
 * to parse more than one configuration document.
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public class ConfigParser {

    // ----------------------------------------------------- Instance Variables

    /**
     * The {@code Digester} to be used for parsing.
     */
    private Digester digester = null;

    /**
     * The {@code RuleSet} to be used for configuring our Digester
     * parsing rules.
     */
    private RuleSet ruleSet = null;

    /**
     * Should Digester use the context class loader?
     */
    private boolean useContextClassLoader = true;

    // ------------------------------------------------------------- Properties

    /**
     * Return the {@code Digester} instance to be used for
     * parsing, creating one if necessary.
     *
     * @return A Digester instance.
     */
    public Digester getDigester() {
        if (digester == null) {
            digester = new Digester();
            RuleSet ruleSet = getRuleSet();
            digester.setNamespaceAware(ruleSet.getNamespaceURI() != null);
            digester.setUseContextClassLoader(getUseContextClassLoader());
            digester.setValidating(false);
            digester.addRuleSet(ruleSet);
        }
        return digester;
    }

    /**
     * Return the {@code RuleSet} to be used for configuring
     * our {@code Digester} parsing rules, creating one if necessary.
     *
     * @return The RuleSet for configuring a Digester instance.
     */
    public RuleSet getRuleSet() {
        if (ruleSet == null) {
            ruleSet = new ConfigRuleSet();
        }
        return ruleSet;
    }

    /**
     * Set the {@code RuleSet} to be used for configuring
     * our {@code Digester} parsing rules.
     *
     * @param ruleSet The new RuleSet to use
     */
    public void setRuleSet(RuleSet ruleSet) {
        this.digester = null;
        this.ruleSet = ruleSet;
    }

    /**
     * Return the "use context class loader" flag. If set to
     * {@code true}, Digester will attempt to instantiate new
     * command and chain instances from the context class loader.
     *
     * @return {@code true} if Digester should use the context class loader.
     */
    public boolean getUseContextClassLoader() {
        return this.useContextClassLoader;
    }

    /**
     * Set the "use context class loader" flag.
     *
     * @param useContextClassLoader The new flag value
     */
    public void setUseContextClassLoader(boolean useContextClassLoader) {
        this.useContextClassLoader = useContextClassLoader;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Parse the XML document at the specified URL, using the configured
     * {@code RuleSet}, registering top level commands into the specified
     * {@link Catalog}. Use this method <strong>only</strong> if you have
     * <strong>NOT</strong> included any {@code factory} element in your
     * configuration resource, and wish to supply the catalog explicitly.
     *
     * @param catalog {@link Catalog} into which configured chains are
     *        to be registered
     * @param url {@code URL} of the XML document to be parsed
     *
     * @throws Exception if a parsing error occurs
     *
     * @deprecated Use parse(URL) on a configuration resource with "factory"
     *  element(s) embedded
     */
    @Deprecated
    public void parse(Catalog catalog, URL url) throws Exception {
        // Prepare our Digester instance
        Digester digester = getDigester();
        digester.clear();
        digester.push(catalog);

        // Parse the configuration document
        digester.parse(url);
    }

    /**
     * Parse the XML document at the specified URL using the configured
     * {@code RuleSet}, registering catalogs with nested chains and
     * commands as they are encountered. Use this method <strong>only</strong>
     * if you have included one or more {@code factory} elements in your
     * configuration resource.
     *
     * @param url {@code URL} of the XML document to be parsed
     *
     * @throws Exception if a parsing error occurs
     */
    public void parse(URL url) throws Exception {
        // Prepare our Digester instance
        Digester digester = getDigester();
        digester.clear();

        // Parse the configuration document
        digester.parse(url);
    }
}