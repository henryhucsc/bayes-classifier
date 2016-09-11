package bayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* 训练集管理器
*/

public class TestingDataManager 
{
    private String[] testingFileClassifications;//训练语料分类集合
    private File testingTextDir;//训练语料存放目录
    private static String defaultPath = "/office_zhongjing/try/bayes/TestingSet";
    
    
    public TestingDataManager() 
    {
        testingTextDir = new File(defaultPath);
        if (!testingTextDir.isDirectory()) 
        {
            throw new IllegalArgumentException("测试语料库搜索失败！ [" +defaultPath + "]");
        }
        this.testingFileClassifications = testingTextDir.list();
    }
    
    /**
    * 返回训练文本类别，这个类别就是目录名
    * @return 训练文本类别
    */
    public String[] getTestingClassifications() 
    {
        return this.testingFileClassifications;
    }
    
    /**
    * 根据训练文本类别返回这个类别下的所有训练文本路径（full path）
    * @param classification 给定的分类
    * @return 给定分类下所有文件的路径（full path）
    */
    public String[] getFilesPath(String classification) 
    {
        File classDir = new File(testingTextDir.getPath() +File.separator +classification);
        String[] ret = classDir.list();
        for (int i = 0; i < ret.length; i++) 
        {
            ret[i] = testingTextDir.getPath() +File.separator +classification +File.separator +ret[i];
        }
        return ret;
    }
    
    /**
    * 返回给定路径的文本文件内容
    * @param filePath 给定的文本文件路径
    * @return 文本内容
    * @throws java.io.FileNotFoundException
    * @throws java.io.IOException
    */
    public static String getText(String filePath) throws FileNotFoundException,IOException 
    {
        InputStreamReader isReader =new InputStreamReader(new FileInputStream(filePath),"utf-8");
        BufferedReader reader = new BufferedReader(isReader);
        String aline;
        StringBuilder sb = new StringBuilder();
        while ((aline = reader.readLine()) != null)
        {
            sb.append(aline + " ");
        }
        isReader.close();
        reader.close();
        return sb.toString();
    }
    
    /**
    * 返回训练文本集中所有的文本数目
    * @return 训练文本集中所有的文本数目
    */
    public int getTestingFileCount()
    {
        int ret = 0;
        for (int i = 0; i < testingFileClassifications.length; i++)
        {
            ret +=getTestingFileCountOfClassification(testingFileClassifications[i]);
        }
        return ret;
    }
    
    /**
    * 返回训练文本集中在给定分类下的训练文本数目
    * @param classification 给定的分类
    * @return 训练文本集中在给定分类下的训练文本数目
    */
    public int getTestingFileCountOfClassification(String classification)
    {
        File classDir = new File(testingTextDir.getPath() +File.separator +classification);
        return classDir.list().length;
    }
    
    /**
    * 返回给定分类中包含关键字／词的训练文本的数目
    * @param classification 给定的分类
    * @param key 给定的关键字／词
    * @return 给定分类中包含关键字／词的训练文本的数目
    */
    public int getCountContainKeyOfClassification(String classification,String key) 
    {
        int ret = 0;
        try 
        {
            String[] filePath = getFilesPath(classification);
            for (int j = 0; j < filePath.length; j++) 
            {
                String text = getText(filePath[j]);
                if (text.contains(key)) 
                {
                    ret++;
                }
            }
        }
        catch (FileNotFoundException ex) 
        {
        		Logger.getLogger(TestingDataManager.class.getName()).log(Level.SEVERE, null,ex);
        } 
        catch (IOException ex)
        {
            Logger.getLogger(TestingDataManager.class.getName()).log(Level.SEVERE, null,ex);
        }
        return ret;
    }
}
