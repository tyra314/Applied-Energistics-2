
package appeng.client.render.tesr;


import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

import appeng.api.features.IInscriberRecipe;
import appeng.client.render.FacingToRotation;
import appeng.core.AELog;
import appeng.core.AppEng;
import appeng.tile.AEBaseTile;
import appeng.tile.misc.TileInscriber;


/**
 * Renders the dynamic parts of an inscriber (the presses, the animation and the item being smashed)
 */
public class InscriberTESR extends TileEntitySpecialRenderer<TileInscriber>
{

	private static final float ITEM_RENDER_SCALE = 1.0f / 1.2f;

	private static final ResourceLocation TEXTURE_INSIDE = new ResourceLocation( AppEng.MOD_ID, "blocks/inscriber_inside" );

	private static TextureAtlasSprite textureInside;

	@Override
	public void render( TileInscriber tile, double x, double y, double z, float partialTicks, int destroyStage, float p_render_10_ )
	{
		// render inscriber

		GlStateManager.pushMatrix();
		GlStateManager.translate( x, y, z );
		GlStateManager.translate( 0.5F, 0.5F, 0.5F );
		FacingToRotation.get( tile.getForward(), tile.getUp() ).glRotateCurrentMat();
		GlStateManager.translate( -0.5F, -0.5F, -0.5F );

		GlStateManager.color( 1.0F, 1.0F, 1.0F, 1.0F );
		GlStateManager.disableLighting();
		GlStateManager.disableRescaleNormal();

		// render sides of stamps

		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture( TextureMap.LOCATION_BLOCKS_TEXTURE );

		// << 20 | light << 4;
		final int br = tile.getWorld().getCombinedLight( tile.getPos(), 0 );
		final int var11 = br % 65536;
		final int var12 = br / 65536;

		OpenGlHelper.setLightmapTextureCoords( OpenGlHelper.lightmapTexUnit, var11, var12 );

		long absoluteProgress = 0;

		if( tile.isSmash() )
		{
			final long currentTime = System.currentTimeMillis();
			absoluteProgress = currentTime - tile.getClientStart();
			if( absoluteProgress > 800 )
			{
				tile.setSmash( false );
			}
		}

		final float relativeProgress = absoluteProgress % 800 / 400.0f;
		float progress = relativeProgress;

		if( progress > 1.0f )
		{
			progress = 1.0f - ( progress - 1.0f );
		}
		float press = 0.2f;
		press -= progress / 5.0f;

		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin( GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX );

		float middle = 0.5f;
		middle += 0.02f;
		final float TwoPx = 2.0f / 16.0f;
		final float base = 0.4f;

		final TextureAtlasSprite tas = textureInside;
		if( tas != null )
		{
			// Bottom of Top Stamp
			buffer.pos( TwoPx, middle + press, TwoPx ).tex( tas.getInterpolatedU( 2 ), tas.getInterpolatedV( 13 ) ).endVertex();
			buffer.pos( 1.0 - TwoPx, middle + press, TwoPx ).tex( tas.getInterpolatedU( 14 ), tas.getInterpolatedV( 13 ) ).endVertex();
			buffer.pos( 1.0 - TwoPx, middle + press, 1.0 - TwoPx ).tex( tas.getInterpolatedU( 14 ), tas.getInterpolatedV( 2 ) ).endVertex();
			buffer.pos( TwoPx, middle + press, 1.0 - TwoPx ).tex( tas.getInterpolatedU( 2 ), tas.getInterpolatedV( 2 ) ).endVertex();

			// Front of Top Stamp
			buffer.pos( TwoPx, middle + base, TwoPx ).tex( tas.getInterpolatedU( 2 ), tas.getInterpolatedV( 3 - 16 * ( press - base ) ) ).endVertex();
			buffer.pos( 1.0 - TwoPx, middle + base, TwoPx ).tex( tas.getInterpolatedU( 14 ), tas.getInterpolatedV( 3 - 16 * ( press - base ) ) ).endVertex();
			buffer.pos( 1.0 - TwoPx, middle + press, TwoPx ).tex( tas.getInterpolatedU( 14 ), tas.getInterpolatedV( 3 ) ).endVertex();
			buffer.pos( TwoPx, middle + press, TwoPx ).tex( tas.getInterpolatedU( 2 ), tas.getInterpolatedV( 3 ) ).endVertex();

			// Top of Bottom Stamp
			middle -= 2.0f * 0.02f;
			buffer.pos( 1.0 - TwoPx, middle - press, TwoPx ).tex( tas.getInterpolatedU( 2 ), tas.getInterpolatedV( 13 ) ).endVertex();
			buffer.pos( TwoPx, middle - press, TwoPx ).tex( tas.getInterpolatedU( 14 ), tas.getInterpolatedV( 13 ) ).endVertex();
			buffer.pos( TwoPx, middle - press, 1.0 - TwoPx ).tex( tas.getInterpolatedU( 14 ), tas.getInterpolatedV( 2 ) ).endVertex();
			buffer.pos( 1.0 - TwoPx, middle - press, 1.0 - TwoPx ).tex( tas.getInterpolatedU( 2 ), tas.getInterpolatedV( 2 ) ).endVertex();

			// Front of Bottom Stamp
			buffer.pos( 1.0 - TwoPx, middle + -base, TwoPx ).tex( tas.getInterpolatedU( 2 ), tas.getInterpolatedV( 3 - 16 * ( press - base ) ) ).endVertex();
			buffer.pos( TwoPx, middle - base, TwoPx ).tex( tas.getInterpolatedU( 14 ), tas.getInterpolatedV( 3 - 16 * ( press - base ) ) ).endVertex();
			buffer.pos( TwoPx, middle - press, TwoPx ).tex( tas.getInterpolatedU( 14 ), tas.getInterpolatedV( 3 ) ).endVertex();
			buffer.pos( 1.0 - TwoPx, middle - press, TwoPx ).tex( tas.getInterpolatedU( 2 ), tas.getInterpolatedV( 3 ) ).endVertex();
		}

		Tessellator.getInstance().draw();

		GlStateManager.popMatrix();

		// render items.
		GlStateManager.color( 1.0F, 1.0F, 1.0F, 1.0F );

		int items = 0;
		if( !tile.getStackInSlot( 0 ).isEmpty() )
		{
			items++;
		}
		if( !tile.getStackInSlot( 1 ).isEmpty() )
		{
			items++;
		}
		if( !tile.getStackInSlot( 2 ).isEmpty() )
		{
			items++;
		}

		// buffer.begin( GL11.GL_QUADS, DefaultVertexFormats.ITEM );

		if( relativeProgress > 1.0f || items == 0 )
		{
			ItemStack is = tile.getStackInSlot( 3 );

			if( is.isEmpty() )
			{
				final IInscriberRecipe ir = tile.getTask();
				if( ir != null )
				{
					is = ir.getOutput().copy();
				}
			}

			this.renderItem( is, 0.0f, tile, buffer, x, y, z );
		}
		else
		{
			this.renderItem( tile.getStackInSlot( 0 ), press, tile, buffer, x, y, z );
			this.renderItem( tile.getStackInSlot( 1 ), -press, tile, buffer, x, y, z );
			this.renderItem( tile.getStackInSlot( 2 ), 0.0f, tile, buffer, x, y, z );
		}

		// Tessellator.getInstance().draw();

		GlStateManager.enableLighting();
		GlStateManager.enableRescaleNormal();
	}

