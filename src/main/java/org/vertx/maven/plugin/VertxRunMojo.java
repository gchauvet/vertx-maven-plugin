package org.vertx.maven.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.vertx.core.Vertx;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.maven.artifact.DependencyResolutionRequiredException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * <p>
 * This goal is used to run a vert.x verticle in it's own instance.
 * </p>
 * <p>
 * The plugin forks a parallel lifecycle to ensure that the "package" phase has
 * been completed before invoking vert.x. This means that you do not need to
 * explicitly execute a "mvn package" first. It also means that a "mvn clean
 * vertx:run" will ensure that a full fresh compile and package is done before
 * invoking vert.x.
 * </p>
 *
 * @goal run
 * @execute phase="package"
 * @requiresDependencyResolution compile+runtime
 * @description Runs vert.x directly from a Maven project.
 */
public class VertxRunMojo extends AbstractMojo {

    /**
     * The Maven project.
     *
     * @parameter expression="${project}"
     * @required
     */
    protected MavenProject mavenProject;

    /**
     * The name of the verticle to run.
     *
     * If you're running a verticle written in JavaScript, Ruby, or Groovy then
     * it's just the name of the script, e.g. server.js, server.rb, or
     * server.groovy.
     *
     * If the verticle is written in Java, the name is the fully qualified class
     * name of the Main class e.g. com.acme.MyVerticle.
     *
     * @parameter expression="${run.verticleClass}"
     */
    private String verticleName;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            final Set<URL> urls = new HashSet<>();
            final List<String> elements = mavenProject.getTestClasspathElements();
            for (String element : elements) {
                urls.add(new File(element).toURI().toURL());
            }

            final ClassLoader contextClassLoader = URLClassLoader.newInstance(
                    urls.toArray(new URL[0]),
                    Thread.currentThread().getContextClassLoader());

            Thread.currentThread().setContextClassLoader(contextClassLoader);

            final Vertx instance = Vertx.vertx();
            getLog().info("Launching verticle [" + verticleName + "]");
            instance.deployVerticle(verticleName);
            for(;;) {
                System.in.read();
            }
            
        } catch (DependencyResolutionRequiredException | MalformedURLException e) {
            throw new MojoExecutionException("Error when loading project classpath", e);
        } catch (IOException ex) {
            Logger.getLogger(VertxRunMojo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
