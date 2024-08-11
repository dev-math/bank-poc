package dev.math.bank.customer.usecases

import dev.math.bank.customer.Customer
import dev.math.bank.customer.CustomerId
import dev.math.bank.customer.CustomerGateway
import dev.math.bank.customer.CustomerAlreadyExistsException
import dev.math.bank.wallet.usecases.CreateWalletForCustomerUseCase

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import io.mockk.mockk
import io.mockk.every
import io.mockk.verify
import io.mockk.Called
import io.mockk.slot
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import java.util.UUID

internal class OpenAccountForCustomerUseCaseTest {
    val request = OpenAccountForCustomerRequest("Travis Scott", "11111111111")
    val createWalletForCustomer = mockk<CreateWalletForCustomerUseCase>()
    val customerGateway = mockk<CustomerGateway>()
    val usecase = OpenAccountForCustomerUseCase(customerGateway, createWalletForCustomer)

    @Test
    fun `should register a customer and request a new wallet for him`() {
        every { customerGateway.findCustomerByCpf(request.cpf) } returns null
        every { customerGateway.save(any()) } returns mockk()

        val walletCustomerId = slot<UUID>()
        every { createWalletForCustomer.execute(capture(walletCustomerId)) } returns mockk()

        usecase.execute(request)

        verify(exactly = 1) {
            createWalletForCustomer.execute(any())
            customerGateway.save(withArg { arg ->
                assertTrue(request.cpf == arg.cpf)
                assertTrue(request.name == arg.name)
                assertTrue(walletCustomerId.captured.equals(arg.id))
            })
        }
    }

    @Test
    fun `should throw exception if customer is already registred`() {
        val customer = Customer(UUID.randomUUID(), request.name, request.cpf)
        every { customerGateway.findCustomerByCpf(request.cpf) } returns customer
        every { customerGateway.save(any()) } returns mockk()

        assertThrows<CustomerAlreadyExistsException> {
            usecase.execute(request)
        }

        verify(exactly = 0) {
            customerGateway.save(any())
            createWalletForCustomer.execute(any())
        }
    }
}