	private void renderItem( ItemStack sis, final float o, final AEBaseTile tile, final BufferBuilder tess, final double x, final double y, final double z )
	{
		if( !sis.isEmpty() )
		{
			sis = sis.copy();

			GlStateManager.pushMatrix();

			GlStateManager.translate( x, y, z );
			GlStateManager.translate( 0.5F, 0.5F, 0.5F );
			FacingToRotation.get( tile.getForward(), tile.getUp() ).glRotateCurrentMat();
			GlStateManager.translate( -0.5F, -0.5F, -0.5F );

			try
			{
				// move to center
				GlStateManager.translate( 0.5f, 0.5f + o, 0.5f );

				GlStateManager.rotate( 90, 1, 0, 0 );

				// set scale
				GlStateManager.scale( ITEM_RENDER_SCALE, ITEM_RENDER_SCALE, ITEM_RENDER_SCALE );

				// heuristic to scale items down much further than blocks
				final Block blk = Block.getBlockFromItem( sis.getItem() );
				if( blk == Blocks.AIR )
				{
					GlStateManager.scale( 0.5, 0.5, 0.5 );
				}

				// << 20 | light << 4;
				final int br = tile.getWorld().getCombinedLight( tile.getPos(), 0 );
				final int var11 = br % 65536;
				final int var12 = br / 65536;

				OpenGlHelper.setLightmapTextureCoords( OpenGlHelper.lightmapTexUnit, var11, var12 );

				Minecraft.getMinecraft().getRenderItem().renderItem( sis, ItemCameraTransforms.TransformType.FIXED );
			}
			catch( final Exception err )
			{
				AELog.debug( err );
			}

			GlStateManager.popMatrix();
		}
	}

	public static void registerTexture( TextureStitchEvent.Pre event )
	{
		textureInside = event.getMap().registerSprite( TEXTURE_INSIDE );
	}
}
