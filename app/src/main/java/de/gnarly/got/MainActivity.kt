package de.gnarly.got

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import de.gnarly.got.details.HouseDetails
import de.gnarly.got.overview.HousesOverviewScreen
import de.gnarly.got.ui.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	@OptIn(ExperimentalMaterial3Api::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			AppTheme {
				Scaffold(
					topBar = {
						SmallTopAppBar(
							title = { Text(text = stringResource(id = R.string.app_name)) }
						)
					}
				) {
					Surface(
						modifier = Modifier
							.fillMaxSize()
							.padding(
								it.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr),
								it.calculateTopPadding(),
								it.calculateEndPadding(LayoutDirection.Ltr),
								it.calculateBottomPadding()
							),
						color = MaterialTheme.colorScheme.background
					) {
						with(rememberNavController().toNavGraph()) {
							NavHost {
								addHousesOverviewContent {
									HousesOverviewScreen(hiltViewModel()) { house ->
										navigateToDetails(house.id)
									}
								}
								addHouseDetailsContent {
									HouseDetails(hiltViewModel())
								}
							}
						}
					}
				}
			}
		}
	}
}

@Composable
private fun NavHostController.toNavGraph(): NavGraph =
	remember { NavGraph(this) }