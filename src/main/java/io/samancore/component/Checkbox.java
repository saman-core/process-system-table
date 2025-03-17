package io.samancore.component;

import com.fasterxml.jackson.databind.JsonNode;
import io.samancore.component.base.Component;
import io.samancore.component.base.Field;
import io.samancore.type.CaseType;
import lombok.Getter;

import static io.samancore.util.GeneralConstant.DATA_TYPE_BOOLEAN;
import static io.samancore.util.GeneralConstant.DATA_TYPE_BYTEA;

@Getter
public class Checkbox extends Component implements Field {

    public Checkbox(CaseType columnCaseSensitive, JsonNode jsonNodeComponent) {
        super(columnCaseSensitive, jsonNodeComponent);
    }

    @Override
    public String getObjectTypeToModel() {
        return DATA_TYPE_BOOLEAN;
    }


    @Override
    public String getObjectTypeToEntity() {
        return getIsEncrypted() ? DATA_TYPE_BYTEA : DATA_TYPE_BOOLEAN;
    }

    @Override
    public String getConversionFromStringToObjectType(String value) {
        var objectType = getObjectTypeToModel();
        return String.format("%s.valueOf( %s.getFirst(\"%s\"))", objectType, value, getKey());
    }

    @Override
    public String getMethodEncrypt() {
        return String.format("return encrypt.%s(element.toString());", getEncryptType().getEncryptMethod());
    }

    @Override
    public String getMethodDecrypt() {
        return String.format("var newElement = Boolean.valueOf(encrypt.%s(element));", getEncryptType().getDecryptMethod());
    }
}