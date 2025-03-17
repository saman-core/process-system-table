package io.samancore.component;

import com.fasterxml.jackson.databind.JsonNode;
import io.samancore.component.base.Component;
import io.samancore.component.base.Field;
import io.samancore.type.CaseType;
import io.samancore.type.SensitiveDataMaskType;
import io.samancore.util.JsonFormIoUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static io.samancore.util.GeneralConstant.*;

@Getter
public class Textfield extends Component implements Field {
    private Boolean isTruncateMultipleSpaces = false;
    private CaseType caseType = CaseType.NONE;
    private String pattern = null;
    private Integer minLength = null;
    private Integer minWords = null;
    private Integer maxWords = null;
    private SensitiveDataMaskType sensitiveDataMaskType = SensitiveDataMaskType.NONE;

    public Textfield(CaseType columnCaseSensitive, JsonNode jsonNodeComponent) {
        super(columnCaseSensitive, jsonNodeComponent);
        this.isTruncateMultipleSpaces = JsonFormIoUtil.getBooleanPropertyFromNode(jsonNodeComponent, TRUNCATE_MULTIPLE_SPACES);
        this.pattern = JsonFormIoUtil.getPattern(jsonNodeComponent);
        this.caseType = JsonFormIoUtil.getCaseType(jsonNodeComponent);
        this.minLength = JsonFormIoUtil.getIntegerPropertyFromValidate(jsonNodeComponent, MIN_LENGTH);
        setMaxLength(JsonFormIoUtil.getIntegerPropertyFromValidate(jsonNodeComponent, MAX_LENGTH));
        this.minWords = JsonFormIoUtil.getIntegerPropertyFromValidate(jsonNodeComponent, MIN_WORDS);
        this.maxWords = JsonFormIoUtil.getIntegerPropertyFromValidate(jsonNodeComponent, MAX_WORDS);
        this.sensitiveDataMaskType = JsonFormIoUtil.getSensitiveDataMaskTypePropertyFromNode(jsonNodeComponent, SENSITIVE_DATA_MASK);
    }

    @Override
    public List<String> getValidationToModel() {
        var validationList = new ArrayList<String>();
        if (getIsEncrypted()) {
            validationList.add(String.format(MAX_BYTE_VALUE_S, getEncryptType().getModelMaxLength()));
        }
        if (getIsRequired()) {
            validationList.add(NOT_BLANK);
            validationList.add(NOT_EMPTY);
        }
        if (pattern != null) {
            validationList.add(String.format(PATTERN_REGEXP_S, pattern));
        }
        if (minLength != null && getModelMaxLength() != null) {
            validationList.add(String.format(SIZE_MIN_D_MAX_D, minLength, getModelMaxLength()));
        } else if (minLength != null) {
            validationList.add(String.format(SIZE_MIN_D, minLength));
        } else if (getModelMaxLength() != null) {
            validationList.add(String.format(SIZE_MAX_D, getModelMaxLength()));
        }
        if (minWords != null && maxWords != null) {
            validationList.add(String.format("@WordLimit(min = %d, max = %d)", minWords, maxWords));
        } else if (minWords != null) {
            validationList.add(String.format("@WordLimit(min = %d)", minWords));
        } else if (maxWords != null) {
            validationList.add(String.format("@WordLimit(max = %d)", maxWords));
        }
        return validationList;
    }

    @Override
    public Boolean evaluateIfNeedPairToEntity() {
        return getIsTruncateMultipleSpaces() || !getCaseType().equals(CaseType.NONE) || getIsEncrypted();
    }

    @Override
    public Boolean evaluateIfNeedPairToModel() {
        return getIsTruncateMultipleSpaces() || !getCaseType().equals(CaseType.NONE) || getIsEncrypted() || getHasSensitiveDataMaskType();
    }

    @Override
    public List<String> getMethodTransformToEntity() {
        var list = new ArrayList<String>();
        list.add(String.format(PRIVATE_S_TRANSFORM_S_TO_ENTITY_S_ELEMENT, getObjectTypeToEntity(), getKeyFormatted(), getObjectTypeToModel()));
        if (!getCaseType().equals(CaseType.NONE)) {
            list.add(String.format(ELEMENT_STRING_ELEMENT_TO_S_JAVA_UTIL_LOCALE_ROOT, getCaseType().getDescription()));
        }
        if (getIsTruncateMultipleSpaces()) {
            list.add(ELEMENT_STRING_ELEMENT_TRIM_REPLACE_ALL_S_2_G);
        }
        if (getIsEncrypted()) {
            list.add(getMethodEncrypt());
        } else list.add(RETURN_ELEMENT);
        list.add(CLOSE_KEY);
        return list;
    }

    @Override
    public List<String> getMethodTransformToModel() {
        var list = new ArrayList<String>();
        list.add(String.format(PRIVATE_S_TRANSFORM_S_TO_MODEL_S_ELEMENT, getObjectTypeToModel(), getKeyFormatted(), getObjectTypeToEntity()));
        String elementName = getElementNameAndAddMethodDecrypt(list);
        if (!getCaseType().equals(CaseType.NONE)) {
            list.add(String.format(S_S_TO_S_JAVA_UTIL_LOCALE_ROOT, elementName, elementName, getCaseType().getDescription()));
        }
        if (getIsTruncateMultipleSpaces()) {
            list.add(String.format(S_S_TRIM_REPLACE_ALL_S_2_G, elementName, elementName));
        }
        if (getHasSensitiveDataMaskType()) {
            list.add(String.format(S_MASKER_S_APPLY_S, elementName, getSensitiveDataMaskType().getDescriptionCapitalize(), elementName));
        }
        list.add(String.format(RETURN_S, elementName));
        list.add(CLOSE_KEY);
        return list;
    }

    @Override
    public List<String> getInjectToTransform() {
        return getHasSensitiveDataMaskType() ? List.of(String.format("@Named(\"%s\")", getSensitiveDataMaskType().getDescription()), String.format(IO_SAMANCORE_COMMON_TRANSFORMER_MASKER_MASKER_S, getSensitiveDataMaskType().getDescriptionCapitalize())) : List.of();
    }

    public boolean getHasSensitiveDataMaskType() {
        return !SensitiveDataMaskType.NONE.equals(sensitiveDataMaskType);
    }

}
