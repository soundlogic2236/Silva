package soundlogic.silva.common.core.handler.portal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import soundlogic.silva.common.core.handler.portal.DimensionHandler.Dimension;
import soundlogic.silva.common.lib.LibMisc;
import vazkii.botania.api.lexicon.LexiconRecipeMappings.EntryData;

public class DimensionHandler {

	public enum Dimension {
		GINNUNGAGAP ("ginnungagap", State.LOCKED, 0x376332, 0x782b79), 
		VIGRIOR ("vigrior", State.DEFAULT, 0x792b2b, 0x792b2b), 
		FOLKVANGR ("folkvangr", State.DEFAULT, 0xf6d5a0, 0xf6d5a0), 
		VALHALLA ("valhalla", State.DEFAULT, 0xe88787, 0xe88787), 
		HELHEIM ("helheim", State.DEFAULT, 0x1d2457, 0x1d2457), 
		ASGARD ("asgard", State.DEFAULT, 0xd8e825, 0xd8e825), 
		ALFHEIM ("alfheim", State.DEFAULT, 0x5fff4a, 0x00c000), 
		MINEGARD ("minegard", State.OVERWORLD, 0x00c172, 0x00c172), 
		JOTUNHEIMR ("jotunheimr", State.DEFAULT, 0x94aba5, 0x94aba5), 
		NIOAVELLIR ("nioavellir", State.DEFAULT, 0x381325, 0x381325), 
		MUSPELHEIM ("muspelheim", State.DEFAULT, 0xe03d1d, 0xe03d1d), 
		NIFLHEIM ("niflheim", State.DEFAULT, 0x1dc2e0, 0x1dc2e0), 
		NIDAVELLIR ("nidavellir", State.DEFAULT, 0xd2d5e7, 0xd2d5e7), 
		VANAHEIMR ("vanaheimr", State.DEFAULT, 0xcf52bf, 0xcf52bf);
		
		private final String unlocalizedName;
		private final State state;
		private final Color portalColor;
		private final Color sparkColor;
		
		public enum State {
			DEFAULT,
			LOCKED,
			OVERWORLD,
		}
		
		Dimension(String unlocalizedName,State state, int portalColor, int sparkColor) {
			this.unlocalizedName=unlocalizedName;
			this.state=state;
			this.portalColor=new Color(portalColor);
			this.sparkColor=new Color(sparkColor);
		}
		public String getUnlocalizedName() {
			return LibMisc.MOD_ID.toLowerCase() + ".dimension." + unlocalizedName;
		}
		public State getState() {
			return state;
		}
		public Color getPortalColor() {
			return portalColor;
		}
		public Color getSparkColor() {
			return sparkColor;
		}
	}
	
	
	public static final int SIGNATURE_WIDTH=5;
	public static final int SIGNATURE_HEIGHT=3;
	
	private static Map<boolean[][], Dimension> signatures;
	private static Map<Dimension, EntryData> signatureEntries = new HashMap();
	
	public static Dimension getDimensionFromSignature(boolean[][] signature) {
		for(Entry<boolean[][], Dimension> entry :signatures.entrySet()) {
			if(signaturesMatch(entry.getKey(),signature))
				return entry.getValue();
		}
		return null;
	}
	
	public static ArrayList<boolean[][]> getSignaturesFromDimension(Dimension dim) {
		ArrayList<boolean[][]> output=new ArrayList<boolean[][]>();
		for(Entry<boolean[][], Dimension> entry :signatures.entrySet()) {
			if(dim==entry.getValue())
				output.add(entry.getKey());
		}
		return output;
	}
	
	public static boolean dimensionMatchesSignature(Dimension dim, boolean[][] signature) {
		for(Entry<boolean[][], Dimension> entry :signatures.entrySet()) {
			if(signaturesMatch(entry.getKey(),signature))
				return entry.getValue()==dim;
		}
		return false;
	}
	
	private static boolean signaturesMatch(boolean[][] sig1, boolean[][] sig2) {
		boolean[][] output = new boolean[SIGNATURE_HEIGHT][SIGNATURE_WIDTH];
		for(int i=0;i<sig1.length;i++) {
			boolean[] row=sig1[i];
			for(int j=0;j<row.length;j++) {
				if(sig1[i][j]!=sig2[i][j])
					return false;
			}
		}
		return true;
	}
	
