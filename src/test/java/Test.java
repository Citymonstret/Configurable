import com.intellectualsites.configurable.Config;
import com.intellectualsites.configurable.ConfigurationFactory;

import java.io.File;

public class Test {

    public static void main(String[] args) {
        Config<Foo> config = ConfigurationFactory.load(Foo.class, new File("."));
        System.out.println(config.get().getBar().atest);
        System.out.println(config.get().getBar().otest);
    }
}
