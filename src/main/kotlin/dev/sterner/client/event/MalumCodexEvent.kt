package dev.sterner.client.event

import com.sammy.malum.client.screen.codex.BookWidgetStyle
import com.sammy.malum.client.screen.codex.PlacedBookEntry
import com.sammy.malum.client.screen.codex.objects.progression.ProgressionEntryObject
import com.sammy.malum.client.screen.codex.pages.recipe.SpiritInfusionPage
import com.sammy.malum.client.screen.codex.pages.text.HeadlineTextPage
import com.sammy.malum.client.screen.codex.screens.ArcanaProgressionScreen
import dev.sterner.VoidBound.modid
import dev.sterner.api.ProgressionEntryObjectExtension
import dev.sterner.registry.VoidBoundItemRegistry
import net.minecraft.nbt.CompoundTag

object MalumCodexEvent {

    val DARK_VOID_GILDED: BookWidgetStyle = BookWidgetStyle(
        BookWidgetStyle.WidgetStylePreset(modid, "void_frame"),
        BookWidgetStyle.WidgetStylePreset("dark_filling"),
        BookWidgetStyle.WidgetDesignType.GILDED
    )

    val VOID_GILDED: BookWidgetStyle = BookWidgetStyle(
        BookWidgetStyle.WidgetStylePreset(modid, "void_frame"),
        BookWidgetStyle.WidgetStylePreset("paper_filling"),
        BookWidgetStyle.WidgetDesignType.GILDED
    )

    val DARK_VOID: BookWidgetStyle = BookWidgetStyle(
        BookWidgetStyle.WidgetStylePreset(modid, "void_frame"),
        BookWidgetStyle.WidgetStylePreset("paper_filling"),
        BookWidgetStyle.WidgetDesignType.DEFAULT
    )

    val VOID: BookWidgetStyle = BookWidgetStyle(
        BookWidgetStyle.WidgetStylePreset(modid, "void_frame"),
        BookWidgetStyle.WidgetStylePreset("paper_filling"),
        BookWidgetStyle.WidgetDesignType.DEFAULT
    )


    fun addVoidBoundEntries(screen: ArcanaProgressionScreen?, entries: MutableList<PlacedBookEntry>) {


        screen?.addEntry("call_of_the_void", 0, 12) { builder ->
            builder.configureWidget { it: ProgressionEntryObject ->
                val ext = it as ProgressionEntryObjectExtension
                val item = VoidBoundItemRegistry.CALL_OF_THE_VOID.get().defaultInstance
                val tag = CompoundTag()
                tag.putBoolean("Glowing", true)
                item.tag = tag
                ext.setIcon(item).setStyle(VOID_GILDED)
            }.addPage(HeadlineTextPage("call_of_the_void", "call_of_the_void.1"))
        }

        screen?.addEntry("soul_steel_golem", 2, -2) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.SOUL_STEEL_GOLEM.get()).setStyle(VOID_GILDED)
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
                it.setIcon(VoidBoundItemRegistry.GOLEM_CORE_GATHER.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("gather_core", "gather_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.GOLEM_CORE_GATHER.get()
                    )
                )
        }

        screen?.addEntry("guard_core", 3, -1) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.GOLEM_CORE_GUARD.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("guard_core", "guard_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.GOLEM_CORE_GUARD.get()
                    )
                )
        }

        screen?.addEntry("butcher_core", 4, -1) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.GOLEM_CORE_BUTCHER.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("butcher_core", "butcher_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.GOLEM_CORE_BUTCHER.get()
                    )
                )
        }

        screen?.addEntry("harvest_core", 3, -3) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.GOLEM_CORE_HARVEST.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("harvest_core", "harvest_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.GOLEM_CORE_HARVEST.get()
                    )
                )
        }

        screen?.addEntry("lumber_core", 4, -4) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.GOLEM_CORE_LUMBER.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("lumber_core", "lumber_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.GOLEM_CORE_LUMBER.get()
                    )
                )
        }

        screen?.addEntry("fill_core", 1, -4) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.GOLEM_CORE_FILL.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("fill_core", "fill_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.GOLEM_CORE_FILL.get()
                    )
                )
        }

        screen?.addEntry("empty_core", 0, -3) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.GOLEM_CORE_EMPTY.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("empty_core", "empty_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.GOLEM_CORE_EMPTY.get()
                    )
                )
        }
    }
}