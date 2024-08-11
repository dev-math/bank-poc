package dev.math.bank.customer.persistence

import dev.math.bank.customer.Customer
import dev.math.bank.customer.CustomerId
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import jakarta.persistence.EntityManager

@ExtendWith(SpringExtension::class)
@DataJpaTest
internal class CustomerRepositoryIntegrationTest {
    @Autowired
    private lateinit var repository: CustomerJpaRepository

    private val gateway: CustomerJpaAdapter by lazy {
        CustomerJpaAdapter(repository)
    }

    private val customerId = UUID.randomUUID()
    private val customer = Customer(
        id = customerId,
        cpf = "feel the night",
        name = "J Cole"
    )

    @Test
    fun `should save a customer`() {
        gateway.save(customer)

        val foundCostumer = repository.findById(customerId).orElse(null)

        assertTrue(foundCostumer.equals(customer))
    }

    @Test
    fun `should find customer by cpf`() {
        gateway.save(customer)

        val foundCostumer = gateway.findCustomerByCpf(customer.cpf)

        assertEquals(foundCostumer?.cpf, customer.cpf)
    }
}
