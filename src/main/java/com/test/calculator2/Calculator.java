package com.test.calculator2;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


/**
 * 第二种解法, 写业务代码计算进行undo和redo
 */
public class Calculator {
    // 前面累计值
    private BigDecimal preTotal;
    // 新值
    private BigDecimal newNum;
    // 当前操作符
    private String curOperator;
    // 前面累计值
    private List<OldNum> oldNumList = new ArrayList<>();
    //初始化索引
    private int oldSize = 0;
    //当前索引
    private int currentSize = -1;
    //undo尽头校验
    private List<OldNum> repeatList = new ArrayList<>();

    public void setNewNum(BigDecimal newNum) {
        // 未计算过,累计总值为第一个输入值
        if (preTotal == null) {
            preTotal = newNum;
        } else {
            this.newNum = newNum;
        }
    }

    public void setCurOperator(String curOperator) {
        this.curOperator = curOperator;
    }

    /**
     * 显示操作结果
     */
    public void display() {
        StringBuilder sb = new StringBuilder();
        if (preTotal != null) {
            sb.append(preTotal);
        }
        if (curOperator != null) {
            sb.append(curOperator);
        }
        if (newNum != null) {
            sb.append(newNum);
        }
        System.out.println(sb.toString());
    }

    /**
     * 取消回到当前
     */
    private void redo() {
        OldNum curOldNum = oldNumList.get(currentSize);
        System.out.println("redo后值:" + curOldNum.getResult() + ",redo前值:" + preTotal + ",redo的操作:" + curOldNum.getCurOperator() + ",redo操作的值:" + curOldNum.getNewNum());
        preTotal = curOldNum.getResult();
    }

    /**
     * 回退上一个
     */
    private void undo() {
        OldNum curOldNum = oldNumList.get(currentSize);
        int size = curOldNum.getIndex() == -1 ? 0 : curOldNum.getIndex();
        OldNum oldNum = oldNumList.get(size);
        if (oldNum == null || repeatList.contains(curOldNum)) {
            System.out.println("无法再undo");
            return;
        }
        System.out.println("undo后值:" + oldNum.getResult() + ",undo前值:" + preTotal + ",undo的操作:" + curOldNum.getCurOperator() + ",undo操作的值:" + curOldNum.getNewNum());
        preTotal = oldNum.getResult();
        oldSize = oldNum.getIndex();
        repeatList.add(curOldNum);
    }

    private void compute() {
        preTotal = preTotal == null ? BigDecimal.ZERO : preTotal;
        if (curOperator == null) {
            System.out.println("请选择操作!");
        }
        // 新增的值
        if (newNum != null) {
            // 累加计算
            BigDecimal ret = calcTwoNum(preTotal, curOperator, newNum);
            OldNum oldNum = new OldNum();
            oldNum.setNewNum(newNum);
            oldNum.setCurOperator(curOperator);
            oldNum.setResult(ret);
            if (oldSize == 0) {
                oldNum.setIndex(currentSize);
            } else if (oldSize == -1) {
                oldNum.setIndex(0);
            } else {
                oldNum.setIndex(oldSize);
            }
            oldNumList.add(oldNum);
            currentSize++;
            preTotal = ret;
            newNum = null;
            curOperator = null;
            oldSize = 0;
        }
    }

    /**
     * 进行累计计算
     *
     * @param preTotal    前面已累计值
     * @param curOperator 当前操作
     * @param newNum      新输入值
     * @return 计算结果
     */
    private BigDecimal calcTwoNum(BigDecimal preTotal, String curOperator, BigDecimal newNum) {
        BigDecimal ret = BigDecimal.ZERO;
        switch (curOperator) {
            case "+":
                ret = preTotal.add(newNum);
                break;
            case "-":
                ret = preTotal.subtract(newNum).setScale(2, RoundingMode.HALF_UP);
                break;
            case "*":
                ret = preTotal.multiply(newNum).setScale(2, RoundingMode.HALF_UP);
                break;
            case "/":
                ret = preTotal.divide(newNum, RoundingMode.HALF_UP);
                break;
        }
        return ret;
    }

    @Data
    static class OldNum {
        //新值
        private BigDecimal newNum;
        //结果
        private BigDecimal result;
        //前一个值的索引
        private int index;
        //操作符
        private String curOperator;
    }

    /**
     * 直接运行下面方法
     *
     * @param args
     */
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setNewNum(new BigDecimal(2));
        calculator.setCurOperator("+");
        calculator.setNewNum(new BigDecimal(4));
        calculator.display();
        calculator.compute();
        calculator.display();
        calculator.setCurOperator("*");
        calculator.setNewNum(new BigDecimal(2));
        calculator.display();
        calculator.compute();
        calculator.display();
        calculator.undo();
        calculator.display();
        System.out.println("开始打断undo并附加额外计算:+3");
        calculator.setCurOperator("+");
        calculator.setNewNum(new BigDecimal(3));
        calculator.display();
        calculator.compute();
        calculator.display();
        System.out.println("打断计算结束,重新进行undo/redo操作!");
        calculator.undo();
        calculator.display();
        calculator.redo();
        calculator.display();
    }
}

