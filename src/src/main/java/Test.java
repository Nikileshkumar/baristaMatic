package com.encora.springsecurity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {
        int[] arr={1,2,3,4,5};
        System.out.println(Arrays.stream(arr).reduce(0,Integer::sum));
        String[] a={"1","2","3","4"};
        System.out.println(Arrays.stream(a).map(Integer::valueOf).reduce(0,Integer::sum));
        //System.out.println(Arrays.stream(arr).sum());
       // System.out.println(Arrays.stream(a).mapToInt(Integer::parseInt).sum());
       Arrays.sort(arr);
        //Array rotation
        int[] arr2=new int[arr.length];
        int k=2;
        for(int i=0;i<arr.length;i++){
            if(i+k<arr.length)
                arr2[i]=arr[i+k];
            else
                arr2[i]=arr[i+k-arr.length];
        }

       Arrays.stream(arr2).forEach(System.out::print);
        System.out.println();
        //max num from String using streams
        String s="100abc230cds231";
        System.out.println(Arrays.stream(s.replaceAll("\\D+",",").split(","))
                .map(Integer::valueOf).max(Integer::compareTo).get());
        //prime numbers
        IntStream.range(2,10)
                .filter(i->IntStream.rangeClosed(2, ((int) Math.sqrt(i))).noneMatch(j->i%j==0))
                .forEach(System.out::println);
        //Second max number in array
        int max1 = arr[0],max2=arr[0];
        for(int i:arr) {
            if (i > max1) {
                max2 = max1;
                max1 = i;
            }
            else if(i>max2)
                max2=i;
        }
        System.out.println(max2);
        //first non repeated word from a string
        s="why sky is so high why sky";
        Map<String,Long> mp=Arrays.stream(s.split(" "))
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
        for(String s1:s.split(" "))
            if(mp.get(s1)==1) {
                System.out.println(s1);
                break;
            }
        //max salary of each department from list of employees
        list.stream()
                .collect(Collectors.groupingBy(Employee::getDept,
                        Collectors.maxBy(Comparator.comparingInt(Employee::getSalary))))
                .entrySet().stream().map(x->x.getKey()+"-"+x.getValue().get().getSalary())
                .forEach(System.out::println);
    }
    static List<Employee> list=Arrays.asList(new Employee(1,"IT",10000),
            new Employee(1,"IT",10000),
            new Employee(2,"IT",20000),
            new Employee(3,"SALES",30000),
            new Employee(4,"SALES",40000),
            new Employee(5,"FIN",50000),
            new Employee(6,"FIN",60000)
            );
}
@AllArgsConstructor
@Data
class Employee{
    private int empId;
    private String dept;
    private int salary;
}