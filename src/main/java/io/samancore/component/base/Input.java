package io.samancore.component.base;

import java.util.List;

public interface Input {

    String getKeyCapitalize();

    List<String> getAllAnnotationToEntity();

    Boolean evaluateIfNeedPairToEntity();

    Boolean evaluateIfNeedPairToModel();

    List<String> getInjectToTransform();

    String getMethodEncrypt();

    String getMethodDecrypt();

    String getPairTransformToEntity();

    String getObjectTypeToEntity();

    String getPairTransformToModel();

    String getObjectTypeToModel();

    List<String> getMethodTransformToEntity();

    List<String> getMethodTransformToModel();

    Boolean evaluateIfNeedDefineIndex();

    Boolean evaluateIfNeedDefineFilter();

    String getConversionFromStringToObjectType(String value);

    Boolean evaluateIfFilterNeedDefineJoin();

    List<String> getValidationToModel();
}
