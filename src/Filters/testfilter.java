package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.util.ArrayList;

public class testfilter implements PixelFilter {
    public testfilter() {
        System.out.println("Filter running...");
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        short[][] newGrid = new short[600][500];
        for (int i = 0; i < 600; i++) {
            for (int j = 0; j < 500; j++) {
                newGrid[i][j] = grid[i][j];
            }
        }

        img.setPixels(newGrid);
        return img;
    }

    public double getDarkness(int r, int c, short[][] grid) {
        int sum = 0;
        int counter = 0;
        for (int row = r-5; row < r+5; row++) {
            for (int col = c-5; col < c+5; col++) {
                sum += grid[row][col];
                counter++;
            }
        }
        return sum / (double) counter;
    }
}

