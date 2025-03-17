package io.samancore.component.base;

import com.fasterxml.jackson.databind.JsonNode;
import io.samancore.type.CaseType;
import io.samancore.type.EncryptType;
import io.samancore.util.JsonFormIoUtil;
import io.samancore.validation.ValidationComponent;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static io.samancore.util.GeneralConstant.*;

@Getter
@Setter
public abstract class Component extends ValidationComponent implements Input {

    private final CaseType dbElementCaseSensitive;
    private final String key;
    private final Boolean isPersistent;
    private final Boolean isRequired;
    private final Boolean isUnique;
    private final Boolean isProtected;
    private final Boolean hasDbIndex;
    private EncryptType encryptType;
    private Integer maxLength;

    public Component(CaseType dbElementCaseSensitive, JsonNode jsonNodeComponent) {
        this.key = JsonFormIoUtil.getKey(jsonNodeComponent);
        validateIfNameIsAReservedWord(this.key);
        validateLengthName(this.getKey(), String.format(S_NAME_LENGTH_SHOULD_BE_MAX_S_CHARACTERS, Component.class.descriptorString(), MAX_LENGTH_NAME_ALLOWED));
        validateIfNameBeginWithLowerCase(this.key);
        validateIfNameContainAnySymbol(this.key);
        this.dbElementCaseSensitive = dbElementCaseSensitive;
        this.isPersistent = JsonFormIoUtil.isPersistent(jsonNodeComponent);
        this.isUnique = JsonFormIoUtil.getBooleanPropertyFromNode(jsonNodeComponent, UNIQUE);
        this.hasDbIndex = JsonFormIoUtil.getBooleanPropertyFromNode(jsonNodeComponent, DB_INDEX);
        this.isProtected = JsonFormIoUtil.getBooleanPropertyFromNode(jsonNodeComponent, PROTECTED);
        this.isRequired = JsonFormIoUtil.isRequired(jsonNodeComponent);
        this.encryptType = JsonFormIoUtil.getEncryptTypePropertyFromNode(jsonNodeComponent, ENCRYPTED);
    }

    @Override
    public String getKeyCapitalize() {
        return StringUtils.capitalize(getKey());
    }

    @Override
    public List<String> getAllAnnotationToEntity() {
        var columnDescription = String.format(COLUMN_NAME_S, getKeyToColumn());
        if (getIsUnique() || getIsRequired() || getIsEncrypted()) {
            columnDescription = columnDescription.concat(", ");
            if (getIsUnique() && !getIsEncrypted()) {
                columnDescription = columnDescription.concat(UNIQUE_TRUE);
            }
            if (getIsRequired()) {
                columnDescription = columnDescription.concat(NULLABLE_FALSE);
            }
            if (getModelMaxLength() != null || getIsEncrypted()) {
                columnDescription = columnDescription.concat(String.format(LENGTH_D, getDbMaxLength()));
            }
            columnDescription = columnDescription.substring(0, columnDescription.lastIndexOf(",")).concat(")");
        } else columnDescription = columnDescription.concat(")");
        return List.of(columnDescription);
    }

    public String getKeyFormatted() {
        return StringUtils.capitalize(getKey());
    }

    public String getKeyToColumn() {
        var nameColumn = "F_".concat(key);
        if (dbElementCaseSensitive.equals(CaseType.LOWERCASE)) {
            return nameColumn.toLowerCase(Locale.ROOT);
        }
        return nameColumn.toUpperCase(Locale.ROOT);
    }

    public boolean getIsEncrypted() {
        return !encryptType.equals(EncryptType.NONE);
    }

    @Override
    public Boolean evaluateIfNeedPairToEntity() {
        return getIsEncrypted();
    }

    @Override
    public Boolean evaluateIfNeedPairToModel() {
        return getIsEncrypted();
    }

    @Override
    public List<String> getInjectToTransform() {
        return List.of();
    }

    @Override
    public String getMethodEncrypt() {
        return String.format("return encrypt.%s(element);", encryptType.getEncryptMethod());
    }

    @Override
    public String getMethodDecrypt() {
        return String.format("var newElement = encrypt.%s(element);", encryptType.getDecryptMethod());
    }


    @Override
    public String getPairTransformToEntity() {
        String pairDefinition = "var pair%s = org.apache.commons.lang3.tuple.Pair.of(\"%s\", (java.util.function.Function<%s, ?>) _%s -> _%s!=null ? transform%sToEntity(_%s) : null);";
        return String.format(pairDefinition, getKeyFormatted(), getKey(), getObjectTypeToModel(), getKey(), getKey(), getKeyFormatted(), getKey());
    }

    @Override
    public String getObjectTypeToEntity() {
        return getIsEncrypted() ? DATA_TYPE_BYTEA : DATA_TYPE_STRING;
    }

    @Override
    public String getPairTransformToModel() {
        String pairDefinition = "var pair%s = org.apache.commons.lang3.tuple.Pair.of(\"%s\", (java.util.function.Function<%s, ?>) _%s -> _%s!=null ? transform%sToModel(_%s) : null);";
        return String.format(pairDefinition, getKeyFormatted(), getKey(), getObjectTypeToEntity(), getKey(), getKey(), getKeyFormatted(), getKey());
    }

    @Override
    public String getObjectTypeToModel() {
        return DATA_TYPE_STRING;
    }

    @Override
    public List<String> getMethodTransformToEntity() {
        var list = new ArrayList<String>();
        list.add(String.format(PRIVATE_S_TRANSFORM_S_TO_ENTITY_S_ELEMENT, getObjectTypeToEntity(), getKeyFormatted(), getObjectTypeToModel()));
        if (getIsEncrypted()) {
            list.add(getMethodEncrypt());
        } else list.add(RETURN_ELEMENT);
        list.add(CLOSE_KEY);
        return list;
    }

    @Override
    public List<String> getMethodTransformToModel() {
        var list = new ArrayList<String>();
        if (getIsEncrypted()) {
            list.add(String.format(PRIVATE_S_TRANSFORM_S_TO_MODEL_S_ELEMENT, getObjectTypeToModel(), getKeyFormatted(), getObjectTypeToEntity()));
            list.add(getMethodDecrypt());
            list.add("return newElement;");
            list.add(CLOSE_KEY);
        }
        return list;
    }

    @Override
    public Boolean evaluateIfNeedDefineIndex() {
        return isPersistent && !getIsEncrypted() && hasDbIndex;
    }

    @Override
    public Boolean evaluateIfNeedDefineFilter() {
        return isPersistent && !getIsEncrypted() && hasDbIndex;
    }

    @Override
    public Boolean evaluateIfFilterNeedDefineJoin() {
        return false;
    }

    @Override
    public String getConversionFromStringToObjectType(String value) {
        return String.format("%s.getFirst(\"%s\")", value, key);
    }

    public Integer getModelMaxLength() {
        return encryptType.equals(EncryptType.NONE) ? maxLength : encryptType.getModelMaxLength();
    }

    public Integer getDbMaxLength() {
        return encryptType.equals(EncryptType.NONE) ? maxLength : encryptType.getDbMaxLength();
    }

    @Override
    public List<String> getValidationToModel() {
        return List.of();
    }

    public String getElementNameAndAddMethodDecrypt(ArrayList<String> list) {
        var object = ELEMENT;
        if (getIsEncrypted()) {
            list.add(getMethodDecrypt());
            object = NEW_ELEMENT;
        }
        return object;
    }
}
