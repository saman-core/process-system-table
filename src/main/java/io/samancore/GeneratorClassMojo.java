package io.samancore;

import io.samancore.util.ProcessTemplate;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.nio.file.Paths;

@Mojo(name = "generate-code", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GeneratorClassMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    ProcessTemplate processTemplate = new ProcessTemplate();


    public void execute() throws MojoExecutionException {
        String baseDirPath = project.getBasedir().getParent();
        getLog().info("[process-template]-baseDirPath:" + baseDirPath);
        getLog().info("[process-template]-artifactId: " + project.getArtifactId());
        try {
            String absolutePath = Paths.get("").toAbsolutePath().toString();
            var template = processTemplate.getTemplateFromFile(absolutePath);
            getLog().info("[process-template]-template loaded: ");
            processTemplate.updateTemplateWithFieldsFromFormJsonFile(absolutePath, template);
            getLog().info("[process-template]- fields loaded: ");
            processTemplate.generateFilesFromTemplate(project, template);
            getLog().info("[process-template]- files generated: ");
        } catch (Exception error) {
            getLog().error("[process-template]-error", error);
            throw new MojoExecutionException(error.getMessage(), error);
        }
    }


}