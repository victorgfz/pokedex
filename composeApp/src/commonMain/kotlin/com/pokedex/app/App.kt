package com.pokedex.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.pokedex.app.presentation.navigation.Screen
import com.pokedex.app.presentation.screens.detail.DetailScreen
import com.pokedex.app.presentation.screens.home.HomeScreen
import com.pokedex.app.presentation.screens.pokedex.PokedexScreen
import com.pokedex.app.presentation.screens.team.TeamScreen
import com.pokedex.app.presentation.screens.team.TeamViewModel
import com.pokedex.app.presentation.theme.PokeDexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    PokeDexTheme {
        val navController  = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentDest: NavDestination? = backStackEntry?.destination

        val isHome     = currentDest?.hasRoute<Screen.Home>() == true
        val isPokedex  = currentDest?.hasRoute<Screen.PokedexList>() == true
        val isTeam     = currentDest?.hasRoute<Screen.Team>() == true
        val isDetail   = currentDest?.hasRoute<Screen.PokemonDetail>() == true

        // TeamViewModel vive no escopo do App — compartilhado entre Detail e Team
        val teamViewModel: TeamViewModel = viewModel { TeamViewModel() }
        val teamCount by teamViewModel.team.collectAsState()

        Scaffold(
            topBar = {
                if (!isHome) {
                    TopAppBar(
                        title = {
                            Text(
                                text = when {
                                    isPokedex -> "Pokédex"
                                    isTeam    -> "Meu Time"
                                    isDetail  -> "Detalhes"
                                    else      -> ""
                                }
                            )
                        },
                        navigationIcon = {
                            if (isDetail) {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Voltar"
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            },
            bottomBar = {
                if (isPokedex || isTeam) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = isPokedex,
                            onClick  = {
                                navController.navigate(Screen.PokedexList) {
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            },
                            icon  = { Icon(Icons.Filled.GridView, contentDescription = null) },
                            label = { Text("Pokédex") }
                        )
                        NavigationBarItem(
                            selected = isTeam,
                            onClick  = {
                                navController.navigate(Screen.Team) {
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            },
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (teamCount.isNotEmpty()) {
                                            Badge { Text(teamCount.size.toString()) }
                                        }
                                    }
                                ) {
                                    Icon(Icons.Filled.Star, contentDescription = null)
                                }
                            },
                            label = { Text("Meu Time") }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController    = navController,
                startDestination = Screen.Home,
                modifier         = Modifier.padding(innerPadding)
            ) {
                composable<Screen.Home> {
                    HomeScreen(
                        onNavigateToPokedex = { navController.navigate(Screen.PokedexList) },
                        onNavigateToTeam    = { navController.navigate(Screen.Team) }
                    )
                }

                composable<Screen.PokedexList> {
                    PokedexScreen(
                        onPokemonClick = { id ->
                            navController.navigate(Screen.PokemonDetail(id))
                        }
                    )
                }

                composable<Screen.PokemonDetail> { entry ->
                    val route = entry.toRoute<Screen.PokemonDetail>()
                    DetailScreen(
                        pokemonId     = route.pokemonId,
                        teamViewModel = teamViewModel,
                        onBack        = { navController.popBackStack() }
                    )
                }

                composable<Screen.Team> {
                    TeamScreen(teamViewModel = teamViewModel)
                }
            }
        }
    }
}