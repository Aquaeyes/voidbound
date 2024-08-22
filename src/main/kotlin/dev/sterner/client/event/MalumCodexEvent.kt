package dev.sterner.client.event

import com.sammy.malum.client.screen.codex.BookWidgetStyle
import com.sammy.malum.client.screen.codex.PlacedBookEntry
import com.sammy.malum.client.screen.codex.PlacedBookEntryBuilder
import com.sammy.malum.client.screen.codex.objects.progression.ProgressionEntryObject
import com.sammy.malum.client.screen.codex.pages.recipe.SpiritInfusionPage
import com.sammy.malum.client.screen.codex.pages.text.HeadlineTextPage
import com.sammy.malum.client.screen.codex.screens.ArcanaProgressionScreen
import com.sammy.malum.client.screen.codex.screens.VoidProgressionScreen
import dev.sterner.VoidBound.modid
import dev.sterner.api.book.ProgressionEntryObjectExtension
import dev.sterner.registry.VoidBoundItemRegistry
import net.minecraft.nbt.CompoundTag

object MalumCodexEvent {

    private val DARK_VOID_GILDED: BookWidgetStyle = BookWidgetStyle(
        BookWidgetStyle.WidgetStylePreset(modid, "void_frame"),
        BookWidgetStyle.WidgetStylePreset("dark_filling"),
        BookWidgetStyle.WidgetDesignType.GILDED
    )

    private val VOID_GILDED: BookWidgetStyle = BookWidgetStyle(
        BookWidgetStyle.WidgetStylePreset(modid, "void_frame"),
        BookWidgetStyle.WidgetStylePreset("paper_filling"),
        BookWidgetStyle.WidgetDesignType.GILDED
    )

    private val DARK_VOID: BookWidgetStyle = BookWidgetStyle(
        BookWidgetStyle.WidgetStylePreset(modid, "void_frame"),
        BookWidgetStyle.WidgetStylePreset("dark_filling"),
        BookWidgetStyle.WidgetDesignType.DEFAULT
    )

    private val VOID: BookWidgetStyle = BookWidgetStyle(
        BookWidgetStyle.WidgetStylePreset(modid, "void_frame"),
        BookWidgetStyle.WidgetStylePreset("paper_filling"),
        BookWidgetStyle.WidgetDesignType.DEFAULT
    )

    fun addVoidBoundVoidEntries(screen: VoidProgressionScreen?, entries: MutableList<PlacedBookEntry>) {
        screen?.addEntry(
            "void.portable_hole", -6, 9
        ) { b: PlacedBookEntryBuilder ->
            b
                .withTraceFragmentEntry()
                .configureWidget { w: ProgressionEntryObject ->
                    w.setIcon(
                        VoidBoundItemRegistry.PORTABLE_HOLE_FOCI.get()
                    ).setStyle(VOID)
                }
                .addPage(HeadlineTextPage("void.portable_hole", "void.portable_hole.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.PORTABLE_HOLE_FOCI.get()
                    )
                )
                .afterUmbralCrystal()
        }

        screen?.addEntry(
            "void.warding", -7, 8
        ) { b: PlacedBookEntryBuilder ->
            b
                .withTraceFragmentEntry()
                .configureWidget { w: ProgressionEntryObject ->
                    w.setIcon(
                        VoidBoundItemRegistry.WARDING_FOCI.get()
                    ).setStyle(DARK_VOID)
                }
                .addPage(HeadlineTextPage("void.warding", "void.warding.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.WARDING_FOCI.get()
                    )
                )
                .afterUmbralCrystal()
        }

        screen?.addEntry(
            "void.osmotic_enchanter", -7, 10
        ) { b: PlacedBookEntryBuilder ->
            b
                .withTraceFragmentEntry()
                .configureWidget { w: ProgressionEntryObject ->
                    w.setIcon(
                        VoidBoundItemRegistry.OSMOTIC_ENCHANTER.get()
                    ).setStyle(VOID_GILDED)
                }
                .addPage(HeadlineTextPage("void.osmotic_enchanter", "void.osmotic_enchanter.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.OSMOTIC_ENCHANTER.get()
                    )
                )
                .afterUmbralCrystal()
        }
    }

