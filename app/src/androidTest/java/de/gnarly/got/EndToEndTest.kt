package de.gnarly.got

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

// improvement mock third party services
@OptIn(ExperimentalCoroutinesApi::class)
class EndToEndTest {
	@get:Rule
	val composeTestRule = createAndroidComposeRule<MainActivity>()

	@Test
	fun endToEnd(): Unit = runTest {
		composeTestRule.setContent {
			MainScreen()
		}

		composeTestRule.waitUntil(10_000) {
			composeTestRule.onAllNodesWithTag("house 1").fetchSemanticsNodes().size == 1
		}
		composeTestRule.onNodeWithTag("house 1").performClick()
		composeTestRule.awaitIdle()
		val houseNameNode = composeTestRule.onNodeWithTag("house name")
		houseNameNode.assertIsDisplayed()
		houseNameNode.assertTextEquals("House Algood")
	}
}