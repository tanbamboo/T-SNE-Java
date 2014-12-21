package com.jujutsu.tsne;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.math.io.files.ASCIIFile;
import org.math.io.parser.ArrayString;
import org.math.plot.FrameView;
import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;
import org.math.plot.PlotPanel;
import org.math.plot.plots.ColoredScatterPlot;
import org.math.plot.plots.ScatterPlot;

public class TSneDemo {
	
	static double perplexity = 40.0;
	private static int initial_dims = 50;

	public TSneDemo() {}
	
    public static double[][] nistReadStringDouble(String s, String columnDelimiter, String rowDelimiter) {
        double[][] array;
        String[] rows = s.split(rowDelimiter);
        array = new double[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            List<Double> colvals = new ArrayList<Double>();
            String[] cols = rows[i].split(columnDelimiter);
            for (int j = 0; j < cols.length; j++) {
                if(!(cols[j].length()==0)) {
                    colvals.add(Double.parseDouble(cols[j]));
                }
            }
            array[i] = new double[colvals.size()];
            for (int j = 0; j < colvals.size(); j++) {
                array[i][j] = colvals.get(j);
            }
        }

        return array;
    }
	
    public static double[][] nistReadStringDouble(String s) {
        return nistReadStringDouble(s, " ", "\n");
    }

    public static double[][] nistReadStringDouble(String s, String columnDelimiter) {
        return nistReadStringDouble(s, columnDelimiter, "\n");
    }
	
	public static void pca_iris() {
    	double [][] X = nistReadStringDouble(ASCIIFile.read(new File("iris_X.txt")), ",");
    	System.out.println("Input is = " + X.length + " x " + X[0].length + " => \n" + ArrayString.printDoubleArray(X));
    	double [][] Y = TSne.pca(X,2);
    	System.out.println("Result is = " + Y.length + " x " + Y[0].length + " => \n" + ArrayString.printDoubleArray(Y));
    }
    
    public static void tsne_iris() {
    	double [][] X = nistReadStringDouble(ASCIIFile.read(new File("/Users/eralljn/Research/Datasets/iris_X.txt")), ","); // ASCIIFile.readDoubleArray(new File("mnist2500_X.txt"));
        System.out.println("Shape is: " + X.length + " x " + X[0].length);
		double [][] Y = TSne.tsne(X, 2, initial_dims, perplexity);
        System.out.println("Shape is: " + Y.length + " x " + Y[0].length);
        
        double [][] setosa = new double [initial_dims][2];
        String [] setosaNames = new String[initial_dims];
        double [][] versicolor = new double [initial_dims][2];
        String [] versicolorNames = new String[initial_dims];
        double [][] virginica = new double [initial_dims][2];
        String [] virginicaNames = new String[initial_dims];
        
        int cnt = 0;
        for (int i = 0; i < initial_dims; i++, cnt++) {
        	for (int j = 0; j < 2; j++) {
            	setosa[i][j] = Y[cnt][j];
            	setosaNames[i] = "setosa";
			}
        }
        for (int i = 0; i < initial_dims; i++, cnt++) {
        	for (int j = 0; j < 2; j++) {
        		versicolor[i][j] = Y[cnt][j];
        		versicolorNames[i] = "versicolor";
			}
        }
        for (int i = 0; i < initial_dims; i++, cnt++) {
        	for (int j = 0; j < 2; j++) {
        		virginica[i][j] = Y[cnt][j];
        		virginicaNames[i] = "virginica";
			}
        }
        
        System.out.println("Result is = " + Y.length + " x " + Y[0].length + " => \n" + ArrayString.printDoubleArray(Y));
        Plot2DPanel plot = new Plot2DPanel();
        
        ScatterPlot setosaPlot = new ScatterPlot("setosa", PlotPanel.COLORLIST[0], setosa);
        setosaPlot.setTags(setosaNames);
        
        ScatterPlot versicolorPlot = new ScatterPlot("versicolor", PlotPanel.COLORLIST[1], versicolor);
        versicolorPlot.setTags(versicolorNames);
        ScatterPlot virginicaPlot = new ScatterPlot("versicolor", PlotPanel.COLORLIST[2], virginica);
        virginicaPlot.setTags(virginicaNames);
        
        plot.plotCanvas.setNotable(true);
        plot.plotCanvas.setNoteCoords(true);
        plot.plotCanvas.addPlot(setosaPlot);
        plot.plotCanvas.addPlot(versicolorPlot);
        plot.plotCanvas.addPlot(virginicaPlot);
        
        //int setosaId = plot.addScatterPlot("setosa", setosa);
        //int versicolorId = plot.addScatterPlot("versicolor", versicolor);
        //int virginicaId = plot.addScatterPlot("virginica", virginica);
        
        FrameView plotframe = new FrameView(plot);
        plotframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        plotframe.setVisible(true);
    }
    
    public static void tsne_trdb() {
    	double [][] X = nistReadStringDouble(ASCIIFile.read(new File("trdb-tsne.txt")), ","); // ASCIIFile.readDoubleArray(new File("mnist2500_X.txt"));
        System.out.println("Shape is: " + X.length + " x " + X[0].length);
        double [][] Y = TSne.tsne(X, 2, initial_dims, perplexity);
        System.out.println("Shape is: " + Y.length + " x " + Y[0].length);
        
        String [] labels = new ASCIIFile(new File("trdb-tsne-labels.txt")).readLines();

        Plot2DPanel plot = new Plot2DPanel();
        ColoredScatterPlot trdbPlot = new ColoredScatterPlot("trdb", Y, labels);
        plot.plotCanvas.setNotable(true);
        plot.plotCanvas.setNoteCoords(true);
        plot.plotCanvas.addPlot(trdbPlot);
                
        FrameView plotframe = new FrameView(plot);
        plotframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        plotframe.setVisible(true);
    }
    
