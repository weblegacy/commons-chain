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
package org.apache.commons.chain.generic;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.Filter;

/**
 * Look up a specified {@link Command} (which could also be a
 * {@link org.apache.commons.chain.Chain})
 * in a {@link Catalog}, and delegate execution to it. If the delegated-to
 * {@link Command} is also a {@link Filter}, its {@code postprocess()}
 * method will also be invoked at the appropriate time.
 *
 * <p>The name of the {@link Command} can be specified either directly (via
 * the {@code name} property) or indirectly (via the {@code nameKey}
 * property). Exactly one of these must be set.</p>
 *
 * <p>If the {@code optional} property is set to {@code true},
 * failure to find the specified command in the specified catalog will be
 * silently ignored. Otherwise, a lookup failure will trigger an
 * {@code IllegalArgumentException}.</p>
 *
 * @param <C> Type of the context associated with this command
 *
 * @author Craig R. McClanahan
 * @version $Revision$ $Date$
 */
public class LookupCommand<C extends Context> implements Filter<C> {

    // -------------------------------------------------------------- Constructors

    /**
     * Create an instance, setting its {@code catalogFactory} property to the
     * value of {@code CatalogFactory.getInstance()}.
     *
     * @since Chain 1.1
     */
    public LookupCommand() {
        this(CatalogFactory.getInstance());
    }

    /**
     * Create an instance and initialize the {@code catalogFactory} property
     * to given {@code factory}.
     *
     * @param factory The Catalog Factory.
     *
     * @since Chain 1.1
     */
    public LookupCommand(CatalogFactory<C> factory) {
        this.catalogFactory = factory;
    }

    // -------------------------------------------------------------- Properties

    private CatalogFactory<C> catalogFactory = null;

    /**
     * Set the {@link CatalogFactory} from which lookups will be
     * performed.
     *
     * @param catalogFactory The Catalog Factory.
     *
     * @since Chain 1.1
     */
    public void setCatalogFactory(CatalogFactory<C> catalogFactory) {
        this.catalogFactory = catalogFactory;
    }

    /**
     * Return the {@link CatalogFactory} from which lookups will be performed.
     *
     * @return The Catalog factory.
     *
     * @since Chain 1.1
     */
    public CatalogFactory<C> getCatalogFactory() {
        return this.catalogFactory;
    }

    private String catalogName = null;

    /**
     * Return the name of the {@link Catalog} to be searched, or
     * {@code null} to search the default {@link Catalog}.
     *
     * @return The Catalog name.
     */
    public String getCatalogName() {
        return this.catalogName;
    }

    /**
     * Set the name of the {@link Catalog} to be searched, or
     * {@code null} to search the default {@link Catalog}.
     *
     * @param catalogName The new {@link Catalog} name or {@code null}
     */
    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    private String name = null;

    /**
     * Return the name of the {@link Command} that we will look up and
     * delegate execution to.
     *
     * @return The name of the Command.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of the {@link Command} that we will look up and
     * delegate execution to.
     *
     * @param name The new command name
     */
    public void setName(String name) {
        this.name = name;
    }

    private String nameKey = null;

    /**
     * Return the context attribute key under which the {@link Command}
     * name is stored.
     *
     * @return The context key of the Command.
     */
    public String getNameKey() {
        return this.nameKey;
    }

    /**
     * Set the context attribute key under which the {@link Command}
     * name is stored.
     *
     * @param nameKey The new context attribute key
     */
    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    private boolean optional = false;

    /**
     * Return {@code true} if locating the specified command
     * is optional.
     *
     * @return {@code true} if the Command is optional.
     */
    public boolean isOptional() {
        return this.optional;
    }

    /**
     * Set the optional flag for finding the specified command.
     *
     * @param optional The new optional flag
     */
    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    private boolean ignoreExecuteResult = false;

    /**
     * Return {@code true} if this command should ignore
     * the return value from executing the looked-up command.
     * Defaults to {@code false}, which means that the return result
     * of executing this lookup will be whatever is returned from that
     * command.
     *
     * @return {@code true} if result of the looked up Command
     *         should be ignored.
     *
     * @since Chain 1.1
     */
    public boolean isIgnoreExecuteResult() {
        return ignoreExecuteResult;
    }

    /**
     * Set the rules for whether or not this class will ignore or
     * pass through the value returned from executing the looked up
     * command.
     *
     * <p>If you are looking up a chain which may be "aborted" and
     * you do not want this class to stop chain processing, then this
     * value should be set to {@code true}.</p>
     *
     * @param ignoreReturn {@code true} if result of the
     *        looked up Command should be ignored.
     *
     * @since Chain 1.1
     */
    public void setIgnoreExecuteResult(boolean ignoreReturn) {
        this.ignoreExecuteResult = ignoreReturn;
    }

    private boolean ignorePostprocessResult = false;

    /**
     * Return {@code true} if this command is a Filter and
     * should ignore the return value from executing the looked-up Filter's
     * {@code postprocess()} method.
     *
     * <p>Defaults to {@code false}, which means that the return result
     * of executing this lookup will be whatever is returned from that
     * Filter.</p>
     *
     * @return {@code true} if result of the looked up Filter's
     *         {@code postprocess()} method should be ignored.
     *
     * @since Chain 1.1
     */
    public boolean isIgnorePostprocessResult() {
        return ignorePostprocessResult;
    }

