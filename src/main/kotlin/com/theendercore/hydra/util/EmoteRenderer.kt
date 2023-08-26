package com.theendercore.hydra.util


import com.mojang.blaze3d.systems.RenderSystem
import com.theendercore.hydra.HydraMod.Companion.cachePath
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.client.util.math.MatrixStack
import org.apache.commons.io.FileUtils
import org.joml.Matrix4f
import java.io.File
import java.io.IOException


object EmoteRenderer {
    //This is a heavily modified version of Minecraft 7TV emote mod renderer
    fun render(
        matrices: MatrixStack,
        emote: String,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        transparency: Float,
    ) {
        var texture: NativeImageBackedTexture? = null
        try {
            val image: NativeImage =
                NativeImage.read(FileUtils.openInputStream(File("$cachePath/emotes/$emote.png")))
            texture = NativeImageBackedTexture(image)
            texture.image!!.upload(0, 0, 0, false)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        RenderSystem.setShaderTexture(0, texture!!.glId)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)

        renderImage(matrices.peek().positionMatrix, x, y, 0, 0, w, h, 128, 128, 128, 128, transparency)
//        GL30.glDeleteTextures(texture.getGlId());
    }


    //This is a just an update version of RenderUtils.renderImage() Minecraft 7TV emote mod
    fun renderImage(
        matrix: Matrix4f?,
        x0: Float,
        y0: Float,
        u: Int,
        v: Int,
        width: Float,
        height: Float,
        regionWidth: Int,
        regionHeight: Int,
        textureWidth: Int,
        textureHeight: Int,
        transparency: Float,
    ) {
        val x1 = x0 + width
        val y1 = y0 + height
        val z = 1
        val u0 = (u + 0.0f) / textureWidth.toFloat()
        val u1 = (u + regionWidth.toFloat()) / textureWidth.toFloat()
        val v0 = (v + 0.0f) / textureHeight.toFloat()
        val v1 = (v + regionHeight.toFloat()) / textureHeight.toFloat()
        RenderSystem.enableBlend()
        if (transparency != 1f) RenderSystem.setShaderColor(1f, 1f, 1f, transparency)
        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.buffer
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE)
        RenderSystem.setShader(GameRenderer::getPositionTexProgram)
        bufferBuilder.vertex(matrix, x0, y1, z.toFloat()).texture(u0, v1).next()
        bufferBuilder.vertex(matrix, x1, y1, z.toFloat()).texture(u1, v1).next()
        bufferBuilder.vertex(matrix, x1, y0, z.toFloat()).texture(u1, v0).next()
        bufferBuilder.vertex(matrix, x0, y0, z.toFloat()).texture(u0, v0).next()
        Tessellator.getInstance().draw()
        RenderSystem.disableBlend()
    }
}