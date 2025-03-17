package io.samancore.component;

import com.fasterxml.jackson.databind.JsonNode;
import io.samancore.component.base.Component;
import io.samancore.component.base.Field;
import io.samancore.type.CaseType;
import io.samancore.util.JsonFormIoUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static io.samancore.util.GeneralConstant.*;

@Getter
public class Datetime extends Component implements Field {

    private String format = "yyyy-MM-dd hh:mm a";
    private Boolean disableWeekends = false;
    private Boolean disableWeekdays = false;
    private String disabledDates = null;
    private String minDate = null;
    private String maxDate = null;


    public Datetime(CaseType columnCaseSensitive, JsonNode jsonNodeComponent) {
        super(columnCaseSensitive, jsonNodeComponent);
        disableWeekdays = JsonFormIoUtil.getBooleanPropertyFromWidget(jsonNodeComponent, DISABLE_WEEKDAYS);
        disableWeekends = JsonFormIoUtil.getBooleanPropertyFromWidget(jsonNodeComponent, DISABLE_WEEKENDS);
        disabledDates = JsonFormIoUtil.getStringPropertyFromWidget(jsonNodeComponent, DISABLED_DATES);
        format = JsonFormIoUtil.getStringPropertyFromWidget(jsonNodeComponent, FORMAT);
        minDate = JsonFormIoUtil.getStringPropertyFromWidget(jsonNodeComponent, MIN_DATE);
        maxDate = JsonFormIoUtil.getStringPropertyFromWidget(jsonNodeComponent, MAX_DATE);

    }

    @Override
    public String getObjectTypeToModel() {
        return DATA_TYPE_DATE;
    }


    @Override
    public String getObjectTypeToEntity() {
        return getIsEncrypted() ? DATA_TYPE_BYTEA : DATA_TYPE_DATE;
    }

    @Override
    public List<String> getValidationToModel() {
        var validationList = new ArrayList<String>();
        if (getDisabledDates() != null || getDisableWeekdays() || getDisableWeekends()) {
            validationList.add(String.format("@DateDisable(disableDates=\"%s\", disableWeekdays=%s, disableWeekends=%s)", getDisabledDates() != null ? getDisabledDates() : "", getDisableWeekdays(), getDisableWeekends()));
        }
        if (getMinDate() != null || getMaxDate() != null) {
            validationList.add(String.format(" @DateLimit(minDate = \"%s\", maxDate = \"%s\")", getMinDate() != null ? getMinDate() : "", getMaxDate() != null ? getMaxDate() : ""));
        }
        return validationList;
    }

    @Override
    public String getMethodEncrypt() {
        return String.format("return encrypt.%s(String.valueOf(element.getTime()));", getEncryptType().getEncryptMethod());
    }

    @Override
    public String getMethodDecrypt() {
        return String.format("var newElement = new java.util.Date(Long.valueOf(encrypt.%s(element)));", getEncryptType().getDecryptMethod());
    }

    @Override
    public String getConversionFromStringToObjectType(String value) {
        var objectType = getObjectTypeToModel();
        return String.format("DateUtils.parseDate( %s.getFirst(\"%s\"), \"%s\")", value, getKey(), DATE_FORMAT);
    }
}