    /**
     * Set the rules for whether or not this class will ignore or
     * pass through the value returned from executing the looked up
     * Filter's {@code postprocess()} method.
     *
     * <p>If you are looking up a Filter which may be "aborted" and
     * you do not want this class to stop chain processing, then this
     * value should be set to {@code true}.</p>
     *
     * @param ignorePostprocessResult {@code true} if result of the
     *         looked up Filter's {@code postprocess()} method should
     *         be ignored.
     *
     * @since Chain 1.1
     */
    public void setIgnorePostprocessResult(boolean ignorePostprocessResult) {
        this.ignorePostprocessResult = ignorePostprocessResult;
    }

    // ---------------------------------------------------------- Filter Methods

    /**
     * Look up the specified command, and (if found) execute it.
     * Unless {@code ignoreExecuteResult} is set to {@code true},
     * return the result of executing the found command. If no command
     * is found, return {@code false}, unless the {@code optional}
     * property is {@code false}, in which case an
     * {@code IllegalArgumentException} will be thrown.
     *
     * @param context The context for this request
     *
     * @return the result of executing the looked-up command, or
     *         {@code false} if no command is found or if the command
     *         is found but the {@code ignoreExecuteResult} property of
     *         this instance is {@code true}
     *
     * @throws IllegalArgumentException if no such {@link Command}
     *         can be found and the {@code optional} property is set
     *         to {@code false}
     * @throws Exception if and error occurs in the looked-up Command.
     */
    @Override
    public boolean execute(C context) throws Exception {
        Command<C> command = getCommand(context);
        if (command != null) {
            boolean result = command.execute(context);
            if (isIgnoreExecuteResult()) {
                return false;
            }
            return result;
        } else {
            return false;
        }
    }

    /**
     * If the executed command was itself a {@link Filter}, call the
     * {@code postprocess()} method of that {@link Filter} as well.
     *
     * @param context The context for this request
     * @param exception Any {@code Exception} thrown by command execution
     *
     * @return the result of executing the {@code postprocess} method
     *         of the looked-up command, unless
     *         {@code ignorePostprocessResult} is {@code true}. If no
     *         command is found, return {@code false}, unless the
     *         {@code optional} property is {@code false}, in which case
     *         {@code IllegalArgumentException} will be thrown.
     */
    @Override
    public boolean postprocess(C context, Exception exception) {
        Command<C> command = getCommand(context);
        if (command != null) {
            if (command instanceof Filter) {
                boolean result = (((Filter<C>) command).postprocess(context, exception));
                if (isIgnorePostprocessResult()) {
                    return false;
                }
                return result;
            }
        }
        return false;
    }

    // --------------------------------------------------------- Private Methods

    /**
     * Return the {@link Catalog} to look up the {@link Command} in.
     *
     * @param context {@link Context} for this request
     *
     * @return The catalog.
     *
     * @throws IllegalArgumentException if no {@link Catalog}
     *         can be found
     *
     * @since Chain 1.2
     */
    protected Catalog<C> getCatalog(C context) {
        CatalogFactory<C> lookupFactory = this.catalogFactory;
        if (lookupFactory == null) {
            lookupFactory = CatalogFactory.getInstance();
        }

        String catalogName = getCatalogName();
        Catalog<C> catalog = null;
        if (catalogName == null) {
            // use default catalog
            catalog = lookupFactory.getCatalog();
        } else {
            catalog = lookupFactory.getCatalog(catalogName);
        }
        if (catalog == null) {
            if (catalogName == null) {
                throw new IllegalArgumentException("Cannot find default catalog");
            } else {
                throw new IllegalArgumentException("Cannot find catalog '" + catalogName + "'");
            }
        }

        return catalog;
    }

    /**
     * Return the {@link Command} instance to be delegated to.
     *
     * @param context {@link Context} for this request
     *
     * @return The looked-up Command.
     *
     * @throws IllegalArgumentException if no such {@link Command}
     *         can be found and the {@code optional} property is
     *         set to {@code false}
     */
    protected Command<C> getCommand(C context) {
        Catalog<C> catalog = getCatalog(context);

        Command<C> command = null;
        String name = getCommandName(context);
        if (name != null) {
            command = catalog.getCommand(name);
            if (command == null && !isOptional()) {
                if (catalogName == null) {
                    throw new IllegalArgumentException("Cannot find command '" + name
                         + "' in default catalog");
                } else {
                    throw new IllegalArgumentException("Cannot find command '" + name
                         + "' in catalog '" + catalogName + "'");
                }
            }
            return command;
        } else {
            throw new IllegalArgumentException("No command name");
        }
    }

    /**
     * Return the name of the {@link Command} instance to be
     * delegated to.
     *
     * @param context {@link Context} for this request
     *
     * @return The name of the {@link Command} instance
     *
     * @since Chain 1.2
     */
    protected String getCommandName(C context) {
        String name = getName();
        if (name == null) {
            name = context.get(getNameKey()).toString();
        }
        return name;
    }
}