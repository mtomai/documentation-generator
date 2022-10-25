package eu.example.poc.documentation.generator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitializingBeanExampleBean implements InitializingBean {

    @Autowired
    private DocumentationGeneratorCore documentationGeneratorCore;

    @Override
    public void afterPropertiesSet() throws Exception {
        documentationGeneratorCore.generateDocumentation();
    }
}