    fun addVoidBoundEntries(screen: ArcanaProgressionScreen?, entries: MutableList<PlacedBookEntry>) {
        screen?.addEntry("call_of_the_void", 0, 12) { builder ->
            builder.configureWidget { it: ProgressionEntryObject ->
                val ext = it as ProgressionEntryObjectExtension
                val item = VoidBoundItemRegistry.CALL_OF_THE_VOID.get().defaultInstance
                val tag = CompoundTag()
                tag.putBoolean("Glowing", true)
                item.tag = tag
                ext.`voidbound$setIcon`(item).setStyle(VOID_GILDED)
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


        //-9, 5

        screen?.addEntry("boots_of_the_traveller", -12, 4) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.BOOTS_OF_THE_TRAVELLER.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("boots_of_the_traveller", "boots_of_the_traveller.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.BOOTS_OF_THE_TRAVELLER.get()
                    )
                )
        }

        screen?.addEntry("hallowed_wand", -13, 5) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.HALLOWED_GOLD_CAPPED_RUNEWOOD_WAND.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("hallowed_wand", "hallowed_wand.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.HALLOWED_GOLD_CAPPED_RUNEWOOD_WAND.get()
                    )
                )
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.SOUL_STAINED_STEEL_CAPPED_SOULWOOD_WAND.get()
                    )
                )
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.CRYSTAL_FOCI.get()
                    )
                )
        }

        screen?.addEntry("fire_foci", -14, 5) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.FIRE_FOCI.get()).setStyle(DARK_VOID)
            }.addPage(HeadlineTextPage("fire_foci", "fire_foci.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.FIRE_FOCI.get()
                    )
                )
        }

        screen?.addEntry("shock_foci", -14, 4) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.SHOCK_FOCI.get()).setStyle(DARK_VOID)
            }.addPage(HeadlineTextPage("shock_foci", "shock_foci.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.SHOCK_FOCI.get()
                    )
                )
        }

        screen?.addEntry("excavation_foci", -14, 6) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.EXCAVATION_FOCI.get()).setStyle(DARK_VOID)
            }.addPage(HeadlineTextPage("excavation_foci", "excavation_foci.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.EXCAVATION_FOCI.get()
                    )
                )
        }

        screen?.addEntry("hallowed_goggles", -10, 4) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.HALLOWED_GOGGLES.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("hallowed_goggles", "hallowed_goggles.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.HALLOWED_GOGGLES.get()
                    )
                )
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.HALLOWED_MONOCLE.get()
                    )
                )
        }

        screen?.addEntry("pickaxe_of_the_core", -10, 3) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.PICKAXE_OF_THE_CORE.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("pickaxe_of_the_core", "pickaxe_of_the_core.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.PICKAXE_OF_THE_CORE.get()
                    )
                )
        }

        screen?.addEntry("axe_of_the_stream", -11, 3) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.AXE_OF_THE_STREAM.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("axe_of_the_stream", "axe_of_the_stream.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.AXE_OF_THE_STREAM.get()
                    )
                )
        }

        screen?.addEntry("shovel_of_the_earthmover", -9, 3) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.SHOVEL_OF_THE_EARTHMOVER.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("shovel_of_the_earthmover", "shovel_of_the_earthmover.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.SHOVEL_OF_THE_EARTHMOVER.get()
                    )
                )
        }

        screen?.addEntry("sword_of_the_zephyr", -10, 2) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.SWORD_OF_THE_ZEPHYR.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("sword_of_the_zephyr", "sword_of_the_zephyr.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.SWORD_OF_THE_ZEPHYR.get()
                    )
                )
        }

        screen?.addEntry("hoe_of_growth", -11, 2) { builder ->
            builder.configureWidget {
                it.setIcon(VoidBoundItemRegistry.HOE_OF_GROWTH.get()).setStyle(VOID)
            }.addPage(HeadlineTextPage("hoe_of_growth", "hoe_of_growth.1"))
                .addPage(
                    SpiritInfusionPage.fromOutput(
                        VoidBoundItemRegistry.HOE_OF_GROWTH.get()
                    )
                )
        }
    }
}