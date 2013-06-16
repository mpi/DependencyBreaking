package tdd.dependencyBreaking.loanCalculator;

import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class LoanCalculator {

    private final double percent;
    
    public LoanCalculator(double percent) {
        this.percent = percent;
    }

    public void calculate(int loan, int months){
        
        if(loan <= 0 || months <= 0){
            JOptionPane.showMessageDialog(null, "Kwota pozyczki oraz liczba miesiecy musi byc wieksza od zera.", "Nieprawidlowe dane", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int rateDesc = (int)Math.ceil(loan/months);
        int[] ratesDescending = new int[months + 1];
        int[] remainingLoanDescending = new int[months + 1];
        remainingLoanDescending[0] = loan;
        ratesDescending[0] = 0;

        double q = 1 + percent/12;
        int rateConst = (int)Math.ceil(loan * Math.pow(q, months)*((q - 1)/(Math.pow(q, months)-1)));
        int[] ratesConstant = new int[months + 1];
        int[] remainingLoanConstant = new int[months + 1];
        remainingLoanConstant[0] = loan;
        ratesConstant[0] = 0;

        for(int i=1; i<months; i++){
            
            ratesDescending[i] = rateDesc + (int)Math.ceil(remainingLoanDescending[i-1] * percent / 12);
            remainingLoanDescending[i] = remainingLoanDescending[i-1] - rateDesc;
            
            ratesConstant[i] = rateConst;
            remainingLoanConstant[i] = (int)(remainingLoanConstant[i-1]*q - rateConst);
        }
        ratesDescending[months] = remainingLoanDescending[months-1];
        remainingLoanDescending[months] = 0;

        ratesConstant[months] = remainingLoanConstant[months-1];
        remainingLoanConstant[months] = 0;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i=0; i <= months; i++) {
            Integer m = Integer.valueOf(i);
            dataset.setValue(remainingLoanDescending[i], "Rata malejąca", m);
            dataset.setValue(remainingLoanConstant[i], "Rata stała", m);
        }
        
        JFreeChart chart = 
                ChartFactory.createBarChart3D("Kredyt", "Miesiące", "Pozostało do spłaty", dataset, PlotOrientation.VERTICAL, true, true, false);
        ChartFrame frame=new ChartFrame("Kredyt",chart);
        frame.setVisible(true);
        frame.setSize(800, 600);
        
    }
    
    public static void main(String[] args) {

        LoanCalculator loanCalculator = new LoanCalculator(0.1);
        loanCalculator.calculate(300000, 12);
        
    }
}
