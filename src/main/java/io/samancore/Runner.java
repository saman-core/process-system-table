package io.samancore;

import io.samancore.util.ProcessTemplate;
import lombok.SneakyThrows;
import org.apache.maven.plugin.MojoExecutionException;

import java.nio.file.Paths;

public class Runner {

    static ProcessTemplate processTemplate = new ProcessTemplate();

    @SneakyThrows
    public static void main(String[] args) {
        try {
            String absolutePath = Paths.get("").toAbsolutePath().toString();
            var template = processTemplate.getTemplateFromFile(absolutePath);
            processTemplate.updateTemplateWithFieldsFromFormJsonFile(absolutePath, template);
            String module = "model";
            String destinationPath = "src/main/java/";
            processTemplate.generateFilesFromTemplate(null, template, module, destinationPath);
        } catch (Exception error) {
            throw new MojoExecutionException(error.getMessage(), error);
        }
    }

}
