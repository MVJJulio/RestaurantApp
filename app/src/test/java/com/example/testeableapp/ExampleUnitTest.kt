package com.example.testeableapp

import com.example.testeableapp.model.MenuData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Pruebas Unitarias para RestaurantViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {

    private lateinit var viewModel: RestaurantViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RestaurantViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // 1. Agregar item al pedido
    @Test
    fun addItem_increasesQuantity() {
        val itemId = 1 // Patatas Bravas
        viewModel.addItem(itemId)
        
        assertEquals(1, viewModel.quantities.value[itemId])
        assertEquals(1, viewModel.orderedItems.value.size)
    }

    // 2. Incrementar/Decrementar cantidad
    @Test
    fun incrementDecrement_modifiesQuantity() {
        val itemId = 1
        viewModel.addItem(itemId)
        
        viewModel.incrementItem(itemId)
        assertEquals(2, viewModel.quantities.value[itemId])
        
        viewModel.decrementItem(itemId)
        assertEquals(1, viewModel.quantities.value[itemId])
    }

    // 3. Eliminar item al decrementar desde 1
    @Test
    fun decrementFromOne_removesItem() {
        val itemId = 5 // Agua
        viewModel.addItem(itemId)
        assertEquals(1, viewModel.quantities.value[itemId])
        
        viewModel.decrementItem(itemId)
        assertFalse("El item debería ser eliminado del mapa", viewModel.quantities.value.containsKey(itemId))
        assertTrue("La lista de items pedidos debería estar vacía", viewModel.orderedItems.value.isEmpty())
    }

    // 4. Cálculo del total a pagar
    @Test
    fun totalCalculation_isCorrect() {
        // Patatas Bravas (5.50) + 2x Cerveza (3.00 * 2) = 11.50
        viewModel.addItem(1)
        viewModel.addItem(7)
        viewModel.incrementItem(7)
        
        val expectedTotal = 5.50 + (2 * 3.00)
        assertEquals(expectedTotal, viewModel.total.value, 0.001)
    }

    /* 
       PRUEBA ADICIONAL 1: Reseteo del estado tras confirmar pedido.
    */
    @Test
    fun dismissConfirmation_clearsAllState() {
        viewModel.addItem(1)
        viewModel.placeOrder()
        
        viewModel.dismissConfirmation()
        
        assertTrue(viewModel.quantities.value.isEmpty())
        assertNull(viewModel.confirmation.value)
        assertTrue(viewModel.isEmpty.value)
    }

    /* 
       PRUEBA ADICIONAL 2: Protección contra pedidos vacíos.
    */
    @Test
    fun placeOrder_whenEmpty_doesNotCreateConfirmation() {
        assertTrue(viewModel.orderedItems.value.isEmpty())
        viewModel.placeOrder()
        assertNull(viewModel.confirmation.value)
    }
}
