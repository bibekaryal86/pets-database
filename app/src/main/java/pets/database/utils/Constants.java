package pets.database.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

  public static final String COLLECTION_NAME_ACCOUNT_DETAILS = "account_details";
  public static final String COLLECTION_NAME_REF_ACCOUNT_TYPE_DETAILS = "ref_account_type_details";
  public static final String COLLECTION_NAME_REF_BANK_DETAILS = "ref_bank_details";
  public static final String COLLECTION_NAME_REF_CATEGORY_DETAILS = "ref_category_details";
  public static final String COLLECTION_NAME_REF_CATEGORY_TYPE_DETAILS =
      "ref_category_type_details";
  public static final String COLLECTION_NAME_REF_MERCHANT_DETAILS = "ref_merchant_details";
  public static final String COLLECTION_NAME_REF_TRANSACTION_TYPE_DETAILS =
      "ref_transaction_type_details";
  public static final String COLLECTION_NAME_TRANSACTION_DETAILS = "transaction_details";
  public static final String COLLECTION_NAME_USER_DETAILS = "user_details";

  public static final String FIELD_NAME_DESCRIPTION = "description";
  public static final String FIELD_NAME_ID = "id";
  public static final String FIELD_NAME_USERNAME = "username";

  public static final String BASIC_AUTH_USR = "BASIC_AUTH_USR";
  public static final String BASIC_AUTH_PWD = "BASIC_AUTH_PWD";

  public static final String MONGODB_ACC_NAME = "MONGODB_ACC_NAME";
  public static final String MONGODB_USR_NAME = "MONGODB_USR_NAME";
  public static final String MONGODB_USR_PWD = "MONGODB_USR_PWD";
}
