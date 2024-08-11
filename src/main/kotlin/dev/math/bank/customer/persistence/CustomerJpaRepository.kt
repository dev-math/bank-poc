package dev.math.bank.customer.persistence

import org.springframework.data.jpa.repository.JpaRepository
import dev.math.bank.customer.Customer
import dev.math.bank.customer.CustomerId

interface CustomerJpaRepository: JpaRepository<Customer, CustomerId> {
	fun findByCpf(cpf: String): Customer
}
