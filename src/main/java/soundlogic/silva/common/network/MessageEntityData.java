package soundlogic.silva.common.network;

import soundlogic.silva.common.Silva;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageEntityData implements IMessage{

	private String identifier;
	private int entityID;
    private NBTTagCompound data;

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, identifier);
		buf.writeInt(entityID);
		ByteBufUtils.writeTag(buf, data);
	}

    @Override
	public void fromBytes(ByteBuf buf) {
    	identifier = ByteBufUtils.readUTF8String(buf);
    	entityID=buf.readInt();
    	data=ByteBufUtils.readTag(buf);
	}

    public static void updateExtendedEntityData(Entity entity, String identifier) {
    	if(entity.worldObj.isRemote)
    		return;
    	NBTTagCompound tag = new NBTTagCompound();
    	entity.getExtendedProperties(identifier).saveNBTData(tag);
    	MessageEntityData message = new MessageEntityData();
    	message.data=tag;
    	message.entityID=entity.getEntityId();
    	message.identifier=identifier;
    	Silva.PACKET_HANDLER.sendToAll(message);
    }
    
    public static class Handler implements IMessageHandler<MessageEntityData, IMessage>
    {
        @Override
        public IMessage onMessage(MessageEntityData message, MessageContext ctx)
        {
        	World world = Minecraft.getMinecraft().theWorld;
            Entity entity = world.getEntityByID(message.entityID);
            if (entity != null)
            {
                IExtendedEntityProperties props = entity.getExtendedProperties(message.identifier);
                props.loadNBTData(message.data);
            }
            return null;
        }
    }
}
