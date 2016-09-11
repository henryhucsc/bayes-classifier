package bayes;

import bayes.ChineseSpliter;

import bayes.ClassConditionalProbability;
import bayes.PriorProbability;
import bayes.TrainingDataManager;
import bayes.StopwordsControl;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.io.* ;

/**
* 朴素贝叶斯分类器
*/
public class BayesClassifier 
{
    private TrainingDataManager tdm;//训练集管理器
    
    private String trainnigDataPath;//训练集路径
    private static double zoomFactor = 10.0f;
    

    /**
    * 默认的构造器，初始化训练集
    */
    public BayesClassifier() 
    {
        tdm =new TrainingDataManager();
        
    }

    /**
    * 计算给定的文本属性向量X在给定的分类Cj中的类条件概率
    * <code>ClassConditionalProbability</code>连乘值
    * @param X 给定的文本属性向量
    * @param Cj 给定的类别
    * @return 分类条件概率连乘值，即<br>
    */
    float calcProd(String[] X, String Cj) 
    {
        float ret = 1.0F;
        // 类条件概率连乘
        for (int i = 0; i <X.length; i++)
        {
            String Xi = X[i];
            //因为结果过小，因此在连乘之前放大10倍，这对最终结果并无影响，因为我们只是比较概率大小而已
            ret *=ClassConditionalProbability.calculatePxc(Xi, Cj)*zoomFactor;
        }
        // 再乘以先验概率
        ret *= PriorProbability.calculatePc(Cj);
        return ret;
    }
    /**
    * 去掉停用词
    * @param text 给定的文本
    * @return 去停用词后结果
    */
    public String[] DropStopWords(String[] oldWords)
    {
        Vector<String> v1 = new Vector<String>();
        for(int i=0;i<oldWords.length;++i)
        {
            if(StopwordsControl.IsStopWord(oldWords[i])==false)
            {//不是停用词
                v1.add(oldWords[i]);
            }
        }
        String[] newWords = new String[v1.size()];
        v1.toArray(newWords);
        return newWords;
    }
    /**
    * 对给定的文本进行分类
    * @param text 给定的文本
    * @return 分类结果
    */
    @SuppressWarnings("unchecked")
    public String classify(String text) 
    {
    	
  
    	String[] terms = null;
        terms= ChineseSpliter.split(text, " ").split(" ");//中文分词处理(分词后结果可能还包含有停用词）
        terms = DropStopWords(terms);//去掉停用词，以免影响分类
        
        String[] Classes = tdm.getTrainingClassifications();//分类
        float probility = 0.0F;
        List<ClassifyResult> crs = new ArrayList<ClassifyResult>();//分类结果
        for (int i = 0; i <Classes.length; i++) 
        {
            String Ci = Classes[i];//第i个分类
            probility = calcProd(terms, Ci);//计算给定的文本属性向量terms在给定的分类Ci中的分类条件概率
            //保存分类结果
            ClassifyResult cr = new ClassifyResult();
            cr.classification = Ci;//分类
            cr.probility = probility;//关键字在分类的条件概率
            System.out.println("In process.");
            System.out.println(Ci + "：" + probility);
            crs.add(cr);
        }
        //对最后概率结果进行排序
        java.util.Collections.sort(crs,new Comparator() 
        {
            public int compare(final Object o1,final Object o2) 
            {
                final ClassifyResult m1 = (ClassifyResult) o1;
                final ClassifyResult m2 = (ClassifyResult) o2;
                final double ret = m1.probility - m2.probility;
                if (ret < 0) 
                {
                    return 1;
                } 
                else 
                {
                    return -1;
                }
            }
        });
        //返回概率最大的分类
        return crs.get(0).classification;
    }
    
    public static String txt2String(File file){
    	        String result = "";
    	        try{
    	        	
    	        	BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
    	            String s = null;
    	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
    	                result = result + "\n" +s;
    	            }
    	          br.close();    
    	        }catch(Exception e){
    	            e.printStackTrace();
    	        }
    	        return result;
    	     }
    public static double evaluate(double goodNum,double num){
    	double accuracy=goodNum/num;
    	return accuracy;
    }
    
    public static void main(String[] args) throws IOException
    {
        //String text = "家里没钱怎么生？";
        //BayesClassifier classifier = new BayesClassifier();//构造Bayes分类器
        //String result = classifier.classify(text);//进行分类
        //System.out.println("此项属于["+result+"]");
    	int goodNum=0;
    	int num=0;
    	/*String dirName="喜闻乐见";
    	
    	
        File file=new File("/office_zhongjing/try/bayes/TrainningSet");
        String files[];
        files=file.list();
        int num = files.length;
        System.out.println("喜闻乐见"+num);
        
        for(int i=0;i<files.length;i++)
        {
          System.out.println(files[i]);
          File singleFile = new File("/office_zhongjing/try/bayes/TrainningSet"+"/"+files[i]);
          
          
          String text = txt2String(singleFile);
          System.out.println(text);
          BayesClassifier classifier = new BayesClassifier();//构造Bayes分类器
          String result = classifier.classify(text);//进行分类
          System.out.println("此项属于["+result+"]");
          
          if (result.equals(dirName)){
        	  System.out.println("good");
        	  goodNum++;
          }else{
        	  System.out.println("bad");
          }
          
        } 
        System.out.println(goodNum);
        
        
        System.out.println(evaluate(goodNum,num));
        */
        
      
        
        TestingDataManager testM = new TestingDataManager();
        String[] attri = testM.getTestingClassifications();
        
        for (int m = 0; m <attri.length; m++) 
        {
        	String att = attri[m];
        	
        	String[] fPath=testM.getFilesPath(att);
        	for (int n = 0; n < fPath.length; n++) 
            {
        		String t = testM.getText(fPath[n]);
        		System.out.println(t);
        		BayesClassifier classifier2 = new BayesClassifier();//构造Bayes分类器
                String result2 = classifier2.classify(t);//进行分类
                System.out.println("此项属于["+result2+"]");
                
                if (result2.equals(att)){
              	  System.out.println("good");
              	  goodNum++;
                }else{
              	  System.out.println("bad");
                }
        		num++;
            }
        }
        System.out.println("总个数为 "+num);
        System.out.println("判断正取的个数为 "+goodNum);
        
        
        System.out.println("正确率： "+evaluate(goodNum,num));
        //
        String text = "生，凑成好字，而且孩子不孤单，等我们老了，孩子压力也不大!";
        System.out.println("输入的文本为 "+text);
        BayesClassifier classifier = new BayesClassifier();//构造Bayes分类器
        String result = classifier.classify(text);//进行分类
        System.out.println("此项属于["+result+"]");
    }
}
