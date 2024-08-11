package dev.math.bank.customer

import java.util.UUID
import jakarta.persistence.*

@Entity
@Table(name = "customers")
data class Customer (
	@Id val id: CustomerId,
	val cpf: String,
	val name: String
)

typealias CustomerId = UUID
