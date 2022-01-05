# java-slf4j-logging-example

## SLF4J

[The Simple Logging Facade for Java (SLF4J)](https://www.slf4j.org/index.html) serves as a simple facade or abstraction for various logging frameworks (e.g. java.util.logging, logback, log4j) allowing the end user to plug in the desired logging framework at deployment time.

Before you start using SLF4J, I highly recommend that you read the two-page [SLF4J user manual](https://www.slf4j.org/manual.html).

Note that SLF4J-enabling your library implies the addition of **only a single mandatory dependency**, namely **slf4j-api.jar**.

If no binding/provider is found on the class path, then SLF4J will default to a no-operation implementation. The following warning is printed because no slf4j binding could be found on your class path.

```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```

The warning will disappear as soon as you add a binding to your class path.

### Binding with a logging framework at deployment time

SLF4J supports various logging frameworks. The SLF4J distribution ships with several jar files referred to as "SLF4J bindings", with each binding corresponding to a supported framework.

- log4j 1.2 SLF4J Binding

slf4j-log4j12-${latest.stable.version}.jar

Binding for [log4j version 1.2](https://logging.apache.org/log4j/1.2/index.html), a widely used logging framework. You also need to place log4j.jar on your class path.

- Log4j 2 SLF4J Binding

log4j-slf4j-impl should be used with SLF4J 1.7.x releases or older. **Do not use this with the log4j-to-slf4j module.**

log4j-slf4j18-impl should be used with SLF4J 1.8.x releases or newer.

> Use of the Log4j 2 SLF4J Binding (log4j-slf4j-impl-2.0.jar) together with the SLF4J adapter (log4j-to-slf4j-2.0.jar) should never be attempted, as it will cause events to endlessly be routed between SLF4J and Log4j 2.

- java.util.logging SLF4J Binding

slf4j-jdk14-${latest.stable.version}.jar

Binding for java.util.logging, also referred to as JDK 1.4 logging

- logback SLF4J Binding

logback-classic-${logback.version}.jar (requires logback-core-${logback.version}.jar)

NATIVE IMPLEMENTATION. There are also SLF4J bindings external to the SLF4J project, e.g. logback which implements SLF4J natively. Logback's ch.qos.logback.classic.Logger class is a direct implementation of SLF4J's org.slf4j.Logger interface. Thus, using SLF4J in conjunction with logback involves strictly zero memory and computational overhead.

To switch logging frameworks, just replace slf4j bindings on your class path. For example, to switch from java.util.logging to log4j, just replace slf4j-jdk14-${latest.stable.version}.jar with slf4j-log4j12-${latest.stable.version}.jar.

**In fact, each SLF4J binding is hardwired at compile time to use one and only one specific logging framework. Do not place more than one binding on your class path.**

Here is a graphical illustration of the general idea.

![concrete-bindings](https://www.slf4j.org/images/concrete-bindings.png)

### What is the fastest way of (not) logging? - Typical usage pattern

The sample code below illustrates the typical usage pattern for SLF4J. Note the use of {}-placeholders. See the question ["What is the fastest way of logging?"](https://www.slf4j.org/faq.html#logging_performance) for more details.

```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Wombat {
    final Logger logger = LoggerFactory.getLogger(Wombat.class);
    
    public void printLog() {
        logger.debug("Temperature set to {}. Old temperature was {}.", t, oldT);
    }
}
```

For some Logger logger, writing:

```
logger.debug("Entry number: " + i + " is " + String.valueOf(entry[i]));
```

Incurs the cost of constructing the message parameter, that is converting both integer i and entry[i] to a String, and concatenating intermediate strings. This, regardless of whether the message will be logged or not.

## Apache Log4j 1

Apache Log4j 1 Github repo link: https://github.com/apache/logging-log4j1

## Logback

Logback Github repo link: https://github.com/qos-ch/logback#about-logback

[Logback](https://logback.qos.ch/) is intended as a successor to the popular log4j project. At present time, logback is divided into three modules, logback-core, logback-classic and logback-access.

The logback-core module lays the groundwork for the other two modules. The logback-classic module can be assimilated to a significantly improved version of log4j 1.x. Moreover, logback-classic natively implements the SLF4J API so that you can readily switch back and forth between logback and other logging frameworks such as log4j 1.x or java.util.logging (JUL).

The logback-access module integrates with Servlet containers, such as Tomcat and Jetty, to provide HTTP-access log functionality. Note that you could easily build your own module on top of logback-core.

### Find the order of logback files

According to the `ContextInitializer` in `logback-classic`, the order of looking for logback files is

- logback.configurationFile
- logback-test.xml
- logback.groovy
- logback.xml

### Reasons to prefer logback over log4j 1.x

Refer to: https://logback.qos.ch/reasonsToSwitch.html

Logback brings a large number of improvements over log4j 1.x, big and small. They are too many to enumerate exhaustively. Nevertheless, here is a non-exhaustive list of reasons for switching to logback from log4j 1.x. Keep in mind that logback is conceptually very similar to log4j 1.x as both projects were founded by the **same developer**.

- Faster implementation
- Extensive battery of tests
- logback-classic speaks SLF4J natively
- Extensive documentation
- Automatic reloading of configuration files
- Conditional processing of configuration files
  To avoid duplication, logback supports [conditional processing of configuration files](https://logback.qos.ch/manual/configuration.html#conditional) with the help of <if>, <then> and <else> elements so that a single configuration file can adequately target several environments.
- [Filters](https://logback.qos.ch/reasonsToSwitch.html#filters)
- Stack traces with packaging data

### Logstash Logback Encoder

Logstash Logback Encoder Github repo link: https://github.com/logfellow/logstash-logback-encoder#logstash-logback-encoder

Provides logback encoders, layouts, and appenders to log in JSON and other formats supported by Jackson.

### Logback error messages and their meanings
Refer to: https://logback.qos.ch/codes.html

#### Appenders must be defined before they are referenced.
In a configuration file, at the point where an appender is referenced by name, it must be defined earlier in the configuration file. Referencing an appender defined later in the file is not allowed. Below are examples of correct and incorrect order of definition and referece.

Below is an example of a correct ordering, where appender definition precedes references made to it.

Refer to: https://logback.qos.ch/codes.html#appender_order
https://howtodoinjava.com/spring-boot2/logging/profile-specific-logging/

## Apache Log4j 2

Apache Log4j 2 Github repo link: https://github.com/apache/logging-log4j2#apache-log4j-2

Apache Log4j 2 is an upgrade to Log4j that provides significant improvements over its predecessor, Log4j 1.x, and provides many of the improvements available in Logback while fixing some inherent problems in Logback's architecture.

## Spring Boot Logging

- https://docs.spring.io/spring-boot/docs/2.1.8.RELEASE/reference/html/howto-logging.html
- https://docs.spring.io/spring-boot/docs/2.1.8.RELEASE/reference/html/boot-features-logging.html#boot-features-logback-extensions

Spring Boot has no mandatory logging dependency, except for the Commons Logging API, which is typically provided by Spring Framework’s `spring-jcl` module. To use Logback, you need to include it and `spring-jcl` on the classpath. The simplest way to do that is through the starters, which all depend on `spring-boot-starter-logging`.

By default, if you use the “Starters”, `Logback` is used for logging.

### Custom Log Configuration

The various logging systems can be activated by including the appropriate libraries on the classpath and can be further customized by providing a suitable configuration file in the root of the classpath or in a location specified by the following Spring Environment property: `logging.config`.

> When possible, we recommend that you use the -spring variants for your logging configuration (for example, logback-spring.xml rather than logback.xml). If you use standard configuration locations, Spring cannot completely control log initialization.

All the supported logging systems can consult System properties when parsing their configuration files. See the default configurations in `spring-boot.jar` for examples:

- [Logback](https://github.com/spring-projects/spring-boot/blob/v2.1.8.RELEASE/spring-boot-project/spring-boot/src/main/resources/org/springframework/boot/logging/logback/defaults.xml)
- [Log4j 2](https://github.com/spring-projects/spring-boot/tree/v2.1.8.RELEASE/spring-boot-project/spring-boot/src/main/resources/org/springframework/boot/logging/log4j2/log4j2.xml)
- [Java Util logging](https://github.com/spring-projects/spring-boot/blob/v2.1.8.RELEASE/spring-boot-project/spring-boot/src/main/resources/org/springframework/boot/logging/java/logging-file.properties)

### Configure Logback for Logging

If you need to apply customizations to logback beyond those that can be achieved with `application.properties`, you’ll need to add a standard logback configuration file. You can add a `logback.xml` file to the root of your classpath for logback to find. You can also use `logback-spring.xml` if you want to use the Spring Boot Logback extensions)

Spring Boot provides a number of logback configurations that be included from your own configuration. These includes are designed to allow certain common Spring Boot conventions to be re-applied.

The following files are provided under `org/springframework/boot/logging/logback/`:

`defaults.xml` - Provides conversion rules, pattern properties and common logger configurations.

#### Logstash Logback Encoder

[Logstash Logback Encoder](https://github.com/logfellow/logstash-logback-encoder) is Logback JSON encoder and appenders.

### Configure Log4j 2 for Logging

Refer to: https://docs.spring.io/spring-boot/docs/2.1.8.RELEASE/reference/html/howto-logging.html#howto-configure-log4j-for-logging
