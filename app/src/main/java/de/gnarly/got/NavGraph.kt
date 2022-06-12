package de.gnarly.got

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class NavGraph(private val navHostController: NavHostController) {

	private val startDestination: String = Destination.HousesOverview.routePattern

	@Composable
	fun NavHost(content: NavGraphBuilder.() -> Unit) {
		NavHost(navController = navHostController, startDestination = startDestination) {
			content()
		}
	}

	fun NavGraphBuilder.addHousesOverviewContent(content: @Composable HousesOverviewContext.(NavBackStackEntry) -> Unit) {
		composable(Destination.HousesOverview.routePattern) {
			HousesOverviewContext(navHostController).content(it)
		}
	}

	fun NavGraphBuilder.addHouseDetailsContent(content: @Composable HouseDetailContext.(NavBackStackEntry) -> Unit) {
		composable(Destination.HouseDetails.routePattern) {
			HouseDetailContext(it).content(it)
		}
	}
}

class HousesOverviewContext(private val navHostController: NavHostController) {
	fun navigateToDetails(id: Int) {
		navHostController.navigate(Destination.HouseDetails.createRoute(id))
	}
}

class HouseDetailContext(private val navBackStackEntry: NavBackStackEntry) {
	val paramId: Int
		get() = navBackStackEntry.arguments?.getString(Destination.HouseDetails.paramId)?.toIntOrNull() ?: 0
}

private sealed class Destination(
	val name: String
) {
	abstract val routePattern: String

	object HousesOverview : Destination("overview") {
		override val routePattern: String = name
	}

	object HouseDetails : Destination("details") {
		const val paramId = "id"
		override val routePattern: String = "$name/{$paramId}"
		fun createRoute(id: Int): String = "$name/$id"
	}
}