package dev.sterner.common.event

import com.sammy.malum.client.screen.codex.PlacedBookEntry
import com.sammy.malum.client.screen.codex.pages.recipe.SpiritInfusionPage
import com.sammy.malum.client.screen.codex.pages.text.HeadlineTextPage
import com.sammy.malum.client.screen.codex.screens.ArcanaProgressionScreen
import dev.sterner.VoidBound.MOD_GILDED
import dev.sterner.VoidBound.VOID_GILDED
import dev.sterner.registry.VoidBoundItemRegistry

object MalumCodexEvent {

    fun addVoidBoundEntries(screen: ArcanaProgressionScreen?, entries: MutableList<PlacedBookEntry>) {


        screen?.addEntry("call_of_the_void", 0, 12) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.CALL_OF_THE_VOID.get()).setStyle(VOID_GILDED)
            }.addPage(HeadlineTextPage("call_of_the_void", "call_of_the_void.1"))
        }

        screen?.addEntry("soul_steel_golem", 2, -2) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.SOUL_STEEL_GOLEM.get()).setStyle(MOD_GILDED)
            }
                .addPage(
                    HeadlineTextPage(
                        "soul_steel_golem",
                        "soul_steel_golem.1"
                    )
                )
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.SOUL_STEEL_GOLEM.get()
                    )
                )
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.CORE_EMPTY.get()
                    )
                )
        }

        screen?.addEntry("gather_core", 1, -3) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.GOLEM_CORE_GATHER.get()).setStyle(MOD_GILDED)
            }.addPage(HeadlineTextPage("gather_core", "gather_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.GOLEM_CORE_GATHER.get()
                    )
                )
        }

        screen?.addEntry("guard_core", 3, -1) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.GOLEM_CORE_GUARD.get()).setStyle(MOD_GILDED)
            }.addPage(HeadlineTextPage("guard_core", "guard_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.GOLEM_CORE_GUARD.get()
                    )
                )
        }

        screen?.addEntry("butcher_core", 4, -1) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.GOLEM_CORE_BUTCHER.get()).setStyle(MOD_GILDED)
            }.addPage(HeadlineTextPage("butcher_core", "butcher_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.GOLEM_CORE_BUTCHER.get()
                    )
                )
        }

        screen?.addEntry("harvest_core", 3, -3) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.GOLEM_CORE_HARVEST.get()).setStyle(MOD_GILDED)
            }.addPage(HeadlineTextPage("harvest_core", "harvest_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.GOLEM_CORE_HARVEST.get()
                    )
                )
        }

        screen?.addEntry("lumber_core", 4, -4) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.GOLEM_CORE_LUMBER.get()).setStyle(MOD_GILDED)
            }.addPage(HeadlineTextPage("lumber_core", "lumber_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.GOLEM_CORE_LUMBER.get()
                    )
                )
        }

        screen?.addEntry("fill_core", 1, -4) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.GOLEM_CORE_FILL.get()).setStyle(MOD_GILDED)
            }.addPage(HeadlineTextPage("fill_core", "fill_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.GOLEM_CORE_FILL.get()
                    )
                )
        }

        screen?.addEntry("empty_core", 0, -3) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.GOLEM_CORE_EMPTY.get()).setStyle(MOD_GILDED)
            }.addPage(HeadlineTextPage("empty_core", "empty_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.GOLEM_CORE_EMPTY.get()
                    )
                )
        }
    }
}