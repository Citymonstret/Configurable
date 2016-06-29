# Configurable
Annotation based configuration library for Java


This is sort of how I want it to end up
```java
@Configuration(name = "config", implementation = ConfigurationImplementation.JSON)
public class RandomClass {
  @Getter
  @ConfigValue(description = "Message sent on login!")
  private final String loginMessage = "Welcome online {0}!";

  @ConfigSection(keepCapitlization = true)
  public class Listeners {
  
    @ConfigValue
    public final boolean asyncPlayerChatEvent = false;

    @ConfigValue
    public final boolean blockDecayEvent = false;

  }  
}

// Creating an instance from the factory singleton
Config<RandomClass> config = ConfigurationFactory.from(RandomClass.class);
// Load the configuration
config.load();
// #get() gets the instance, then just access the fields
config.get().getLoginMessage(); // Using lombok getter
// Inner-classes are "sections"
config.getSection("Listeners").get("asyncPlayerChatEvent", Boolean.class);
// Just using the instance
RandomClass instance = config.get();
instance.Listeners.asyncPlayerChatEvent;
```

And this is how it would end up
```json
// config.json
{
  "loginMessage": "Welcome online {0}!",
  "Listeners": {
    "asyncPlayerChatEvent": false,
    "blockDecayEvent": false
  }
}
```
