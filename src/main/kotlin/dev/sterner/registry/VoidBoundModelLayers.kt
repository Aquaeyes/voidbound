package dev.sterner.registry

import dev.sterner.client.model.*
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry

object VoidBoundModelLayers {

    fun init() {
        EntityModelLayerRegistry.registerModelLayer(SoulSteelGolemEntityModel.LAYER_LOCATION) { SoulSteelGolemEntityModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(GolemCoreModel.LAYER_LOCATION) { GolemCoreModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(FociModel.LAYER_LOCATION) { FociModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(WandItemModel.LAYER_LOCATION) { WandItemModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(CrimsonKnightModel.LAYER_LOCATION) { CrimsonKnightModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(CrimsonArcherModel.LAYER_LOCATION) { CrimsonArcherModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(CrimsonClericModel.LAYER_LOCATION) { CrimsonClericModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(CrimsonJesterModel.LAYER_LOCATION) { CrimsonJesterModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(CrimsonHeavyKnightModel.LAYER_LOCATION) { CrimsonHeavyKnightModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(CrimsonNecromancerModel.LAYER_LOCATION) { CrimsonNecromancerModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(CrimsonBookModel.LAYER_LOCATION) { CrimsonBookModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(EnchanterCrimsonBookModel.LAYER_LOCATION) { EnchanterCrimsonBookModel.createBodyLayer() }

        EntityModelLayerRegistry.registerModelLayer(ObeliskCoreModel.LAYER_LOCATION) { ObeliskCoreModel.createBodyLayer() }
        EntityModelLayerRegistry.registerModelLayer(ObeliskModel.LAYER_LOCATION) { ObeliskModel.createBodyLayer() }

    }
}