package com.example.testeableapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Rule
import org.junit.Test

class RestaurantOrderTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // 1. Mensaje de inicio
    @Test
    fun initialScreen_showsEmptyOrderMessage() {
        composeTestRule.onNodeWithTag("emptyOrderMessage")
            .assertIsDisplayed()
    }

    // 2. Todos los items del menú visibles
    fun allMenuItems_areDisplayedWithAddButtons() {
        // Verificar categorías
        composeTestRule.onNodeWithTag("categoryTitle_Tapas").assertIsDisplayed()
        composeTestRule.onNodeWithTag("categoryTitle_Bebidas").assertIsDisplayed()
        composeTestRule.onNodeWithTag("categoryTitle_Postres").assertIsDisplayed()

        // Patatas Bravas (ID 1)
        composeTestRule.onNodeWithTag("menuItemName_1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("addButton_1").assertIsDisplayed()

        // Tarta de queso (ID 10)
        composeTestRule.onNodeWithTag("menuItemName_10").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("addButton_10").assertIsDisplayed()
    }

    // 3. El total general se actualiza correctamente al agregar un ítem
    @Test
    fun addingItem_updatesTotalValue() {
        // Añadimos Patatas Bravas (ID 1)
        composeTestRule.onNodeWithTag("addButton_1")
            .performScrollTo()
            .performClick()

            .performScrollTo()
            .assertIsDisplayed()

        // Verificamos que el valor del total aparezca
        composeTestRule.onNodeWithTag("totalValue")
            .assertIsDisplayed()
    }

    /*
       PRUEBA DE UI ADICIONAL 1: Diálogo de confirmación al realizar pedido.
    */
    @Test
    fun clickingPlaceOrder_showsConfirmationDialog() {
        // Necesitamos al menos un item para poder realizar el pedido
        composeTestRule.onNodeWithTag("addButton_2").performScrollTo().performClick()

        composeTestRule.onNodeWithTag("placeOrderButton").performScrollTo().performClick()

        // Verificar que el diálogo y su título estén presentes
        composeTestRule.onNodeWithTag("confirmationDialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("confirmationTitle").assertIsDisplayed()
    }

    /*
       PRUEBA DE UI ADICIONAL 2: Eliminación de ítem desde el resumen de pedido.
    */
    @Test
    fun decrementingItemToZero_removesItFromOrderList() {
        // Añadir item(Agua mineral ID 5)
        composeTestRule.onNodeWithTag("addButton_5").performScrollTo().performClick()

        composeTestRule.onNodeWithTag("decrementOrderItem_5")
            .performScrollTo()
            .performClick()

        // Verificar que vuelve a aparecer el mensaje de pedido vacío
        composeTestRule.onNodeWithTag("emptyOrderMessage").assertIsDisplayed()
    }
}
