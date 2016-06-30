# Configurable
Annotation based configuration library for Java

| Dependency | Explanation |
|:-----------|-------------|
|[lombok](https://projectlombok.org/) | To make the code prettier, and easier to work with |
|[json-io](https://github.com/jdereg/json-io) | For the JSON implementation |
|[SnakeYAML](https://bitbucket.org/asomov/snakeyaml) | For the YAML implementation |

| TODO |
|------|
| Find workaround for silly finals & inlining |
| _maybe_ Comments |


# Build
Build using gradle ```gradlew build```

# Maven
We're on maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
<dependency>
    <groupId>com.github.Sauilitired</groupId>
	<artifactId>Configurable</artifactId>
	<version>-SNAPSHOT</version>
</dependency>
```


Example of a config declaration using lombok for ```@Getter```
```java
@Configuration(name = "config", implementation = ConfigurationImplementation.JSON)
public class RandomClass {

  @Getter
  private String loginMessage = "Welcome online {0}!";

  @Getter
  private transient Listeners listeners = new Listeners();

  @ConfigSection
  public class Listeners {

    @Getter
    public boolean asyncPlayerChatEvent = false;

    @Getter
    private boolean blockDecayEvent = false;

  }  
}
```

And this is how you would manage it
```java
final File FOLDER = new File(".");

Config<RandomClass> config = ConfigurationFactory.from(RandomClass.class);
try {
  config.read(FOLDER);
} catch(final Exception e) {
  System.out.println("Failed to read the config :/");
}

// Get the RandomClass instance
RandomClass instance = config.get();
String loginMessage = instance.getLoginMessage();

// But you can also access it like this
String loginMessage = config.get("loginMessage", String.class);
```

And this is how it would end up
```json
// config.json
{
  "loginMessage": "Welcome online {0}!",
  "Listeners": {
    "@type":"java.util.HashMap",
    "asyncPlayerChatEvent": false,
    "blockDecayEvent": false
  }
}
```


Another example, where the inner class is static (which removes the requirement for the instance field - See JavaDocs)
```java
@Configuration(implementation = ConfigurationImplementation.YAML, requiresAnnotations = true)
public class Tester {

    @ConfigSection
    public static class Sub {
        @ConfigValue
        public static String test = "Hello";
    }
    
}   
```

Would be read as:
```java
try {
    Config<Tester> config = ConfigurationFactory.from(Tester.class);
    config.read(new File("."));
    System.out.println(Sub.test);
} catch (Exception e) {
    e.printStackTrace();
}
```

And would ouput (Tester.yml)
```yaml
Sub:
  test: Hello
```
