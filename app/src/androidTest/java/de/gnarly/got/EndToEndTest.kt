package de.gnarly.got

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

// improvement mock third party services
class EndToEndTest {
	@get:Rule
	val composeTestRule = createAndroidComposeRule<MainActivity>()

	@Test
	fun endToEnd(): Unit = runBlocking {
		composeTestRule.setContent {
			MainScreen()
		}

		composeTestRule.awaitIdle()
		composeTestRule.onNodeWithTag("house 1").performClick()
		composeTestRule.awaitIdle()
		val houseNameNode = composeTestRule.onNodeWithTag("house name")
		houseNameNode.assertIsDisplayed()
		houseNameNode.assertTextEquals("House Algood")
	}
}