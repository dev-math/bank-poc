package dev.math.bank.wallet

import dev.math.bank.customer.CustomerId
import java.util.UUID

data class Wallet(val id: UUID, val customerId: CustomerId, val balance: Int)
