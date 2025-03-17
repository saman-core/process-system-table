package io.samancore.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.samancore.component.Number;
import io.samancore.component.*;
import io.samancore.component.base.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static io.samancore.util.GeneralConstant.*;

public class JsonUtil {

    private JsonUtil() {
    }

    public static List<Field> getFieldsOfComponent(Template template, ArrayNode componentsArrayNode) {
        List<Field> fieldList = new ArrayList<>();
        for (JsonNode jsonNodeComponent : componentsArrayNode) {
            ArrayNode jsonNodeComponentColumns = (ArrayNode) jsonNodeComponent.get(JSON_COMPONENT_COLUMNS);
            ArrayNode jsonNodeComponentChildren = (ArrayNode) jsonNodeComponent.get(JSON_COMPONENT_COMPONENTS);
            ArrayNode jsonNodeComponentRows = (ArrayNode) jsonNodeComponent.get(JSON_COMPONENT_ROWS);
            if (jsonNodeComponentColumns != null) {
                for (JsonNode jsonNodeColumn : jsonNodeComponentColumns) {
                    ArrayNode componentsFromColumn = (ArrayNode) jsonNodeColumn.get(JSON_COMPONENT_COMPONENTS);
                    fieldList.addAll(getFieldsOfComponent(template, componentsFromColumn));
                }
            } else if (jsonNodeComponentChildren != null) {
                fieldList.addAll(getFieldsOfComponent(template, jsonNodeComponentChildren));
            } else if (jsonNodeComponentRows != null) {
                for (JsonNode jsonNodeRows : jsonNodeComponentRows) {
                    fieldList.addAll(getFieldsOfComponent(template, (ArrayNode) jsonNodeRows));
                }
            } else {
                var field = getField(template, jsonNodeComponent);
                if (field != null && field.getKey() != null && !field.getKey().isEmpty()) {
                    fieldList.add(field);
                }
            }
        }
        return fieldList;
    }

    private static Field getField(Template template, JsonNode jsonNodeComponent) {
        return switch (jsonNodeComponent.get("type").asText().toLowerCase(Locale.ROOT)) {
            case COMPONENT_TEXTFIELD, COMPONENT_SAMANTEXTFIELD -> new Textfield(template.getDbElementCaseSensitive(), jsonNodeComponent);
            case COMPONENT_CHECKBOX, COMPONENT_SAMANCHECKBOX -> new Checkbox(template.getDbElementCaseSensitive(), jsonNodeComponent);
            case COMPONENT_DATETIME, COMPONENT_SAMANDATETIME -> new Datetime(template.getDbElementCaseSensitive(), jsonNodeComponent);
            case COMPONENT_NUMBER, COMPONENT_SAMANNUMBER -> new Number(template.getDbElementCaseSensitive(), jsonNodeComponent);
            default -> null;
        };
    }
}
