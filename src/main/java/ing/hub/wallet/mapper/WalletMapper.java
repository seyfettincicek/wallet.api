/* (C)2025 */
package ing.hub.wallet.mapper;

import ing.hub.wallet.api.dto.WalletDto;
import ing.hub.wallet.entity.Wallet;

public final class WalletMapper {
	private WalletMapper() {
		// WalletMapper
	}

	public static WalletDto toDto(final Wallet wallet) {

		return WalletDto.builder()
				.id(wallet.getId())
				.customerId(wallet.getCustomer().getId())
				.walletName(wallet.getWalletName())
				.currency(wallet.getCurrency())
				.activeForShopping(wallet.getActiveForShopping())
				.activeForWithdraw(wallet.getActiveForWithdraw())
				.balance(wallet.getBalance())
				.usableBalance(wallet.getUsableBalance())
				.build();
	}
}
