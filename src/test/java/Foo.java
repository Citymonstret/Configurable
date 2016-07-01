import com.intellectualsites.configurable.AnnotationMode;
import com.intellectualsites.configurable.ConfigurationImplementation;
import com.intellectualsites.configurable.annotations.ConfigSection;
import com.intellectualsites.configurable.annotations.ConfigValue;
import com.intellectualsites.configurable.annotations.Configuration;
import lombok.Getter;

@Configuration(name = "config", implementation = ConfigurationImplementation.YAML)
public class Foo {

    // Will be included
    public static String staticString = "Hello World!";

    // Will be ignored
    public static transient String ignoredStatic = "I am ignored :(";

    @Getter
    private transient final Bar bar = new Bar();

    @ConfigSection(name = "bar", annotationMode = AnnotationMode.REQUIRED)
    public class Bar {

        @ConfigValue
        public int atest = 999;

        // Ignored, as "annotationMode = AnnotationMode.REQUIRED"
        public int otest = 888;
    }
}
