package br.tiagohm.restler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.tiagohm.restler.ui.page.HomeMenuItem
import br.tiagohm.restler.ui.page.HomePage
import br.tiagohm.restler.ui.theme.RestlerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RestlerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet {
                                Text("Restler", modifier = Modifier.padding(16.dp))
                                Divider()
                                NavigationDrawerItem(
                                    label = { Text("Drawer Item") },
                                    selected = false,
                                    onClick = { }
                                )
                            }
                        }
                    ) {
                        HomePage(onMenuItemClick = {
                            when (it) {
                                HomeMenuItem.MENU -> {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }

                                HomeMenuItem.SEND -> Unit
                                HomeMenuItem.NEW_TAB -> Unit
                                HomeMenuItem.DUPLICATE_TAB -> Unit
                                HomeMenuItem.RESTORE_TAB -> Unit
                                HomeMenuItem.CLEAR -> Unit
                                HomeMenuItem.SAVE_AS -> Unit
                                HomeMenuItem.SETTINGS -> Unit
                            }
                        })
                    }
                }
            }
        }
    }
}
