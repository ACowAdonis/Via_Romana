package net.rasanovum.viaromana.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom RenderTypes that use PARTICLE vertex format to route through
 * gbuffers_textured in shader packs like Photon, achieving proper
 * translucent rendering without requiring shader modifications.
 */
@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public final class ParticleRenderTypes extends RenderStateShard {

    private ParticleRenderTypes() {
        super("via_romana_particle", () -> {}, () -> {});
    }

    private static final ShaderStateShard PARTICLE_SHADER_STATE = new ShaderStateShard(GameRenderer::getParticleShader);

    /**
     * Creates a translucent RenderType using PARTICLE vertex format.
     * This should route to gbuffers_textured in Iris/Photon which uses
     * the forward translucent pipeline with proper alpha blending.
     */
    public static RenderType particleTranslucent(ResourceLocation texture) {
        return RenderType.create(
            "viaromana_particle_translucent",
            DefaultVertexFormat.PARTICLE,
            VertexFormat.Mode.QUADS,
            1536,  // Buffer size
            false, // Affects crumbling
            true,  // Sort on upload (translucent)
            RenderType.CompositeState.builder()
                .setShaderState(PARTICLE_SHADER_STATE)
                .setTextureState(new TextureStateShard(texture, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setWriteMaskState(COLOR_DEPTH_WRITE)
                .createCompositeState(false)
        );
    }
}
