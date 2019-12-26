package com.cluster.warehouse.config;

/**
 * Application constants.
 */
public final class Constants {

    //profiles
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_CLOUD = "cloud";

	//Date Formatter
	public static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
	public static final String DATE_FORMAT = "dd-MM-yyyy";

    public static final String VALID_COUNT = "validCount";
    public static final String INVALID_COUNT = "invalidCount";


	//Collection Names
	public static final String VALID_DEAL = "deal";
	public static final String INVALID_DEAL = "invalid_deal";

	//Deal fields
	public static final String FAILED_REASON = "reason";
	public static final String EXTENSION = "extension";
	public static final String FILE_SOURCE = "source";
	public static final String UPLOADED_ON = "uploadedOn";
	public static final String FROM_ISO_CODE = "fromIsoCode";
	public static final String TO_ISO_CODE = "toIsoCode";
	public static final String AMOUNT = "amount";
	public static final String TIME = "time";
	public static final String ID = "id";
	public static final String DEAL_ID = "dealId";

	//Document Fields
	public static final String _ID = "_id";
	public static final String _TO_ISO_CODE = "to_iso_code";
	public static final String _FROM_ISO_CODE = "from_iso_code";
	public static final String _UPLOADED_ON = "uploaded_on";

	//Summary fields
	public static final String DURATION = "duration";
	public static final String TOTAL = "total";
	public static final String VALID = "valid";
	public static final String INVALID = "invalid";
	public static final String DATE = "date";
	public static final String LAST_UPDATED = "lastUpdated";

	//Processing Error
	public static final String FAILED_DUPLICATE_ERROR = "Already exist";
	public static final String FAILED_MUST_NOT_BE_NULL = " cannot be empty.";
	public static final String FAILED_REQUIRED_SIZE = "Incomplete field value for {}";
	public static final String FAILED_REQUIRED_AMOUNT = "Amount must be a number";
	public static final String FAILED_REQUIRED_ISO = " should have 3 characters";
	public static final String FAILED_REQUIRED_TIME = "Does not have a valid time, use " + DATE_TIME_FORMAT;


    private Constants() {
    }
}
