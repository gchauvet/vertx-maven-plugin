vertx-maven-plugin
==================

Maven Plugin for running verticles in their own vert.x instance.

Install
-----
This plugin is now available on Maven Central.

Group ID: org.anacoders.plugins

Artifact ID: vertx-maven-plugin

Current release version: 2.0.0


Versions
--------

This plugin's versions require to include vertx-core (>= 3.0.0) to this plugin dendency (see below)

Usage
-----

### vertx:run

This goal will run a verticle or vert.x module in it's own vert.x instance.  vert.x will continue to run until the plugin is explicitly stopped.  Simply type:

```sh
	mvn vertx:run
```

The plugin forks a parallel lifecycle to ensure that the "package" phase has been completed before invoking 
vert.x. This means that you do not need to explicitly execute a "mvn package" first. It also means that a 
"mvn clean vertx:run" will ensure that a full fresh compile and package is done before invoking vert.x.  
	
For Java verticles, the plugin will need to be configured in your project's POM as follows:

```xml
	<plugin>
		<groupId>org.anacoders.plugins</groupId>
		<artifactId>vertx-maven-plugin</artifactId>
		<version>2.0.0</version>
		<configuration>
			<verticleName>com.acme.MyVerticle</verticleName>
		</configuration>
	</plugin>  
```
For Groovy verticles, the plugin will need to be configured in your project's POM as follows:
```xml
	<plugin>
		<groupId>org.anacoders.plugins</groupId>
		<artifactId>vertx-maven-plugin</artifactId>
		<version>2.0.0</version>
		<configuration>
			<verticleName>com/acme/MyVerticle.groovy</verticleName>
		</configuration>
	</plugin>  
```
For modules, the plugin will need to be configured in your project's POM as follows:
```xml
	<plugin>
		<groupId>org.anacoders.plugins</groupId>
		<artifactId>vertx-maven-plugin</artifactId>
		<version>2.0.0</version>
		<configuration>
			<moduleName>some-module-name</moduleName>
			<moduleRepoUrl>http://some.module.repo.url</moduleRepoUrl>
		</configuration>
	</plugin>  
```
Note that the moduleRepoUrl parameter is optional, the default value is: http://github.com/vert-x/vertx-mods
	
Sometimes you want automatic execution of the plugin, for example when doing integration testing.
To do this you can run the plugin in Maven execution scenarios and use the daemon=true configuration option to prevent vert.x from running indefinitely.
```xml
	<plugin>
		<groupId>org.anacoders.plugins</groupId>
		<artifactId>vertx-maven-plugin</artifactId>
		<version>2.0.0</version>
		<configuration>
			<verticleName>com.acme.MyVerticle</verticleName>
		</configuration>
		<executions>
			<execution>
				<id>instance1</id>
				<phase>pre-integration-test</phase>
				<goals>
					<goal>run</goal>
				</goals>
				<configuration>
					<daemon>true</daemon>
				</configuration>
			</execution>
		</executions>
	</plugin> 
```