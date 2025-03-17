package io.samancore.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.samancore.component.Template;
import io.samancore.component.base.Field;
import io.samancore.type.CaseType;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES;

public class ProcessTemplate {
    private static final GeneratorClass generator = new GeneratorClass();

    public Template getTemplateFromFile(String absolutePath) throws IOException {
        var templateResourcePath = absolutePath.concat("/").concat(GeneralConstant.TEMPLATE_FILE_NAME);
        File templateFile = new File(templateResourcePath);
        InputStream templateInputStream = new FileInputStream(templateFile);
        Properties templateProperties = new Properties();
        templateProperties.load(templateInputStream);
        var caseType = CaseType.getByInitialOrDescriptionDefaultLowerCase(templateProperties.getProperty("dbElementCaseSensitive"));
        return new Template(templateProperties.getProperty("packageName"), templateProperties.getProperty("templateName"), templateProperties.getProperty("productName"), caseType);
    }

    public void updateTemplateWithFieldsFromFormJsonFile(String absolutePath, Template template) throws IOException {
        JsonMapper jsonMapper = JsonMapper.builder()
                .configure(ALLOW_UNQUOTED_FIELD_NAMES, true)
                .configure(ALLOW_SINGLE_QUOTES, true)
                .configure(MapperFeature.ALLOW_EXPLICIT_PROPERTY_RENAMING, true)
                .build();
        var jsonResourcePath = absolutePath.concat("/").concat(GeneralConstant.JSON_FILE_NAME);
        File jsonFile = new File(jsonResourcePath);
        JsonNode jsonForm = jsonMapper.readValue(jsonFile, JsonNode.class);
        ArrayNode components = (ArrayNode) jsonForm.get("components");
        List<Field> fieldList = JsonUtil.getFieldsOfComponent(template, components);
        template.setFields(fieldList);
    }

    public void generateFilesFromTemplate(MavenProject project, Template template, String module, String baseDir) throws IOException {
        if (module == null || module.isEmpty()) {
            module = getModule(project);
        }

        if (module != null) {
            List<GeneratorClass.OutputFile> outputFiles = generator.compile(template, module);
            var destinationPath = baseDir;
            if (destinationPath == null) {
                destinationPath = project.getBasedir().getPath().concat("/target/generated-sources/cde/java/");
                project.addCompileSourceRoot(destinationPath);
            }
            for (GeneratorClass.OutputFile outputFile : outputFiles) {
                File resourcesFileDestination = new File(destinationPath);
                outputFile.writeToDestination(null, resourcesFileDestination);
            }
        }
    }

    public void generateFilesFromTemplate(MavenProject project, Template template) throws IOException {
        generateFilesFromTemplate(project, template, null, null);
    }

    private static String getModule(MavenProject project) {
        String module = null;
        var artifact = project.getArtifactId().toLowerCase(Locale.ROOT);
        if (artifact.contains("model")) {
            module = "model";
        } else if (artifact.contains("client")) {
            module = "client";
        } else if (artifact.contains("application")) {
            module = "application";
        } else if (artifact.contains("data")) {
            module = "data";
        }
        return module;
    }
}
