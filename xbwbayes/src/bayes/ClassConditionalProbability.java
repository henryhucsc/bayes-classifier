package bayes;

/**
*类条件概率计算
* P(xj|cj)=( N(X=xi, C=cj )+1 ) / ( N(C=cj)+M+V ) 
* 其中，N(X=xi, C=cj）表示类别cj中包含属性x
* i的训练文本数量；N(C=cj)表示类别cj中的训练文本数量；M值用于避免
* N(X=xi, C=cj）过小所引发的问题；
* V表示类别的总数。
* 
* 条件概率
* 定义 设A, B是两个事件，且P(A)>0 称
* P(B∣A)=P(AB)/P(A)
* 为在条件A下发生的条件事件B发生的条件概率。
*/

public class ClassConditionalProbability 
{
    private static TrainingDataManager tdm = new TrainingDataManager();
    private static final float M = 0F;
    
    /**
    * 计算类条件概率
    * @param x 给定的文本属性
    * @param c 给定的分类
    * @return 给定条件下的类条件概率
    */
    public static float calculatePxc(String x, String c) 
    {
        float ret = 0F;
        float Nxc = tdm.getCountContainKeyOfClassification(c, x);
        float Nc = tdm.getTrainingFileCountOfClassification(c);
        float V = tdm.getTrainingClassifications().length;
        ret = (Nxc + 1) / (Nc + M + V); //为了避免出现0这样极端情况，进行加权处理
        return ret;
    }
}