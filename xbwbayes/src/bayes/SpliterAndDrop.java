package bayes;

import java.io.IOException;
import java.io.*;

public class SpliterAndDrop {

	
	public static void main(String[] args) throws IOException
	{
		TrainingDataManager trainM = new TrainingDataManager();
		BayesClassifier trainN  = new BayesClassifier();
	    String[] attri = trainM.getTrainingClassifications();
	    int count=0;
	    int fileIndex=0;
	    String term = "";
	    
	    for (int m = 0; m <attri.length; m++) 
	    {
	    	String att = attri[m];
	    	
	    	
	    	String[] fPath=trainM.getFilesPath(att);
	    	for (int n = 0; n < fPath.length; n++) 
	        {
	    		String t = trainM.getText(fPath[n]);
	    		String[] terms = null;
	    	    terms= ChineseSpliter.split(t, " ").split(" ");//中文分词处理(分词后结果可能还包含有停用词）
	    	    terms = trainN.DropStopWords(terms);
	    	    for(int i=0;i<terms.length;i++){
	    	    
	    	    term = term+" "+terms[i];
	    		count++;
	    	    }
	    	    FileWriter fw=new FileWriter(new File("/office_zhongjing/try/bayes/storageForSpliter/"+fileIndex+ ".txt"));
	    		fw.append(term);
	    		fw.flush();
	    		fw.close();
	    	    fileIndex++;
	}
}
	    System.out.println(count);
	    
    }
	}
