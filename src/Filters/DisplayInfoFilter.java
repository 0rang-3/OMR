package Filters;

import Interfaces.PixelFilter;
import core.DImage;
import org.apache.fontbox.cff.CIDKeyedFDSelect;
import org.bridj.cpp.com.VARIANT;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class DisplayInfoFilter implements PixelFilter {
    public DisplayInfoFilter() {
        System.out.println("Filter running...");
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();

        System.out.println("Image is " + grid.length + " by "+ grid[0].length);

        int blackCount = 0;
        int numberOfQuestions = 12;
        int whiteCount = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] < 10) blackCount++;
                if (grid[r][c] > 240) whiteCount++;
            }
        }

        System.out.println(blackCount + " nearly black pixels and " + whiteCount + " nearly white pixels");
        System.out.println("----------------------------------------");
        System.out.println("If you want, you could output information to a file instead of printing it.");

        short[][] newGrid = new short[grid.length][grid[0].length];
        for (int r = 0; r < newGrid.length; r++) {
            for (int c = 0; c < newGrid[r].length; c++) {
                newGrid[r][c] = grid[r][c];
            }
        }

        ArrayList<Integer> answers = new ArrayList<>();
        double dark1 = 0.0;
        double dark2 = 0.0;
        double dark3 = 0.0;
        double dark4 = 0.0;
        double dark5 = 0.0;

        for (int r = 121; r < 48*numberOfQuestions+121; r += 48) {
            int counter = 1;
            for (int c = 115; c < 24*numberOfQuestions+115; c += 24) {
                //get average darkness
                if(counter == 1) {
                    dark1 = getDarkness(r, c, newGrid);
                } else if(counter == 2) {
                    dark2 = getDarkness(r, c, newGrid);
                } else if(counter == 3) {
                    dark3 = getDarkness(r, c, newGrid);
                } else if(counter == 4) {
                    dark4 = getDarkness(r, c, newGrid);
                } else if(counter == 5) {
                    dark5 = getDarkness(r, c, newGrid);
                }
                counter++;
            }
            if(dark1 < dark2 && dark1 < dark3 && dark1 < dark4 && dark1 < dark5) {
                answers.add(1);
            } else if(dark2 < dark1 && dark2 < dark3 && dark2 < dark4 && dark2 < dark5) {
                answers.add(2);
            } else if(dark3 < dark1 && dark3 < dark2 && dark3 < dark4 && dark3 < dark5) {
                answers.add(3);
            } else if(dark4 < dark1 && dark4 < dark2 && dark4 < dark3 && dark4 < dark5) {
                answers.add(4);
            } else if(dark5 < dark1 && dark5 < dark2 && dark5 < dark3 && dark5 < dark4) {
                answers.add(5);
            }
        }

        ArrayList<String> answersSTR = new ArrayList<>();
        for (int i = 0; i < answers.size(); i++) {
            String letter = "";
            if(answers.get(i) == 1) {
                letter = "A";
            } else if(answers.get(i) == 2) {
                letter = "B";
            } else if(answers.get(i) == 3) {
                letter = "C";
            } else if(answers.get(i) == 4) {
                letter = "D";
            } else if(answers.get(i) == 5) {
                letter = "E";
            }
            answersSTR.add((i+1) + ". " + letter);
            System.out.println(answersSTR.get(i));
            //System.out.println((i+1) + " - " + answers.get(i));
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

