#vertx-maven-plugin
Maven Plugin for running verticles in their own vert.x instance.

##Versions
This plugin's versions require to include vertx-core (>= 3.0.0) to this plugin dependencies (see examples below).

## Usage

### vertx:run

This goal will run a verticle or vert.x module in it's own vert.x instance.  vert.x will continue to run until the plugin is explicitly stopped.  Simply type:
```sh
mvn vertx:run
```

The plugin forks a parallel lifecycle to ensure that the "package" phase has been completed before invoking vert.x. This means that you do not need to explicitly execute a "mvn package" first. It also means that a "mvn clean vertx:run" will ensure that a full fresh compile and package is done before invoking vert.x.
For Java verticles, the plugin will need to be configured in your project's POM as follows:

```xml
<plugin>
    <groupId>org.anacoders.plugins</groupId>
    <artifactId>vertx-maven-plugin</artifactId>
    <version>2.0.0</version>
    <dependencies>
        <dependency>
            <groupId>io.vertx</groupId>
            <artifactId>vertx-core</artifactId>
            <version>${vertx.version}</version>
        </dependency>
    </dependencies>
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
    <dependencies>
        <dependency>
            <groupId>io.vertx</groupId>
            <artifactId>vertx-core</artifactId>
            <version>${vertx.version}</version>
        </dependency>
    </dependencies>
    <configuration>
        <verticleName>com/acme/MyVerticle.groovy</verticleName>
        </configuration>
</plugin>  
```