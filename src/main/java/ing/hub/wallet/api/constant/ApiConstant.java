/* (C)2025 */
package ing.hub.wallet.api.constant;

public final class ApiConstant {

	private ApiConstant() {
		// Utility class
	}

	private static final String API_PATH = "/api";

	// CONTROLLERS
	public static final String WALLET_PATH = API_PATH + "/wallets";
	public static final String TRANSACTION_PATH = API_PATH + "/transactions";
	public static final String PUBLIC_PATH = API_PATH + "/public";

	public static final String FILTER = "/filter";
	public static final String CREATE = "/create";
	public static final String WITHDRAW = "/withdraw";
	public static final String DEPOSIT = "/deposit";
	public static final String PROCESS = "/process";

	public static final String INITIALIZE = "/initialize";
}
