package soundlogic.silva.client.core.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;

public class BookConverter {

	private static final String originalsPath = "../src/main/resources/assets/silva/books/original/poems.txt";
	private static final String parsedPath = "../src/main/resources/assets/silva/books/parsed/";
	
	public static void convertBooks() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(originalsPath));
		String line = reader.readLine();
		String title = "";
		String subtitle = "";
		String text = "";
		ArrayList<String[]> poems = new ArrayList<String[]>();
		boolean inPoem=false;
		while(line !=null) {
			String[] parts=line.split("\t");
			if(parts[0].equals("TITLE")) {
				String[] curPoem=new String[3];
				curPoem[0]=title;
				curPoem[1]=subtitle;
				curPoem[2]=text;
				poems.add(curPoem);
				
				title=parts[1];
				subtitle="";
				text="";
				inPoem=false;
			}
			if(parts[0].equals("SUBTITLE")) {
				subtitle=parts[1];
			}
			if(parts[0].equals("POEM")) {
				if(parts.length==1)
					text+="\n";
				else
					text+=parts[1]+"\n";
			}
			line=reader.readLine();
		}
		reader.close();
		wrapAndSaveBooks(poems);
		saveBookList(poems);
	}

	private static void wrapAndSaveBooks(ArrayList<String[]> poems) throws IOException {
		for(String[] poem : poems)
			wrapAndSaveBook(poem);
	}

	private static void wrapAndSaveBook(String[] poem) throws IOException {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		String title=poem[0];
		String subtitle=poem[1];
		String text = poem[2];
		String[] lines = text.split("\n");
		ArrayList<String> pages = new ArrayList<String>();
		String curPage="";
		int linesOnPage = 0;
		for(String line : lines) {
			if(linesOnPage == 0 && line.equals(""))
				continue;
			List<String> brokenLines = fontRenderer.listFormattedStringToWidth(line, 118);
			linesOnPage+=brokenLines.size();
			for(String brokenLine : brokenLines) {
				curPage+=brokenLine + "\n";
				linesOnPage++;
				if(linesOnPage>10 || (linesOnPage>8 && line.equals(""))) {
					pages.add(curPage);
					curPage="";
					linesOnPage = 0;
				}
			}
		}
		saveBook(title, subtitle, pages);
	}

	private static void saveBook(String title, String subtitle, ArrayList<String> pages) throws IOException {
		if(title.equals(""))
			return;
		BufferedWriter writer = new BufferedWriter(new FileWriter(parsedPath+title+".txt"));
		
		writer.write(title);
		writer.write("\n");
		writer.write(subtitle);
		writer.write("\n");
		writer.write("<END PAGE>");
		writer.write("\n");
		
		for(String page : pages) {
			writer.write(page);
			writer.write("<END PAGE>");
			writer.write("\n");
		}
		
		writer.close();
	}

	private static void saveBookList(ArrayList<String[]> poems) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(parsedPath+"bookList.txt"));
		
		for(String[] poem : poems) {
			if(poem[0]!="") {
				writer.write(poem[0]);
				writer.write("\n");
			}
		}
		
		writer.close();
	}
}
