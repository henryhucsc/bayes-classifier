package bayes;

import java.util.*;
import java.util.regex.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Math.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class Tf_Idf_Cal {
	//private static int [][] Tf;
	


public static int[][] getTf(String[] text,int countWord,int countFile) throws FileNotFoundException, IOException{
	 
int fileIndex=0;
int termIndex=0;
	BayesClassifier trainN  = new BayesClassifier();
	TrainingDataManager trainL= new TrainingDataManager();
	//int iCount = trainN.getCountContainKeyOfClassification(String classification,String key);
	

	String[] terms = null;
    
    terms = text;
    
    
    String[] Classes = trainL.getTrainingClassifications();//分类
    
    int [][] Tf=new int[countWord][Classes.length];
    
    for (int m = 0; m <terms.length; m++) 
    {
    String term=terms[m];
    fileIndex=0;
    for (int i = 0; i <Classes.length; i++) 
    {
        String Ci = Classes[i];//第i个分类
        String[] path=trainL.getFilesPath(Ci);
        
        /*for (int j = 0; j <path.length; j++) 
        {
        	String Pi=path[j];
        	String needText=trainL.getText(Pi);
        	
        	
        	Pattern p=Pattern.compile(term);
        	String u= needText;
        	Matcher same=p.matcher(u);
        	
        	while(same.find()){
        	Tf[termIndex][fileIndex]++;
        	System.out.println("termIndex: "+termIndex);
        	System.out.println("fileIndex: "+fileIndex);
        	System.out.println("next");
        	}
        	fileIndex++;
        	//System.out.print(fileIndex);        
        }
        */
        Tf[m][i]=trainL.getCountContainKeyOfClassification(Ci,term);
    }
        termIndex++;
    }
    
    return Tf;
    
}

public static double[] Idf(String[] text){
	
	TrainingDataManager trainF= new TrainingDataManager();
	double totalFile=trainF.getTrainingFileCount();
	
	
	String[] Classes = trainF.getTrainingClassifications();
	
	BayesClassifier trainN  = new BayesClassifier();
	
	

	String[] terms = null;
    
    terms = text;
    //double [] Idf=new double [terms.length];
    int [] wordCount=new int [terms.length];
    double [] Idf=new double [terms.length];
    
    for (int m = 0; m <terms.length; m++) 
    {
    String term=terms[m];
	
	for (int i = 0; i <Classes.length; i++) 
    {
		String Ci = Classes[i];
		wordCount[m]+=trainF.getCountContainKeyOfClassification(Ci,term); 
	    }
//getTrainingFileCount()
Idf[m]=Math.log(totalFile/(wordCount[m]+1));
   
    }
    
    
    return Idf;
}

public static double[] sumOfSquare(String[] text){
	TrainingDataManager trainF= new TrainingDataManager();
	double totalFile=trainF.getTrainingFileCount();
	
	
	String[] Classes = trainF.getTrainingClassifications();
	
	BayesClassifier trainN  = new BayesClassifier();
	
	

	String[] terms = null;
    
    terms = text;
    //double [] Idf=new double [terms.length];
    double [] wordCount=new double [terms.length];//total number of i word in all files
    double [][] wCountSingleFile=new double[terms.length][Classes.length];//number of i word in each class
    double [] sumOfS=new double [terms.length];
    
    for (int m = 0; m <terms.length; m++) 
    {
    String term=terms[m];
	
	for (int i = 0; i <Classes.length; i++) 
    {
		String Ci = Classes[i];
		wCountSingleFile[m][i]+=trainF.getCountContainKeyOfClassification(Ci,term); 
		wordCount[m]+=trainF.getCountContainKeyOfClassification(Ci,term); 
	    }
    }
for(int c=0;c<sumOfS.length;c++){
	for (int t=0;t<Classes.length;t++){
		sumOfS[c]+=Math.pow((wCountSingleFile[c][t]-wordCount[c]/Classes.length),2);
	}
}
	

    
    
    return sumOfS;
}
public static void main(String[] args) throws IOException
{
	TrainingDataManager trainM = new TrainingDataManager();
	BayesClassifier trainN  = new BayesClassifier();
    String[] attri = trainM.getTrainingClassifications();
    String[] wordStore=new String[9999];
    for(int t=0;t<wordStore.length;t++){
    	wordStore[t]="temp";
    }
    
    int judge=0;//if 0,not same;1,got same
    int countWordIndex=0;
    int countFile=0;
    
    for (int m = 0; m <attri.length; m++) 
    {
    	String att = attri[m];
    	
    	String[] fPath=trainM.getFilesPath(att);
    	for (int n = 0; n < fPath.length; n++) 
        {
    		String t = trainM.getText(fPath[n]);
    		countFile++;
    		String[] terms = null;
    	    terms= ChineseSpliter.split(t, " ").split(" ");//中文分词处理(分词后结果可能还包含有停用词）
    	    terms = trainN.DropStopWords(terms);
    	    for (int x = 0; x <terms.length; x++){
    	    	String term=terms[x];
    	    	for(int y=0;y<wordStore.length;y++){
    	    		
    	    		if(wordStore[y].equals(term)){
    	    			judge++;
    	    			//System.out.println("found same");
    	    		}
    	    	}
    	    	if (judge==0){
    	    		wordStore[countWordIndex]=term;
    	    		countWordIndex++;
    	    	}else if(judge==1){
    	    		
    	    	}else{
    	    		System.out.println("Exercise me? judge num error!");
    	    	}
    	    	judge=0;
    	    }
        }
    }
    String [] filterWordStore=new String[countWordIndex];
    for(int z=0;z<countWordIndex;z++){
    	filterWordStore[z]=wordStore[z];
    	System.out.println(filterWordStore[z]);
    }
	
	//String txt="家里没钱怎么生？";
	
	
	double[] newIdf=Idf(filterWordStore);
	int [][] newTf=getTf(filterWordStore,countWordIndex,countFile);
			for (int x = 0; x < newTf.length; x++) {
		        for (int y = 0; y < newTf[0].length; y++) {
		            System.out.print(newTf[x][y] + " ");
		        }
		        System.out.print("\n");
		    }
	
	for (int x = 0; x < newIdf.length; x++) {
    	System.out.println(newIdf[x]);
    }
	//cal tf-idf
	double[][] tf_idf=new double [newTf.length][newTf[0].length];
	System.out.println("Here is tf_idf");
	for (int q = 0; q < newTf.length; q++) {
        for (int p= 0; p < newTf[0].length; p++) {
            tf_idf[q][p]=newTf[q][p]*newIdf[p];
        	System.out.print(tf_idf[q][p] + " ");
        }
        System.out.print("\n");
    }
	double[] newSumOfS=sumOfSquare(filterWordStore);
	System.out.println("Here is the Di value");
	for (int k = 0; k < newSumOfS.length; k++) {
		System.out.println(newSumOfS[k]);
	}
	double[][] finalWeight=new double [tf_idf.length][tf_idf[0].length];
	//
	System.out.println("Here is the final weighted values");
	for (int g = 0; g < tf_idf.length; g++) {
        for (int h= 0; h < tf_idf[0].length; h++) {
            finalWeight[g][h]=tf_idf[g][h]*newSumOfS[h];
        	System.out.print(tf_idf[g][h] + " ");
        }
        System.out.print("\n");
    }
}
}
