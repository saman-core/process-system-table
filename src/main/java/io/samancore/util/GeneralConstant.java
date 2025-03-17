package io.samancore.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class GeneralConstant {
    public static final Set<String> RESERVED_WORDS = new HashSet<>(Arrays.asList(
            "_", "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue",
            "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "goto", "if",
            "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private",
            "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this",
            "throw", "throws", "transient", "try", "void", "volatile", "while",
            "true", "false", "null",
            "exports", "module", "non-sealed\n", "open", "opens", "permits",
            "provides", "record", "requires", "sealed", "to", "transitive", "uses",
            "var", "when", "with", "yield"));
    public static final Pattern PATTERN_LETTER_NUMBER = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
    public static final int MAX_LENGTH_NAME_ALLOWED = 19;
    public static final int MAX_LENGTH_PHONE_NUMBER = 20;
    public static final int MAX_LENGTH_TIME = 8;
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX";
    public static final int DEFAULT_DECIMAL_PLACES = 2; //TODO read from property
    public static final int DEFAULT_INTEGER_PLACES = 10; //TODO read from property
    public static final int MAX_DECIMAL_PLACES = 8; //TODO read from property
    public static final int MAX_INTEGER_PLACES = 19; //TODO read from property
    public static final String DEFAULT_NAME_NUMBER = "1";
    public static final String TEMPLATE_FILE_NAME = "template-details";
    public static final String JSON_FILE_NAME = "form.json";
    public static final String LONG_NUMBER = "longNumber";
    public static final String KEY = "key";
    public static final String PERSISTENT = "persistent";
    public static final String UNIQUE = "unique";
    public static final String VALIDATE = "validate";
    public static final String REQUIRED = "required";
    public static final String DB_INDEX = "dbIndex";
    public static final String DECIMAL_LIMIT = "decimalLimit";
    public static final String REQUIRE_DECIMAL = "requireDecimal";
    public static final String MULTIPLE = "multiple";
    public static final String DATA_TYPE_INTEGER = "Integer";
    public static final String DATA_TYPE_LONG = "Long";
    public static final String DATA_TYPE_DOUBLE = "Double";
    public static final String DATA_TYPE_BIG_DECIMAL = "java.math.BigDecimal";
    public static final String DATA_TYPE_STRING = "String";
    public static final String DATA_TYPE_BOOLEAN = "Boolean";
    public static final String DATA_TYPE_DATE = "java.util.Date";
    public static final String DATA_TYPE_BYTEA = "byte[]";
    public static final String DATABASE_TYPE_TEXT = "text";
    public static final String SET_STRING = "java.util.Set<String>";
    public static final String SET_LONG = "java.util.Set<Long>";
    public static final String PATTERN = "pattern";
    public static final String CASE = "case";
    public static final String PROTECTED = "protected";
    public static final String TRUNCATE_MULTIPLE_SPACES = "truncateMultipleSpaces";
    public static final String MIN_LENGTH = "minLength";
    public static final String MAX_LENGTH = "maxLength";
    public static final String MIN_WORDS = "minWords";
    public static final String MAX_WORDS = "maxWords";
    public static final String ENCRYPTED = "encrypted";
    public static final String MIN = "min";
    public static final String MAX = "max";
    public static final String JSON_COMPONENT_COMPONENTS = "components";
    public static final String JSON_COMPONENT_COLUMNS = "columns";
    public static final String JSON_COMPONENT_ROWS = "rows";
    public static final String COMPONENT_TEXTFIELD = "textfield";
    public static final String COMPONENT_SAMANTEXTFIELD = "samantextfield";
    public static final String COMPONENT_TEXTAREA = "textarea";
    public static final String COMPONENT_SAMANTEXTAREA = "samantextarea";
    public static final String COMPONENT_PASSWORD = "password";
    public static final String COMPONENT_SAMANPASSWORD = "samanpassword";
    public static final String COMPONENT_EMAIL = "email";
    public static final String COMPONENT_SAMANEMAIL = "samanemail";
    public static final String COMPONENT_URL = "url";
    public static final String COMPONENT_SAMANURL = "samanurl";
    public static final String COMPONENT_RADIO = "radio";
    public static final String COMPONENT_SAMANRADIO = "samanradio";
    public static final String COMPONENT_CHECKBOX = "checkbox";
    public static final String COMPONENT_SAMANCHECKBOX = "samancheckbox";
    public static final String COMPONENT_DATETIME = "datetime";
    public static final String COMPONENT_SAMANDATETIME = "samandatetime";
    public static final String COMPONENT_NUMBER = "number";
    public static final String COMPONENT_SAMANNUMBER = "samannumber";
    public static final String COMPONENT_PHONENUMBER = "phonenumber";
    public static final String COMPONENT_SAMANPHONENUMBER = "samanphonenumber";
    public static final String COMPONENT_TIME = "time";
    public static final String COMPONENT_SAMANTIME = "samantime";
    public static final String COMPONENT_SIGNATURE = "signature";
    public static final String COMPONENT_SAMANSIGNATURE = "samansignature";
    public static final String COMPONENT_SELECT = "select";
    public static final String COMPONENT_SAMANSELECT = "samanselect";
    public static final String COMPONENT_TAGS = "tags";
    public static final String COMPONENT_SAMANTAGS = "samantags";
    public static final String COMPONENT_HIDDEN = "hidden";
    public static final String COMPONENT_SAMANHIDDEN = "samanhidden";
    public static final String INPUT_MASK = "inputMask";
    public static final String WIDGET = "widget";
    public static final String FORMAT = "format";
    public static final String MIN_DATE = "minDate";
    public static final String MAX_DATE = "maxDate";
    public static final String DISABLE_WEEKDAYS = "disableWeekdays";
    public static final String DISABLE_WEEKENDS = "disableWeekends";
    public static final String DISABLED_DATES = "disabledDates";
    public static final String DELIMITER = "delimiter";
    public static final String PAIR_S = "pair%s";
    public static final String RETURN_ELEMENT = " return element;";
    public static final String RETURN_S = " return %s;";
    public static final String CLOSE_KEY = "}";
    public static final String ELEMENT_STRING_ELEMENT_TRIM_REPLACE_ALL_S_2_G = "element = ((String)element).trim().replaceAll(\"^ +| +$|( )+\", \"$1\");";
    public static final String S_S_TRIM_REPLACE_ALL_S_2_G = "%s = (%s).trim().replaceAll(\"^ +| +$|( )+\", \"$1\");";
    public static final String S_MASKER_S_APPLY_S = "%s = masker%s.apply(%s);";
    public static final String ELEMENT_STRING_ELEMENT_TO_S_JAVA_UTIL_LOCALE_ROOT = "element = ((String)element).to%s(java.util.Locale.ROOT);";
    public static final String S_S_TO_S_JAVA_UTIL_LOCALE_ROOT = "%s = (%s).to%s(java.util.Locale.ROOT);";
    public static final String PRIVATE_S_TRANSFORM_S_TO_ENTITY_S_ELEMENT = "private %s transform%sToEntity(%s element){";
    public static final String PRIVATE_S_TRANSFORM_S_TO_MODEL_S_ELEMENT = "private %s transform%sToModel(%s element){";
    public static final String NOT_BLANK = "@NotBlank";
    public static final String NOT_EMPTY = "@NotEmpty";
    public static final String NOT_NULL = "@NotNull";
    public static final String DATA_SRC = "dataSrc";
    public static final String DATA_SRC_RESOURCE = "resource";
    public static final String UNIQUE_TRUE = "unique = true, ";
    public static final String NULLABLE_FALSE = "nullable = false, ";
    public static final String COLUMN_NAME_S = "@Column( name= \"%s\"";
    public static final String COLUMN_ID = "_id";
    public static final String LENGTH_D = "length  = %d, ";
    public static final String PREFIX_TABLENAME_ST = "ST_";
    public static final String PREFIX_SEQUENCENAME_SQ = "SQ_";
    public static final String UNDERSCORE = "_";
    public static final String S_NAME_LENGTH_SHOULD_BE_MAX_S_CHARACTERS = "%s name length should be max %s characters";
    public static final String IO_SAMANCORE_COMMON_TRANSFORMER_MASKER_MASKER_S = "io.samancore.common.transformer.Masker masker%s;";
    public static final String SENSITIVE_DATA_MASK = "sensitiveDataMask";
    public static final String MAX_BYTE_VALUE_S = "@MaxByte(value = %s )";
    public static final String SIZE_MAX_D = "@Size(max = %d)";
    public static final String SIZE_MIN_D = "@Size(min = %d)";
    public static final String SIZE_MIN_D_MAX_D = "@Size(min = %d, max = %d)";
    public static final String PATTERN_REGEXP_S = "@Pattern(regexp = \"%s\")";
    public static final String ELEMENT = "element";
    public static final String NEW_ELEMENT = "newElement";


    private GeneralConstant() {
    }
}
