package Data;
import java.awt.Image;

/**
 * The class for the Game data object.
 * @author Aiden Pearson
 */
public class Game {
	String name;
	String description;
	String iconRef; //File link to the image
	String genre;
	//String[] filters;
	
	double[] price = new double[4];
	//0 - Generic
	//After match platforms
	
	boolean[] platforms = new boolean[3];
	//0 - Steam
	//1 - Epic
	//2 - Switch
	
	/**
	 * Creates a game object using the name.
	 * @param name Name of the game
	 */
	public Game(String name) {
		this.name = name;
	}
	
	//Get and set methods for name
	public void SetName(String name) {
		this.name = name;
	}
	
	public String GetName() {
		return name;
	}
	
	//Get and set method for description
	public void SetDescription(String description) {
		this.description = description;
	}
	
	public String GetDescription() {
		return description;
	}
	
	//Get and set iconRef
	public void SetIconRef(String iconRef) {
		this.iconRef = "iconImages/" + iconRef;
	}
	
	public String GetIconRef() {
		return iconRef;
	}
	
	//Get and set genre
	public void SetGenre(String genre) {
		this.genre = genre;
	}
	
	public String GetGenre() {
		return genre;
	}
	
	//Get and set price, with catches for just one value
	public void SetPrice(double price) {
		for (int i = 0; i < this.price.length; i++) {
			this.price[i] = price;
		}
	}
	
	public void SetPrice(double[] price) {
		this.price = price;
	}
	
	public double GetPrice() {
		return price[0];
	}
	
	public double[] GetPriceAll() {
		return price;
	}
	
	//Get or set platforms
	public void SetPlatforms(boolean[] platforms) {
		this.platforms = platforms;
	}
	
	public void SetPlatforms(boolean platform, int position) {
		this.platforms[position] = platform;
	}
	
	public boolean[] GetPlatforms() {
		return platforms;
	}
	
	public boolean GetPlatform(int position) {
		return platforms[position];
	}
}
