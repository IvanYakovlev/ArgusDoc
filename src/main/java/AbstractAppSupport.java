import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class AbstractAppSupport extends Application {

        private static String[] savedArgs;

        protected ConfigurableApplicationContext context;

        @Override
        public void init() throws Exception {
            context = SpringApplication.run(getClass(), savedArgs);
            context.getAutowireCapableBeanFactory().autowireBean(this);
        }

        @Override
        public void stop() throws Exception {
            super.stop();
            context.close();
        }

        protected static void launchApp(Class<? extends AbstractAppSupport> appClass, String[] args) {
            AbstractAppSupport.savedArgs = args;
            Application.launch(appClass, args);
        }
}

