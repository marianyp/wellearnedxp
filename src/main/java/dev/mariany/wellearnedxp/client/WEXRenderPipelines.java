package dev.mariany.wellearnedxp.client;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.BlendFactor;
import dev.mariany.wellearnedxp.WellEarnedXP;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderPipelines;

@Environment(EnvType.CLIENT)
public class WEXRenderPipelines {
    public static final RenderPipeline GUI_TEXTURED_ADDITIVE_HIGHLIGHT = RenderPipelines.register(
            RenderPipeline
                    .builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
                    .withLocation(WellEarnedXP.id("pipeline/gui_textured_additive_highlight"))
                    .withColorTargetState(
                            new ColorTargetState(new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE))
                    )
                    .build()
    );
}
