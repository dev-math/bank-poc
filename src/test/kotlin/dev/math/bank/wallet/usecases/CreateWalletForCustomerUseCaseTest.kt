package dev.math.bank.wallet.usecases

import dev.math.bank.customer.CustomerId
import dev.math.bank.wallet.usecases.CreateWalletForCustomerUseCase
import dev.math.bank.wallet.Wallet
import dev.math.bank.wallet.CustomerWalletAlreadyExistsException
import dev.math.bank.wallet.WalletGateway

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import io.mockk.mockk
import io.mockk.every
import io.mockk.verify
import kotlin.test.assertTrue
import java.util.UUID

internal class CreateWalletForCustomerUseCaseTest {
    val request = UUID.randomUUID()
    val walletGateway = mockk<WalletGateway>()
    val usecase = CreateWalletForCustomerUseCase(walletGateway)

    @Test
    fun `should create and save a new walllet with 0 balance`() {
        every { walletGateway.save(any()) } returns mockk()
        every { walletGateway.findWalletByCustomerId(request) } returns null

        usecase.execute(request)

        verify(exactly = 1) {
            walletGateway.save(withArg {
                assertTrue(it.balance == 0)
                assertTrue(it.customerId == request)
            })
        }
    }

    @Test
    fun `should throw exception if customer already have a wallet`() {
        every { 
            walletGateway.findWalletByCustomerId(request) 
        } returns Wallet(UUID.randomUUID(), request, 0)

        assertThrows<CustomerWalletAlreadyExistsException> { 
            usecase.execute(request)
        }

        verify(exactly = 0) {
            walletGateway.save(any())
        }
    }
}
