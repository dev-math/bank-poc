package dev.math.bank.wallet.usecases

import dev.math.bank.core.UseCase
import dev.math.bank.customer.CustomerId
import dev.math.bank.wallet.Wallet
import dev.math.bank.wallet.WalletGateway
import dev.math.bank.wallet.CustomerWalletAlreadyExistsException
import java.util.UUID

class CreateWalletForCustomerUseCase(
    private val walletGateway: WalletGateway
): UseCase<CustomerId, Wallet> {
    override fun execute(request: CustomerId): Wallet {
        if (walletGateway.findWalletByCustomerId(request) != null) {
            throw CustomerWalletAlreadyExistsException("Customer with id ${request} already has a wallet")
        }

        val wallet = Wallet(UUID.randomUUID(), request, 0)
        return walletGateway.save(wallet)
    }

    interface WalletGateway {
        fun save(wallet: Wallet): Wallet
        fun findWalletByCustomerId(customer: CustomerId): Wallet?
    }
}
