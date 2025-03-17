package io.samancore.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class GeneratorClass {

    public static final List<String> DATA_TEMPLATES_LIST = List.of("Entity.vm");
    public static final List<String> MODEL_TEMPLATES_LIST = List.of("Model.vm", "RequestParameter.vm");
    public static final List<String> CLIENT_TEMPLATES_LIST = List.of("Client.vm", "RestClient.vm", "RestClientWrapper.vm");
    public static final List<String> APPLICATION_TEMPLATES_LIST = List.of("Api.vm", "Service.vm", "ServiceImpl.vm", "Repository.vm", "RepositoryReactive.vm", "Transformer.vm");
    public static final Map<String, List<String>> FILES_MODULE_GENERATOR_MAP = Map.of("model", MODEL_TEMPLATES_LIST, "application", APPLICATION_TEMPLATES_LIST, "client", CLIENT_TEMPLATES_LIST, "data", DATA_TEMPLATES_LIST);
    public static final Map<String, String> ROUTE_PACKAGE_OUTPUT_MAP = Map.ofEntries(
            Map.entry("Api", "api"),
            Map.entry("Model", "model"),
            Map.entry("RequestParameter", "request/parameter"),
            Map.entry("Entity", "entity"),
            Map.entry("Repository", "repository"),
            Map.entry("Service", "service"),
            Map.entry("ServiceImpl", "service/impl"),
            Map.entry("RepositoryReactive", "repository/reactive"),
            Map.entry("Transformer", "transformer"),
            Map.entry("Client", "client"),
            Map.entry("RestClient", "client/rest/microprofile"),
            Map.entry("RestClientWrapper", "client/rest"));

    private VelocityEngine velocityEngine;
    private static final String javaSuffix = ".java";
    private static final String velocityFileSuffix = ".vm";

    public GeneratorClass() {
        initialize();
    }

    public String getJavaFileNameDestination(String fileName, String templateName) {
        return templateName.concat(fileName).concat(javaSuffix);
    }

    public String makePathDestination(String templateName, String templatePackageName, String fileName) {
        fileName = fileName.replace(velocityFileSuffix, "");
        var javaFileName = getJavaFileNameDestination(fileName, templateName);
        if (templatePackageName == null || templatePackageName.isEmpty()) {
            return javaFileName;
        } else {
            var packageDestination = ROUTE_PACKAGE_OUTPUT_MAP.get(fileName);
            return templatePackageName.toLowerCase(Locale.ROOT).replace('.', File.separatorChar) + File.separatorChar + templateName.toLowerCase(Locale.ROOT) + File.separatorChar + packageDestination + File.separatorChar + javaFileName;
        }
    }

    void initialize() {
        initializeVelocity();
    }

    private void initializeVelocity() {
        this.velocityEngine = new VelocityEngine();
        velocityEngine.addProperty("resource.loaders", "class, file");
        velocityEngine.addProperty("resource.loader.class.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.addProperty("resource.loader.file.class",
                "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        velocityEngine.addProperty("resource.loader.file.path", "/, ., ");
        velocityEngine.setProperty("runtime.strict_mode.enable", true);
        velocityEngine.setProperty("parser.space_gobbling", "bc");
    }


    private String renderTemplate(String templateName, VelocityContext context) {
        Template template;
        try {
            template = this.velocityEngine.getTemplate(templateName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

    public List<OutputFile> compile(io.samancore.component.Template template, String module) {
        List<OutputFile> outputFiles = new ArrayList<>();
        VelocityContext context = new VelocityContext();
        context.put("this", this);
        context.put("template", template);

        String templateDir = System.getProperty("velocity.templates.".concat(module),
                "/velocity/templates/".concat(module).concat(File.separator));

        var templatesToGenerateList = FILES_MODULE_GENERATOR_MAP.get(module);
        for (String templateFileName : templatesToGenerateList) {
            var output = renderTemplate(templateDir.concat(templateFileName), context);
            var outputFilePathDestination = makePathDestination(template.getNameCapitalize(), template.getPackageName(), templateFileName);

            OutputFile outputFile = new OutputFile();
            outputFile.path = outputFilePathDestination;
            outputFile.contents = output;
            outputFiles.add(outputFile);
        }
        return outputFiles;
    }

    public static class OutputFile {
        String path;
        String contents;
        String outputCharacterEncoding;

        /**
         * Writes output to path destination directory when it is newer than src,
         * creating directories as necessary. Returns the created file.
         */
        public File writeToDestination(File src, File destDir) throws IOException {
            File f = new File(destDir, path);
            if (src != null && f.exists() && f.lastModified() >= src.lastModified())
                return f; // already up to date: ignore
            f.getParentFile().mkdirs();
            Writer fw = null;
            FileOutputStream fos = null;
            try {
                if (outputCharacterEncoding != null) {
                    fos = new FileOutputStream(f);
                    fw = new OutputStreamWriter(fos, outputCharacterEncoding);
                } else {
                    fw = Files.newBufferedWriter(f.toPath(), UTF_8);
                }
                fw.write(contents);
            } finally {
                if (fw != null)
                    fw.close();
                if (fos != null)
                    fos.close();
            }
            return f;
        }
    }
}
