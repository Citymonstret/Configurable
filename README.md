# Configurable
Annotation based configuration library for Java

| Dependency | Explanation |
|:-----------|-------------|
|[lombok](https://projectlombok.org/) | To make the code prettier, and easier to work with |
|[json-io](https://github.com/jdereg/json-io) | For the JSON implementation |
|[guava](https://github.com/google/guava) | Immutable maps are the best thing ever |

| TODO |
|------|
| YAML Implementation |
| Make gradle package this nicely |
| Start using jitpack.io |
| Find workaround for silly finals & inlining |
| _maybe_ Comments |

Example of a config declaration using lombok for ```@Getter```
```java
@Configuration(name = "config", implementation = ConfigurationImplementation.JSON)
public class RandomClass {

  @Getter
  @ConfigValue
  private String loginMessage = "Welcome online {0}!";

  @Getter
  private Listeners listeners = new Listeners();

  @ConfigSection
  public class Listeners {

    @Getter
    @ConfigValue
    public boolean asyncPlayerChatEvent = false;

    @Getter
    @ConfigValue
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
    "@type":"java.util.HashMap", // Ignore this
    "asyncPlayerChatEvent": false,
    "blockDecayEvent": false
  }
}
```