/* (C)2025 */
package ing.hub.wallet.util;

import org.apache.commons.validator.routines.checkdigit.IBANCheckDigit;

public final class ApiUtils {

	private ApiUtils() {}

	public static boolean isValidIban(final String iban) {
		return iban != null && IBANCheckDigit.IBAN_CHECK_DIGIT.isValid(iban);
	}
}
