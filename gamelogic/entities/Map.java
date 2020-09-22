package gamelogic.entities;

import java.io.Serializable;
import java.io.*;
import java.util.*;

public class Map implements Serializable{

	int hCount = 48, vCount = 24;
	int size = hCount * vCount;
	boolean[][] maze;
	int[] spawnPoints = {1,1,1,22,45,1,45,22};

	public Map ()
    {
        maze = generateMap();
    }

	/**
	 * select a random configuration for a corner
	 * @param file the corner to choose from
	 * @param size the number of tiles in the corner
	 * @return the character data for the tiles
	 */
    private static ArrayList<Character> chooseRandomSection(String file,int size)
	{
		ArrayList<Character> characters = new ArrayList<Character>(size/4);
		Random rand = new Random();
		int randomNum = rand.nextInt(3);
		if(randomNum == 0) { readFile(characters, "src/res/map/" + file + ".txt");}
		else if(randomNum == 1) { readFile(characters, "src/res/map/" + file + "2.txt");}
		else { readFile(characters, "src/res/map/" + file + "3.txt");}

		return characters;
	}

	/**
	 * read a file and write character data to an arraylist
	 * @param map the arraylist to write to
	 * @param filePath the file to read from
	 */
	private static void readFile(ArrayList<Character> map, String filePath) {
		try {
	        FileInputStream inputStream = new FileInputStream(filePath);
	        while (inputStream.available() > 0) {
	            char inputData = (char) inputStream.read();
	            map.add(inputData);
	        }
	        inputStream.close();
	    } catch (IOException ioe) {
	        System.out.println("Trouble reading from the file: " + ioe.getMessage());
	    }
	}

	/**
	 * create a map with randomly selected corners
	 * @return the 2d array representing the map, true for filled tiles and false for empty
	 */
	private boolean[][] generateMap() {
		boolean[][] maze = new boolean[hCount][vCount];
		ArrayList<Character> bottomLeft = chooseRandomSection("bottomLeft",size/4);
		ArrayList<Character> bottomRight = chooseRandomSection("bottomRight",size/4);
		ArrayList<Character> topRight = chooseRandomSection("topRight",size/4);
		ArrayList<Character> topLeft = chooseRandomSection("topLeft",size/4);

		int currentY = 0, currentX = 0;
		for(char currentChar:topLeft)
		{
			if(currentChar == 'w')
			{
				maze[currentX % (hCount/2)][currentY] = true;
			}
			else
			{
				maze[currentX % (hCount/2)][currentY] = false;
			}

			currentX ++;
			if(currentX % (hCount/2) == 0)
			{
				currentY++;
			}
		}

		currentY = 0; currentX = 0;
		for(char currentChar:topRight)
		{
			if(currentChar == 'w')
			{
				maze[currentX % (hCount/2) + hCount/2][currentY] = true;
			}
			else
			{
				maze[currentX % (hCount/2) + hCount/2][currentY] = false;
			}

			currentX ++;
			if(currentX % (hCount/2) == 0)
			{
				currentY++;
			}
		}

		currentY = 0; currentX = 0;
		for(char currentChar:bottomLeft)
		{
			if(currentChar == 'w')
			{
				maze[currentX % (hCount/2)][currentY + (vCount/2)] = true;
			}
			else
			{
				maze[currentX % (hCount/2)][currentY + (vCount/2)] = false;
			}

			currentX ++;
			if(currentX % (hCount/2) == 0)
			{
				currentY++;
			}
		}

		currentY = 0; currentX = 0;
		for(char currentChar:bottomRight)
		{
			if(currentChar == 'w')
			{
				maze[(currentX % (hCount/2)) + hCount/2][currentY + (vCount/2)] = true;
			}
			else
			{
				maze[(currentX % (hCount/2)) + hCount/2][currentY + (vCount/2)] = false;
			}

			currentX ++;
			if(currentX % (hCount/2) == 0)
			{
				currentY++;
			}
		}

		return maze;
	}

    public int getSize() {
        return size;
    }

	public int gethCount() {
		return hCount;
	}

	public int getvCount() {
		return vCount;
	}

	public boolean[][] getMaze() {
		return maze;
	}

	public int[] getSpawnPoints() {
		return spawnPoints;
	}
}
