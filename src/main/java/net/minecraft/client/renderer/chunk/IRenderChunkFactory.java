package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IRenderChunkFactory
{
	RenderChunk makeRenderChunk(World worldIn, RenderGlobal p_178602_2_, BlockPos pos, int p_178602_4_);
}