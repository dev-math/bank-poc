package dev.math.bank.customer.persistence

import dev.math.bank.customer.CustomerGateway
import dev.math.bank.customer.Customer
import org.springframework.stereotype.Component

@Component
class CustomerJpaAdapter(private val repository: CustomerJpaRepository): CustomerGateway {
	override fun findCustomerByCpf(cpf: String): Customer? {
		return repository.findByCpf(cpf)
	}

	override fun save(customer: Customer): Customer {
		return repository.save(customer)
	}
}
