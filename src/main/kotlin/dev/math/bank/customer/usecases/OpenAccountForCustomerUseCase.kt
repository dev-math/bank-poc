package dev.math.bank.customer.usecases

import dev.math.bank.core.UseCase
import dev.math.bank.customer.CustomerGateway
import dev.math.bank.customer.Customer
import dev.math.bank.customer.CustomerId
import dev.math.bank.customer.CustomerAlreadyExistsException
import dev.math.bank.wallet.usecases.CreateWalletForCustomerUseCase
import java.util.UUID

class OpenAccountForCustomerUseCase(
    private val customerGateway: CustomerGateway,
    private val createWalletForCustomer: CreateWalletForCustomerUseCase
) : UseCase<OpenAccountForCustomerRequest, Customer> {
    override fun execute(request: OpenAccountForCustomerRequest): Customer {
	if (customerGateway.findCustomerByCpf(request.cpf) != null) {
	    throw CustomerAlreadyExistsException("Customer with cpf ${request.cpf} already exists")
	}

	val customer = Customer(UUID.randomUUID(), request.cpf, request.name)
	customerGateway.save(customer)
	createWalletForCustomer.execute(customer.id)
	return customer
    }

    interface CustomerGateway {
	fun findCustomerByCpf(cpf: String): Customer?
	fun save(customer: Customer): Customer
    }
}

data class OpenAccountForCustomerRequest(val name: String, val cpf: String)