	public static void initSignatures() {
		signatures = new HashMap<boolean[][], Dimension>();
		addSignature(
				Dimension.GINNUNGAGAP, new int[][]	
				{{1,1,0,1,1},
				 {1,1,0,1,1},
				 {1,1,0,1,1},
				 }); // The End
		addSignature(
				Dimension.GINNUNGAGAP, new int[][]	
				{{1,1,0,1,1},
				 {1,0,1,0,1},
				 {1,1,0,1,1},
				 }); // The End
		addSignature(
				Dimension.GINNUNGAGAP, new int[][]	
				{{1,1,1,1,1},
				 {0,0,1,0,0},
				 {1,1,0,1,1},
				 }); // The End
		addSignature(
				Dimension.GINNUNGAGAP, new int[][]	
				{{1,1,1,1,1},
				 {0,0,1,0,0},
				 {1,1,0,1,1},
				 }); // The End
		addSignature(
				Dimension.VIGRIOR, new int[][]	
				{{1,1,1,0,1},
				 {1,0,1,0,1},
				 {1,0,1,1,1},
				 }); // Ragnarok Field
		addSignature(
				Dimension.FOLKVANGR, new int[][]	
				{{0,1,0,1,0},
				 {1,1,1,1,1},
				 {0,1,0,1,0},
				 }); // Field of the Host
		addSignature(
				Dimension.VALHALLA, new int[][]	
				{{1,1,1,1,1},
				 {1,0,1,0,1},
				 {1,1,1,1,1},
				 }); // Hall of the Slain
		addSignature(
				Dimension.HELHEIM, new int[][]	
				{{0,1,1,1,0},
				 {0,0,1,0,0},
				 {1,1,1,1,1},
				 }); // Realm of the Dead
		addSignature(
				Dimension.ASGARD, new int[][]	
				{{1,1,1,0,1},
				 {1,1,1,0,1},
				 {1,0,1,0,1},
				 }); // Home of the Gods
		addSignature(
				Dimension.ALFHEIM, new int[][]	
				{{0,1,1,1,0},
				 {0,1,1,1,0},
				 {0,1,0,1,1},
				 }); // Realm of the Elves
		addSignature(
				Dimension.MINEGARD, new int[][]	
				{{1,1,1,1,0},
				 {1,0,1,0,1},
				 {1,1,0,1,1},
				 }); // The overworld
		addSignature(
				Dimension.JOTUNHEIMR, new int[][]	
				{{1,1,1,1,0},
				 {1,0,1,1,1},
				 {1,0,0,1,1},
				 }); // Realm of Rocks and Caves and Giants
		addSignature(
				Dimension.NIOAVELLIR, new int[][]	
				{{1,0,1,1,0},
				 {0,1,0,1,1},
				 {1,0,0,1,1},
				 }); // Dark Elves Home
		addSignature(
				Dimension.MUSPELHEIM, new int[][]	
				{{1,0,0,0,1},
				 {0,1,0,1,0},
				 {1,0,1,0,1},
				 }); // Realm of Fire
		addSignature(
				Dimension.NIFLHEIM, new int[][]	
				{{1,0,1,0,1},
				 {0,1,0,1,0},
				 {1,0,0,0,1},
				 }); // Realm of Frost
		addSignature(
				Dimension.NIDAVELLIR, new int[][]	
				{{1,0,1,0,1},
				 {1,1,0,1,1},
				 {1,0,1,0,1},
				 }); // Dwarf's Home
	}
	
	private static boolean[][] intToBooleanSignature(int[][] input) {
		boolean[][] output = new boolean[SIGNATURE_HEIGHT][SIGNATURE_WIDTH];
		for(int i=0;i<input.length;i++) {
			int[] row=input[i];
			for(int j=0;j<row.length;j++) {
				output[i][j]=input[i][j]!=0;
			}
		}
		return output;
	}
	
	private static void addSignature(Dimension dim, int[][] signature)
	{
		signatures.put(intToBooleanSignature(signature), dim);
	}
	
	public static int[][] signatureToConnections(boolean[][] signature) {
		//-1: No connections
		// 1: Up
		// 2: Right
		// 4: Down
		// 8: Left
		// Can add
		int[][] output=new int[SIGNATURE_HEIGHT][SIGNATURE_WIDTH];
		for(int i=0;i<signature.length;i++) {
			boolean[] row=signature[i];
			for(int j=0;j<row.length;j++) {
				if(!signature[i][j])
					output[i][j]=-1;
				else
					output[i][j]=0;
			}
		}
		for(int i=1;i<signature.length;i++) {
			boolean[] row=signature[i];
			for(int j=0;j<row.length;j++) {
				if(signature[i][j])
					output[i][j]+=signature[i-1][j] ? 1 : 0;
			}
		}
		for(int i=0;i<signature.length;i++) {
			boolean[] row=signature[i];
			for(int j=1;j<row.length;j++) {
				if(signature[i][j])
					output[i][j]+=signature[i][j-1] ? 2 : 0;
			}
		}
		for(int i=0;i<signature.length-1;i++) {
			boolean[] row=signature[i];
			for(int j=0;j<row.length;j++) {
				if(signature[i][j])
					output[i][j]+=signature[i+1][j] ? 4 : 0;
			}
		}
		for(int i=0;i<signature.length;i++) {
			boolean[] row=signature[i];
			for(int j=0;j<row.length-1;j++) {
				if(signature[i][j])
					output[i][j]+=signature[i][j+1] ? 8 : 0;
			}
		}
		return output;
	}
	public static boolean[][] connectedSignatureToSide(int[][] signature, int side) {
		// 0: Up
		// 1: Right
		// 2: Down
		// 3: Left
		boolean[][] output=new boolean[SIGNATURE_HEIGHT][SIGNATURE_WIDTH];
		for(int i=0;i<signature.length;i++) {
			int[] row=signature[i];
			for(int j=0;j<row.length;j++) {
				output[i][j]=connectedNumberMatchesSide(signature[i][j],side);
			}
		}
		return output;
	}
	
	public static boolean[][] connectedSignatureToUpSide(int[][] signature) {
		return connectedSignatureToSide(signature,0);
	}
	public static boolean[][] connectedSignatureToRightSide(int[][] signature) {
		return connectedSignatureToSide(signature,1);
	}
	public static boolean[][] connectedSignatureToDownSide(int[][] signature) {
		return connectedSignatureToSide(signature,2);
	}
	public static boolean[][] connectedSignatureToLeftSide(int[][] signature) {
		return connectedSignatureToSide(signature,3);
	}
	
	private static boolean connectedNumberMatchesSide(int num,int side) {
		if(num==-1)
			return false;
		return (num & (int)Math.pow(2, side)) != 0; 
	}

	public static EntryData getSignatureEntryForDimension(Dimension dim) {
		if(signatureEntries.containsKey(dim))
			return signatureEntries.get(dim);
		return null;
	}

	public static void setSignatureEntry(Dimension dimension,
			EntryData entryData) {
		signatureEntries.put(dimension, entryData);
	}

}