    public static void tsne_trdb_lda() {
    	double [][] X = nistReadStringDouble(ASCIIFile.read(new File("TRDB-Theta.txt")), ","); // ASCIIFile.readDoubleArray(new File("mnist2500_X.txt"));
        System.out.println("Shape is: " + X.length + " x " + X[0].length);
        double [][] Y = TSne.tsne(X, 2, initial_dims, perplexity);
        System.out.println("Shape is: " + Y.length + " x " + Y[0].length);
        
        String [] labels = new ASCIIFile(new File("TRDB-Labels.txt")).readLines();

        Plot2DPanel plot = new Plot2DPanel();
        ColoredScatterPlot trdbPlot = new ColoredScatterPlot("trdb", Y, labels);
        plot.plotCanvas.setNotable(true);
        plot.plotCanvas.setNoteCoords(true);
        plot.plotCanvas.addPlot(trdbPlot);
                
        FrameView plotframe = new FrameView(plot);
        plotframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        plotframe.setVisible(true);
    }

    public static void tsne_trdb3D() {
    	double [][] X = nistReadStringDouble(ASCIIFile.read(new File("trdb-tsne.txt")), ","); // ASCIIFile.readDoubleArray(new File("mnist2500_X.txt"));
        System.out.println("Shape is: " + X.length + " x " + X[0].length);
        double [][] Y = TSne.tsne(X, 3, initial_dims, perplexity, 200, false);
        System.out.println("Shape is: " + Y.length + " x " + Y[0].length);
        
        String [] labels = new ASCIIFile(new File("trdb-tsne-labels.txt")).readLines();

        Plot3DPanel plot = new Plot3DPanel();
        ColoredScatterPlot trdbPlot = new ColoredScatterPlot("trdb", Y, labels);
        plot.plotCanvas.setNotable(true);
        plot.plotCanvas.setNoteCoords(true);
        plot.plotCanvas.addPlot(trdbPlot);
                
        FrameView plotframe = new FrameView(plot);
        plotframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        plotframe.setVisible(true);
    }

    public static void tsne_mnist() {
    	int nistSize = 2500;
    	//int nistSize = 500;
        System.out.println("Running example on " + nistSize + " MNIST digits...");
        double [][] X = nistReadStringDouble(ASCIIFile.read(new File("/Users/eralljn/Research/Datasets/MNist/mnist" + nistSize + "_X.txt"))); // ASCIIFile.readDoubleArray(new File("mnist2500_X.txt"));
    	String [] labels = new ASCIIFile(new File("/Users/eralljn/Research/Datasets/MNist/mnist2500_labels.txt")).readLines();
    	for (int i = 0; i < labels.length; i++) {
			labels[i] = labels[i].trim().substring(0, 1);
		}
        System.out.println("Shape is: " + X.length + " x " + X[0].length);
        double [][] Y = TSne.tsne(X, 2, initial_dims, perplexity);
        System.out.println("Result is = " + Y.length + " x " + Y[0].length + " => \n" + ArrayString.printDoubleArray(Y));
        Plot2DPanel plot = new Plot2DPanel();
        
        ColoredScatterPlot setosaPlot = new ColoredScatterPlot("setosa", Y, labels);
        plot.plotCanvas.setNotable(true);
        plot.plotCanvas.setNoteCoords(true);
        plot.plotCanvas.addPlot(setosaPlot);
                
        FrameView plotframe = new FrameView(plot);
        plotframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        plotframe.setVisible(true);
    }

    public static void tsne_embeddings() {
    	int nistSize = 500;
        System.out.println("Running example on " + nistSize + " MNIST digits...");
        double [][] X = nistReadStringDouble(ASCIIFile.read(new File("embeddings-data.txt")));
        String [] labels = new ASCIIFile(new File("embeddings-labels.txt")).readLines();
        System.out.println("Shape is: " + X.length + " x " + X[0].length);
        double [][] Y = TSne.tsne(X, 2, initial_dims, perplexity);
        System.out.println("Result is = " + Y.length + " x " + Y[0].length + " => \n" + ArrayString.printDoubleArray(Y));
        Plot2DPanel plot = new Plot2DPanel();
        
        ColoredScatterPlot trdbPlot = new ColoredScatterPlot("trdb", Y, labels);
        plot.plotCanvas.setNotable(true);
        plot.plotCanvas.setNoteCoords(true);
        plot.plotCanvas.addPlot(trdbPlot);
                
        FrameView plotframe = new FrameView(plot);
        plotframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        plotframe.setVisible(true);
    }
    
    public static void main(String [] args) {
        System.out.println("Run Y = tsne.tsne(X, no_dims, perplexity) to perform t-SNE on your dataset.");
        //pca_iris();
        //tsne_random();
        //tsne_iris();
        tsne_mnist();
        //tsne_trdb();
        //tsne_trdb_lda();
        //tsne_trdb3D();
        //tsne_embeddings();
    }

}