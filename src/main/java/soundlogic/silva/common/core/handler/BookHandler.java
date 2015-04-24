package soundlogic.silva.common.core.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import soundlogic.silva.common.lib.LibMisc;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class BookHandler {

	static ResourceLocation bookList = new ResourceLocation(LibMisc.MOD_ID.toLowerCase(), "books/parsed/bookList.txt");
	static ArrayList<BookData> bookData = null;
	static Random random;
	
	public static class BookData {
		private final String title;
		private final String[] pages;
		
		private BookData (String title, String[] pages) {
			this.title=title;
			this.pages=pages;
		}
		
	}
	
	public static void loadBooks() {
		bookData = new ArrayList<BookData>();
		random = new Random();
		try {
			IResource bookList = Minecraft.getMinecraft().getResourceManager().getResource(BookHandler.bookList);
			InputStream bookStream = bookList.getInputStream();
			ArrayList<String> bookNames = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(bookStream));
			String line=reader.readLine();
			while(line != null && !line.equals("")) {
				bookNames.add(line);
				line=reader.readLine();
			}
			loadBooks(bookNames);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void loadBooks(ArrayList<String> bookNames) throws IOException {
		for(String name : bookNames)
			loadBook(name);
	}

	private static void loadBook(String name) throws IOException {
		ResourceLocation bookLocation = new ResourceLocation(LibMisc.MOD_ID.toLowerCase(), "books/parsed/"+name+".txt");
		IResource bookResource = Minecraft.getMinecraft().getResourceManager().getResource(bookLocation);
		InputStream bookStream = bookResource.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(bookStream));
		ArrayList<String> pages = new ArrayList<String>();
		String page = "";
		String line=reader.readLine();
		while(line!=null) {
			if(line.equals("<END PAGE>")) {
				pages.add(page);
				page="";
			}
			else {
				page+=line+"\n";
			}
			line=reader.readLine();
		}
		String[] data = new String[pages.size()];
		for(int i=0;i<data.length;i++)
			data[i]=pages.get(i);
		bookData.add(new BookData(name,data));
	}
	
	public static BookData getRandomBook() {
		System.out.println(bookData.size());
		return bookData.get(random.nextInt(bookData.size()));
	}
	
	public static NBTTagCompound getBookNBTData(String author, BookData book) {
		NBTTagCompound output = new NBTTagCompound();
		output.setString("author", author);
		output.setString("title", book.title);
		NBTTagList pages = new NBTTagList();
		for(String page : book.pages) {
			pages.appendTag(new NBTTagString(page));
		}
		output.setTag("pages", pages);
		return output;
	}
	
	public static ItemStack getRandomBookForPlayer(EntityPlayer player) {
		ItemStack written = new ItemStack(Items.written_book);
		written.setTagCompound(getBookNBTData(player.getCommandSenderName(), getRandomBook()));
		return written;
	}
	
}
