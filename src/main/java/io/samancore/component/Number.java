package io.samancore.component;

import com.fasterxml.jackson.databind.JsonNode;
import io.samancore.component.base.Component;
import io.samancore.component.base.Field;
import io.samancore.type.CaseType;
import io.samancore.util.JsonFormIoUtil;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static io.samancore.util.GeneralConstant.*;

@Getter
public class Number extends Component implements Field {
    private Boolean requireDecimal = false;
    private Boolean isLongNumber = false;
    private Boolean delimiterThouzand = false;
    private Integer decimalLimit = null;
    private BigDecimal minValue = null;
    private BigDecimal maxValue = null;

    public Number(CaseType columnCaseSensitive, JsonNode jsonNodeComponent) {
        super(columnCaseSensitive, jsonNodeComponent);
        this.isLongNumber = JsonFormIoUtil.getBooleanPropertyFromNode(jsonNodeComponent, LONG_NUMBER);
        this.delimiterThouzand = JsonFormIoUtil.getBooleanPropertyFromNode(jsonNodeComponent, DELIMITER);
        this.decimalLimit = JsonFormIoUtil.getDecimalLimit(jsonNodeComponent);
        this.requireDecimal = JsonFormIoUtil.getBooleanPropertyFromNode(jsonNodeComponent, REQUIRE_DECIMAL);
        this.minValue = JsonFormIoUtil.getBigDecimalPropertyFromValidate(jsonNodeComponent, MIN);
        this.maxValue = JsonFormIoUtil.getBigDecimalPropertyFromValidate(jsonNodeComponent, MAX);
        if(requireDecimal){
            if (decimalLimit == null || decimalLimit.intValue()==0) {
                decimalLimit =  isLongNumber ? MAX_DECIMAL_PLACES : DEFAULT_DECIMAL_PLACES;
            }else{
                decimalLimit = decimalLimit > MAX_DECIMAL_PLACES ? MAX_DECIMAL_PLACES : decimalLimit;
            }
        }else {
            decimalLimit = 0;
        }
    }

    @Override
    public String getObjectTypeToModel() {
        if (requireDecimal || decimalLimit != 0) {
            return DATA_TYPE_BIG_DECIMAL;
        } else if (isLongNumber) {
            return DATA_TYPE_LONG;
        } else return DATA_TYPE_INTEGER;

    }

    @Override
    public String getObjectTypeToEntity() {
        if (getIsEncrypted()) {
            return DATA_TYPE_BYTEA;
        } else if (requireDecimal || decimalLimit != 0) {
            return DATA_TYPE_BIG_DECIMAL;
        } else if (isLongNumber) {
            return DATA_TYPE_LONG;
        } else return DATA_TYPE_INTEGER;

    }

    @Override
    public List<String> getAllAnnotationToEntity() {
        String columnDescription = String.format(COLUMN_NAME_S, getKeyToColumn());
        if (getIsUnique() || getIsRequired() || this.requireDecimal) {
            String unique = UNIQUE_TRUE;
            String required = NULLABLE_FALSE;

            columnDescription = columnDescription.concat(", ");
            if (!getIsEncrypted() && requireDecimal) {
                var precision = (isLongNumber ? MAX_INTEGER_PLACES: DEFAULT_INTEGER_PLACES) + decimalLimit ;
                String precisionScale = String.format("precision = %d, ", precision);
                String scale = String.format("scale = %d, ", decimalLimit);
                columnDescription = columnDescription.concat(precisionScale).concat(scale);
            }
            if (getIsUnique()) {
                columnDescription = columnDescription.concat(unique);
            }
            if (getIsRequired()) {
                columnDescription = columnDescription.concat(required);
            }
            columnDescription = columnDescription.substring(0, columnDescription.lastIndexOf(",")).concat(")");
        } else columnDescription = columnDescription.concat(")");
        return List.of(columnDescription);
    }

    @Override
    public List<String> getValidationToModel() {
        var validationList = new ArrayList<String>();
        if (getIsRequired()) {
            validationList.add(NOT_NULL);
        }
        if (getIsEncrypted()) {
            validationList.add(String.format(MAX_BYTE_VALUE_S, getEncryptType().getModelMaxLength()));
        }
        if (minValue != null || maxValue != null) {
            if (isLongNumber) {
                if (minValue != null) {
                    validationList.add(String.format("@MinDecimal(value = \"%s\" )", minValue));
                }
                if (maxValue != null) {
                    validationList.add(String.format("@MaxDecimal(value = \"%s\" )", maxValue));
                }
            } else {
                if (minValue != null) {
                    validationList.add(String.format("@Min(value = %s )", minValue));
                }
                if (maxValue != null) {
                    validationList.add(String.format("@Max(value = %s )", maxValue));
                }
            }
        }
        return validationList;
    }

    @Override
    public String getMethodEncrypt() {
        return String.format("return encrypt.%s(element.toString());", getEncryptType().getEncryptMethod());
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
        list.add(String.format(PRIVATE_S_TRANSFORM_S_TO_MODEL_S_ELEMENT, getObjectTypeToModel(), getKeyFormatted(), getObjectTypeToEntity()));
        String elementName = getElementNameAndAddMethodDecrypt(list);
        var objectType = getObjectTypeToModel();
        String descriptionReturn = String.format("%s.valueOf", objectType);
        if (objectType.equals(DATA_TYPE_BIG_DECIMAL)) {
            descriptionReturn = String.format("new %s", objectType);
        }
        list.add(String.format("return %s(%s);", descriptionReturn, elementName));
        list.add(CLOSE_KEY);
        return list;
    }

    @Override
    public String getConversionFromStringToObjectType(String value) {
        var objectType = getObjectTypeToModel();
        String descriptionReturn = String.format("%s.valueOf", objectType);
        if (objectType.equals(DATA_TYPE_BIG_DECIMAL)) {
            descriptionReturn = String.format("new %s", objectType);
        }
        return String.format(" %s(%s.getFirst(\"%s\"))", descriptionReturn, value, getKey());
    }
}
