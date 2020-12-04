
/*****************************

* 프로그램명: grade

* 작성자 :  2019038034 서일훈

* 작성일 :  20/09/18

*프로그램 설명 :  키보드로 부터 학번, 이름, 국어, 영어, 수학 
                C언어를 입력받아 총점, 평균, 학점을 계산하는 프로그램

*********************************/

import java.util.*;

public class grade{

    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);

        System.out.println("학번, 이름, 국어 점수, 영어 점수, 수학 점수, C언어 점수 순으로 입력해주세요.");
        int stdnum = scanner.nextInt();
        String name = scanner.next();
        int korean = scanner.nextInt();
        int english = scanner.nextInt();
        int math = scanner.nextInt();
        int clan = scanner.nextInt();

        int sum = korean + english + math + clan;
        double mean = sum / 4.0;
        String grade;

        if(mean>=90.0)
            grade = "A";
        else if(mean>=80.0)
            grade = "B";
        else if(mean>=70.0)
            grade = "C";
        else if(mean>=60.0)
            grade = "D";
        else
            grade = "F";

        scanner.close();
        System.out.println("학번 : " + stdnum + "\n" + "이름 : " + name + "\n" + "총점 : " + sum + "\n" + "평균 : " + mean + "\n" + "학점 : " + grade );

    }
}