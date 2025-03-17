package io.samancore.component.base;

public interface Field extends Input {

    String getKey();

    Boolean getIsPersistent();

    Boolean getHasDbIndex();

    String getPairTransformToEntity();

    String getKeyFormatted();

    String getPairTransformToModel();
}
